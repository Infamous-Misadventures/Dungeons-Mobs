package com.infamous.dungeons_mobs.items.shield.bewlr;

import com.infamous.dungeons_mobs.client.models.armor.VanguardShieldModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.client.models.geom.ModModelLayers.VANGUARD_SHIELD;
import static com.infamous.dungeons_mobs.items.shield.ShieldTextures.LOCATION_VANGUARD_SHIELD;

@OnlyIn(Dist.CLIENT)
public class VanguardShieldBEWLR extends BlockEntityWithoutLevelRenderer {

    private final VanguardShieldModel modelVanguardShield;

    public VanguardShieldBEWLR(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        modelVanguardShield = new VanguardShieldModel(p_172551_.bakeLayer(VANGUARD_SHIELD));
    }

    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = stack.getItem();
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        Material rendermaterial = LOCATION_VANGUARD_SHIELD;
        VertexConsumer ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.modelVanguardShield.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
        //this.modelVanguardShield.getHandle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.modelVanguardShield.getRoot().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();

    }

}