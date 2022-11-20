package com.infamous.dungeons_mobs.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobAncientData {
    public static final List<String> defaultAdjectives = Arrays.asList("The Abominable", "The Wretched", "The Harrowing", "The Devastating", "The Ominous", "The Grim", "The Terrible");
    public static final List<String> defaultNouns = Arrays.asList("Calamity", "Abomination", "Terror", "Cataclysm", "Harbringer", "Catastrophe", "Disaster", "Torment");
    public static final List<ResourceLocation> defaultMinions = Arrays.asList(mcLoc("slime"));

    public static final MobAncientData DEFAULT = new MobAncientData(defaultAdjectives, defaultNouns, defaultMinions, Arrays.asList());

    public static final Codec<MobAncientData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.listOf().optionalFieldOf("adjectives", defaultAdjectives).forGetter(data -> data.adjectives),
                    Codec.STRING.listOf().optionalFieldOf("nouns", defaultNouns).forGetter(data -> data.nouns),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("minions", defaultMinions).forGetter(data -> data.minions),
                    UniqueAncientData.CODEC.listOf().optionalFieldOf("uniques", Arrays.asList()).forGetter(data -> data.uniques)
            ).apply(builder, MobAncientData::new));

    private final List<String> adjectives;
    private final List<String> nouns ;
    private final List<ResourceLocation> minions;
    private final List<UniqueAncientData> uniques;

    public MobAncientData(List<String> adjectives, List<String> nouns, List<ResourceLocation> minions, List<UniqueAncientData> uniques) {
        this.adjectives = adjectives;
        this.nouns = nouns;
        this.minions = minions;
        this.uniques = uniques;
    }

    public List<String> getAdjectives() {
        return adjectives;
    }

    public List<String> getNouns() {
        return nouns;
    }

    public List<ResourceLocation> getMinions() {
        return minions;
    }

    public List<UniqueAncientData> getUniques() {
        return uniques;
    }

    public String getAncientName(Random random){
        return adjectives.get(random.nextInt(adjectives.size())) + " " + nouns.get(random.nextInt(nouns.size()));
    }

    private static ResourceLocation mcLoc(String path){
        return new ResourceLocation(path);
    }
}
