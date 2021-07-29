package com.infamous.dungeons_mobs.client.models.illager;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
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

public class GeomancerModel<T extends GeomancerEntity> extends BipedModel<T> {
	private final ModelRenderer tunic;
	private final ModelRenderer collar;

	public GeomancerModel(float modelSize, float yOffset, int textureWidthIn, int textureHeightIn) {
		super(modelSize);
		texWidth = textureWidthIn;
		texHeight = textureHeightIn;

		this.head = new ModelRenderer(this);
		this.head.setPos(0.0F, 0.0F + yOffset, 0.0F);
		this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);
		this.head.texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
		this.head.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);
		this.head.texOffs(47, 30).addBox(-3.0F, -2.9F, -4.5F, 6.0F, 5.0F, 1.0F, modelSize);
		this.head.texOffs(49, 33).addBox(-2.0F, 2.1F, -4.5F, 4.0F, 1.0F, 1.0F, modelSize);

		this.hat.visible = false; // no mapped headwear texture

		this.body = new ModelRenderer(this);
		this.body.setPos(0.0F, 0.0F + yOffset, 0.0F);
		this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);

		this.collar = new ModelRenderer(this);
		this.collar.setPos(0.0F, 0.0F + yOffset, 0.0F);
		this.collar.texOffs(0, 0).addBox(-6.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(0, 0).addBox(-6.0F, -1.0F, -4.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(0, 0).addBox(-4.0F, 1.0F, -5.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(0, 0).addBox(-6.0F, -1.0F, 2.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 0).addBox(-4.0F, 1.0F, 3.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(52, 4).addBox(-2.0F, 2.0F, 3.0F, 4.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 0).addBox(2.0F, 1.0F, 3.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 40).addBox(4.0F, -1.0F, 2.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 40).addBox(4.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 40).addBox(4.0F, -1.0F, -4.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 40).addBox(2.0F, 1.0F, -5.0F, 2.0F, 2.0F, 2.0F, modelSize);
		this.collar.texOffs(56, 20).addBox(-1.0F, 3.0F, -5.0F, 2.0F, 2.0F, 2.0F, modelSize);

		this.tunic = new ModelRenderer(this);
		this.tunic.setPos(0.0F, 0.0F + yOffset, 0.0F);
		this.tunic.texOffs(0, 38).addBox(-4.0F, -24.0F + 24.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F + modelSize);

		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.setPos(2.0F, 12.0F + yOffset, 0.0F);
		this.rightLeg.texOffs(0, 22).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);

		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.setPos(-2.0F, 12.0F + yOffset, 0.0F);
		this.leftLeg.texOffs(0, 22).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize, true);

		this.rightArm = new ModelRenderer(this);
		this.rightArm.setPos(5.0F, 2.0F + yOffset, 0.0F);
		this.rightArm.texOffs(40, 46).addBox(-13.0F + 10.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);

		this.leftArm = new ModelRenderer(this);
		this.leftArm.setPos(-5.0F, 2.0F + yOffset, 0.0F);
		this.leftArm.texOffs(40, 46).addBox(9.0F - 10.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.tunic, this.collar));
	}

	@Override
	public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		this.rightArmPose = BipedModel.ArmPose.EMPTY;
		this.leftArmPose = BipedModel.ArmPose.EMPTY;
		if (entityIn.getMainArm() == HandSide.RIGHT) {
			this.giveModelRightArmPoses(Hand.MAIN_HAND, entityIn);
			this.giveModelLeftArmPoses(Hand.OFF_HAND, entityIn);
		} else {
			this.giveModelRightArmPoses(Hand.OFF_HAND, entityIn);
			this.giveModelLeftArmPoses(Hand.MAIN_HAND, entityIn);
		}
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	private void giveModelRightArmPoses(Hand hand, T entityIn) {
		ItemStack itemstack = entityIn.getItemInHand(hand);
		UseAction useaction = itemstack.getUseAnimation();
		if (entityIn.getArmPose() != AbstractIllagerEntity.ArmPose.CROSSED) {
			switch (useaction) {
				case BLOCK:
					if(entityIn.isBlocking()){
						this.rightArmPose = BipedModel.ArmPose.BLOCK;
					}
					else{
						this.rightArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
				case CROSSBOW:
					this.rightArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isUsingItem()) {
						this.rightArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
					break;
				case SPEAR:
					this.leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
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
		ItemStack itemstack = entityIn.getItemInHand(hand);
		UseAction useaction = itemstack.getUseAnimation();
		if (entityIn.getArmPose() != AbstractIllagerEntity.ArmPose.CROSSED) {
			switch (useaction) {
				case BLOCK:
					if(entityIn.isBlocking()){
						this.leftArmPose = BipedModel.ArmPose.BLOCK;
					}
					else{
						this.leftArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
				case CROSSBOW:
					this.leftArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isUsingItem()) {
						this.leftArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
					break;
				case SPEAR:
					this.leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
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
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllagerEntity.ArmPose armpose = entityIn.getArmPose();
		//this.arms.rotationPointY = 3.0F;
		//this.arms.rotationPointZ = -1.0F;
		//this.arms.rotateAngleX = -0.75F;
		this.tunic.copyFrom(body);
		boolean isWearingChestplateOrLeggings = entityIn.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem || entityIn.getItemBySlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
		this.tunic.visible = !isWearingChestplateOrLeggings;
		boolean flag = armpose == AbstractIllagerEntity.ArmPose.CROSSED;
		//this.arms.showModel = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;
		if (flag) {
			this.leftArm.y = 3.0F;
			this.leftArm.z = -1.0F;
			this.leftArm.xRot = -0.75F;
			this.rightArm.y = 3.0F;
			this.rightArm.z = -1.0F;
			this.rightArm.xRot = -0.75F;
		}
		switch (armpose) {
			case ATTACKING:
				if (!entityIn.getMainHandItem().isEmpty()
						&& !(entityIn.getMainHandItem().getItem() instanceof ShootableItem)
						&& !(entityIn.isBlocking())){
					// raises arm with weapon, moves left arm back and forth while attacking
					ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
				}
                /*
                else if(!entityIn.getHeldItemMainhand().isEmpty()
                        && !(entityIn.getHeldItemMainhand().getItem() instanceof ShootableItem)){
                    ModelUtils.readyWeaponWhileBlocking(this.bipedRightArm, this.bipedLeftArm, entityIn, this.swingProgress, ageInTicks);
                }

                 */
				break;
			case CELEBRATING:
				this.rightArm.z = 0.0F;
				this.rightArm.x = -5.0F;
				this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
				this.rightArm.zRot = 2.670354F;
				this.rightArm.yRot = 0.0F;
				this.leftArm.z = 0.0F;
				this.leftArm.x = 5.0F;
				this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
				this.leftArm.zRot = -2.3561945F;
				this.leftArm.yRot = 0.0F;
				break;
			case SPELLCASTING:
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
				break;
			default:
				break;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}