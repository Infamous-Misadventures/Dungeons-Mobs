package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.client.models.armor.ChefHatModel;
import com.infamous.dungeons_mobs.client.models.armor.SkeletonVanguardHelmetModel;
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

public class ChefHatItem extends ArmorItem {
    public ChefHatItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties group) {
        super(armorMaterial, slotType, group);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) new ChefHatModel<>(1.0F, slot, entityLiving);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return MODID + ":textures/models/armor/chef_hat.png";
    }
}
