package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;

public interface IGeoEntityMeleeAttack {

	// play animation
	int Timer();
	void setTimer(int timer);
	boolean isMelee();
	void setMelee(boolean melee);

	// animation attack tick
	int MeleeAnimationTick();

	// attack goal
	void setCanMelee(boolean canMelee);

	// attack goal
	boolean CanMelee();

	// should mobs have area attack
	boolean AreaAttack();

	// let mob can be walk when melee
	boolean CanWalkWhenMelee();

	// mob walk speed when melee
	int WalkWhenMeleeSpeed();

	// Damage
	default double getDamageValue(LivingEntity entity) {
		return entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
	}

}
