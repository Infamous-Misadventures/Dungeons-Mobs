package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WindcallerModel extends AnimatedGeoModel<WindcallerEntity> {

    @Override
    public ResourceLocation getAnimationResource(WindcallerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/windcaller.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(WindcallerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/windcaller.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WindcallerEntity entity) {
        if(DungeonsMobsConfig.COMMON.ENABLE_3D_SLEEVES.get()){
            return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/windcaller.png");
        }else{
            return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/windcaller_sleeved.png");
        }
    }

    @Override
    public void setCustomAnimations(WindcallerEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        LivingEntity entityIn = entity;

        IBone head = this.getAnimationProcessor().getBone("bipedHead");

        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        cape.setHidden(entity.getItemBySlot(EquipmentSlot.CHEST).getItem() != entity.getArmorSet().getChest().get());

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }
}
