package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class QuickGrowingVineEntity extends AbstractVineEntity {

	AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public QuickGrowingVineEntity(EntityType<? extends QuickGrowingVineEntity> p_i50147_1_, Level p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}


	public static AttributeSupplier.Builder setCustomAttributes(){
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 15.0D);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", this.getAnimationTransitionTime(), this::predicate));
	}
	
	@Override
	public int getAnimationTransitionTime() {
		return 2;
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.deathTime > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_retract", LOOP));
		} else if (this.burstAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_burst", LOOP));
		} else if (this.retractAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_retract", LOOP));
		} else {
			if (this.isOut() || this.burstAnimationTick > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_idle", LOOP));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("quick_growing_vine_idle_underground", LOOP));
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
		return 10;
	}

	@Override
	public int getRetractAnimationLength() {
		return 10;
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
	public boolean isKelp() {
		return false;
	}

	@Override
	public boolean shouldDieInWrongHabitat() {
		return true;
	}

	@Override
	public int wrongHabitatDieChance() {
		return 50;
	}

	@Override
	public void spawnAreaDamage() {
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 2.5F, DamageSource.mobAttack(this), 0.0F, 1.25F, 0.25F, 0.25F, 5, false, false, 0.75, 0.25, false, 0, 1);
		this.level.addFreshEntity(areaDamage);
	}

	@Override
	public void setDefaultFeatures() {
		this.setLengthInSegments(2 + this.random.nextInt(3));
		this.setVanishes(false);
		this.setAlwaysOut(false);
		this.setShouldRetract(true);
		this.setDetectionDistance(5);
	}

}