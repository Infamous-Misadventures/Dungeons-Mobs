package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public abstract class AbstractVineModel extends AnimatedGeoModel<AbstractVineEntity> {

	@Override
	public void setLivingAnimations(AbstractVineEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		IBone everything = this.getAnimationProcessor().getBone("everything");

		if (entity.tickCount <= entity.getAnimationTransitionTime()) {
			everything.setHidden(true);
		} else {
			everything.setHidden(false);
		}

		for (int i = 1; i < 26; i++) {
			IBone part = this.getAnimationProcessor().getBone("part" + i);
			int partsToShow = 26 - entity.getLengthInSegments();
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
		AbstractVineEntity vine = (AbstractVineEntity) animatable;
		parser.setValue("query.vine_length", vine.getLengthInSegments());
	}
}