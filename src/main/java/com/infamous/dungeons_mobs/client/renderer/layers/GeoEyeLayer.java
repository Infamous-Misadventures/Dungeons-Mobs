package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.ender.BlastlingEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
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
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                       T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        GeoModelProvider<T> geomodel = this.getEntityModel();
        if (entitylivingbaseIn instanceof BlastlingEntity) {
            renderModel(geomodel, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ender/blastling" + (1 + ((int) ((BlastlingEntity) entitylivingbaseIn).flameTicks) % 3) + "_eyes.png"), matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            renderModel(geomodel, textureLocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 0.8F, 0.8F, 0.8F);
        }
    }

    @Override
    public RenderType getRenderType(ResourceLocation textureLocation) {
        return RenderType.eyes(textureLocation);
    }

}