package com.infamous.dungeons_mobs.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_mobs.DungeonsMobs.ANCIENT_DATA;

//@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AncientDataHelper {

    public static MobAncientData merger(List<MobAncientData> raws){
        List<String> adjectives = new ArrayList<>();
        List<String> nouns = new ArrayList<>();
        List<ResourceLocation> minions = new ArrayList<>();
        List<UniqueAncientData> uniques = new ArrayList<>();
        raws.forEach(raw -> {
            adjectives.addAll(raw.getAdjectives());
            nouns.addAll(raw.getNouns());
            minions.addAll(raw.getMinions());
            uniques.addAll(raw.getUniques());
        });
        return new MobAncientData(adjectives, nouns, minions, uniques);
    }

    public static MobAncientData getMobAncientData(ResourceLocation mobResourceLocation){
        return ANCIENT_DATA.data.getOrDefault(mobResourceLocation, MobAncientData.DEFAULT);
    }

    public static String getAncientName(LivingEntity entity){
        return getMobAncientData(entity.getType().getRegistryName()).getAncientName(entity.getRandom());
    }
}
