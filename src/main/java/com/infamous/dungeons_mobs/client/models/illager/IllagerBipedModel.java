package com.infamous.dungeons_mobs.client.models.illager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.util.Hand;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

// Borrowed from tallestred / seymourimadeit's IllagersWearArmor mod
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;

public class IllagerBipedModel<T extends AbstractIllagerEntity> extends BipedModel<T> {
    public ModelRenderer arms;
    public ModelRenderer jacket;

    public IllagerBipedModel(float modelSize, float yOffset, int textureWidthIn, int textureHeightIn) {
        super(modelSize);
        this.head = (new ModelRenderer(this)).setTexSize(textureWidthIn, textureHeightIn);
        this.head.setPos(0.0F, 0.0F + yOffset, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);
        this.hat = (new ModelRenderer(this, 32, 0)).setTexSize(textureWidthIn, textureHeightIn);
        this.hat.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, modelSize + 0.45F);
        this.hat.visible = false;
        ModelRenderer nose = (new ModelRenderer(this)).setTexSize(textureWidthIn, textureHeightIn);
        nose.setPos(0.0F, yOffset - 2.0F, 0.0F);
        nose.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
        this.head.addChild(nose);
        this.body = (new ModelRenderer(this)).setTexSize(textureWidthIn, textureHeightIn);
        this.body.setPos(0.0F, 0.0F + yOffset, 0.0F);
        this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);

        this.jacket = (new ModelRenderer(this)).setTexSize(textureWidthIn, textureHeightIn);
        this.jacket.setPos(0.0F, 0.0F + yOffset, 0.0F);
        this.jacket.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, modelSize + 0.5F);

        this.arms = (new ModelRenderer(this)).setTexSize(textureWidthIn, textureHeightIn);
        this.arms.setPos(0.0F, 0.0F + yOffset + 2.0F, 0.0F);
        this.arms.texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
        ModelRenderer upperArm = (new ModelRenderer(this, 44, 22)).setTexSize(textureWidthIn, textureHeightIn);
        upperArm.mirror = true;
        upperArm.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
        this.arms.addChild(upperArm);
        this.arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, modelSize);

        this.rightLeg = (new ModelRenderer(this, 0, 22)).setTexSize(textureWidthIn, textureHeightIn);
        this.rightLeg.setPos(-2.0F, 12.0F + yOffset, 0.0F);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);

        this.leftLeg = (new ModelRenderer(this, 0, 22)).setTexSize(textureWidthIn, textureHeightIn);
        this.leftLeg.mirror = true;
        this.leftLeg.setPos(2.0F, 12.0F + yOffset, 0.0F);
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);

        this.rightArm = (new ModelRenderer(this, 40, 46)).setTexSize(textureWidthIn, textureHeightIn);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.rightArm.setPos(-5.0F, 2.0F + yOffset, 0.0F);

        this.leftArm = (new ModelRenderer(this, 40, 46)).setTexSize(textureWidthIn, textureHeightIn);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.leftArm.setPos(5.0F, 2.0F + yOffset, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.arms, this.jacket));
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
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AbstractIllagerEntity.ArmPose armpose = entityIn.getArmPose();
        this.arms.y = 3.0F;
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
        this.jacket.copyFrom(body);
        boolean isWearingChestplateOrLeggings = entityIn.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem || entityIn.getItemBySlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
        this.jacket.visible = !isWearingChestplateOrLeggings;
        boolean flag = armpose == AbstractIllagerEntity.ArmPose.CROSSED;
        this.arms.visible = flag;
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
}