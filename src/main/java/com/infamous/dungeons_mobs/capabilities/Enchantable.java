package com.infamous.dungeons_mobs.capabilities;

import com.infamous.dungeons_mobs.mobenchants.MobEnchantment;

import java.util.ArrayList;
import java.util.List;

public class Enchantable implements IEnchantable{
    ArrayList<MobEnchantment> enchantments = new ArrayList<>();

    @Override
    public boolean addEnchant(MobEnchantment enchantment) {
        enchantments.add(enchantment);
        return true;
    }

    @Override
    public boolean removeEnchant(MobEnchantment enchantment) {
        enchantments.remove(enchantment);
        return true;
    }

    @Override
    public List<MobEnchantment> getEnchantments() {
        return enchantments;
    }

    public boolean hasEnchantment() {
        return !this.enchantments.isEmpty();
    }

    public boolean hasEnchantment(MobEnchantment mobEnchantment) {
        return this.enchantments.contains(mobEnchantment);
    }
}
