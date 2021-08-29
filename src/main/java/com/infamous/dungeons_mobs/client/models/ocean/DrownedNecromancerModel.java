package com.infamous.dungeons_mobs.client.models.ocean;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class DrownedNecromancerModel<T extends DrownedNecromancerEntity> extends BipedModel<T> {
	private final ModelRenderer jacket;
	private final ModelRenderer leftSleeve;
	private final ModelRenderer rightSleeve;
	private final ModelRenderer leftPants;
	private final ModelRenderer rightPants;
	private final ModelRenderer cape;

	public DrownedNecromancerModel() {
		this(0.0F);
	}

	public DrownedNecromancerModel(float modelSize) {
		super(modelSize);
		texWidth = 92;
		texHeight = 92;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, true);

		jacket = new ModelRenderer(this);
		jacket.setPos(0.0F, 0.0F, 0.0F);
		jacket.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.5F, true);
		//body.addChild(jacket);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, true);

		hat = new ModelRenderer(this);
		hat.setPos(0.0F, 0.0F, 0.0F);
		hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, true);
		hat.texOffs(56, 21).addBox(-4.0F, -11.25F, -4.0F, 8.0F, 5.0F, 8.0F, 1.5F, true);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(6.0F, 2.0F, 0.0F);
		leftArm.texOffs(0, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-5.0F, 2.0F, 0.0F);
		rightArm.texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		leftSleeve = new ModelRenderer(this);
		leftSleeve.setPos(6.0F, 2.0F, 0.0F);
		leftSleeve.texOffs(48, 48).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
		leftSleeve.texOffs(0, 81).addBox(-2.25F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, 0.5F, false);
		//leftArm.addChild(leftSleeve);

		rightSleeve = new ModelRenderer(this);
		rightSleeve.setPos(-5.0F, 2.0F, 0.0F);
		rightSleeve.texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
		rightSleeve.texOffs(0, 81).addBox(-3.75F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, 0.5F, true);
		//rightArm.addChild(rightSleeve);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(1.9F, 12.0F, 0.0F);
		leftLeg.texOffs(16, 48).addBox(-1.85F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-1.9F, 12.0F, 0.0F);
		rightLeg.texOffs(32, 48).addBox(-2.15F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		leftPants = new ModelRenderer(this);
		leftPants.setPos(1.9F, 12.0F, 0.0F);
		leftPants.texOffs(0, 48).addBox(-1.65F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, true);
		//leftLeg.addChild(leftPants);

		rightPants = new ModelRenderer(this);
		rightPants.setPos(-1.9F, 12.0F, 0.0F);
		rightPants.texOffs(0, 32).addBox(-2.35F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
		//rightLeg.addChild(rightPants);

		cape = new ModelRenderer(this);
		cape.setPos(0.0F, 0.75F, 3.0F);
		cape.texOffs(24, 64).addBox(-7.0F, 0.0F, -5.0F, 14.0F, 23.0F, 5.0F, 0.25F, false);
		//body.addChild(cape);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		if(entityIn.isUsingMagic()){
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = 2.3561945F;
			this.leftArm.zRot = -2.3561945F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.yRot = 0.0F;
		}
		if (this.swimAmount > 0.0F) {
			this.rightArm.xRot = this.rotlerpRad(this.swimAmount, this.rightArm.xRot, -2.5132742F) + this.swimAmount * 0.35F * MathHelper.sin(0.1F * ageInTicks);
			this.leftArm.xRot = this.rotlerpRad(this.swimAmount, this.leftArm.xRot, -2.5132742F) - this.swimAmount * 0.35F * MathHelper.sin(0.1F * ageInTicks);
			this.rightArm.zRot = this.rotlerpRad(this.swimAmount, this.rightArm.zRot, -0.15F);
			this.leftArm.zRot = this.rotlerpRad(this.swimAmount, this.leftArm.zRot, 0.15F);
			this.leftLeg.xRot -= this.swimAmount * 0.55F * MathHelper.sin(0.1F * ageInTicks);
			this.rightLeg.xRot += this.swimAmount * 0.55F * MathHelper.sin(0.1F * ageInTicks);
			this.head.xRot = 0.0F;
		}

		this.leftPants.copyFrom(this.leftLeg);
		this.rightPants.copyFrom(this.rightLeg);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
		this.jacket.copyFrom(this.body);
		this.cape.copyFrom(this.body);
		this.cape.y += 0.75F;
		this.cape.z += 3.0F;

		this.hat.copyFrom(this.head);
	}

    protected Iterable<ModelRenderer> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket, this.cape));
    }

    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        this.leftSleeve.visible = visible;
        this.rightSleeve.visible = visible;
        this.leftPants.visible = visible;
        this.rightPants.visible = visible;
        this.jacket.visible = visible;
        this.cape.visible = visible;
    }

	public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
		float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArm(sideIn);
		modelrenderer.x += f;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.x -= f;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}