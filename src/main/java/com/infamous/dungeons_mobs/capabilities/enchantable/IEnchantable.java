package com.infamous.dungeons_mobs.capabilities.enchantable;

import com.infamous.dungeons_mobs.mobenchants.MobEnchantment;

import java.util.List;

public interface IEnchantable {

    boolean addEnchant(MobEnchantment enchantment);

    boolean removeEnchant(MobEnchantment enchantment);

    List<MobEnchantment> getEnchantments();

    boolean hasEnchantment();

    boolean hasEnchantment(MobEnchantment mobEnchantment);
}
