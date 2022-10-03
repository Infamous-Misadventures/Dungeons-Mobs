package com.infamous.dungeons_mobs.client.renderer.water;

import java.util.Arrays;
import java.util.List;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.DrownedModel;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class CustomDrownedRenderer extends AbstractZombieRenderer<DrownedEntity, DrownedModel<DrownedEntity>> {
    private static final ResourceLocation DROWNED_LOCATION = new ResourceLocation("textures/entity/zombie/drowned.png");
    private static final ResourceLocation SEAWEED_ARMORED_DROWNED_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/seaweed_armored_drowned.png");
    private static final ResourceLocation PALE_ARMORED_DROWNED_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/pale_armored_drowned.png");
    private static final List<ResourceLocation> ARMORED_DROWNED_LOCATIONS = Arrays.asList(SEAWEED_ARMORED_DROWNED_LOCATION, PALE_ARMORED_DROWNED_LOCATION);

    public CustomDrownedRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new DrownedModel<>(0.0F, 0.0F, 64, 64), new DrownedModel<>(0.5F, true), new DrownedModel<>(1.0F, true));
        this.addLayer(new CustomDrownedOuterLayer<>(this));
    }

    @Override
    protected void scale(DrownedEntity drowned, MatrixStack matrixStack, float p_225620_3_) {
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
    public ResourceLocation getTextureLocation(ZombieEntity drowned) {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(drowned);
        if(cap != null && cap.isElite()){
            return ARMORED_DROWNED_LOCATIONS.get(drowned.getId() % ARMORED_DROWNED_LOCATIONS.size());
        } else{
            return DROWNED_LOCATION;
        }
    }
}
