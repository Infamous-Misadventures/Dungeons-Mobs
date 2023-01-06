package com.infamous.dungeons_mobs.client.models.blaze;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.blaze.WildfireEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class WildfireModel extends AnimatedGeoModel {

    @Override
    public ResourceLocation getAnimationResource(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/wildfire.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/wildfire.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureResource(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/blaze/wildfire.png");
    }

    @Override
    public void setCustomAnimations(IAnimatable entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        LivingEntity entityIn = (LivingEntity) entity;

        IBone head = this.getAnimationProcessor().getBone("head");
        
        IBone shield1 = this.getAnimationProcessor().getBone("shield1");
        IBone shield2 = this.getAnimationProcessor().getBone("shield2");
        IBone shield3 = this.getAnimationProcessor().getBone("shield3");
        IBone shield4 = this.getAnimationProcessor().getBone("shield4");
        
        WildfireEntity wildfire = ((WildfireEntity)entity);
        
        if (wildfire.getShields() >= 4) {
        	shield1.setHidden(false);
        	shield2.setHidden(false);
        	shield3.setHidden(false);
        	shield4.setHidden(false);
        } else if (wildfire.getShields() == 3) {
        	shield1.setHidden(true);
        	shield2.setHidden(false);
        	shield3.setHidden(false);
        	shield4.setHidden(false);
        } else if (wildfire.getShields() == 2) {
        	shield1.setHidden(true);
        	shield2.setHidden(true);
        	shield3.setHidden(false);
        	shield4.setHidden(false);
        } else if (wildfire.getShields() == 1) {
        	shield1.setHidden(true);
        	shield2.setHidden(true);
        	shield3.setHidden(true);
        	shield4.setHidden(false);
        } else if (wildfire.getShields() <= 0) {
        	shield1.setHidden(true);
        	shield2.setHidden(true);
        	shield3.setHidden(true);
        	shield4.setHidden(true);
        }

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }
    
	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick) {
		super.setMolangQueries(animatable, currentTick);
		
		MolangParser parser = GeckoLibCache.getInstance().parser;
		LivingEntity livingEntity = (LivingEntity) animatable;
		Vec3 velocity = livingEntity.getDeltaMovement();
		float groundSpeed = Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		parser.setValue("query.ground_speed", () -> groundSpeed * 30);
	}
}
