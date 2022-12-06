package com.infamous.dungeons_mobs.items.shield.bewlr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.infamous.dungeons_mobs.items.shield.ShieldTextures.LOCATION_ROYAL_GUARD_SHIELD_BASE;
import static com.infamous.dungeons_mobs.items.shield.ShieldTextures.LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN;
import static net.minecraft.client.model.geom.ModelLayers.SHIELD;

@OnlyIn(Dist.CLIENT)
public class RoyalGuardShieldBEWLR extends BlockEntityWithoutLevelRenderer {

    private final ShieldModel royalGuardShieldModel;

    public RoyalGuardShieldBEWLR(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        royalGuardShieldModel = new ShieldModel(p_172551_.bakeLayer(SHIELD));
    }

    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = stack.getItem();
        boolean flag = stack.getTagElement("BlockEntityTag") != null;
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        Material rendermaterial = flag ? LOCATION_ROYAL_GUARD_SHIELD_BASE : LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN;
        VertexConsumer ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.royalGuardShieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
        this.royalGuardShieldModel.handle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        if (flag) {
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
            BannerRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay, this.royalGuardShieldModel.plate(), rendermaterial, false, list, stack.hasFoil());
        } else {
            this.royalGuardShieldModel.plate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrixStack.popPose();
    }

}