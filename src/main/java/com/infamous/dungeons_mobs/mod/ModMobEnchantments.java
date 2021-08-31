package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.mobenchants.DoubleDamageMobEnchantment;
import com.infamous.dungeons_mobs.mobenchants.MobEnchantment;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ModMobEnchantments {
    public static final DeferredRegister<MobEnchantment> MOB_ENCHANTMENTS_DEFERRED = DeferredRegister.create(MobEnchantment.class, MODID);
    public static final Lazy<IForgeRegistry<MobEnchantment>> MOB_ENCHANTMENTS = Lazy.of(ModMobEnchantments.MOB_ENCHANTMENTS_DEFERRED.makeRegistry("mob_enchantment", RegistryBuilder::new));

    public static final RegistryObject<DoubleDamageMobEnchantment> DOUBLE_DAMAGE = MOB_ENCHANTMENTS_DEFERRED.register("double_damage", () -> new DoubleDamageMobEnchantment(MobEnchantment.Rarity.COMMON));
}
