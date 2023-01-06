package com.infamous.dungeons_mobs.items.shield;

import java.util.function.Consumer;

import com.infamous.dungeons_mobs.items.shield.bewlr.VanguardShieldBEWLR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class VanguardShieldItem extends ShieldItem {
	public VanguardShieldItem(Properties builder) {
		super(builder);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
		return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy
					.of(() -> new VanguardShieldBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
							Minecraft.getInstance().getEntityModels()));

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer.get();
			}
		});
	}
}
