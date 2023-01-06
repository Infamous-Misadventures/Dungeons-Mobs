package com.infamous.dungeons_mobs.items;

import java.util.function.Consumer;

import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class ColoredTridentItem extends TridentItem implements IHasInventorySprite {
	private final DyeColor tridentColor;

	public ColoredTridentItem(Properties properties, DyeColor dyeColor) {
		super(properties);
		this.tridentColor = dyeColor;
	}

	public DyeColor getTridentColor() {
		return tridentColor;
	}

	@Override
	public String getModelResource() {
		return "replace with actual location, currently just here to assure the Tridents are working correctly";
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy
					.of(() -> new CustomISTER(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
							Minecraft.getInstance().getEntityModels()));

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer.get();
			}
		});
	}
}
