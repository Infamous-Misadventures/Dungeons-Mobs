package com.infamous.dungeons_mobs.client.renderer.redstone;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.redstone.RedstoneGolemModel;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoEyeLayer;
import com.infamous.dungeons_mobs.client.renderer.layers.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RedstoneGolemRenderer extends GeoEntityRenderer<RedstoneGolemEntity> {
    @SuppressWarnings("unchecked")
    public RedstoneGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RedstoneGolemModel());
        this.addLayer(new GeoEyeLayer<RedstoneGolemEntity>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/redstone/redstone_golem_light.png")) {
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                               RedstoneGolemEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                               float ageInTicks, float netHeadYaw, float headPitch) {
                if (!entitylivingbaseIn.isSummoningMines()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
                            ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        this.addLayer(new PulsatingGlowLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/redstone/redstone_golem_yellow_light.png"), 0.1F, 0.5F, 0.0F) {
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                               LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                               float ageInTicks, float netHeadYaw, float headPitch) {

                RedstoneGolemEntity redstoneGolem = ((RedstoneGolemEntity) entitylivingbaseIn);

                if (!redstoneGolem.isSummoningMines()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
                            ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        this.addLayer(new GeoEyeLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/redstone/redstone_golem_yellow_light.png")) {
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                               LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                               float ageInTicks, float netHeadYaw, float headPitch) {

                RedstoneGolemEntity redstoneGolem = ((RedstoneGolemEntity) entitylivingbaseIn);

                if (redstoneGolem.isSummoningMines()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
                            ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        this.addLayer(new PulsatingGlowLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/redstone/redstone_golem_white_light.png"), 0.2F, 0.75F, 0.0F) {
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                               LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                               float ageInTicks, float netHeadYaw, float headPitch) {

                RedstoneGolemEntity redstoneGolem = ((RedstoneGolemEntity) entitylivingbaseIn);

                if (redstoneGolem.isSummoningMines()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
                            ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    protected void applyRotations(RedstoneGolemEntity entityLiving, PoseStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 1.0f;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public RenderType getRenderType(RedstoneGolemEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}