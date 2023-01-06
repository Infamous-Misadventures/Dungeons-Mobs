package com.infamous.dungeons_mobs.compat;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.mobenchants.BurningMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.ChillingMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.DeflectMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.EchoMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.GravityPulseMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.HealsAlliesMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.RadianceMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.RegenerationMobEnchant;
import com.infamous.dungeons_mobs.mobenchants.RushMobEnchant;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantWithMobCompat {
    public static final String ENCHANTWITHMOB_MOD_ID = "enchantwithmob";
    private static boolean IS_LOADED = false;

    @SubscribeEvent
    public static void onInterMod(InterModProcessEvent event){
        if(ModList.get().isLoaded(ENCHANTWITHMOB_MOD_ID)){
            IS_LOADED = true;
        }
    }

    public static boolean isLoaded(){
        return IS_LOADED;
    }

    public static void initMobEnchants(IEventBus modEventBus){
        modEventBus.register(BurningMobEnchant.class);
        modEventBus.register(ChillingMobEnchant.class);
        modEventBus.register(DeflectMobEnchant.class);
        modEventBus.register(EchoMobEnchant.class);
        modEventBus.register(GravityPulseMobEnchant.class);
        modEventBus.register(HealsAlliesMobEnchant.class);
        modEventBus.register(RadianceMobEnchant.class);
        modEventBus.register(RegenerationMobEnchant.class);
        modEventBus.register(RushMobEnchant.class);

    }
}
