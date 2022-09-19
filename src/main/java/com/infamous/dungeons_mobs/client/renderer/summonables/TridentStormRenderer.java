package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.TridentStormModel;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;
import com.infamous.dungeons_mobs.entities.summonables.WindcallerTornadoEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class TridentStormRenderer extends GeoProjectilesRenderer<TridentStormEntity> {
   public TridentStormRenderer(EntityRendererManager renderManager) {
      super(renderManager, new TridentStormModel());
   }
   
	@Override
	public void renderEarly(TridentStormEntity animatable, MatrixStack stackIn, float partialTicks,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		
		stackIn.mulPose(Vector3f.YP.rotationDegrees(animatable.yRot * ((float) Math.PI / 180F)));
		
		if (animatable.lifeTime <= 1) {
			float scaleFactor = 0.0F;
			stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		} else {
			
		}
	}
   
   @Override
	protected int getBlockLightLevel(TridentStormEntity p_225624_1_, BlockPos p_225624_2_) {
		return 15;
	}

   @Override
   public RenderType getRenderType(TridentStormEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}