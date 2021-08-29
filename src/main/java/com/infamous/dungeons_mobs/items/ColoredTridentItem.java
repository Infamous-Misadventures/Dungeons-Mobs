package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.TridentItem;

import java.util.concurrent.Callable;

public class ColoredTridentItem extends TridentItem {
    private final DyeColor tridentColor;

    public ColoredTridentItem(Properties properties, DyeColor dyeColor) {
        super(properties.setISTER(ColoredTridentItem::getISTER));
        this.tridentColor = dyeColor;
    }

    public DyeColor getTridentColor() {
        return tridentColor;
    }

    private static Callable<ItemStackTileEntityRenderer> getISTER() {
        return CustomISTER::new;
    }
}
