package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public abstract class AbstractVineModel extends AnimatedGeoModel<AbstractVineEntity> {

	@Override
	public void setCustomAnimations(AbstractVineEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);

		IBone everything = this.getAnimationProcessor().getBone("everything");

		everything.setHidden(entity.tickCount <= entity.getAnimationTransitionTime());

		for (int i = 1; i < 26; i++) {
			IBone part = this.getAnimationProcessor().getBone("part" + i);
			int partsToShow = 26 - entity.getLengthInSegments();
			if (part != null) {
				part.setHidden(i < partsToShow);
			}
		}

	}

	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick) {
		super.setMolangQueries(animatable, currentTick);

		MolangParser parser = GeckoLibCache.getInstance().parser;
		AbstractVineEntity vine = (AbstractVineEntity) animatable;
		parser.setValue("query.vine_length", vine::getLengthInSegments);
	}
}