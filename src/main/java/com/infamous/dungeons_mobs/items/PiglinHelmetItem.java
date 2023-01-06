package com.infamous.dungeons_mobs.items;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class PiglinHelmetItem extends ArmorItem {

    public PiglinHelmetItem(ArmorMaterial armorMaterial, EquipmentSlot slotType, Properties properties) {
        super(armorMaterial, slotType, properties);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return String.format(MODID + ":textures/models/armor/%s.png", ForgeRegistries.ITEMS.getKey(this).getPath());
    }
}
