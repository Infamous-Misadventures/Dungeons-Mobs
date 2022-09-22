package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class PoisonAnemoneEntity extends PoisonQuillVineEntity {

	public PoisonAnemoneEntity(EntityType<? extends PoisonAnemoneEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.POISON_ANEMONE_IDLE.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.POISON_ANEMONE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.POISON_ANEMONE_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSoundFoley(DamageSource p_184601_1_) {
		return null;
	}

	@Override
	public SoundEvent getBurstSound() {
		return ModSoundEvents.POISON_ANEMONE_BURST.get();
	}

	@Override
	public SoundEvent getRetractSound() {
		return ModSoundEvents.POISON_ANEMONE_BURST.get();
	}
	
	@Override
	public SoundEvent getCloseSound() {
		return ModSoundEvents.POISON_ANEMONE_CLOSE.get();
	}
	
	@Override
	public SoundEvent getShootSound() {
		return ModSoundEvents.POISON_ANEMONE_SHOOT.get();
	}
	
	@Override
	public boolean isKelp() {
		return true;
	}
	
	@Override
	public int wrongHabitatDieChance() {
		return 75;
	}
	
	@Override
	public void spawnAreaDamage() {
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 5.0F, DamageSource.mobAttack(this), 0.0F, 1.5F, 0.25F, 0.25F, false, false, 0.75, 0.25, false, 0, 2);
		this.level.addFreshEntity(areaDamage);
	}
	
	@Override
	public void setDefaultFeatures() {
		super.setDefaultFeatures();
		this.setLengthInSegments(4 + this.random.nextInt(9));
	}
}
