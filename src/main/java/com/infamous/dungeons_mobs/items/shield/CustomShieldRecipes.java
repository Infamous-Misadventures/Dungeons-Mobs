package com.infamous.dungeons_mobs.items.shield;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CustomShieldRecipes extends SpecialRecipe {

	public static final IRecipeSerializer<CustomShieldRecipes> SERIALIZER = new SpecialRecipeSerializer<CustomShieldRecipes>(
			CustomShieldRecipes::new);

	public CustomShieldRecipes(ResourceLocation idIn) {
		super(idIn);
	}

	public boolean matches(CraftingInventory inv, World worldIn) {
		ItemStack itemstack = ItemStack.EMPTY;
		ItemStack itemstack1 = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack itemstack2 = inv.getItem(i);
			if (!itemstack2.isEmpty()) {
				if (itemstack2.getItem() instanceof BannerItem) {
					if (!itemstack1.isEmpty()) {
						return false;
					}

					itemstack1 = itemstack2;
				} else {
					if (!(itemstack2.getItem() instanceof RoyalGuardShieldItem)) {
						return false;
					}

					if (!itemstack.isEmpty()) {
						return false;
					}

					if (itemstack2.getTagElement("BlockEntityTag") != null) {
						return false;
					}

					itemstack = itemstack2;
				}
			}
		}

		if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public ItemStack assemble(CraftingInventory inv) {
		ItemStack itemstack = ItemStack.EMPTY;
		ItemStack itemstack1 = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack itemstack2 = inv.getItem(i);
			if (!itemstack2.isEmpty()) {
				if (itemstack2.getItem() instanceof BannerItem) {
					itemstack = itemstack2;
				} else if (itemstack2.getItem() instanceof RoyalGuardShieldItem) {
					itemstack1 = itemstack2.copy();
				}
			}
		}

		if (itemstack1.isEmpty()) {
			return itemstack1;
		} else {
			CompoundNBT compoundnbt = itemstack.getTagElement("BlockEntityTag");
			CompoundNBT compoundnbt1 = compoundnbt == null ? new CompoundNBT() : compoundnbt.copy();
			compoundnbt1.putInt("Base", ((BannerItem) itemstack.getItem()).getColor().getId());
			itemstack1.addTagElement("BlockEntityTag", compoundnbt1);
			return itemstack1;
		}
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}