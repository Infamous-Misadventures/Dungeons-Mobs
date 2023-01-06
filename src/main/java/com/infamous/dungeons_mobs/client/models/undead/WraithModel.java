package com.infamous.dungeons_mobs.client.models.undead;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WraithModel extends AnimatedGeoModel<WraithEntity> {

	@Override
	public ResourceLocation getAnimationResource(WraithEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/wraith.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(WraithEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/wraith.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(WraithEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/wraith/wraith.png");
	}

    @Override
    public void setCustomAnimations(WraithEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("bipedHead");
        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        
        cape.setHidden(true);
        
        IBone leftHand = this.getAnimationProcessor().getBone("bipedHandLeft");
        IBone rightHand = this.getAnimationProcessor().getBone("bipedHandRight");
        
        if (entity.tickCount % 2 == 0 && rightHand instanceof GeoBone && leftHand instanceof GeoBone && entity.isSpellcasting()) {
        	GeoBone leftHandBone = ((GeoBone)leftHand);
        	GeoBone rightHandBone = ((GeoBone)rightHand);
        	entity.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, leftHandBone.getWorldPosition().x, leftHandBone.getWorldPosition().y, leftHandBone.getWorldPosition().z, entity.getRandom().nextGaussian() * 0.01, entity.getRandom().nextGaussian() * 0.01, entity.getRandom().nextGaussian() * 0.01);
        	entity.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, rightHandBone.getWorldPosition().x, rightHandBone.getWorldPosition().y, rightHandBone.getWorldPosition().z, entity.getRandom().nextGaussian() * 0.01, entity.getRandom().nextGaussian() * 0.01, entity.getRandom().nextGaussian() * 0.01);
        }

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }
}
