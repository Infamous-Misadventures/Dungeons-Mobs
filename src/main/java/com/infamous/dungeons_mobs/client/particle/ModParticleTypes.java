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
}
