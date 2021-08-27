package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
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
      ItemStack itemstack = this.getItemRaw();
      return itemstack.isEmpty() ? new ItemStack(ModItems.WRAITH_FIRE_CHARGE.get()) : itemstack;
   }

   /**
    * Called when the arrow hits an entity
    */
   protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
      super.onHitEntity(p_213868_1_);
      if (!this.level.isClientSide) {
         Entity entity = p_213868_1_.getEntity();
         if (!entity.fireImmune() || entity instanceof WraithEntity) {
            Entity entity1 = this.getOwner();
            int i = entity.getRemainingFireTicks();
            entity.setSecondsOnFire(5);
            boolean flag = entity.hurt(DamageSource.fireball(this, entity1), 10.0F); // twice as much damage as normal fire
            if (!flag) {
               entity.setRemainingFireTicks(i);
            } else if (entity1 instanceof LivingEntity) {
               this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
         }

      }
   }

   protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
      super.onHitBlock(p_230299_1_);
      if (!this.level.isClientSide) {
         Entity entity = this.getOwner();
         if (entity == null || !(entity instanceof MobEntity) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getEntity())) {
            BlockPos blockpos = p_230299_1_.getBlockPos().relative(p_230299_1_.getDirection());
            if (this.level.isEmptyBlock(blockpos)) {
               BlockState soulFireBlockState = ModBlocks.WRAITH_FIRE_BLOCK.get().defaultBlockState();
               this.level.setBlockAndUpdate(blockpos, soulFireBlockState);
            }
         }

      }
   }

   /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onHit(RayTraceResult result) {
      super.onHit(result);
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