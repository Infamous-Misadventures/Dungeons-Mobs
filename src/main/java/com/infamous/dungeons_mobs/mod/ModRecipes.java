package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.items.shield.CustomShieldRecipes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ModRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<IRecipeSerializer<?>> SHIELD_RECIPE = RECIPES.register("shield_decoration",
            () -> CustomShieldRecipes.SERIALIZER);
}
