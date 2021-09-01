package com.infamous.dungeons_mobs.mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.mobenchants.*;

import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ModMobEnchantments {
    public static final DeferredRegister<MobEnchantment> MOB_ENCHANTMENTS_DEFERRED = DeferredRegister.create(MobEnchantment.class, MODID);
    public static final Lazy<IForgeRegistry<MobEnchantment>> MOB_ENCHANTMENTS = Lazy.of(ModMobEnchantments.MOB_ENCHANTMENTS_DEFERRED.makeRegistry("mob_enchantment", RegistryBuilder::new));

    public static final RegistryObject<DoubleDamageMobEnchantment> DOUBLE_DAMAGE = MOB_ENCHANTMENTS_DEFERRED.register("double_damage", () -> new DoubleDamageMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<ProtectionMobEnchantment> PROTECTION = MOB_ENCHANTMENTS_DEFERRED.register("protection", () -> new ProtectionMobEnchantment(MobEnchantment.Rarity.COMMON));
    public static final RegistryObject<QuickMobEnchantment> QUICK = MOB_ENCHANTMENTS_DEFERRED.register("quick", () -> new QuickMobEnchantment(MobEnchantment.Rarity.COMMON));
    public static final RegistryObject<FireTrailMobEnchantment> FIRE_TRAIL = MOB_ENCHANTMENTS_DEFERRED.register("fire_trail", () -> new FireTrailMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
    public static final RegistryObject<ThornsMobEnchantment> THORNS = MOB_ENCHANTMENTS_DEFERRED.register("thorns", () -> new ThornsMobEnchantment(MobEnchantment.Rarity.COMMON));
}
