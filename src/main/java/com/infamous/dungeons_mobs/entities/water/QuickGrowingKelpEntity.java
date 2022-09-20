package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class QuickGrowingKelpEntity extends QuickGrowingVineEntity {

	public QuickGrowingKelpEntity(EntityType<? extends QuickGrowingKelpEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
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
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 2.5F, DamageSource.mobAttack(this), 0.0F, 1.25F, 0.25F, 0.25F, false, false, 0.75, 0.25, false, 0, 2);
		this.level.addFreshEntity(areaDamage);
	}
	
}
