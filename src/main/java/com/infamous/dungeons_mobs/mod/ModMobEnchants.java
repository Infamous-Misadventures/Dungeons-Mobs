package com.infamous.dungeons_mobs.mod;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.mobenchants.RadianceMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.RegenerationMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.RushMobEnchant;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModMobEnchants {
    public static final DeferredRegister<MobEnchant> MOB_ENCHANTS_DEFERRED = DeferredRegister.create(MobEnchant.class, DungeonsMobs.MODID);

    public static final RegistryObject<RushMobEnchant> RUSH = MOB_ENCHANTS_DEFERRED.register("rush", () -> new RushMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3)));
    public static final RegistryObject<RegenerationMobEnchant> REGENERATION = MOB_ENCHANTS_DEFERRED.register("regeneration", () -> new RegenerationMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3)));
    public static final RegistryObject<RadianceMobEnchant> RADIANCE = MOB_ENCHANTS_DEFERRED.register("radiance", () -> new RadianceMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 3)));
}
