package com.infamous.dungeons_mobs.items.shield;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.armor.SkeletonVanguardShieldModel;
import com.infamous.dungeons_mobs.items.ColoredTridentItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.entity.model.TridentModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.infamous.dungeons_mobs.items.shield.ShieldTextures.*;

@OnlyIn(Dist.CLIENT)
public class CustomISTER extends ItemStackTileEntityRenderer {

	private final ShieldModel modelShield = new ShieldModel();
	private final SkeletonVanguardShieldModel modelVanguardShield = new SkeletonVanguardShieldModel();
	private final TridentModel tridentModel = new TridentModel();

	public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		Item item = stack.getItem();
		if (item instanceof RoyalGuardShieldItem) {
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
		else if (item instanceof SkeletonVanguardShieldItem) {
			matrixStack.pushPose();
			matrixStack.scale(1.0F, -1.0F, -1.0F);
			RenderMaterial rendermaterial = LOCATION_SKELETON_VANGUARD_SHIELD;
			IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.modelShield.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
			//this.modelVanguardShield.getHandle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			this.modelVanguardShield.getPlate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

			matrixStack.popPose();
		} else if (item instanceof ColoredTridentItem) {
			renderTridentEntityModel(stack, matrixStack, buffer, transformType, combinedLight, combinedOverlay, (ColoredTridentItem) item);

		}
	}

	private void renderTridentSpriteModel(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemCameraTransforms.TransformType transformType, int combinedLight, int combinedOverlay, ColoredTridentItem item, boolean inHand) {
		matrixStack.pushPose();
		//matrixStack.translate(0.5, 0.5, 0.5);
		IBakedModel model = Minecraft.getInstance().getModelManager().getModel(getTridentMRL(item.getTridentColor(), inHand));
		Minecraft.getInstance().getItemRenderer().render(stack, transformType, false, matrixStack, buffer, combinedLight, combinedOverlay, model);
		matrixStack.popPose();
	}

	private void renderTridentEntityModel(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemCameraTransforms.TransformType transformType, int combinedLight, int combinedOverlay, ColoredTridentItem item) {
		matrixStack.pushPose();
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		ResourceLocation texture = getTridentTexture(item.getTridentColor());
		IVertexBuilder foilBufferDirect = ItemRenderer.getFoilBufferDirect(buffer, this.tridentModel.renderType(texture), false, stack.hasFoil());
		this.tridentModel.renderToBuffer(matrixStack, foilBufferDirect, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
	}

	public static ResourceLocation getTridentTexture(DyeColor dyeColor) {
		String path = String.format("textures/entity/%s_trident.png", dyeColor.getName());
		return new ResourceLocation(DungeonsMobs.MODID, path);
	}

	public static ModelResourceLocation getTridentMRL(DyeColor dyeColor, boolean inHand) {
		String path = String.format("%s_trident%s", dyeColor.getName(), inHand ? "_in_hand" : "");
		ResourceLocation resourceLoc = new ResourceLocation(DungeonsMobs.MODID, path);
		return new ModelResourceLocation(resourceLoc, "inventory");
	}

}