package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.entities.illagers.IllagerArmsUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IllagerBipedArmorLayer<T extends AbstractIllagerEntity, M extends IllagerBipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A> {

    private final A armorModel;
    private final IllagerBipedModel<T> crossedArmsArmorModel;

    public IllagerBipedArmorLayer(IEntityRenderer<T, M> p_i50936_1_, A p_i50936_2_, A p_i50936_3_, IllagerBipedModel<T> crossedArmsArmorModel) {
        super(p_i50936_1_, p_i50936_2_, p_i50936_3_);
        this.armorModel = p_i50936_3_;
        this.crossedArmsArmorModel = crossedArmsArmorModel;
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        super.render(p_225628_1_, p_225628_2_, p_225628_3_, p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
        this.renderArmorPiece(p_225628_1_, p_225628_2_, p_225628_4_, EquipmentSlotType.CHEST, p_225628_3_, crossedArmsArmorModel);
    }

    private void renderArmorPiece(MatrixStack p_241739_1_, IRenderTypeBuffer p_241739_2_, T p_241739_3_, EquipmentSlotType p_241739_4_, int p_241739_5_, IllagerBipedModel<T> crossedArmsModel) {
        ItemStack itemstack = p_241739_3_.getItemBySlot(p_241739_4_);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem) itemstack.getItem();
            if (armoritem.getSlot() == p_241739_4_) {
                this.getParentModel().copyPropertiesTo(crossedArmsModel);
                ResourceLocation crossedTexture = this.getArmorResource(p_241739_3_, itemstack, p_241739_4_, "crossed");
                boolean armsCanBeCrossed = IllagerArmsUtil.resourceExists(crossedTexture);
                this.setPartVisibilityCrossedArms(crossedArmsModel, p_241739_4_, armsCanBeCrossed);
                if(!armsCanBeCrossed) return;
                boolean flag1 = itemstack.hasFoil();
                if (armoritem instanceof net.minecraft.item.IDyeableArmorItem) {
                    int i = ((net.minecraft.item.IDyeableArmorItem) armoritem).getColor(itemstack);
                    float f = (float) (i >> 16 & 255) / 255.0F;
                    float f1 = (float) (i >> 8 & 255) / 255.0F;
                    float f2 = (float) (i & 255) / 255.0F;
                    this.renderModel(p_241739_1_, p_241739_2_, p_241739_5_, flag1, crossedArmsModel, f, f1, f2, crossedTexture);
                    this.renderModel(p_241739_1_, p_241739_2_, p_241739_5_, flag1, crossedArmsModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(p_241739_3_, itemstack, p_241739_4_, "crossed_overlay"));
                } else {
                    this.renderModel(p_241739_1_, p_241739_2_, p_241739_5_, flag1, crossedArmsModel, 1.0F, 1.0F, 1.0F, crossedTexture);
                }
            }
        }
    }

    private void renderModel(MatrixStack p_241738_1_, IRenderTypeBuffer p_241738_2_, int p_241738_3_, boolean p_241738_5_, IllagerBipedModel<T> p_241738_6_, float p_241738_8_, float p_241738_9_, float p_241738_10_, ResourceLocation armorResource) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(p_241738_2_, RenderType.armorCutoutNoCull(armorResource), false, p_241738_5_);
        p_241738_6_.renderToBuffer(p_241738_1_, ivertexbuilder, p_241738_3_, OverlayTexture.NO_OVERLAY, p_241738_8_, p_241738_9_, p_241738_10_, 1.0F);
    }

    @Override
    protected void setPartVisibility(A entityModel, EquipmentSlotType p_188359_2_) {
        super.setPartVisibility(entityModel, p_188359_2_);
        if (p_188359_2_ == EquipmentSlotType.CHEST) {
//            if (this.getParentModel().arms.visible) {
//                entityModel.rightArm.visible = false;
//                entityModel.leftArm.visible = false;
//            } else {
                entityModel.rightArm.visible = true;
                entityModel.leftArm.visible = true;
//            }
        }
    }

    private void setPartVisibilityCrossedArms(IllagerBipedModel<T> illagerEntityModel, EquipmentSlotType p_188359_2_, boolean armsCanBeCrossed) {
        illagerEntityModel.setAllVisible(false);
        illagerEntityModel.jacket.visible = false;
        illagerEntityModel.arms.visible = false;
        if (p_188359_2_ == EquipmentSlotType.CHEST) {
            illagerEntityModel.jacket.visible = true;
            if (this.getParentModel().arms.visible && armsCanBeCrossed) {
                illagerEntityModel.arms.visible = true;
                illagerEntityModel.rightArm.visible = false;
                illagerEntityModel.leftArm.visible = false;
            } else {
                illagerEntityModel.arms.visible = false;
                illagerEntityModel.rightArm.visible = false;
                illagerEntityModel.leftArm.visible = false;
            }
        }
    }
}
