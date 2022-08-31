package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.mobenchants.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModMobEnchantments {
    public static final DeferredRegister<MobEnchantment> MOB_ENCHANTMENTS_DEFERRED = DeferredRegister.create(MobEnchantment.class, DungeonsMobs.MODID);

    public static final RegistryObject<ZDoneDoubleDamageMobEnchantment> DOUBLE_DAMAGE = MOB_ENCHANTMENTS_DEFERRED.register("double_damage", () -> new ZDoneDoubleDamageMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<ZDoneProtectionMobEnchantment> PROTECTION = MOB_ENCHANTMENTS_DEFERRED.register("protection", () -> new ZDoneProtectionMobEnchantment(MobEnchantment.Rarity.COMMON));
    public static final RegistryObject<ZDoneQuickMobEnchantment> QUICK = MOB_ENCHANTMENTS_DEFERRED.register("quick", () -> new ZDoneQuickMobEnchantment(MobEnchantment.Rarity.COMMON));
    public static final RegistryObject<FireTrailMobEnchantment> FIRE_TRAIL = MOB_ENCHANTMENTS_DEFERRED.register("fire_trail", () -> new FireTrailMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
    public static final RegistryObject<ZDoneThornsMobEnchantment> THORNS = MOB_ENCHANTMENTS_DEFERRED.register("thorns", () -> new ZDoneThornsMobEnchantment(MobEnchantment.Rarity.COMMON));
    public static final RegistryObject<DeflectMobEnchantment> DEFLECT = MOB_ENCHANTMENTS_DEFERRED.register("deflect", () -> new DeflectMobEnchantment(MobEnchantment.Rarity.UNCOMMON));
    public static final RegistryObject<RegenerationMobEnchantment> REGENERATION = MOB_ENCHANTMENTS_DEFERRED.register("regeneration", () -> new RegenerationMobEnchantment(MobEnchantment.Rarity.UNCOMMON));
    public static final RegistryObject<TempoTheftMobEnchantment> TEMPO_THEFT = MOB_ENCHANTMENTS_DEFERRED.register("tempo_theft", () -> new TempoTheftMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<ZDoneMultishotMobEnchantment> MULTISHOT = MOB_ENCHANTMENTS_DEFERRED.register("multishot", () -> new ZDoneMultishotMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<EchoMobEnchantment> ECHO = MOB_ENCHANTMENTS_DEFERRED.register("echo", () -> new EchoMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
    public static final RegistryObject<LevitationShotMobEnchantment> LEVITATION_SHOT = MOB_ENCHANTMENTS_DEFERRED.register("levitation_shot", () -> new LevitationShotMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
    public static final RegistryObject<RushMobEnchantment> RUSH = MOB_ENCHANTMENTS_DEFERRED.register("rush", () -> new RushMobEnchantment(MobEnchantment.Rarity.COMMON));
    public static final RegistryObject<BurningMobEnchantment> BURNING = MOB_ENCHANTMENTS_DEFERRED.register("burning", () -> new BurningMobEnchantment(MobEnchantment.Rarity.UNCOMMON));
    public static final RegistryObject<ChillingMobEnchantment> CHILLING = MOB_ENCHANTMENTS_DEFERRED.register("chilling", () -> new ChillingMobEnchantment(MobEnchantment.Rarity.UNCOMMON));
    public static final RegistryObject<GravityPulseMobEnchantment> GRAVITY_PULSE = MOB_ENCHANTMENTS_DEFERRED.register("gravity_pulse", () -> new GravityPulseMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<RadianceMobEnchantment> RADIANCE = MOB_ENCHANTMENTS_DEFERRED.register("radiance", () -> new RadianceMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<HealsAlliesMobEnchantment> HEALS_ALLIES = MOB_ENCHANTMENTS_DEFERRED.register("heals_allies", () -> new HealsAlliesMobEnchantment(MobEnchantment.Rarity.RARE));
    public static final RegistryObject<ZDoneHugeMobEnchantment> HUGE = MOB_ENCHANTMENTS_DEFERRED.register("huge", () -> new ZDoneHugeMobEnchantment(MobEnchantment.Rarity.VERY_RARE));
}
