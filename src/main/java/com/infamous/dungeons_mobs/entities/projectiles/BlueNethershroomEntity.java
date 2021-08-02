package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.utils.PotionHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = IRendersAsItem.class
)
public class BlueNethershroomEntity extends ProjectileItemEntity implements IRendersAsItem {

   public static final int LIGHT_BLUE_HEX_COLOR_CODE = 0x00e0ff;

   public BlueNethershroomEntity(EntityType<? extends BlueNethershroomEntity> entityType, World world) {
      super(entityType, world);
   }

   public BlueNethershroomEntity(World world, LivingEntity shooter) {
      super(ModEntityTypes.BLUE_NETHERSHROOM.get(), shooter, world);
   }

   public BlueNethershroomEntity(World world, double x, double y, double z) {
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

   protected void onHit(RayTraceResult rtr) {
      super.onHit(rtr);
      ItemStack itemstack = this.getItem();
      Potion potion = PotionUtils.getPotion(itemstack);
      List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
      if (!list.isEmpty()) {
         if(!this.level.isClientSide){
            Entity target = null;
            if(rtr instanceof EntityRayTraceResult){
               target = ((EntityRayTraceResult) rtr).getEntity();
            }
            this.makeAreaOfEffectCloud(target, itemstack, potion);
            this.remove();
         } else{
            BlockPos blockPos = this.blockPosition();
            int color = PotionUtils.getColor(potion);
            CompoundNBT tag = itemstack.getTag();
            if (tag != null && tag.contains("CustomPotionColor", 99)) {
               color = tag.getInt("CustomPotionColor");
            }
            com.infamous.dungeons_mobs.client.util.ParticleGenerator.generatePotionImpact(this.level, potion, this.getItem(), blockPos, color, SoundEvents.FUNGUS_BREAK);
         }
      }
   }

   private void makeAreaOfEffectCloud(@Nullable Entity target, ItemStack itemStack, Potion potion) {
      AreaEffectCloudEntity aoeCloud = new AreaEffectCloudEntity(this.level,
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

      for(EffectInstance effectinstance : PotionUtils.getCustomEffects(itemStack)) {
         aoeCloud.addEffect(new EffectInstance(effectinstance));
      }

      CompoundNBT compoundnbt = itemStack.getTag();
      if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99)) {
         aoeCloud.setFixedColor(compoundnbt.getInt("CustomPotionColor"));
      }

      this.level.addFreshEntity(aoeCloud);
   }

   @Override
   public IPacket<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}