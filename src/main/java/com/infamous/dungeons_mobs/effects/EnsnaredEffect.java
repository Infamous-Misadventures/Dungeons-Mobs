package com.infamous.dungeons_mobs.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EnsnaredEffect extends MobEffect {

	public EnsnaredEffect(MobEffectCategory typeIn, int liquidColourIn) {
		super(typeIn, liquidColourIn);
	}

	@Override
	public void applyEffectTick(LivingEntity p_76394_1_, int p_76394_2_) {
		if (!p_76394_1_.level.isClientSide) {
			p_76394_1_.teleportTo(p_76394_1_.getX(), p_76394_1_.getY(), p_76394_1_.getZ());
			p_76394_1_.setDeltaMovement(0, p_76394_1_.getDeltaMovement().y > 0 ? 0 : p_76394_1_.getDeltaMovement().y,
					0);
		}
		super.applyEffectTick(p_76394_1_, p_76394_2_);
	}

	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}
}
