package com.infamous.dungeons_mobs.capabilities.enchantable;

import com.infamous.dungeons_mobs.mobenchants.MobEnchantment;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.MOB_ENCHANTMENTS;

public class EnchantableStorage implements Capability.IStorage<IEnchantable> {

    public static final String ENCHANTS_KEY = "Enchants";

    @Override
    public INBT writeNBT(Capability<IEnchantable> capability, IEnchantable instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        ListNBT listnbt = new ListNBT();
        instance.getEnchantments().forEach(mobEnchant -> {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putString("id", String.valueOf(mobEnchant.getRegistryName()));
            listnbt.add(compoundnbt);
        });
        tag.put(ENCHANTS_KEY, listnbt);
        return tag;
    }

    @Override
    public void readNBT(Capability<IEnchantable> capability, IEnchantable instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        ListNBT listNBT = tag.getList(ENCHANTS_KEY, 10);

        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            ResourceLocation resourcelocation = ResourceLocation.tryParse(compoundnbt.getString("id"));
            MobEnchantment mobEnchantment = MOB_ENCHANTMENTS.get().getValue(resourcelocation);
            instance.addEnchant(mobEnchantment);
        }
    }
}