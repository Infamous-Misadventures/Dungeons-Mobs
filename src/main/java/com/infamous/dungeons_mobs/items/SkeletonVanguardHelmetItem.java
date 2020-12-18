package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.client.models.armor.SkeletonVanguardHelmetModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class SkeletonVanguardHelmetItem extends ArmorItem {
    public SkeletonVanguardHelmetItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Item.Properties group) {
        super(armorMaterial, slotType, group);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) new SkeletonVanguardHelmetModel<>(1.0F, slot, entityLiving);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return MODID + ":textures/models/armor/skeleton_vanguard_helmet.png";
    }
}
