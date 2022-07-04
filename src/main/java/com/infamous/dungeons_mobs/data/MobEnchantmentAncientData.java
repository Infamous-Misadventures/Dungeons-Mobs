package com.infamous.dungeons_mobs.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;

public class MobEnchantmentAncientData {
    public static final List<String> defaultAdjectives = Arrays.asList();
    public static final List<String> defaultNouns = Arrays.asList();

    public static final MobEnchantmentAncientData DEFAULT = new MobEnchantmentAncientData(defaultAdjectives, defaultNouns);

    public static final Codec<MobEnchantmentAncientData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.listOf().optionalFieldOf("adjectives", defaultAdjectives).forGetter(data -> data.adjectives),
                    Codec.STRING.listOf().optionalFieldOf("nouns", defaultNouns).forGetter(data -> data.nouns)
            ).apply(builder, MobEnchantmentAncientData::new));

    private final List<String> adjectives;
    private final List<String> nouns ;

    public MobEnchantmentAncientData(List<String> adjectives, List<String> nouns) {
        this.adjectives = adjectives;
        this.nouns = nouns;
    }

    public List<String> getAdjectives() {
        return adjectives;
    }

    public List<String> getNouns() {
        return nouns;
    }
}
