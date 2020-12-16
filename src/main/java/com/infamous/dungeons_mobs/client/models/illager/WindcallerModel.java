package com.infamous.dungeons_mobs.client.models.illager;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class WindcallerModel<T extends WindcallerEntity> extends BipedModel<T> {
	private final ModelRenderer jacket;
	private final ModelRenderer arms;

	public WindcallerModel(float modelSize, float yOffset, int textureWidthIn, int textureHeightIn) {
		super(modelSize);
		textureWidth = textureWidthIn;
		textureHeight = textureHeightIn;

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + yOffset, 0.0F);
		this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F,  modelSize, false);
		this.bipedHead.setTextureOffset(32, 0).addBox(-4.0F, -20.0F, -4.0F, 8.0F, 10.0F, 8.0F,  modelSize, false);
		ModelRenderer nose = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
		nose.setRotationPoint(0.0F, yOffset - 2.0F, 0.0F);
		nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
		this.bipedHead.addChild(nose);

		//this.bipedHead.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false); // nose
		this.bipedHeadwear.showModel = false; // headwear is not going to work too well on a windcaller

		this.bipedBody = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + yOffset, 0.0F);
		this.bipedBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);

		this.jacket = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
		this.jacket.setRotationPoint(0.0F, 0.0F + yOffset, 0.0F);
		this.jacket.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, modelSize + 0.5F);

		this.arms = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
		this.arms.setRotationPoint(0.0F, 0.0F + yOffset + 2.0F, 0.0F);
		this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
		ModelRenderer upperArm = (new ModelRenderer(this, 44, 22)).setTextureSize(textureWidthIn, textureHeightIn);
		upperArm.mirror = true;
		upperArm.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
		this.arms.addChild(upperArm);
		this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, modelSize);

		this.bipedRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(textureWidthIn, textureHeightIn);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + yOffset, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);

		this.bipedLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(textureWidthIn, textureHeightIn);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + yOffset, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);

		this.bipedRightArm = (new ModelRenderer(this, 40, 46)).setTextureSize(textureWidthIn, textureHeightIn);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + yOffset, 0.0F);

		this.bipedLeftArm = (new ModelRenderer(this, 40, 46)).setTextureSize(textureWidthIn, textureHeightIn);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + yOffset, 0.0F);
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.arms, this.jacket));
	}

	@Override
	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		this.rightArmPose = BipedModel.ArmPose.EMPTY;
		this.leftArmPose = BipedModel.ArmPose.EMPTY;
		if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
			this.giveModelRightArmPoses(Hand.MAIN_HAND, entityIn);
			this.giveModelLeftArmPoses(Hand.OFF_HAND, entityIn);
		} else {
			this.giveModelRightArmPoses(Hand.OFF_HAND, entityIn);
			this.giveModelLeftArmPoses(Hand.MAIN_HAND, entityIn);
		}
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	private void giveModelRightArmPoses(Hand hand, T entityIn) {
		ItemStack itemstack = entityIn.getHeldItem(hand);
		UseAction useaction = itemstack.getUseAction();
		if (entityIn.getArmPose() != AbstractIllagerEntity.ArmPose.CROSSED) {
			switch (useaction) {
				case BLOCK:
					if(entityIn.isActiveItemStackBlocking()){
						this.rightArmPose = BipedModel.ArmPose.BLOCK;
					}
					else{
						this.rightArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
				case CROSSBOW:
					this.rightArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isHandActive()) {
						this.rightArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
					break;
				case SPEAR:
					this.leftArmPose = ArmPose.THROW_SPEAR;
					break;
				default:
					this.rightArmPose = BipedModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.rightArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
			}
		}
	}

	private void giveModelLeftArmPoses(Hand hand, T entityIn) {
		ItemStack itemstack = entityIn.getHeldItem(hand);
		UseAction useaction = itemstack.getUseAction();
		if (entityIn.getArmPose() != AbstractIllagerEntity.ArmPose.CROSSED) {
			switch (useaction) {
				case BLOCK:
					if(entityIn.isActiveItemStackBlocking()){
						this.leftArmPose = BipedModel.ArmPose.BLOCK;
					}
					else{
						this.leftArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
				case CROSSBOW:
					this.leftArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isHandActive()) {
						this.leftArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
					break;
				case SPEAR:
					this.leftArmPose = ArmPose.THROW_SPEAR;
					break;
				default:
					this.leftArmPose = BipedModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.leftArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
			}
		}
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllagerEntity.ArmPose armpose = entityIn.getArmPose();
		this.arms.rotationPointY = 3.0F;
		this.arms.rotationPointZ = -1.0F;
		this.arms.rotateAngleX = -0.75F;
		this.jacket.copyModelAngles(bipedBody);
		boolean isWearingChestplateOrLeggings = entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem || entityIn.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
		this.jacket.showModel = !isWearingChestplateOrLeggings;
		boolean flag = armpose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.showModel = flag;
		this.bipedLeftArm.showModel = !flag;
		this.bipedRightArm.showModel = !flag;
		if (flag) {
			this.bipedLeftArm.rotationPointY = 3.0F;
			this.bipedLeftArm.rotationPointZ = -1.0F;
			this.bipedLeftArm.rotateAngleX = -0.75F;
			this.bipedRightArm.rotationPointY = 3.0F;
			this.bipedRightArm.rotationPointZ = -1.0F;
			this.bipedRightArm.rotateAngleX = -0.75F;
		}
		switch (armpose) {
			case ATTACKING:
				if (!entityIn.getHeldItemMainhand().isEmpty()
						&& !(entityIn.getHeldItemMainhand().getItem() instanceof ShootableItem)
						&& !(entityIn.isActiveItemStackBlocking())){
					// raises arm with weapon, moves left arm back and forth while attacking
					ModelHelper.func_239103_a_(this.bipedRightArm, this.bipedLeftArm, entityIn, this.swingProgress, ageInTicks);
				}
                /*
                else if(!entityIn.getHeldItemMainhand().isEmpty()
                        && !(entityIn.getHeldItemMainhand().getItem() instanceof ShootableItem)){
                    ModelUtils.readyWeaponWhileBlocking(this.bipedRightArm, this.bipedLeftArm, entityIn, this.swingProgress, ageInTicks);
                }

                 */
				break;
			case CELEBRATING:
				this.bipedRightArm.rotationPointZ = 0.0F;
				this.bipedRightArm.rotationPointX = -5.0F;
				this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
				this.bipedRightArm.rotateAngleZ = 2.670354F;
				this.bipedRightArm.rotateAngleY = 0.0F;
				this.bipedLeftArm.rotationPointZ = 0.0F;
				this.bipedLeftArm.rotationPointX = 5.0F;
				this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
				this.bipedLeftArm.rotateAngleZ = -2.3561945F;
				this.bipedLeftArm.rotateAngleY = 0.0F;
				break;
			case SPELLCASTING:
				this.bipedRightArm.rotationPointZ = 0.0F;
				this.bipedRightArm.rotationPointX = -5.0F;
				this.bipedLeftArm.rotationPointZ = 0.0F;
				this.bipedLeftArm.rotationPointX = 5.0F;
				this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
				this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
				this.bipedRightArm.rotateAngleZ = 2.3561945F;
				this.bipedLeftArm.rotateAngleZ = -2.3561945F;
				this.bipedRightArm.rotateAngleY = 0.0F;
				this.bipedLeftArm.rotateAngleY = 0.0F;
				break;
			default:
				break;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}