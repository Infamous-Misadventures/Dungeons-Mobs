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

	public PulsatingGlowLayer(IGeoRenderer<T> endermanReplacementRenderer, ResourceLocation textureLocation) {
		super(endermanReplacementRenderer);
		this.textureLocation = textureLocation;
	}

		@Override
		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
				T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
				float ageInTicks, float netHeadYaw, float headPitch) {
			
		      GeoModelProvider<T> geomodel = (GeoModelProvider<T>)this.getEntityModel();
		      float glow = Math.max(0.0F, MathHelper.cos(ageInTicks * 0.045F) * 0.25F);
			  renderModel(geomodel, textureLocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, glow, glow, glow);    
		   }
		
		@Override
		public RenderType getRenderType(ResourceLocation textureLocation) {
			return RenderType.eyes(textureLocation);
		}

}