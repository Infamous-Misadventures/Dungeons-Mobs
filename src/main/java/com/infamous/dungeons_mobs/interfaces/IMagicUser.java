package com.infamous.dungeons_mobs.interfaces;

import com.infamous.dungeons_mobs.entities.magic.MagicType;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public interface IMagicUser {

	// Call this in the implementing MobEntity's tick method
	static <T extends Mob & IMagicUser> void spawnMagicParticles(T magicUser) {
		if (magicUser.level.isClientSide && magicUser.isUsingMagic()) {
			MagicType magicType = magicUser.getMagicType();
			double d0 = magicType.getParticleSpeed()[0];
			double d1 = magicType.getParticleSpeed()[1];
			double d2 = magicType.getParticleSpeed()[2];
			float f = magicUser.yBodyRot * ((float) Math.PI / 180F)
					+ Mth.cos((float) magicUser.tickCount * 0.6662F) * 0.25F;
			float f1 = Mth.cos(f);
			float f2 = Mth.sin(f);
			magicUser.level.addParticle(ParticleTypes.ENTITY_EFFECT, magicUser.getX() + (double) f1 * 0.6D,
					magicUser.getY() + 1.8D, magicUser.getZ() + (double) f2 * 0.6D, d0, d1, d2);
			magicUser.level.addParticle(ParticleTypes.ENTITY_EFFECT, magicUser.getX() - (double) f1 * 0.6D,
					magicUser.getY() + 1.8D, magicUser.getZ() - (double) f2 * 0.6D, d0, d1, d2);
		}
	}

	boolean isUsingMagic();

	int getMagicUseTicks();

	void setMagicUseTicks(int magicUseTicks);

	MagicType getMagicType();

	void setMagicType(MagicType spellTypeIn);

	SoundEvent getMagicSound();
}
