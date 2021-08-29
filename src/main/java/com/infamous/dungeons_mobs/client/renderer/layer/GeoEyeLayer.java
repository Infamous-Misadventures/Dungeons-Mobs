package com.infamous.dungeons_mobs.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class GeoEyeLayer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
	   
	public ResourceLocation textureLocation;

	public GeoEyeLayer(IGeoRenderer<T> endermanReplacementRenderer, ResourceLocation textureLocation) {
		super(endermanReplacementRenderer);
		this.textureLocation = textureLocation;
	}

		@Override
		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
				T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
				float ageInTicks, float netHeadYaw, float headPitch) {
			
			//if (entitylivingbaseIn instanceof JungleerEntity) {
			//matrixStackIn.scale(1.35F, 1.35F, 1.35F);
			//}
			
		      GeoModelProvider<T> geomodel = (GeoModelProvider<T>)this.getEntityModel();
				//IGeoRenderer<LivingEntity> renderer = (IGeoRenderer<LivingEntity>) AnimationUtils.getRenderer(entitylivingbaseIn);
		     // if (entitylivingbaseIn instanceof ChorusGormandizerEntity && !((ChorusGormandizerEntity)entitylivingbaseIn).hasFur()) {
			  //    renderModel(geomodel, new ResourceLocation(ChorusGormandizers.MOD_ID, "textures/entities/chorus_gormandizer_eyes_shaved.png"), matrixStackIn, bufferIn, 1572, entitylivingbaseIn, 1.0F, 1.0F, 1.0F, 1.0F);  
		     // } else {
			      renderModel(geomodel, textureLocation, matrixStackIn, bufferIn, 1572, entitylivingbaseIn, 1.0F, 1.0F, 1.0F, 1.0F);    
		     // }
		      
			//	if (entitylivingbaseIn instanceof CrimsonerEntity) {
			//		matrixStackIn.scale(((CrimsonerEntity)entitylivingbaseIn).getAge(), ((CrimsonerEntity)entitylivingbaseIn).getAge(), ((CrimsonerEntity)entitylivingbaseIn).getAge());
			//	}
		   }

}