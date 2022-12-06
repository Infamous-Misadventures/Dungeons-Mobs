package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PotionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.entity.Entity.RemovalReason;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = ItemSupplier.class
)
public class BlueNethershroomEntity extends ThrowableItemProjectile implements ItemSupplier {

   public static final int LIGHT_BLUE_HEX_COLOR_CODE = 0x00e0ff;

   public BlueNethershroomEntity(EntityType<? extends BlueNethershroomEntity> entityType, Level world) {
      super(entityType, world);
   }

   public BlueNethershroomEntity(Level world, LivingEntity shooter) {
      super(ModEntityTypes.BLUE_NETHERSHROOM.get(), shooter, world);
   }

   public BlueNethershroomEntity(Level world, double x, double y, double z) {
      super(ModEntityTypes.BLUE_NETHERSHROOM.get(), x, y, z, world);
   }

   public static void setLightBluePotionColor(ItemStack itemStack){
      PotionHelper.setColor(itemStack, LIGHT_BLUE_HEX_COLOR_CODE);
   }

   protected Item getDefaultItem() {
      return ModItems.BLUE_NETHERSHROOM.get();
   }

   protected float getGravity() {
      return 0.05F;
   }

   protected void onHit(HitResult rtr) {
      super.onHit(rtr);
      ItemStack itemstack = this.getItem();
      Potion potion = PotionUtils.getPotion(itemstack);
      List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
      if (!list.isEmpty()) {
         if(!this.level.isClientSide){
            Entity target = null;
            if(rtr instanceof EntityHitResult){
               target = ((EntityHitResult) rtr).getEntity();
            }
            this.makeAreaOfEffectCloud(target, itemstack, potion);
            this.remove(RemovalReason.DISCARDED);
         } else{
            BlockPos blockPos = this.blockPosition();
            int color = PotionUtils.getColor(potion);
            CompoundTag tag = itemstack.getTag();
            if (tag != null && tag.contains("CustomPotionColor", 99)) {
               color = tag.getInt("CustomPotionColor");
            }
            com.infamous.dungeons_mobs.client.util.ParticleGenerator.generatePotionImpact(this.level, potion, this.getItem(), blockPos, color, ModSoundEvents.FUNGUS_THROWER_FUNGUS_LAND.get());
         }
      }
   }

   private void makeAreaOfEffectCloud(@Nullable Entity target, ItemStack itemStack, Potion potion) {
      AreaEffectCloud aoeCloud = new AreaEffectCloud(this.level,
              target != null ? target.getX() : this.getX(),
              target != null ? target.getY() : this.getY(),
              target != null ? target.getZ() : this.getZ());
      Entity owner = this.getOwner();
      if (owner instanceof LivingEntity) {
         aoeCloud.setOwner((LivingEntity)owner);
      }

      aoeCloud.setRadius(3.0F);
      aoeCloud.setRadiusOnUse(-0.5F);
      aoeCloud.setWaitTime(10);
      aoeCloud.setRadiusPerTick(-aoeCloud.getRadius() / (float)aoeCloud.getDuration());
      aoeCloud.setPotion(potion);

      for(MobEffectInstance effectinstance : PotionUtils.getCustomEffects(itemStack)) {
         aoeCloud.addEffect(new MobEffectInstance(effectinstance));
      }

      CompoundTag compoundnbt = itemStack.getTag();
      if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99)) {
         aoeCloud.setFixedColor(compoundnbt.getInt("CustomPotionColor"));
      }

      this.level.addFreshEntity(aoeCloud);
   }

   @Override
   public Packet<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}