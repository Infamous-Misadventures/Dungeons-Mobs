package com.infamous.dungeons_mobs.client.models.jungle;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;

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

public class WhispererModel<T extends WhispererEntity> extends AnimatedGeoModel<T> {

	@Override
	public ResourceLocation getAnimationFileLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/whisperer.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/whisperer.geo.json") ;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/whisperer.png");
	}

	@Override
	public void setLivingAnimations(T entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		IBone head = this.getAnimationProcessor().getBone("jaw");
		IBone cape = this.getAnimationProcessor().getBone("bipedCape");
		
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
		Vector3d velocity = livingEntity.getDeltaMovement();
		float groundSpeed = MathHelper.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		parser.setValue("query.ground_speed", groundSpeed * 12.5);
	}
}
