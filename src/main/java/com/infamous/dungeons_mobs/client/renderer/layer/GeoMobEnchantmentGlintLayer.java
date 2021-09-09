package com.infamous.dungeons_mobs.client.renderer.layer;

import static com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.QUICK;

import com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper;
import com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.CreeperChargeLayer;
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
		private GeoModelProvider<T> modelProvider;
	   
	   public GeoMobEnchantmentGlintLayer(IGeoRenderer<T> geoRenderer) {
		      super(geoRenderer);
		   }

			@Override
			public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
					T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
					float netHeadYaw, float headPitch) {
		    //    getEnchantableCapability(entitylivingbaseIn).ifPresent(cap -> {
		    //        if(cap.hasEnchantment()) {
		            	
		  		          GeoModelProvider<T> geomodel = (GeoModelProvider<T>)this.getEntityModel();
					      renderModel(geomodel, this.getTextureLocation(), matrixStackIn, bufferIn, 1572, entitylivingbaseIn, 1.0F, 1.0F, 1.0F, 1.0F);    
					      
		         //   }
		            
				//});
			}
			
			@Override
			public RenderType getRenderType(ResourceLocation textureLocation) {
				return RenderType.entityGlint();
			}

		   protected float xOffset(float p_225634_1_) {
			      return p_225634_1_ * 0.01F;
			   }

			   protected ResourceLocation getTextureLocation() {
			      return POWER_LOCATION;
			   }
			   
		}
