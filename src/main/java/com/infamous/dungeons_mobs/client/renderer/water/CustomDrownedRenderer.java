package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;

import java.util.Arrays;
import java.util.List;

public class CustomDrownedRenderer extends AbstractZombieRenderer<Drowned, DrownedModel<Drowned>> {
    private static final ResourceLocation DROWNED_LOCATION = new ResourceLocation("textures/entity/zombie/drowned.png");
    private static final ResourceLocation SEAWEED_ARMORED_DROWNED_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/seaweed_armored_drowned.png");
    private static final ResourceLocation PALE_ARMORED_DROWNED_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/pale_armored_drowned.png");
    private static final List<ResourceLocation> ARMORED_DROWNED_LOCATIONS = Arrays.asList(SEAWEED_ARMORED_DROWNED_LOCATION, PALE_ARMORED_DROWNED_LOCATION);

    public CustomDrownedRenderer(EntityRendererProvider.Context rendererContext) {
        super(rendererContext, new DrownedModel<>(rendererContext.bakeLayer(ModelLayers.DROWNED)), new DrownedModel<>(rendererContext.bakeLayer(ModelLayers.DROWNED_INNER_ARMOR)), new DrownedModel<>(rendererContext.bakeLayer(ModelLayers.DROWNED_OUTER_ARMOR)));
        this.addLayer(new CustomDrownedOuterLayer<>(this, new DrownedModel<>(rendererContext.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER))));
    }

    @Override
    protected void scale(Drowned drowned, PoseStack matrixStack, float p_225620_3_) {
        super.scale(drowned, matrixStack, p_225620_3_);
    }

    @Override
    protected void setupRotations(Drowned drowned, PoseStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupRotations(drowned, matrixStack, p_225621_3_, p_225621_4_, p_225621_5_);
        float swimAmount = drowned.getSwimAmount(p_225621_5_);
        if (swimAmount > 0.0F) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(swimAmount, drowned.getXRot(), -10.0F - drowned.getXRot())));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie drowned) {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(drowned);
        if(cap != null && cap.isElite()){
            return ARMORED_DROWNED_LOCATIONS.get(drowned.getId() % ARMORED_DROWNED_LOCATIONS.size());
        } else{
            return DROWNED_LOCATION;
        }
    }
}
