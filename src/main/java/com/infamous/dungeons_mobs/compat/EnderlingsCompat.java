package com.infamous.dungeons_mobs.compat;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderlingsCompat {
    public static final String ENDERLINGS_MOD_ID = "enderlings";
    private static boolean IS_LOADED = false;

    @SubscribeEvent
    public static void onInterMod(InterModProcessEvent event) {
        if (ModList.get().isLoaded(ENDERLINGS_MOD_ID)) {
            IS_LOADED = true;
        }
    }


    public static boolean isLoaded() {
        return IS_LOADED;
    }

}
