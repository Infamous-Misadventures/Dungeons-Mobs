package com.infamous.dungeons_mobs.items.shield;

import com.infamous.dungeons_mobs.client.models.armor.SkeletonVanguardShieldModel;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.infamous.dungeons_mobs.items.shield.ShieldTextures.*;

@OnlyIn(Dist.CLIENT)
public class ShieldTileEntityRenderer extends ItemStackTileEntityRenderer {

	private final ShieldModel modelShield = new ShieldModel();
	private final SkeletonVanguardShieldModel modelVanguardShield = new SkeletonVanguardShieldModel();

	public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		Item item = stack.getItem();
		if (item == ModItems.ROYAL_GUARD_SHIELD.get()) {
		boolean flag = stack.getTagElement("BlockEntityTag") != null;
		matrixStack.pushPose();
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		RenderMaterial rendermaterial = flag ? LOCATION_ROYAL_GUARD_SHIELD_BASE : LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN;
		IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.modelShield.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
		this.modelShield.handle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (flag) {
			List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.createPatterns(ShieldItem.getColor(stack), BannerTileEntity.getItemPatterns(stack));
			BannerTileEntityRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay, this.modelShield.plate(), rendermaterial, false, list, stack.hasFoil());
		} else {
			this.modelShield.plate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrixStack.popPose();
		}
		else if (item == ModItems.SKELETON_VANGUARD_SHIELD.get()) {
			matrixStack.pushPose();
			matrixStack.scale(1.0F, -1.0F, -1.0F);
			RenderMaterial rendermaterial = LOCATION_SKELETON_VANGUARD_SHIELD;
			IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.modelShield.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
			//this.modelVanguardShield.getHandle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			this.modelVanguardShield.getPlate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

			matrixStack.popPose();
		}
	}

}