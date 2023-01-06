package com.infamous.dungeons_mobs.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class UniqueAncientData {
    public static final Codec<UniqueAncientData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.fieldOf("name").forGetter(data -> data.name),
                    ResourceLocation.CODEC.listOf().fieldOf("mob_enchantments").forGetter(data -> data.mobEnchantments),
                    ResourceLocation.CODEC.fieldOf("minion").forGetter(data -> data.minion),
                    ResourceLocation.CODEC.listOf().fieldOf("minion_mob_enchantments").forGetter(data -> data.minionMobEnchantments),
                    Codec.INT.fieldOf("minion_count").forGetter(data -> data.minionCount)
            ).apply(builder, UniqueAncientData::new));

    private final String name;
    private final List<ResourceLocation> mobEnchantments;
    private final ResourceLocation minion;
    private final List<ResourceLocation> minionMobEnchantments;
    private final int minionCount;

    public UniqueAncientData(String name, List<ResourceLocation> mobEnchantments, ResourceLocation minion, List<ResourceLocation> minionMobEnchantments, int minionCount) {
        this.name = name;
        this.mobEnchantments = mobEnchantments;
        this.minion = minion;
        this.minionMobEnchantments = minionMobEnchantments;
        this.minionCount = minionCount;
    }

    public String getName() {
        return name;
    }

    public List<ResourceLocation> getMobEnchantments() {
        return mobEnchantments;
    }

    public ResourceLocation getMinion() {
        return minion;
    }

    public List<ResourceLocation> getMinionMobEnchantments() {
        return minionMobEnchantments;
    }

    public int getMinionCount() {
        return minionCount;
    }
}
