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
        super(RenderType::entityTranslucent, modelSize, 0.0F, 64, 64);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTexSize(64, 32);
        this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, modelSize);
    }

    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
        this.bipedCape.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn.getItemBySlot(EquipmentSlotType.CHEST).isEmpty()) {
            if (entityIn.isCrouching()) {
                this.bipedCape.z = 1.4F;
                this.bipedCape.y = 1.85F;
            } else {
                this.bipedCape.z = 0.0F;
                this.bipedCape.y = 0.0F;
            }
        } else if (entityIn.isCrouching()) {
            this.bipedCape.z = 0.3F;
            this.bipedCape.y = 0.8F;
        } else {
            this.bipedCape.z = -1.1F;
            this.bipedCape.y = -0.85F;
        }

    }

    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        this.bipedCape.visible = visible;
    }
}
