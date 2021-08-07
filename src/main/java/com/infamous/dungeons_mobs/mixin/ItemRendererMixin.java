package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.items.ColoredTridentItem;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
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

    @Shadow @Final private ItemModelMesher itemModelShaper;

    @Shadow
    public static IVertexBuilder getFoilBufferDirect(IRenderTypeBuffer p_239391_0_, RenderType p_239391_1_, boolean p_239391_2_, boolean p_239391_3_) {
        return null;
    }

    @Shadow public abstract void renderModelLists(IBakedModel p_229114_1_, ItemStack p_229114_2_, int p_229114_3_, int p_229114_4_, MatrixStack p_229114_5_, IVertexBuilder p_229114_6_);

    @Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
    private void renderCustomTrident(ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHandHackery, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_229111_6_, int p_229111_7_, IBakedModel bakedModel, CallbackInfo ci){
        Item item = stack.getItem();
        if(!(item instanceof ColoredTridentItem)) return;

        ci.cancel();
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            boolean renderSprite = transformType == ItemCameraTransforms.TransformType.GUI || transformType == ItemCameraTransforms.TransformType.GROUND || transformType == ItemCameraTransforms.TransformType.FIXED;
            if (renderSprite) {
                bakedModel = getCustomTridentMRL((ColoredTridentItem) item, false);
            }

            bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, bakedModel, transformType, leftHandHackery);
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            if (!bakedModel.isCustomRenderer() && renderSprite) {
                boolean direct = true;
                if (bakedModel.isLayered()) { net.minecraftforge.client.ForgeHooksClient.drawItemLayered((ItemRenderer)(Object)this, bakedModel, stack, matrixStack, renderTypeBuffer, p_229111_6_, p_229111_7_, direct); }
                else {
                    RenderType rendertype = RenderTypeLookup.getRenderType(stack, direct);
                    IVertexBuilder ivertexbuilder;
                    ivertexbuilder = getFoilBufferDirect(renderTypeBuffer, rendertype, true, stack.hasFoil());

                    this.renderModelLists(bakedModel, stack, p_229111_6_, p_229111_7_, matrixStack, ivertexbuilder);
                }
            } else {
                item.getItemStackTileEntityRenderer().renderByItem(stack, transformType, matrixStack, renderTypeBuffer, p_229111_6_, p_229111_7_);
            }

            matrixStack.popPose();
        }
    }

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/ItemModelMesher;getItemModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/renderer/model/IBakedModel;"), method = "getModel")
    private IBakedModel checkForCustomTrident(IBakedModel original, ItemStack stack, @Nullable World world, @Nullable LivingEntity livingEntity){
        Item item = stack.getItem();
        if(item instanceof ColoredTridentItem){
            return getCustomTridentMRL((ColoredTridentItem) item, true);
        } else{
            return original;
        }
    }

    private IBakedModel getCustomTridentMRL(ColoredTridentItem item, boolean b) {
        return this.itemModelShaper.getModelManager().getModel(CustomISTER.getTridentMRL(item.getTridentColor(), b));
    }
}
