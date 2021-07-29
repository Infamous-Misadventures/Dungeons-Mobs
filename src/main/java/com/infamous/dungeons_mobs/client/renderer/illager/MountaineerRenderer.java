package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class MountaineerRenderer extends MobRenderer<VindicatorEntity, IllagerBipedModel<VindicatorEntity>> {
    private static final ResourceLocation MOUNTAINEER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/mountaineer.png");
    private static final ResourceLocation GOLD_ARMORED_MOUNTAINEER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/gold_armored_mountaineer.png");
    private static final ResourceLocation DIAMOND_ARMORED_MOUNTAINEER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/diamond_armored_mountaineer.png");
    private static final ResourceLocation VINDICATOR_TEXTURE = new ResourceLocation("textures/entity/illager/vindicator.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MountaineerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new IllagerBipedModel<>(0.0F, 0.0F, 64, 64), 0.5f);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new BipedArmorLayer(this, new IllagerArmorModel(0.5F), new IllagerArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<VindicatorEntity, IllagerBipedModel<VindicatorEntity>>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, VindicatorEntity vindicatorEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (vindicatorEntity.isAggressive() || vindicatorEntity.getArmPose() == AbstractIllagerEntity.ArmPose.NEUTRAL) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, vindicatorEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        this.model.hat.visible = true; // apparently this cost tallestred hours of his life
    }

    @Override
    protected void scale(VindicatorEntity vindicatorEntity, MatrixStack matrixStack, float v) {
        if(vindicatorEntity instanceof ArmoredMountaineerEntity){
            float scaleFactor = 1.1F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        else{
            float scaleFactor = 0.9375F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        super.scale(vindicatorEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(VindicatorEntity entity) {
        if(entity.getClass() == ArmoredMountaineerEntity.class){
            ArmoredMountaineerEntity armoredMountaineerEntity = (ArmoredMountaineerEntity)entity;
            if(armoredMountaineerEntity.isDiamond()){
                return DIAMOND_ARMORED_MOUNTAINEER_TEXTURE;
            }
            else return GOLD_ARMORED_MOUNTAINEER_TEXTURE;
        }
        else if(entity instanceof MountaineerEntity){
            return MOUNTAINEER_TEXTURE;
        }
        else{
            return VINDICATOR_TEXTURE;
        }
    }
}