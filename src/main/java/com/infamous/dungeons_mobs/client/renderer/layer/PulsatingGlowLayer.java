package com.infamous.dungeons_mobs.client.renderer.layer;

import java.util.List;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.ender.BlastlingEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.summonables.GeomancerBombEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class PulsatingGlowLayer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {

	public ResourceLocation textureLocation;

	public float pulseSpeed;
	public float pulseAmount;
	public float minimumPulseAmount;
  
	public float pulseSpeedMath;

	public PulsatingGlowLayer(IGeoRenderer<T> endermanReplacementRenderer, ResourceLocation textureLocation,
							  float pulseSpeed,
							  float pulseAmount,
							  float minimumPulseAmount) {
		super(endermanReplacementRenderer);
		this.textureLocation = textureLocation;
		this.pulseSpeed = pulseSpeed;
		this.pulseAmount = pulseAmount;
		this.minimumPulseAmount = minimumPulseAmount;
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
					   T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
					   float ageInTicks, float netHeadYaw, float headPitch) {

		GeoModelProvider<T> geomodel = (GeoModelProvider<T>)this.getEntityModel();

		// original speed: 0.045F
		// original amount: 0.25F
		float glow = Math.max(minimumPulseAmount, MathHelper.cos(ageInTicks * pulseSpeed) * pulseAmount);

		if (entitylivingbaseIn instanceof RedstoneGolemEntity) {
			if (((RedstoneGolemEntity)entitylivingbaseIn).isSummoningMines() && ((RedstoneGolemEntity)entitylivingbaseIn).getAttackTimer() >= 20 && ((RedstoneGolemEntity)entitylivingbaseIn).getAttackTimer() <= 77)
				glow = Math.max(minimumPulseAmount, MathHelper.cos(ageInTicks * pulseSpeed * ((((RedstoneGolemEntity)entitylivingbaseIn).getAttackTimer()) / 50.0f)) * pulseAmount);

		}
		renderModel(geomodel, textureLocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, glow, glow, glow);
	}

	@Override
	public RenderType getRenderType(ResourceLocation textureLocation) {
		return RenderType.eyes(textureLocation);
	}

}
