package com.infamous.dungeons_mobs.interfaces;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.processor.IBone;

@OnlyIn(Dist.CLIENT)
public interface IHasGeoArm {
	
	IBone getArm(HandSide p_191216_1_);
	
	void translateToHand(HandSide p_225599_1_, MatrixStack p_225599_2_);
	
	void translateAndRotate(IBone bone, MatrixStack p_228307_1_, double moveX, double moveY, double moveZ);
}
