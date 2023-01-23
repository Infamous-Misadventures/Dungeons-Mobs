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
import software.bernie.geckolib3.resource.GeckoLibCache;

public class RoyalGuardModel extends HeadTurningAnimatedGeoModel<RoyalGuardEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(RoyalGuardEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/royal_guard.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(RoyalGuardEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/geo_illager.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureLocation(RoyalGuardEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/royal_guard.png");
    }

    @Override
    public void setCustomAnimations(RoyalGuardEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        IBone illagerArms = this.getAnimationProcessor().getBone("illagerArms");
        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        
        illagerArms.setHidden(true);
        cape.setHidden(true);
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
