package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.worldgen.DungeonsMobsStructureModifiers;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;

public class ModStructureModifiers {
    public static final Codec<HolderSet<Structure>> LIST_CODEC = RegistryCodecs.homogeneousList(Registry.STRUCTURE_REGISTRY, Structure.DIRECT_CODEC);

    public static final DeferredRegister<Codec<? extends StructureModifier>> STRUCTURE_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, DungeonsMobs.MODID);

    /**
     * Stock structure modifier for adding mob spawns to structures.
     */
    public static final RegistryObject<Codec<DungeonsMobsStructureModifiers.AddSpawnsStructureModifier>> ADD_SPAWNS_STRUCTURE_MODIFIER_TYPE = STRUCTURE_MODIFIER_SERIALIZERS.register("add_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    LIST_CODEC.fieldOf("structures").forGetter(DungeonsMobsStructureModifiers.AddSpawnsStructureModifier::structures),
                    // Allow either a list or single spawner, attempting to decode the list format first.
                    // Uses the better EitherCodec that logs both errors if both formats fail to parse.
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of), // convert list/singleton to list when decoding
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list) // convert list to singleton/list when encoding
                    ).fieldOf("spawners").forGetter(DungeonsMobsStructureModifiers.AddSpawnsStructureModifier::spawners)
            ).apply(builder, DungeonsMobsStructureModifiers.AddSpawnsStructureModifier::new))
    );

    /**
     * Stock structure modifier for removing mob spawns from structures.
     */
    public static final RegistryObject<Codec<DungeonsMobsStructureModifiers.RemoveSpawnsStructureModifier>> REMOVE_SPAWNS_STRUCTURE_MODIFIER_TYPE = STRUCTURE_MODIFIER_SERIALIZERS.register("remove_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    LIST_CODEC.fieldOf("structures").forGetter(DungeonsMobsStructureModifiers.RemoveSpawnsStructureModifier::structures),
                    RegistryCodecs.homogeneousList(ForgeRegistries.Keys.ENTITY_TYPES).fieldOf("entity_types").forGetter(DungeonsMobsStructureModifiers.RemoveSpawnsStructureModifier::entityTypes)
            ).apply(builder, DungeonsMobsStructureModifiers.RemoveSpawnsStructureModifier::new))
    );
}
