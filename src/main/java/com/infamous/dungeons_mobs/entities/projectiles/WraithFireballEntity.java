package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class WraithFireballEntity extends AbstractFireballEntity {

   public WraithFireballEntity(World worldIn){
      super(ModEntityTypes.WRAITH_FIREBALL.get(), worldIn);
   }

   public WraithFireballEntity(EntityType<? extends WraithFireballEntity> entityType, World world) {
      super(entityType, world);
   }

   public WraithFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
      super(ModEntityTypes.WRAITH_FIREBALL.get(), shooter, accelX, accelY, accelZ, worldIn);
   }

   public WraithFireballEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
      super(ModEntityTypes.WRAITH_FIREBALL.get(), x, y, z, accelX, accelY, accelZ, worldIn);
   }

   @OnlyIn(Dist.CLIENT)
   public ItemStack getItem() {
      ItemStack itemstack = this.getStack();
      return itemstack.isEmpty() ? new ItemStack(ModItems.WRAITH_FIRE_CHARGE.get()) : itemstack;
   }

   /**
    * Called when the arrow hits an entity
    */
   protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
      super.onEntityHit(p_213868_1_);
      if (!this.world.isRemote) {
         Entity entity = p_213868_1_.getEntity();
         if (!entity.isImmuneToFire()) {
            Entity entity1 = this.func_234616_v_();
            int i = entity.getFireTimer();
            entity.setFire(5);
            boolean flag = entity.attackEntityFrom(DamageSource.func_233547_a_(this, entity1), 10.0F); // twice as much damage as normal fire
            if (!flag) {
               entity.forceFireTicks(i);
            } else if (entity1 instanceof LivingEntity) {
               this.applyEnchantments((LivingEntity)entity1, entity);
            }
         }

      }
   }

   protected void func_230299_a_(BlockRayTraceResult p_230299_1_) {
      super.func_230299_a_(p_230299_1_);
      if (!this.world.isRemote) {
         Entity entity = this.func_234616_v_();
         if (entity == null || !(entity instanceof MobEntity) || this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.getEntity())) {
            BlockPos blockpos = p_230299_1_.getPos().offset(p_230299_1_.getFace());
            if (this.world.isAirBlock(blockpos)) {
               BlockState soulFireBlockState = ModBlocks.WRAITH_FIRE_BLOCK.get().getDefaultState();
               this.world.setBlockState(blockpos, soulFireBlockState);
            }
         }

      }
   }

   /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onImpact(RayTraceResult result) {
      super.onImpact(result);
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