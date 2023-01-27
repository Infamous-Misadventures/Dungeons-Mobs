package com.infamous.dungeons_mobs.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EnsnaredEffect extends MobEffect {

    public EnsnaredEffect(MobEffectCategory typeIn, int liquidColourIn) {
        super(typeIn, liquidColourIn);
    }

    @Override
    public void applyEffectTick(LivingEntity owner, int amplifier) {
        if (!owner.level.isClientSide) {
            owner.setDeltaMovement(0, owner.getDeltaMovement().y, 0);
            owner.setSpeed(0);
        }
        super.applyEffectTick(owner, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }
}
