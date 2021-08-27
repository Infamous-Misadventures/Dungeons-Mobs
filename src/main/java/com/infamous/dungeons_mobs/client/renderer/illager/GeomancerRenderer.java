package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.illager.GeomancerModel;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class GeomancerRenderer extends MobRenderer<GeomancerEntity, GeomancerModel<GeomancerEntity>> {

    private static final ResourceLocation GEOMANCER_TEXTURE = new ResourceLocation(MODID,"textures/entity/illager/geomancer.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GeomancerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new GeomancerModel<>(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new BipedArmorLayer(this, new IllagerArmorModel(0.5F), new IllagerArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<GeomancerEntity, GeomancerModel<GeomancerEntity>>(this) {
            @Override
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, GeomancerEntity geomancerEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (geomancerEntity.isCastingSpell() || geomancerEntity.isAggressive() || geomancerEntity.getArmPose() == AbstractIllagerEntity.ArmPose.NEUTRAL) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, geomancerEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    @Override
    protected void scale(GeomancerEntity geomancerEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 0.9375F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(geomancerEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(GeomancerEntity geomancerEntity) {
        return GEOMANCER_TEXTURE;
    }
}
