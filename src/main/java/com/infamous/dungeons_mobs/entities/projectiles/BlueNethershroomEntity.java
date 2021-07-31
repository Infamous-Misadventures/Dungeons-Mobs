package com.infamous.dungeons_mobs.entities.projectiles;

import java.util.List;
import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = IRendersAsItem.class
)
public class BlueNethershroomEntity extends ProjectileItemEntity implements IRendersAsItem {

   public BlueNethershroomEntity(EntityType<? extends BlueNethershroomEntity> entityType, World world) {
      super(entityType, world);
   }

   public BlueNethershroomEntity(World world, LivingEntity shooter) {
      super(ModEntityTypes.BLUE_NETHERSHROOM.get(), shooter, world);
   }

   public BlueNethershroomEntity(World world, double x, double y, double z) {
      super(ModEntityTypes.BLUE_NETHERSHROOM.get(), x, y, z, world);
   }

   protected Item getDefaultItem() {
      return ModItems.BLUE_NETHERSHROOM.get();
   }

   protected float getGravity() {
      return 0.05F;
   }

   protected void onHit(RayTraceResult rtr) {
      super.onHit(rtr);
      if (!this.level.isClientSide) {
         ItemStack itemstack = this.getItem();
         Potion potion = PotionUtils.getPotion(itemstack);
         List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
         if (!list.isEmpty()) {
            this.makeAreaOfEffectCloud(itemstack, potion);
         }

         int levelEventId = potion.hasInstantEffects() ? 2007 : 2002;
         this.level.levelEvent(levelEventId, this.blockPosition(), PotionUtils.getColor(itemstack));
         this.remove();
      }
   }

   private void makeAreaOfEffectCloud(ItemStack itemStack, Potion potion) {
      AreaEffectCloudEntity aoeCloud = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
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
}