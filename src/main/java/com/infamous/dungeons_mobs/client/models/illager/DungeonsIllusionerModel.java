package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DungeonsIllusionerModel extends AnimatedGeoModel {

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/illusioner.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/illager_geo.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        //ChorusGormandizerEntity entityIn = (ChorusGormandizerEntity) entity;
        return new ResourceLocation(DungeonsMobs.MODID, "textures/geo_entity/illager/illusioner.png");
    }

    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        LivingEntity entityIn = (LivingEntity) entity;
        IBone leftEye = this.getAnimationProcessor().getBone("lefteye");
        IBone rightEye = this.getAnimationProcessor().getBone("righteye");
        IBone eyeBrow = this.getAnimationProcessor().getBone("head3");
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        IBone s1 = this.getAnimationProcessor().getBone("armourHead2");
        IBone s2 = this.getAnimationProcessor().getBone("h");
        s1.setHidden(true);
        s2.setHidden(true);
        IBone s3 = this.getAnimationProcessor().getBone("armourHead");
        s3.setHidden(false);
        boolean g3 =(entityIn.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof GeoArmorItem);

        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            rightEye.setPositionX((float) Math.max(Math.min((extraData.netHeadYaw / 80) + Math.sin(leftEye.getPositionX() * Math.PI / 180F), 1), 0.1));
            leftEye.setPositionX((float) Math.min(Math.max((extraData.netHeadYaw / 80) + Math.sin(rightEye.getPositionX() * Math.PI / 180F), -1), -0.1));
            rightEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), -0.0F));
            leftEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), -0.0F));
            eyeBrow.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), -0.F));
            if (g3) {
                s3.setRotationX(-(head.getRotationX()/2));
                s3.setRotationY(-(head.getRotationY()/2));
                s3.setRotationZ(-(head.getRotationZ()/2));
            }
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }

}
