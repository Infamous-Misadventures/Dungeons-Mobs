package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_mobs.interfaces.IHasGeoArm;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class GeoHeldItemLayer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {

	public double xPos;
	public double yPos;
	public double zPos;

	public GeoHeldItemLayer(IGeoRenderer<T> endermanReplacementRenderer, double x, double y, double z) {
		super(endermanReplacementRenderer);
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
	}

	   private void renderArmWithItem(T p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack p_229135_5_, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
	      if (!p_229135_2_.isEmpty()) {
	         p_229135_5_.pushPose();
	       //  if (p_229135_2_.getItem() == Items.BOW) {
	       //  p_229135_5_.mulPose(Vector3f.XP.rotationDegrees(-270.0F));
	        // } else {
	         //}
		     p_229135_5_.mulPose(Vector3f.XP.rotationDegrees(-90.0F));	        	 
	         p_229135_5_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
	         boolean flag = p_229135_4_ == HandSide.LEFT;
	      //   if (p_229135_2_.getItem() == Items.BOW) {
		  //       p_229135_5_.translate(flag ? xPos : -xPos, yPos, -zPos * 1.25);  	 
	     //    } else {
		         p_229135_5_.translate(flag ? xPos : -xPos, yPos, zPos);	 
	      //   }
		         ((IHasGeoArm)this.getEntityModel()).translateToHand(p_229135_4_, p_229135_5_);
	         Minecraft.getInstance().getItemInHandRenderer().renderItem(p_229135_1_, p_229135_2_, p_229135_3_, flag, p_229135_5_, p_229135_6_, p_229135_7_);
	         p_229135_5_.popPose();
	      }
	   }

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
			T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {

	      boolean flag = entitylivingbaseIn.getMainArm() == HandSide.RIGHT;
	      ItemStack itemstack = flag ? entitylivingbaseIn.getOffhandItem() : entitylivingbaseIn.getMainHandItem();
	      ItemStack itemstack1 = flag ? entitylivingbaseIn.getMainHandItem() : entitylivingbaseIn.getOffhandItem();
	      if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
	    	  matrixStackIn.pushPose();

	         this.renderArmWithItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
	         this.renderArmWithItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
	         matrixStackIn.popPose();
	      }
		
	}
	}