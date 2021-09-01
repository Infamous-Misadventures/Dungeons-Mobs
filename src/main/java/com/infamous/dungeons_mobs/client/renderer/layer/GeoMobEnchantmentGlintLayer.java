package com.infamous.dungeons_mobs.client.renderer.layer;

import com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper;
import com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class GeoMobEnchantmentGlintLayer<T extends Entity & IAnimatable> extends GeoLayerRenderer<T> {
	   private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	   //private AnimatedGeoModel<T> modelProvider;
	   
	   public GeoMobEnchantmentGlintLayer(IGeoRenderer<T> geoRenderer) {
		      super(geoRenderer);
		   }

			@Override
			public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
					T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
					float netHeadYaw, float headPitch) {
				if (entitylivingbaseIn.getCapability(EnchantableProvider.ENCHANTABLE_CAPABILITY) != null) {
					// EnchantableHelper.getEnchantableCapability(entitylivingbaseIn).ifPresent(cap
					// -> {
					// if (cap.hasEnchantment()) {
					
					float f = (float) entitylivingbaseIn.tickCount + ageInTicks;
					IVertexBuilder ivertexbuilder = bufferIn
							.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f), f * 0.01F));
					RenderType renderType = this.getRenderType(this.getTextureLocation());
					
					GeoModelProvider<T> modelProviderIn = (GeoModelProvider<T>) this.getEntityModel();
					GeoModel model = modelProviderIn.getModel(modelProviderIn.getModelLocation(entitylivingbaseIn));
					this.getRenderer().render(model, entitylivingbaseIn, partialTicks, renderType, matrixStackIn,
							bufferIn, ivertexbuilder, packedLightIn,
							LivingRenderer.getOverlayCoords((LivingEntity) entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F,
							1.0F);
					// }
					// });
				}
			}

		   protected float xOffset(float p_225634_1_) {
			      return p_225634_1_ * 0.01F;
			   }

			   protected ResourceLocation getTextureLocation() {
			      return POWER_LOCATION;
			   }
		}
