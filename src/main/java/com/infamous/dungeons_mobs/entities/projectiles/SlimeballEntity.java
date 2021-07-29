package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = IRendersAsItem.class
)
public class SlimeballEntity extends DamagingProjectileEntity implements IRendersAsItem {
   private static final DataParameter<ItemStack> STACK = EntityDataManager.defineId(SlimeballEntity.class, DataSerializers.ITEM_STACK);

   public SlimeballEntity(World worldIn){
      super(ModEntityTypes.SLIMEBALL.get(), worldIn);
   }


   public SlimeballEntity(EntityType<? extends SlimeballEntity> entityType, World world) {
      super(entityType, world);
   }

   public SlimeballEntity(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
      super(ModEntityTypes.SLIMEBALL.get(), x, y, z, accelX, accelY, accelZ, world);
   }

   public SlimeballEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
      super(ModEntityTypes.SLIMEBALL.get(), shooter, accelX, accelY, accelZ, world);
   }

   public void setStack(ItemStack stack) {
      if (stack.getItem() != Items.SLIME_BALL || stack.hasTag()) {
         this.getEntityData().set(STACK, Util.make(stack.copy(), (itemStack) -> {
            itemStack.setCount(1);
         }));
      }
   }

   protected ItemStack getStack() {
      return this.getEntityData().get(STACK);
   }

   @OnlyIn(Dist.CLIENT)
   public ItemStack getItem() {
      ItemStack itemstack = this.getStack();
      return itemstack.isEmpty() ? new ItemStack(Items.SLIME_BALL) : itemstack;
   }

   protected void defineSynchedData() {
      this.getEntityData().define(STACK, ItemStack.EMPTY);
   }

   public void addAdditionalSaveData(CompoundNBT compound) {
      super.addAdditionalSaveData(compound);
      ItemStack itemstack = this.getStack();
      if (!itemstack.isEmpty()) {
         compound.put("Item", itemstack.save(new CompoundNBT()));
      }

   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditionalSaveData(CompoundNBT compound) {
      super.readAdditionalSaveData(compound);
      ItemStack itemstack = ItemStack.of(compound.getCompound("Item"));
      this.setStack(itemstack);
   }

   protected boolean shouldBurn() {
      return false;
   }

   protected IParticleData getTrailParticle() {
      return ParticleTypes.ITEM_SLIME;
   }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity entity = rayTraceResult.getEntity();
        int attackDamage = 3;
        if(!(entity instanceof SlimeEntity)){
           entity.hurt(DamageSource.thrown(this, this.getOwner()), (float)attackDamage);
        }
    }

    /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onHit(RayTraceResult result) {
      super.onHit(result);
      if(result instanceof EntityRayTraceResult){
         EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)result;
         if(!(entityRayTraceResult.getEntity() instanceof SlimeEntity)){
            if (!this.level.isClientSide) {
            this.remove();
            }
         }
      }
      else {
         removeIfWorldNotRemote();
      }
   }

   private void removeIfWorldNotRemote() {
      if (!this.level.isClientSide) {
         this.remove();
      }
   }

   /**
    * Returns true if other Entities should be prevented from moving through this Entity.
    */
   public boolean isPickable() {
      return false;
   }

   /**
    * Called when the entity is attacked.
    */
   public boolean hurt(DamageSource source, float amount) {
      return false;
   }

   @Override
   public IPacket<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}