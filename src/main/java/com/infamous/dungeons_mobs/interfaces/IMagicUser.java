package com.infamous.dungeons_mobs.interfaces;

import com.infamous.dungeons_mobs.entities.magic.MagicType;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public interface IMagicUser {

    // Call this in the implementing MobEntity's tick method
    static <T extends MobEntity & IMagicUser> void spawnMagicParticles(T magicUser) {
        if (magicUser.world.isRemote && magicUser.isUsingMagic()) {
            MagicType magicType = magicUser.getMagicType();
            double d0 = magicType.getParticleSpeed()[0];
            double d1 = magicType.getParticleSpeed()[1];
            double d2 = magicType.getParticleSpeed()[2];
            float f = magicUser.renderYawOffset * ((float)Math.PI / 180F) + MathHelper.cos((float)magicUser.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            magicUser.world.addParticle(ParticleTypes.ENTITY_EFFECT, magicUser.getPosX() + (double)f1 * 0.6D, magicUser.getPosY() + 1.8D, magicUser.getPosZ() + (double)f2 * 0.6D, d0, d1, d2);
            magicUser.world.addParticle(ParticleTypes.ENTITY_EFFECT, magicUser.getPosX() - (double)f1 * 0.6D, magicUser.getPosY() + 1.8D, magicUser.getPosZ() - (double)f2 * 0.6D, d0, d1, d2);
        }
    }

    boolean isUsingMagic();

    int getMagicUseTicks();

    void setMagicUseTicks(int magicUseTicks);

    MagicType getMagicType();

    void setMagicType(MagicType spellTypeIn);

    SoundEvent getMagicSound();
}
