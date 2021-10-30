package com.infamous.dungeons_mobs.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobAncientData {
    public static final List<String> defaultAdjectivesValue = Arrays.asList("The Abominable", "The Wretched", "The Harrowing", "The Devastating", "The Ominous", "The Grim", "The Terrible");
    public static final List<String> defaultNounsValue = Arrays.asList("Calamity", "Abomination", "Terror", "Cataclysm", "Harbringer", "Catastrophe", "Disaster", "Torment");

    public static final MobAncientData DEFAULT = new MobAncientData(defaultAdjectivesValue, defaultNounsValue);

    public static final Codec<MobAncientData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.listOf().optionalFieldOf("adjectives", defaultAdjectivesValue).forGetter(data -> data.adjectives),
                    Codec.STRING.listOf().optionalFieldOf("nouns", defaultNounsValue).forGetter(data -> data.nouns)
            ).apply(builder, MobAncientData::new));

    private final List<String> adjectives;
    private final List<String> nouns ;

    public MobAncientData(List<String> adjectives, List<String> nouns) {
        this.adjectives = adjectives;
        this.nouns = nouns;
    }

    public List<String> getAdjectives() {
        return adjectives;
    }

    public List<String> getNouns() {
        return nouns;
    }

    public String getAncientName(Random random){
        return adjectives.get(random.nextInt(adjectives.size())) + " " + nouns.get(random.nextInt(nouns.size()));
    }
}
