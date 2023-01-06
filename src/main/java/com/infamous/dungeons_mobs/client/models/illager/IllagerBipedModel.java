package com.infamous.dungeons_mobs.client.models.illager;

import static com.infamous.dungeons_mobs.entities.illagers.IllagerArmsUtil.armorHasCrossedArms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;

public class IllagerBipedModel<T extends AbstractIllager> extends HumanoidModel<T> {
	public ModelPart nose = this.head.getChild("nose");
	public ModelPart jacket = this.body.getChild("jacket");
	public ModelPart arms;

	public IllagerBipedModel(ModelPart part) {
		super(part);
		this.arms = part.getChild("arms");
		this.hat.visible = false;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		partdefinition1.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F,
				8.0F, 12.0F, 8.0F, new CubeDeformation(0.45F)), PartPose.ZERO);
		partdefinition1.addOrReplaceChild("nose",
				CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
				PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F,
				6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition partdefinition2 = partdefinition.addOrReplaceChild("arms",
				CubeListBuilder.create().texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F).texOffs(40, 38)
						.addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
		partdefinition2.addOrReplaceChild("left_shoulder",
				CubeListBuilder.create().texOffs(44, 22).mirror().addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(-2.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(2.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(5.0F, 2.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.arms, this.jacket));
	}

	@Override
	public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
		this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (entityIn.getMainArm() == HumanoidArm.RIGHT) {
			this.giveModelRightArmPoses(InteractionHand.MAIN_HAND, entityIn);
			this.giveModelLeftArmPoses(InteractionHand.OFF_HAND, entityIn);
		} else {
			this.giveModelRightArmPoses(InteractionHand.OFF_HAND, entityIn);
			this.giveModelLeftArmPoses(InteractionHand.MAIN_HAND, entityIn);
		}
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	private void giveModelRightArmPoses(InteractionHand hand, T entityIn) {
		ItemStack itemstack = entityIn.getItemInHand(hand);
		UseAnim useaction = itemstack.getUseAnimation();
		if (entityIn.getArmPose() != AbstractIllager.IllagerArmPose.CROSSED
				|| !armorHasCrossedArms(entityIn, entityIn.getItemBySlot(EquipmentSlot.CHEST))) {
			switch (useaction) {
			case BLOCK:
				if (entityIn.isBlocking()) {
					this.rightArmPose = HumanoidModel.ArmPose.BLOCK;
				} else {
					this.rightArmPose = HumanoidModel.ArmPose.ITEM;
				}
				break;
			case CROSSBOW:
				this.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
				if (entityIn.isUsingItem()) {
					this.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}
				break;
			case BOW:
				this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
				break;
			case SPEAR:
				this.leftArmPose = ArmPose.THROW_SPEAR;
				break;
			default:
				this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
				if (!itemstack.isEmpty()) {
					this.rightArmPose = HumanoidModel.ArmPose.ITEM;
				}
				break;
			}
		}
	}

	private void giveModelLeftArmPoses(InteractionHand hand, T entityIn) {
		ItemStack itemstack = entityIn.getItemInHand(hand);
		UseAnim useaction = itemstack.getUseAnimation();
		if (entityIn.getArmPose() != AbstractIllager.IllagerArmPose.CROSSED
				|| !armorHasCrossedArms(entityIn, entityIn.getItemBySlot(EquipmentSlot.CHEST))) {
			switch (useaction) {
			case BLOCK:
				if (entityIn.isBlocking()) {
					this.leftArmPose = HumanoidModel.ArmPose.BLOCK;
				} else {
					this.leftArmPose = HumanoidModel.ArmPose.ITEM;
				}
				break;
			case CROSSBOW:
				this.leftArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
				if (entityIn.isUsingItem()) {
					this.leftArmPose = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}
				break;
			case BOW:
				this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
				break;
			case SPEAR:
				this.leftArmPose = ArmPose.THROW_SPEAR;
				break;
			default:
				this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
				if (!itemstack.isEmpty()) {
					this.leftArmPose = HumanoidModel.ArmPose.ITEM;
				}
				break;
			}
		}
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllager.IllagerArmPose armpose = entityIn.getArmPose();
		this.arms.y = 3.0F;
		this.arms.z = -1.0F;
		this.arms.xRot = -0.75F;
		this.jacket.copyFrom(body);
		boolean isWearingChestplateOrLeggings = entityIn.getItemBySlot(EquipmentSlot.CHEST)
				.getItem() instanceof ArmorItem
				|| entityIn.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem;
		this.jacket.visible = !isWearingChestplateOrLeggings;
		boolean flag = armpose == AbstractIllager.IllagerArmPose.CROSSED
				&& armorHasCrossedArms(entityIn, entityIn.getItemBySlot(EquipmentSlot.CHEST));
		this.arms.visible = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;

		float f = 1.0F;
		if (flag) {
			f = (float) entityIn.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		if (flag) {
			this.leftArm.y = 3.0F;
			this.leftArm.z = -1.0F;
			this.leftArm.xRot = -0.75F;
			this.rightArm.y = 3.0F;
			this.rightArm.z = -1.0F;
			this.rightArm.xRot = -0.75F;
		}

		if (this.riding) {
			this.rightArm.xRot = (-(float) Math.PI / 5F);
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = (-(float) Math.PI / 5F);
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = ((float) Math.PI / 10F);
			this.leftLeg.zRot = 0.07853982F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = (-(float) Math.PI / 10F);
			this.rightLeg.zRot = -0.07853982F;
		} else {
			this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.leftLeg.yRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
			this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
		}
		if (entityIn instanceof MountaineerEntity && ((MountaineerEntity) entityIn).isClimbing()) {
			this.rightArm.xRot = -1.8849558F + Mth.sin(ageInTicks * 0.35F) * 0.5F;
			this.leftArm.xRot = -1.8849558F - Mth.sin(ageInTicks * 0.35F) * 0.5F;
		} else {
			switch (armpose) {
			case ATTACKING:
				if (!entityIn.getMainHandItem().isEmpty()
						&& !(entityIn.getMainHandItem().getItem() instanceof ProjectileWeaponItem)
						&& !(entityIn.isBlocking())) {
					// raises arm with weapon, moves left arm back and forth while attacking
					AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
				}
				/*
				 * else if(!entityIn.getHeldItemMainhand().isEmpty() &&
				 * !(entityIn.getHeldItemMainhand().getItem() instanceof ShootableItem)){
				 * ModelUtils.readyWeaponWhileBlocking(this.bipedRightArm, this.bipedLeftArm,
				 * entityIn, this.swingProgress, ageInTicks); }
				 * 
				 */
				break;
			case CELEBRATING:
				this.rightArm.z = 0.0F;
				this.rightArm.x = -5.0F;
				this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
				this.rightArm.zRot = 2.670354F;
				this.rightArm.yRot = 0.0F;
				this.leftArm.z = 0.0F;
				this.leftArm.x = 5.0F;
				this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
				this.leftArm.zRot = -2.3561945F;
				this.leftArm.yRot = 0.0F;
				break;
			case SPELLCASTING:
				this.rightArm.z = 0.0F;
				this.rightArm.x = -5.0F;
				this.leftArm.z = 0.0F;
				this.leftArm.x = 5.0F;
				this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
				this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
				this.rightArm.zRot = 2.3561945F;
				this.leftArm.zRot = -2.3561945F;
				this.rightArm.yRot = 0.0F;
				this.leftArm.yRot = 0.0F;
				break;
			default:
				break;
			}
		}
	}

	public void copyPropertiesTo(IllagerBipedModel<T> p_217148_1_) {
		super.copyPropertiesTo(p_217148_1_);
		p_217148_1_.arms.copyFrom(this.arms);
		p_217148_1_.jacket.copyFrom(this.jacket);
	}
}