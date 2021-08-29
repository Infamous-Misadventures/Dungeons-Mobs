package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.client.models.armor.PillagerHelmetModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import net.minecraft.item.Item.Properties;

public class PillagerHelmetItem extends ArmorItem {
    private final boolean isDiamond;

    public PillagerHelmetItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn, boolean isDiamondIn) {
        super(materialIn, slot, builderIn);
        this.isDiamond = isDiamondIn;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) new PillagerHelmetModel<>(1.0F, slot, entityLiving);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (this.isDiamond) {
            return MODID + ":textures/models/armor/diamond_pillager_helmet.png";
        }
        return MODID + ":textures/models/armor/gold_pillager_helmet.png";
    }
}
