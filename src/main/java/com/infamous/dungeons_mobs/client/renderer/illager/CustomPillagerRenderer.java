package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.entities.illagers.ArmoredPillagerEntity;
import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomPillagerRenderer extends MobRenderer<PillagerEntity, IllagerBipedModel<PillagerEntity>> {
    private static final ResourceLocation GOLD_ARMORED_PILLAGER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/gold_armored_pillager.png");
    private static final ResourceLocation DIAMOND_ARMORED_PILLAGER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/diamond_armored_pillager.png");
    private static final ResourceLocation PILLAGER_TEXTURE = new ResourceLocation("textures/entity/illager/pillager.png");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CustomPillagerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new IllagerBipedModel<>(0.0F, 0.0F, 64, 64), 0.5f);
        this.addLayer(new BipedArmorLayer(this, new IllagerArmorModel(0.5F), new IllagerArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new HeadLayer<PillagerEntity, IllagerBipedModel<PillagerEntity>>(this));
    }

    @Override
    protected void scale(PillagerEntity pillagerEntity, MatrixStack matrixStack, float v) {
        if(pillagerEntity instanceof ArmoredPillagerEntity){
            float scaleFactor = 1.1F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        else{
            float scaleFactor = 0.9375F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        super.scale(pillagerEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(PillagerEntity entity) {
        if(entity instanceof ArmoredPillagerEntity){
            ArmoredPillagerEntity armoredPillagerEntity = (ArmoredPillagerEntity)entity;
            if(armoredPillagerEntity.isDiamond()){
                return DIAMOND_ARMORED_PILLAGER_TEXTURE;
            }
            else return GOLD_ARMORED_PILLAGER_TEXTURE;
        }
        else{
            return PILLAGER_TEXTURE;
        }
    }
}