package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.undead.SunkenSkeletonModel;
import com.infamous.dungeons_mobs.entities.water.SunkenSkeletonEntity;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SunkenSkeletonRenderer<T extends SunkenSkeletonEntity> extends BipedRenderer<T, SunkenSkeletonModel<T>> {
   private static final ResourceLocation SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/sunken_skeleton.png");
   private static final ResourceLocation RED_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/red_coral_armored_sunken_skeleton.png");
   private static final ResourceLocation YELLOW_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/yellow_coral_armored_sunken_skeleton.png");

   public SunkenSkeletonRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new SunkenSkeletonModel<>(), 0.5F);
      this.addLayer(new BipedArmorLayer<>(this, new SunkenSkeletonModel<>(0.5F, true), new SunkenSkeletonModel<>(1.0F, true)));
   }

   @Override
   protected void scale(T skeleton, MatrixStack matrixStack, float v) {
      if(skeleton instanceof IArmoredMob){
         float scaleFactor = 1.1F;
         matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
      }
      super.scale(skeleton, matrixStack, v);
   }

   @Override
   protected void setupRotations(T skeleton, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
      super.setupRotations(skeleton, matrixStack, p_225621_3_, p_225621_4_, p_225621_5_);
      float swimAmount = skeleton.getSwimAmount(p_225621_5_);
      if (swimAmount > 0.0F) {
         matrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(swimAmount, skeleton.xRot, -10.0F - skeleton.xRot)));
      }
   }

   @Override
   public ResourceLocation getTextureLocation(T skeleton) {
         if(skeleton instanceof IArmoredMob){
            if(((IArmoredMob) skeleton).hasStrongArmor()){
               return YELLOW_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION;
            } else{
               return RED_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION;
            }
         } else {
            return SUNKEN_SKELETON_LOCATION;
         }
   }
}