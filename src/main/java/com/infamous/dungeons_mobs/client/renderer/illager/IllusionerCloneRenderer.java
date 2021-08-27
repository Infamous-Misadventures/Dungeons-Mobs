package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.ResourceLocation;

public class IllusionerCloneRenderer extends MobRenderer<IllusionerCloneEntity, IllagerBipedModel<IllusionerCloneEntity>> {

    private static final ResourceLocation ILLUSIONER_CLONE_TEXTURE = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/illusioner.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public IllusionerCloneRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new IllagerBipedModel<>(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new BipedArmorLayer(this, new IllagerArmorModel(0.5F), new IllagerArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<IllusionerCloneEntity, IllagerBipedModel<IllusionerCloneEntity>>(this) {
            @Override
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, IllusionerCloneEntity illusionerCloneEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (illusionerCloneEntity.isAggressive() || illusionerCloneEntity.getArmPose() == AbstractIllagerEntity.ArmPose.NEUTRAL) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, illusionerCloneEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        this.model.hat.visible = true; // apparently this cost tallestred hours of his life
    }

    @Override
    protected void scale(IllusionerCloneEntity illusionerCloneEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 0.9375F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(illusionerCloneEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(IllusionerCloneEntity entity) {
        return ILLUSIONER_CLONE_TEXTURE;
    }
}
