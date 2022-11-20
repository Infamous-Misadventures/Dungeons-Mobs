package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.geom.ModModelLayers;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.client.renderer.layers.IllagerBipedArmorLayer;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Vindicator;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomVindicatorRenderer extends MobRenderer<Vindicator, IllagerBipedModel<Vindicator>> {
    private static final ResourceLocation GOLD_ARMORED_VINDICATOR_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/gold_armored_vindicator.png");
    private static final ResourceLocation DIAMOND_ARMORED_VINDICATOR_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/diamond_armored_vindicator.png");
    private static final ResourceLocation VINDICATOR_TEXTURE = new ResourceLocation("textures/entity/illager/vindicator.png");
    private static final ResourceLocation MOUNTAINEER_TEXTURE = new ResourceLocation(MODID,"textures/entity/illager/mountaineer.png");
    private static final ResourceLocation VINDICATOR_CHEF_TEXTURE = new ResourceLocation(MODID, "textures/entity/illager/vindicator_chef.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CustomVindicatorRenderer(EntityRendererProvider.Context renderContext) {
        super(renderContext, new IllagerBipedModel<>(renderContext.bakeLayer(ModModelLayers.BIPED_ILLAGER_MODEL)), 0.5f);
        this.addLayer(new HumanoidArmorLayer(this, new IllagerArmorModel(renderContext.bakeLayer(ModModelLayers.ILLAGER_ARMOR_MODEL_INNER_LAYER)), new IllagerArmorModel(renderContext.bakeLayer(ModModelLayers.ILLAGER_ARMOR_MODEL_OUTER_LAYER))));
        this.addLayer(new CustomHeadLayer<>(this, renderContext.getModelSet()));
        this.addLayer(new ItemInHandLayer<Vindicator, IllagerBipedModel<Vindicator>>(this) {
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Vindicator vindicatorEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (vindicatorEntity.isAggressive() || vindicatorEntity.getArmPose() == AbstractIllager.IllagerArmPose.NEUTRAL) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, vindicatorEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    @Override
    protected void scale(Vindicator vindicatorEntity, PoseStack matrixStack, float v) {
        float scaleFactor = 0.9375F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(vindicatorEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(Vindicator entity) {
        if(entity instanceof MountaineerEntity){
            return MOUNTAINEER_TEXTURE;
        } else if(entity.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(ModItems.DIAMOND_VINDICATOR_HELMET.get())){
            return DIAMOND_ARMORED_VINDICATOR_TEXTURE;
        }else if(entity.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(ModItems.GOLD_VINDICATOR_HELMET.get())){
            return GOLD_ARMORED_VINDICATOR_TEXTURE;
        } else {
            return VINDICATOR_TEXTURE;
        }
    }


}