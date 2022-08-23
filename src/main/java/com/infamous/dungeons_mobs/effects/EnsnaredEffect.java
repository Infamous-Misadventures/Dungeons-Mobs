package com.infamous.dungeons_mobs.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EnsnaredEffect extends Effect {
	
	   public EnsnaredEffect(EffectType typeIn, int liquidColourIn) {
		   super(typeIn, liquidColourIn);
	   }

	   @Override
	public void applyEffectTick(LivingEntity p_76394_1_, int p_76394_2_) {
		   if (!p_76394_1_.level.isClientSide) {
			   p_76394_1_.teleportTo(p_76394_1_.getX(), p_76394_1_.getY(), p_76394_1_.getZ());
		   p_76394_1_.setDeltaMovement(0, p_76394_1_.getDeltaMovement().y > 0 ? 0 : p_76394_1_.getDeltaMovement().y, 0);
		   }
		   super.applyEffectTick(p_76394_1_, p_76394_2_);
	}
	   
	   @Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}
}
