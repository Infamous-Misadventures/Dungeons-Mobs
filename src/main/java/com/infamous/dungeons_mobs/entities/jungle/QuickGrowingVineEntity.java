package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class QuickGrowingVineEntity extends AbstractVineEntity implements IAnimatable {

	AnimationFactory factory = new AnimationFactory(this);

	public QuickGrowingVineEntity(EntityType<? extends QuickGrowingVineEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}


	public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 15.0D);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.deathTime > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_retract", true));
		} else if (this.burstAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_burst", true));
		} else if (this.retractAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_retract", true));
		} else {
			if (this.isOut() || this.burstAnimationTick > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_idle", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_idle_underground", true));
			}
		} 
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public int getBurstAnimationLength() {
		return 12;
	}

	@Override
	public int getRetractAnimationLength() {
		return 8;
	}

	@Override
	protected SoundEvent getAmbientSoundFoley() {
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.QUICK_GROWING_VINE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.QUICK_GROWING_VINE_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSoundFoley(DamageSource p_184601_1_) {
		return null;
	}

	@Override
	public SoundEvent getBurstSound() {
		return ModSoundEvents.QUICK_GROWING_VINE_BURST.get();
	}

	@Override
	public SoundEvent getRetractSound() {
		return ModSoundEvents.QUICK_GROWING_VINE_BURST_DOWN.get();
	}

	@Override
	public SoundEvent getBurstSoundFoley() {
		return null;
	}

	@Override
	public SoundEvent getRetractSoundFoley() {
		return null;
	}

	@Override
	public void spawnAreaDamage() {
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 2.5F, DamageSource.mobAttack(this), 0.0F, 1.25F, 0.25F, 0.25F, false, false, 0.75, 0.25, false, 0, 1);
		this.level.addFreshEntity(areaDamage);
	}

	@Override
	public void setDefaultFeatures() {
		//this.setLengthInSegments(2 + this.random.nextInt(3));
		this.setLengthInBlocks(3);
		this.setVanishes(false);
		this.setAlwaysOut(false);
		this.setShouldRetract(true);
		this.setDetectionDistance(5);
	}

}