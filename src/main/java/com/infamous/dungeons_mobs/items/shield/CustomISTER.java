package com.infamous.dungeons_mobs.items.shield;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.armor.VanguardShieldModel;
import com.infamous.dungeons_mobs.items.ColoredTridentItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.infamous.dungeons_mobs.client.models.geom.ModModelLayers.VANGUARD_SHIELD;
import static com.infamous.dungeons_mobs.items.shield.ShieldTextures.*;
import static net.minecraft.client.model.geom.ModelLayers.SHIELD;
import static net.minecraft.client.model.geom.ModelLayers.TRIDENT;

@OnlyIn(Dist.CLIENT)
public class CustomISTER extends BlockEntityWithoutLevelRenderer {

	private final ShieldModel royalGuardShieldModel;
	private final VanguardShieldModel modelVanguardShield;
	private final TridentModel tridentModel;

	public CustomISTER(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
		super(p_172550_, p_172551_);
		modelVanguardShield = new VanguardShieldModel(p_172551_.bakeLayer(VANGUARD_SHIELD));
		tridentModel = new TridentModel(p_172551_.bakeLayer(TRIDENT));
		royalGuardShieldModel = new ShieldModel(p_172551_.bakeLayer(SHIELD));
	}

	public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
		Item item = stack.getItem();
		if (item instanceof RoyalGuardShieldItem) {
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
		else if (item instanceof VanguardShieldItem) {
			matrixStack.pushPose();
			matrixStack.scale(1.0F, -1.0F, -1.0F);
			Material rendermaterial = LOCATION_VANGUARD_SHIELD;
			VertexConsumer ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.royalGuardShieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
			//this.modelVanguardShield.getHandle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			this.modelVanguardShield.getRoot().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

			matrixStack.popPose();
		} else if (item instanceof ColoredTridentItem) {
			renderTridentEntityModel(stack, matrixStack, buffer, transformType, combinedLight, combinedOverlay, (ColoredTridentItem) item);

		}
	}

	private void renderTridentSpriteModel(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, ItemTransforms.TransformType transformType, int combinedLight, int combinedOverlay, ColoredTridentItem item, boolean inHand) {
		matrixStack.pushPose();
		//matrixStack.translate(0.5, 0.5, 0.5);
		BakedModel model = Minecraft.getInstance().getModelManager().getModel(getTridentMRL(item.getTridentColor(), inHand));
		Minecraft.getInstance().getItemRenderer().render(stack, transformType, false, matrixStack, buffer, combinedLight, combinedOverlay, model);
		matrixStack.popPose();
	}

	private void renderTridentEntityModel(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, ItemTransforms.TransformType transformType, int combinedLight, int combinedOverlay, ColoredTridentItem item) {
		matrixStack.pushPose();
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		ResourceLocation texture = getTridentTexture(item.getTridentColor());
		VertexConsumer foilBufferDirect = ItemRenderer.getFoilBufferDirect(buffer, this.tridentModel.renderType(texture), false, stack.hasFoil());
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