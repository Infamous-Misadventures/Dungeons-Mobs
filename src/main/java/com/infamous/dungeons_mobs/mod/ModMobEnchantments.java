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
    public static final RegistryObject<DeflectMobEnchantment> DEFLECT = MOB_ENCHANTMENTS_DEFERRED.register("deflect", () -> new DeflectMobEnchantment(MobEnchantment.Rarity.UNCOMMON));
    public static final RegistryObject<RegenerationMobEnchantment> REGENERATION = MOB_ENCHANTMENTS_DEFERRED.register("regeneration", () -> new RegenerationMobEnchantment(MobEnchantment.Rarity.UNCOMMON));
    public static final RegistryObject<TempoTheftMobEnchantment> TEMPO_THEFT = MOB_ENCHANTMENTS_DEFERRED.register("tempo_theft", () -> new TempoTheftMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<MultishotMobEnchantment> MULTISHOT = MOB_ENCHANTMENTS_DEFERRED.register("multishot", () -> new MultishotMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<EchoMobEnchantment> ECHO = MOB_ENCHANTMENTS_DEFERRED.register("echo", () -> new EchoMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
    public static final RegistryObject<LevitationShotMobEnchantment> LEVITATION_SHOT = MOB_ENCHANTMENTS_DEFERRED.register("levitation_shot", () -> new LevitationShotMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
}
