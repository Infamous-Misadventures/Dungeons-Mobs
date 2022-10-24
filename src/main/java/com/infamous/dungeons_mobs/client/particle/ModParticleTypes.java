package com.infamous.dungeons_mobs.client.particle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DungeonsMobs.MODID);

    public static final RegistryObject<BasicParticleType> SNOWFLAKE = PARTICLES.register("snowflake", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> REDSTONE_SPARK = PARTICLES.register("redstone_spark", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> WIND = PARTICLES.register("wind", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> DUST = PARTICLES.register("dust", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> NECROMANCY = PARTICLES.register("necromancy", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> CORRUPTED_DUST = PARTICLES.register("corrupted_dust", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> CORRUPTED_MAGIC = PARTICLES.register("corrupted_magic", () -> new BasicParticleType(true));
}
