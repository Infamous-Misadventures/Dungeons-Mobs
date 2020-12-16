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

	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		Item item = stack.getItem();
		if (item == ModItems.ROYAL_GUARD_SHIELD.get()) {
		boolean flag = stack.getChildTag("BlockEntityTag") != null;
		matrixStack.push();
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		RenderMaterial rendermaterial = flag ? LOCATION_ROYAL_GUARD_SHIELD_BASE : LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN;
		IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(buffer, this.modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
		this.modelShield.func_228294_b_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (flag) {
			List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.getPatternColorData(ShieldItem.getColor(stack), BannerTileEntity.getPatternData(stack));
			BannerTileEntityRenderer.func_241717_a_(matrixStack, buffer, combinedLight, combinedOverlay, this.modelShield.func_228293_a_(), rendermaterial, false, list, stack.hasEffect());
		} else {
			this.modelShield.func_228293_a_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrixStack.pop();
		}
		else if (item == ModItems.SKELETON_VANGUARD_SHIELD.get()) {
			matrixStack.push();
			matrixStack.scale(1.0F, -1.0F, -1.0F);
			RenderMaterial rendermaterial = LOCATION_SKELETON_VANGUARD_SHIELD;
			IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(buffer, this.modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
			//this.modelVanguardShield.getHandle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			this.modelVanguardShield.getPlate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

			matrixStack.pop();
		}
	}

}