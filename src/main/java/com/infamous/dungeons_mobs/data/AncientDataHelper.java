package com.infamous.dungeons_mobs.data;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.data.util.MergeableCodecDataManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_mobs.DungeonsMobs.ANCIENT_DATA;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AncientDataHelper {

    public static MobAncientData merger(List<MobAncientData> raws){
        List<String> adjectives = new ArrayList<>();
        List<String> nouns = new ArrayList<>();
        raws.forEach(raw -> {
            adjectives.addAll(raw.getAdjectives());
            nouns.addAll(raw.getNouns());
        });
        return new MobAncientData(adjectives, nouns);
    }

    public static MobAncientData getMobAncientData(ResourceLocation mobResourceLocation){
        return ANCIENT_DATA.data.getOrDefault(mobResourceLocation, MobAncientData.DEFAULT);
    }

    public static String getAncientName(LivingEntity entity){
        return getMobAncientData(entity.getType().getRegistryName()).getAncientName(entity.getRandom());
    }

    @SubscribeEvent
    void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(ANCIENT_DATA);
    }
}
