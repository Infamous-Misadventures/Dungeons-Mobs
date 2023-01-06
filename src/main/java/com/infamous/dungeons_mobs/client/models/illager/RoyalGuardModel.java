package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.RoyalGuardEntity;

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

public class RoyalGuardModel extends AnimatedGeoModel<RoyalGuardEntity> {

    @Override
    public ResourceLocation getAnimationResource(RoyalGuardEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/royal_guard.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RoyalGuardEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/geo_illager.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureResource(RoyalGuardEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/royal_guard.png");
    }

    @Override
    public void setCustomAnimations(RoyalGuardEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        LivingEntity entityIn = entity;

        IBone head = this.getAnimationProcessor().getBone("bipedHeadBaseRotator");
        IBone armorHead = this.getAnimationProcessor().getBone("armorBipedHead");
        IBone illagerArms = this.getAnimationProcessor().getBone("illagerArms");
        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        
        illagerArms.setHidden(true);
        cape.setHidden(true);

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
		parser.setValue("query.ground_speed", () -> groundSpeed * 20);
	}
}
