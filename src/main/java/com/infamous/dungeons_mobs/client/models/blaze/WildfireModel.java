package com.infamous.dungeons_mobs.client.models.blaze;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.blaze.WildfireEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public class WildfireModel extends AnimatedGeoModel {

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/wildfire.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/wildfire.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/blaze/wildfire.png");
    }

    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

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
		Vector3d velocity = livingEntity.getDeltaMovement();
		float groundSpeed = MathHelper.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		parser.setValue("query.ground_speed", groundSpeed * 30);
	}
}
