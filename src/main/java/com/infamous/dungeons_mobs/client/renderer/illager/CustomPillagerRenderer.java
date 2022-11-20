package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.geom.ModModelLayers;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Pillager;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomPillagerRenderer extends MobRenderer<Pillager, IllagerBipedModel<Pillager>> {
    private static final ResourceLocation GOLD_ARMORED_PILLAGER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/gold_armored_pillager.png");
    private static final ResourceLocation DIAMOND_ARMORED_PILLAGER_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/diamond_armored_pillager.png");
    private static final ResourceLocation PILLAGER_TEXTURE = new ResourceLocation("textures/entity/illager/pillager.png");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CustomPillagerRenderer(EntityRendererProvider.Context renderContext) {
        super(renderContext, new IllagerBipedModel<>(renderContext.bakeLayer(ModModelLayers.BIPED_ILLAGER_MODEL)), 0.5f);
        this.addLayer(new HumanoidArmorLayer(this, new IllagerArmorModel(renderContext.bakeLayer(ModModelLayers.ILLAGER_ARMOR_MODEL_INNER_LAYER)), new IllagerArmorModel(renderContext.bakeLayer(ModModelLayers.ILLAGER_ARMOR_MODEL_OUTER_LAYER))));
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new CustomHeadLayer<>(this, renderContext.getModelSet()));
    }

    @Override
    protected void scale(Pillager pillagerEntity, PoseStack matrixStack, float v) {
        float scaleFactor = 0.9375F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(pillagerEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(Pillager entity) {
        if(entity.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(ModItems.DIAMOND_PILLAGER_HELMET.get())){
            return DIAMOND_ARMORED_PILLAGER_TEXTURE;
        }else if(entity.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(ModItems.GOLD_PILLAGER_HELMET.get())){
            return GOLD_ARMORED_PILLAGER_TEXTURE;
        } else {
            return PILLAGER_TEXTURE;
        }
    }
}