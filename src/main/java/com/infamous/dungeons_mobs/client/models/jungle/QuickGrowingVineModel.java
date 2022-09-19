package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public class QuickGrowingVineModel extends AnimatedGeoModel<QuickGrowingVineEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(QuickGrowingVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/quick_growing_vine.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(QuickGrowingVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/quick_growing_vine.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(QuickGrowingVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/quick_growing_vine.png");
	}
	
    @Override
    public void setLivingAnimations(QuickGrowingVineEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        
        IBone everything = this.getAnimationProcessor().getBone("everything");
        
        if (entity.tickCount <= 2 || (!entity.isOut() && entity.burstAnimationTick <= 0 && entity.retractAnimationTick <= 0)) {
        	everything.setHidden(true);
        } else {
        	everything.setHidden(false);
        }
        
        for (int i = 1; i < 26; i++) {
        	IBone part = this.getAnimationProcessor().getBone("part" + i);
        	int partsToShow = 27 - entity.getLengthInSegments();
        	if (part != null) {
	        	if (i >= partsToShow) {
	        		part.setHidden(false);
	        	} else {
	        		part.setHidden(true);
	        	}
        	}
        }

    }
	
	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick) {
		super.setMolangQueries(animatable, currentTick);
		
		MolangParser parser = GeckoLibCache.getInstance().parser;
		QuickGrowingVineEntity vine = (QuickGrowingVineEntity) animatable;
		parser.setValue("query.vine_length", vine.getLengthInSegments());
	}
}