package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
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

public class CustomIllusionerRenderer extends MobRenderer<DungeonsIllusionerEntity, IllagerBipedModel<DungeonsIllusionerEntity>> {
    private static final ResourceLocation ILLUSIONER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/illusioner.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CustomIllusionerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new IllagerBipedModel<>(0.0F, 0.0F, 64, 64), 0.5f);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new BipedArmorLayer(this, new IllagerArmorModel(0.5F), new IllagerArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<DungeonsIllusionerEntity, IllagerBipedModel<DungeonsIllusionerEntity>>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DungeonsIllusionerEntity illusioner, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (illusioner.isCastingSpell() || illusioner.isAggressive() || illusioner.getArmPose() == AbstractIllagerEntity.ArmPose.NEUTRAL) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, illusioner, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        this.model.hat.visible = true;
    }

    @Override
    protected void scale(DungeonsIllusionerEntity illusioner, MatrixStack matrixStack, float v) {
        float scaleFactor = 0.9375F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(illusioner, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(DungeonsIllusionerEntity illusioner) {
        return ILLUSIONER_TEXTURE;
    }
}