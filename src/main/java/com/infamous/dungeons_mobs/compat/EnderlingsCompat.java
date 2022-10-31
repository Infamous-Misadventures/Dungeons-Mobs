package com.infamous.dungeons_mobs.compat;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderlingsCompat {
    public static final String ENDERLINGS_MOD_ID = "enderlings";
    private static boolean IS_LOADED = false;
    @SubscribeEvent
    public static void onInterMod(InterModProcessEvent event){
        if(ModList.get().isLoaded(ENDERLINGS_MOD_ID)){
            IS_LOADED = true;
        }
    }


    public static boolean isLoaded(){
        return IS_LOADED;
    }

}
