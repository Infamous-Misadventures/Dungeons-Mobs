package com.infamous.dungeons_mobs.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModdedCapeModel<T extends LivingEntity> extends BipedModel<T> {
    private final ModelRenderer bipedCape;

    public ModdedCapeModel(float modelSize) {
        super(RenderType::getEntityTranslucent, modelSize, 0.0F, 64, 64);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, modelSize);
    }

    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
        this.bipedCape.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
            if (entityIn.isCrouching()) {
                this.bipedCape.rotationPointZ = 1.4F;
                this.bipedCape.rotationPointY = 1.85F;
            } else {
                this.bipedCape.rotationPointZ = 0.0F;
                this.bipedCape.rotationPointY = 0.0F;
            }
        } else if (entityIn.isCrouching()) {
            this.bipedCape.rotationPointZ = 0.3F;
            this.bipedCape.rotationPointY = 0.8F;
        } else {
            this.bipedCape.rotationPointZ = -1.1F;
            this.bipedCape.rotationPointY = -0.85F;
        }

    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.bipedCape.showModel = visible;
    }
}
