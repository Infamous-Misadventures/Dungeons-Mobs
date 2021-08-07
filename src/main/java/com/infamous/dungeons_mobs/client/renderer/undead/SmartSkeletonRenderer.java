package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.undead.SkeletonVanguardModel;
import com.infamous.dungeons_mobs.client.models.undead.SmartSkeletonModel;
import com.infamous.dungeons_mobs.entities.undead.ArmoredSkeletonEntity;
import com.infamous.dungeons_mobs.entities.water.SunkenSkeletonEntity;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SmartSkeletonRenderer<T extends AbstractSkeletonEntity> extends BipedRenderer<T, SmartSkeletonModel<T>> {
   private static final ResourceLocation VANILLA_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");
   private static final ResourceLocation SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/sunken_skeleton.png");
   private static final ResourceLocation RED_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/red_coral_armored_sunken_skeleton.png");
   private static final ResourceLocation YELLOW_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/yellow_coral_armored_sunken_skeleton.png");

   public SmartSkeletonRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new SmartSkeletonModel<>(), 0.5F);
      this.addLayer(new BipedArmorLayer<>(this, new SmartSkeletonModel<>(0.5F, true), new SmartSkeletonModel<>(1.0F, true)));
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
      if(skeleton instanceof SunkenSkeletonEntity){
         if(skeleton instanceof IArmoredMob){
            if(((IArmoredMob) skeleton).hasStrongArmor()){
               return YELLOW_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION;
            } else{
               return RED_CORAL_ARMORED_SUNKEN_SKELETON_LOCATION;
            }
         } else{
            return SUNKEN_SKELETON_LOCATION;
         }
      } else{
         return VANILLA_SKELETON_LOCATION;
      }
   }
}