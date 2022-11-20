package com.infamous.dungeons_mobs.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.processor.IBone;

@OnlyIn(Dist.CLIENT)
public interface IHasGeoArm {
	
	IBone getArm(HumanoidArm p_191216_1_);
	
	void translateToHand(HumanoidArm p_225599_1_, PoseStack p_225599_2_);
	
	void translateAndRotate(IBone bone, PoseStack p_228307_1_);
}
