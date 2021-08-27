package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.illager.WindcallerModel;
import com.infamous.dungeons_mobs.client.renderer.layer.WindcallerCapeLayer;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
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

public class WindcallerRenderer extends MobRenderer<WindcallerEntity, WindcallerModel<WindcallerEntity>> {

    private static final ResourceLocation WINDCALLER_TEXTURE = new ResourceLocation(MODID,"textures/entity/illager/windcaller.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public WindcallerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new WindcallerModel<>(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new BipedArmorLayer(this, new IllagerArmorModel(0.5F), new IllagerArmorModel(1.0F)));
        this.addLayer(new WindcallerCapeLayer(this));
        this.addLayer(new HeldItemLayer<WindcallerEntity, WindcallerModel<WindcallerEntity>>(this) {
            @Override
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, WindcallerEntity windcallerEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (windcallerEntity.isCastingSpell() || windcallerEntity.isAggressive() || windcallerEntity.getArmPose() == AbstractIllagerEntity.ArmPose.NEUTRAL) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, windcallerEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
        //this.entityModel.bipedHeadwear.showModel = true; // apparently this cost tallestred hours of his life
    }

    @Override
    protected void scale(WindcallerEntity windcallerEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 0.9375F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(windcallerEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(WindcallerEntity windcallerEntity) {
        return WINDCALLER_TEXTURE;
    }
}
