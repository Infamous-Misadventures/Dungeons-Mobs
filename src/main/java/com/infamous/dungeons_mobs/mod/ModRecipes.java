package com.infamous.dungeons_mobs.mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.items.shield.CustomShieldRecipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

	public static final RegistryObject<RecipeSerializer<?>> SHIELD_RECIPE = RECIPES.register("shield_decoration",
			() -> CustomShieldRecipes.SERIALIZER);
}
