package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.AbstractVineModel;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class AbstractVineRenderer<M extends AbstractVineModel> extends GeoEntityRenderer<AbstractVineEntity> {

	public AbstractVineRenderer(EntityRendererProvider.Context renderManager, M model) {
		super(renderManager, model);
	}

	public boolean isShaking(AbstractVineEntity p_230495_1_) {
		return p_230495_1_.isInWrongHabitat();
	}

	@Override
	protected void applyRotations(AbstractVineEntity entityLiving, PoseStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {
		if (this.isShaking(entityLiving)) {
			rotationYaw += (float) (Math.cos((double) entityLiving.tickCount * 3.25D) * Math.PI * (double) 0.4F);
		}
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}
	
	@Override
	protected int getBlockLightLevel(AbstractVineEntity p_225624_1_, BlockPos p_225624_2_) {
		return p_225624_1_.isOnFire() ? 15 : p_225624_1_.level.getBrightness(LightLayer.BLOCK, p_225624_1_.getParts()[0].blockPosition());
	}

	@Override
	public RenderType getRenderType(AbstractVineEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(textureLocation);
	}

	@Override
	protected float getDeathMaxRotation(AbstractVineEntity entityLivingBaseIn) {
		return 0;
	}

	public ResourceLocation getTextureLocation(AbstractVineEntity entity) {
		return super.getTextureLocation(entity);
	}
}