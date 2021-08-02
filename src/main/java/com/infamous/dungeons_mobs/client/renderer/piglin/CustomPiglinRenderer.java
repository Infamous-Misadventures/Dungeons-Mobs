package com.infamous.dungeons_mobs.client.renderer.piglin;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.renderer.layer.FungusSackLayer;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CustomPiglinRenderer extends PiglinRenderer {
   public static final Map<EntityType<?>, Map<String, ResourceLocation>> RESOURCE_LOCATION_MAP = new HashMap<>();

   public CustomPiglinRenderer(EntityRendererManager rendererManager, boolean isZombified, boolean isFungusThrower) {
      super(rendererManager, isZombified);
      if(isFungusThrower){
         this.addLayer(new FungusSackLayer<>(this));
      }
   }

   @Override
   protected void scale(MobEntity mobEntity, MatrixStack matrixStack, float v) {
      if(mobEntity instanceof IArmoredMob){
         float scaleFactor = 1.2F;
         matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
      } else{
         super.scale(mobEntity, matrixStack, v);
      }
   }

   @Override
   public ResourceLocation getTextureLocation(MobEntity mobEntity) {
      // Check for the vanilla piglin or ziglin not holding a crossbow -  if so, we want to use the vanilla path for its texture
      boolean isVanillaMob = mobEntity.getType() == EntityType.PIGLIN || mobEntity.getType() == EntityType.ZOMBIFIED_PIGLIN;
      if(isVanillaMob && mobEntity instanceof ISmartCrossbowUser && !((ISmartCrossbowUser) mobEntity).isCrossbowUser()){
         return super.getTextureLocation(mobEntity);
      }

      String skinVariantName = this.getSkinVariantName(mobEntity);
      return RESOURCE_LOCATION_MAP
              .computeIfAbsent(mobEntity.getType(), type -> new HashMap<>())
              .computeIfAbsent(skinVariantName, s -> new ResourceLocation(DungeonsMobs.MODID, this.getPath(skinVariantName)));
   }

   private String getPath(String skinVariantName) {
      return "textures/entity/piglin/" + skinVariantName + ".png";
   }

   private String getSkinVariantName(MobEntity mobEntity){
      String skinVariantName = mobEntity.getType().getRegistryName().getPath();
      skinVariantName = maybeAddArmorPrefix(mobEntity, skinVariantName);
      skinVariantName = maybeAddHunterSuffix(mobEntity, skinVariantName);
      return skinVariantName;
   }

   private String maybeAddArmorPrefix(MobEntity mobEntity, String in) {
      if (mobEntity instanceof IArmoredMob) {
         return ((IArmoredMob) mobEntity).getArmorName() + "_" + in;
      }
      return in;
   }

   private String maybeAddHunterSuffix(MobEntity mobEntity, String in) {
      if(mobEntity instanceof ISmartCrossbowUser && ((ISmartCrossbowUser) mobEntity).isCrossbowUser()){
         return in + "_hunter";
      }
      return in;
   }

}