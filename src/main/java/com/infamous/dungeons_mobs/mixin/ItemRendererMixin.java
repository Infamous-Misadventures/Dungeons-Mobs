package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.items.ColoredTridentItem;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.RenderProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow @Final private ItemModelShaper itemModelShaper;

    @Shadow
    public static VertexConsumer getFoilBufferDirect(MultiBufferSource p_239391_0_, RenderType p_239391_1_, boolean p_239391_2_, boolean p_239391_3_) {
        return null;
    }

    @Shadow public abstract void renderModelLists(BakedModel p_229114_1_, ItemStack p_229114_2_, int p_229114_3_, int p_229114_4_, PoseStack p_229114_5_, VertexConsumer p_229114_6_);

    @Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
    private void renderCustomTrident(ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHandHackery, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int p_229111_6_, int p_229111_7_, BakedModel bakedModel, CallbackInfo ci){
        Item item = stack.getItem();
        if(!((item instanceof IHasInventorySprite && ((IHasInventorySprite)item).getModelLocation() != null))) return;

        ci.cancel();
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            boolean renderSprite = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
            if (renderSprite) {
                if (item instanceof ColoredTridentItem) {
                    bakedModel = getCustomTridentMRL((ColoredTridentItem) item, false);
                }
            }

            bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, bakedModel, transformType, leftHandHackery);
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            if (!bakedModel.isCustomRenderer() && renderSprite) {
                boolean direct = true;
                if (bakedModel.isLayered()) { net.minecraftforge.client.ForgeHooksClient.drawItemLayered((ItemRenderer)(Object)this, bakedModel, stack, matrixStack, renderTypeBuffer, p_229111_6_, p_229111_7_, direct); }
                else {
                    RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, direct);
                    VertexConsumer ivertexbuilder;
                    ivertexbuilder = getFoilBufferDirect(renderTypeBuffer, rendertype, true, stack.hasFoil());

                    this.renderModelLists(bakedModel, stack, p_229111_6_, p_229111_7_, matrixStack, ivertexbuilder);
                }
            } else {
                RenderProperties.get(item).getItemStackRenderer().renderByItem(stack, transformType, matrixStack, renderTypeBuffer, p_229111_6_, p_229111_7_);
            }

            matrixStack.popPose();
        }
    }

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;"), method = "getModel")
    private BakedModel checkForCustomTridentModel(BakedModel original, ItemStack stack, @Nullable Level world, @Nullable LivingEntity livingEntity){
        Item item = stack.getItem();
        if (item instanceof ColoredTridentItem) {
            return getCustomTridentMRL((ColoredTridentItem) item, true);
        } else {
            return original;
        }
    }

    private BakedModel getCustomTridentMRL(ColoredTridentItem item, boolean b) {
        return this.itemModelShaper.getModelManager().getModel(CustomISTER.getTridentMRL(item.getTridentColor(), b));
    }
}
