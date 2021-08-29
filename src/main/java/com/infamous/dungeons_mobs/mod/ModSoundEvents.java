package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.DungeonsMobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEvents {
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			DungeonsMobs.MODID);
	
	public static final RegistryObject<SoundEvent> WRAITH_IDLE = SOUNDS.register("entity.wraith.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.idle")));
	public static final RegistryObject<SoundEvent> WRAITH_HURT = SOUNDS.register("entity.wraith.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.hurt")));
	public static final RegistryObject<SoundEvent> WRAITH_DEATH = SOUNDS.register("entity.wraith.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.death")));
	public static final RegistryObject<SoundEvent> WRAITH_ATTACK = SOUNDS.register("entity.wraith.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.attack")));
	public static final RegistryObject<SoundEvent> WRAITH_FIRE = SOUNDS.register("entity.wraith.fire", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.fire")));
	public static final RegistryObject<SoundEvent> WRAITH_FLY = SOUNDS.register("entity.wraith.fly", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.fly")));
	public static final RegistryObject<SoundEvent> WRAITH_TELEPORT = SOUNDS.register("entity.wraith.teleport", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wraith.teleport")));

	public static final RegistryObject<SoundEvent> ENCHANTER_IDLE = SOUNDS.register("entity.enchanter.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.idle")));
	public static final RegistryObject<SoundEvent> ENCHANTER_HURT = SOUNDS.register("entity.enchanter.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.hurt")));
	public static final RegistryObject<SoundEvent> ENCHANTER_DEATH = SOUNDS.register("entity.enchanter.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.death")));
	public static final RegistryObject<SoundEvent> ENCHANTER_PRE_ATTACK = SOUNDS.register("entity.enchanter.pre_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.pre_attack")));
	public static final RegistryObject<SoundEvent> ENCHANTER_ATTACK = SOUNDS.register("entity.enchanter.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.attack")));
	public static final RegistryObject<SoundEvent> ENCHANTER_SPELL = SOUNDS.register("entity.enchanter.spell", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.spell")));
	public static final RegistryObject<SoundEvent> ENCHANTER_BEAM = SOUNDS.register("entity.enchanter.beam", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.beam")));
	public static final RegistryObject<SoundEvent> ENCHANTER_BEAM_LOOP = SOUNDS.register("entity.enchanter.beam_loop", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.enchanter.beam_loop")));
}
