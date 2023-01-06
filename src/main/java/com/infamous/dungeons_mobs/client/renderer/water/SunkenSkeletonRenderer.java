package com.infamous.dungeons_mobs.client.renderer.water;

import static com.infamous.dungeons_mobs.client.models.geom.ModModelLayers.SUNKEN_SKELETON;
import static net.minecraft.client.model.geom.ModelLayers.SKELETON_INNER_ARMOR;
import static net.minecraft.client.model.geom.ModelLayers.SKELETON_OUTER_ARMOR;

import java.util.Arrays;
import java.util.List;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.undead.SunkenSkeletonModel;
import com.infamous.dungeons_mobs.entities.water.SunkenSkeletonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SunkenSkeletonRenderer<T extends SunkenSkeletonEntity> extends HumanoidMobRenderer<T, SunkenSkeletonModel<T>> {
   private static final ResourceLocation SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/sunken_skeleton.png");
   private static final ResourceLocation RED_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/red_coral_armored_sunken_skeleton.png");
   private static final ResourceLocation YELLOW_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/yellow_coral_armored_sunken_skeleton.png");
   private static final List<ResourceLocation> ARMORED_SKELETON_LOCATIONS = Arrays.asList(RED_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION, YELLOW_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION);

   public SunkenSkeletonRenderer(EntityRendererProvider.Context renderContext) {
      super(renderContext, new SunkenSkeletonModel<>(renderContext.bakeLayer(SUNKEN_SKELETON)), 0.5F);
      this.addLayer(new HumanoidArmorLayer<>(this, new SunkenSkeletonModel<>(renderContext.bakeLayer(SKELETON_INNER_ARMOR)), new SunkenSkeletonModel<>(renderContext.bakeLayer(SKELETON_OUTER_ARMOR))));
   }

   @Override
   protected void scale(T skeleton, PoseStack matrixStack, float v) {
      super.scale(skeleton, matrixStack, v);
   }

   @Override
   protected void setupRotations(T skeleton, PoseStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
      super.setupRotations(skeleton, matrixStack, p_225621_3_, p_225621_4_, p_225621_5_);
      float swimAmount = skeleton.getSwimAmount(p_225621_5_);
      if (swimAmount > 0.0F) {
         matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(swimAmount, skeleton.getXRot(), -10.0F - skeleton.getXRot())));
      }
   }

   @Override
   public ResourceLocation getTextureLocation(T skeleton) {
//      EliteMob cap = EliteMobHelper.getEliteMobCapability(skeleton);
//      if(cap != null && cap.isElite()){
//         return ARMORED_SKELETON_LOCATIONS.get(skeleton.getId() % ARMORED_SKELETON_LOCATIONS.size());
//      } else{
      return SUNKEN_SKELETON_LOCATION;
//         }
   }
}