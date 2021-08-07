package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.DrownedModel;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class CustomDrownedRenderer extends AbstractZombieRenderer<DrownedEntity, DrownedModel<DrownedEntity>> {
    private static final ResourceLocation DROWNED_LOCATION = new ResourceLocation("textures/entity/zombie/drowned.png");
    private static final ResourceLocation SEAWEED_ARMORED_DROWNED_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/seaweed_armored_drowned.png");
    private static final ResourceLocation PALE_ARMORED_DROWNED_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/pale_armored_drowned.png");

    public CustomDrownedRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new DrownedModel<>(0.0F, 0.0F, 64, 64), new DrownedModel<>(0.5F, true), new DrownedModel<>(1.0F, true));
        this.addLayer(new CustomDrownedOuterLayer<>(this));
    }

    @Override
    protected void scale(DrownedEntity drowned, MatrixStack matrixStack, float p_225620_3_) {
        if(drowned instanceof IArmoredMob){
            float scaleFactor = 1.1F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        super.scale(drowned, matrixStack, p_225620_3_);
    }

    @Override
    protected void setupRotations(DrownedEntity drowned, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupRotations(drowned, matrixStack, p_225621_3_, p_225621_4_, p_225621_5_);
        float swimAmount = drowned.getSwimAmount(p_225621_5_);
        if (swimAmount > 0.0F) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(swimAmount, drowned.xRot, -10.0F - drowned.xRot)));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(DrownedEntity drowned) {
        if(drowned instanceof IArmoredMob){
            if(((IArmoredMob) drowned).hasStrongArmor()){
                return PALE_ARMORED_DROWNED_LOCATION;
            } else{
                return SEAWEED_ARMORED_DROWNED_LOCATION;
            }
        } else{
            return DROWNED_LOCATION;
        }
    }
}
