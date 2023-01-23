package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class GeomancerModel extends HeadTurningAnimatedGeoModel<GeomancerEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(GeomancerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/geomancer.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GeomancerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/geomancer.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GeomancerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/geomancer.png");
	}

    @Override
    public void setCustomAnimations(GeomancerEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        IBone illagerArms = this.getAnimationProcessor().getBone("illagerArms");
        
        cape.setHidden(true);
        illagerArms.setHidden(true);
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