package com.infamous.dungeons_mobs.client.models.ocean;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

/**
 * Necromancer - BklynKian
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class DrownedNecromancerModel extends AnimatedGeoModel<DrownedNecromancerEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(DrownedNecromancerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/drowned_necromancer.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DrownedNecromancerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/drowned_necromancer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DrownedNecromancerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/drowned_necromancer.png");
    }

    @Override
    public void setLivingAnimations(DrownedNecromancerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }
}
