package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class IceologerModel extends AnimatedGeoModel {

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/iceologer.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/iceologer.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/iceologer.png");
    }

    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        LivingEntity entityIn = (LivingEntity) entity;

        IBone head = this.getAnimationProcessor().getBone("bipedHead");
        IBone armorHead = this.getAnimationProcessor().getBone("armorBipedHead");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
//            armorHead.setRotationX(-(head.getRotationX()/2));
//            armorHead.setRotationY(-(head.getRotationY()/2));
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }
}
