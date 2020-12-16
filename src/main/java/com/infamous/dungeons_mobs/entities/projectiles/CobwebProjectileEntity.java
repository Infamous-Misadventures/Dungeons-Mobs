package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SpiderEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
public class CobwebProjectileEntity extends DamagingProjectileEntity implements IRendersAsItem {
   private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(CobwebProjectileEntity.class, DataSerializers.ITEMSTACK);

   public CobwebProjectileEntity(World worldIn){
      super(ModEntityTypes.SLIMEBALL.get(), worldIn);
   }


   public CobwebProjectileEntity(EntityType<? extends CobwebProjectileEntity> entityType, World world) {
      super(entityType, world);
   }

   public CobwebProjectileEntity(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
      super(ModEntityTypes.COBWEB_PROJECTILE.get(), x, y, z, accelX, accelY, accelZ, world);
   }

   public CobwebProjectileEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
      super(ModEntityTypes.COBWEB_PROJECTILE.get(), shooter, accelX, accelY, accelZ, world);
   }

   public void setStack(ItemStack stack) {
      if (stack.getItem() != Items.COBWEB || stack.hasTag()) {
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
      return itemstack.isEmpty() ? new ItemStack(Items.COBWEB) : itemstack;
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
      return ParticleTypes.CRIT;
   }

    @Override
    protected void onEntityHit(EntityRayTraceResult rayTraceResult) {
        super.onEntityHit(rayTraceResult);
        Entity entity = rayTraceResult.getEntity();
        if(!(entity instanceof SpiderEntity)){
           BlockPos blockPos = entity.getPosition();
           if(GeomancyHelper.canAllowBlockEntitySpawn(this, blockPos)){
              this.createCobweb(blockPos);
           }
        }
    }

   private void createCobweb(BlockPos blockPos) {
      CobwebTrapEntity cobwebTrapEntity = new CobwebTrapEntity(this.world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100);
      this.world.addEntity(cobwebTrapEntity);
   }

   @Override
   protected void func_230299_a_(BlockRayTraceResult rayTraceResult) {
      super.func_230299_a_(rayTraceResult);
      BlockPos blockPos = rayTraceResult.getPos().offset(this.getAdjustedHorizontalFacing().getOpposite());
      if(GeomancyHelper.canAllowBlockEntitySpawn(this, blockPos)){
         this.createCobweb(blockPos);
      }
   }

   /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onImpact(RayTraceResult result) {
      super.onImpact(result);
      removeIfWorldNotRemote();
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