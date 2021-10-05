package com.infamous.dungeons_mobs.client.renderer.layer;

import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MobEnchantmentGlintLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public MobEnchantmentGlintLayer(IEntityRenderer<T, M> p_i226038_1_) {
        super(p_i226038_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        EnchantableHelper.getEnchantableCapability(p_225628_4_).ifPresent(cap -> {
            if (cap.hasEnchantment()) {
                float f = (float) p_225628_4_.tickCount + p_225628_7_;
                EntityModel<T> entitymodel = this.getParentModel();
                entitymodel.prepareMobModel(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_);
                this.getParentModel().copyPropertiesTo(entitymodel);
                IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f), f * 0.01F));
                entitymodel.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
                entitymodel.renderToBuffer(p_225628_1_, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        });
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }
}
