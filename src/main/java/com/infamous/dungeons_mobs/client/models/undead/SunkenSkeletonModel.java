package com.infamous.dungeons_mobs.client.models.undead;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class SunkenSkeletonModel<T extends AbstractSkeletonEntity> extends SmartSkeletonModel<T> {
    
    public SunkenSkeletonModel() {
        this(0.0F, false);
    }

    public SunkenSkeletonModel(float modelSize) {
        this(modelSize, false);
    }
    
	public SunkenSkeletonModel(float modelSize, boolean p_i46303_2_) {
		super(modelSize, p_i46303_2_);
		if (!p_i46303_2_) {
			texWidth = 64;
			texHeight = 32;
	
			head = new ModelRenderer(this);
			head.setPos(0.0F, 0.0F, 0.0F);
			head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			head.texOffs(32, 0).addBox(-1.0F, -15.0F, -4.0F, 9.0F, 9.0F, 1.0F, 0.0F, false);
			head.texOffs(44, 10).addBox(-1.0F, -3.0F, 2.0F, 6.0F, 1.0F, 4.0F, 0.0F, false);
			
			hat.visible = false;
	
			body = new ModelRenderer(this);
			body.setPos(0.0F, 0.0F, 0.0F);
			body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
	
			leftArm = new ModelRenderer(this);
			leftArm.setPos(5.0F, 2.0F, 0.0F);
			leftArm.texOffs(0, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
	
			rightArm = new ModelRenderer(this);
			rightArm.setPos(-5.0F, 2.0F, 0.0F);
			rightArm.texOffs(0, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
	
			leftLeg = new ModelRenderer(this);
			leftLeg.setPos(2.0F, 12.0F, 0.1F);
			leftLeg.texOffs(40, 16).addBox(-1.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, 0.0F, true);
	
			rightLeg = new ModelRenderer(this);
			rightLeg.setPos(-2.0F, 12.0F, 0.1F);
			rightLeg.texOffs(40, 16).addBox(-1.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, 0.0F, false);
			rightLeg.texOffs(52, 1).addBox(-2.0F, 3.0F, -2.1F, 2.0F, 1.0F, 4.0F, 0.0F, false);
		}
	}
}