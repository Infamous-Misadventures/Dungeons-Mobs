package com.infamous.dungeons_mobs.client.particle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DungeonsMobs.MODID);

    public static final RegistryObject<SimpleParticleType> SNOWFLAKE = PARTICLES.register("snowflake", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> REDSTONE_SPARK = PARTICLES.register("redstone_spark", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WIND = PARTICLES.register("wind", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DUST = PARTICLES.register("dust", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> NECROMANCY = PARTICLES.register("necromancy", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CORRUPTED_DUST = PARTICLES.register("corrupted_dust", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CORRUPTED_MAGIC = PARTICLES.register("corrupted_magic", () -> new SimpleParticleType(true));
}
