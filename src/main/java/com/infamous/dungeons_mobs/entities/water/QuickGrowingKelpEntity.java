package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class QuickGrowingKelpEntity extends QuickGrowingVineEntity {

	public QuickGrowingKelpEntity(EntityType<? extends QuickGrowingKelpEntity> p_i50147_1_, Level p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.QUICK_GROWING_KELP_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.QUICK_GROWING_KELP_DEATH.get();
	}

	@Override
	public SoundEvent getBurstSound() {
		return ModSoundEvents.QUICK_GROWING_KELP_BURST.get();
	}

	@Override
	public SoundEvent getRetractSound() {
		return ModSoundEvents.QUICK_GROWING_KELP_BURST_DOWN.get();
	}

	@Override
	public boolean isKelp() {
		return true;
	}

	@Override
	public int wrongHabitatDieChance() {
		return 30;
	}

	@Override
	public void spawnAreaDamage() {
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 2.5F,
				DamageSource.mobAttack(this), 0.0F, 1.25F, 0.25F, 0.25F, 0, false, false, 0.75, 0.25, false, 0, 2);
		this.level.addFreshEntity(areaDamage);
	}

	@Override
	public void setDefaultFeatures() {
		super.setDefaultFeatures();
		this.setLengthInSegments(4 + this.random.nextInt(9));
	}

}
