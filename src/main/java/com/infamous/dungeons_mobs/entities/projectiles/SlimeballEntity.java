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
   private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(SlimeballEntity.class, DataSerializers.ITEMSTACK);

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
         this.getDataManager().set(STACK, Util.make(stack.copy(), (itemStack) -> {
            itemStack.setCount(1);
         }));
      }
   }

   protected ItemStack getStack() {
      return this.getDataManager().get(STACK);
   }

   @OnlyIn(Dist.CLIENT)
   public ItemStack getItem() {
      ItemStack itemstack = this.getStack();
      return itemstack.isEmpty() ? new ItemStack(Items.SLIME_BALL) : itemstack;
   }

   protected void registerData() {
      this.getDataManager().register(STACK, ItemStack.EMPTY);
   }

   public void writeAdditional(CompoundNBT compound) {
      super.writeAdditional(compound);
      ItemStack itemstack = this.getStack();
      if (!itemstack.isEmpty()) {
         compound.put("Item", itemstack.write(new CompoundNBT()));
      }

   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditional(CompoundNBT compound) {
      super.readAdditional(compound);
      ItemStack itemstack = ItemStack.read(compound.getCompound("Item"));
      this.setStack(itemstack);
   }

   protected boolean isFireballFiery() {
      return false;
   }

   protected IParticleData getParticle() {
      return ParticleTypes.ITEM_SLIME;
   }

    @Override
    protected void onEntityHit(EntityRayTraceResult rayTraceResult) {
        super.onEntityHit(rayTraceResult);
        Entity entity = rayTraceResult.getEntity();
        int attackDamage = 3;
        if(!(entity instanceof SlimeEntity)){
           entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), (float)attackDamage);
        }
    }

    /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onImpact(RayTraceResult result) {
      super.onImpact(result);
      if(result instanceof EntityRayTraceResult){
         EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)result;
         if(!(entityRayTraceResult.getEntity() instanceof SlimeEntity)){
            if (!this.world.isRemote) {
            this.remove();
            }
         }
      }
      else {
         removeIfWorldNotRemote();
      }
   }

   private void removeIfWorldNotRemote() {
      if (!this.world.isRemote) {
         this.remove();
      }
   }

   /**
    * Returns true if other Entities should be prevented from moving through this Entity.
    */
   public boolean canBeCollidedWith() {
      return false;
   }

   /**
    * Called when the entity is attacked.
    */
   public boolean attackEntityFrom(DamageSource source, float amount) {
      return false;
   }

   @Override
   public IPacket<?> createSpawnPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}