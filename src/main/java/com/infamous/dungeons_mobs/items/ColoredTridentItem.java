package com.infamous.dungeons_mobs.items;

import java.util.concurrent.Callable;

import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.TridentItem;

public class ColoredTridentItem extends TridentItem implements IHasInventorySprite {
    private final DyeColor tridentColor;

    public ColoredTridentItem(Properties properties, DyeColor dyeColor) {
        super(properties.setISTER(ColoredTridentItem::getISTER));
        this.tridentColor = dyeColor;
    }

    public DyeColor getTridentColor() {
        return tridentColor;
    }
    
    @Override
    public String getModelLocation() {
    	return "replace with actual location, currently just here to assure the Tridents are working correctly";
    }

    private static Callable<ItemStackTileEntityRenderer> getISTER() {
        return CustomISTER::new;
    }
}
