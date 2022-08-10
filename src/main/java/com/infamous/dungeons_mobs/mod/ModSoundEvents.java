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
	
	public static final RegistryObject<SoundEvent> GEOMANCER_IDLE = SOUNDS.register("entity.geomancer.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.idle")));
	public static final RegistryObject<SoundEvent> GEOMANCER_HURT = SOUNDS.register("entity.geomancer.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.hurt")));
	public static final RegistryObject<SoundEvent> GEOMANCER_DEATH = SOUNDS.register("entity.geomancer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.death")));
	public static final RegistryObject<SoundEvent> GEOMANCER_PRE_ATTACK = SOUNDS.register("entity.geomancer.pre_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.pre_attack")));
	public static final RegistryObject<SoundEvent> GEOMANCER_ATTACK = SOUNDS.register("entity.geomancer.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.attack")));
	public static final RegistryObject<SoundEvent> GEOMANCER_WALL_SPAWN = SOUNDS.register("entity.geomancer.wall_spawn", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.wall_spawn")));
	public static final RegistryObject<SoundEvent> GEOMANCER_WALL_DESPAWN = SOUNDS.register("entity.geomancer.wall_despawn", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.wall_despawn")));
	public static final RegistryObject<SoundEvent> GEOMANCER_BOMB_SPAWN = SOUNDS.register("entity.geomancer.bomb_spawn", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.geomancer.bomb_spawn")));

	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_IDLE = SOUNDS.register("entity.redstone_golem.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.idle")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_HURT = SOUNDS.register("entity.redstone_golem.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.hurt")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_DEATH = SOUNDS.register("entity.redstone_golem.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.death")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_ATTACK = SOUNDS.register("entity.redstone_golem.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.attack")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_SUMMON_MINE = SOUNDS.register("entity.redstone_golem.summon_mine", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.summon_mine")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_IDLE = SOUNDS.register("entity.squall_golem.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.idle")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_HURT = SOUNDS.register("entity.squall_golem.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.hurt")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_DEATH = SOUNDS.register("entity.squall_golem.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.death")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_ATTACK = SOUNDS.register("entity.squall_golem.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.attack")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_OPEN = SOUNDS.register("entity.squall_golem.on", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.on")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_OFF = SOUNDS.register("entity.squall_golem.off", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.off")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_WALK = SOUNDS.register("entity.squall_golem.walk", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.walk")));
	public static final RegistryObject<SoundEvent> IRON_GOLEM_IDLE = SOUNDS.register("entity.iron_golem.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.iron_golem.idle")));
	public static final RegistryObject<SoundEvent> ROYAL_GUARD_ATTACK = SOUNDS.register("entity.royal_guard.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.royal_guard.attack")));
	public static final RegistryObject<SoundEvent> WINDCALLER_IDLE = SOUNDS.register("entity.windcaller.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.idle")));
	public static final RegistryObject<SoundEvent> WINDCALLER_HURT = SOUNDS.register("entity.windcaller.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.hurt")));
	public static final RegistryObject<SoundEvent> WINDCALLER_DEATH = SOUNDS.register("entity.windcaller.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.death")));
	public static final RegistryObject<SoundEvent> WINDCALLER_LIFT_ATTACK = SOUNDS.register("entity.windcaller.pre_lift_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.pre_lift_attack")));
	public static final RegistryObject<SoundEvent> WINDCALLER_BLAST_ATTACK = SOUNDS.register("entity.windcaller.pre_blast_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.pre_blast_attack")));
	public static final RegistryObject<SoundEvent> B_WIND = SOUNDS.register("entity.windcaller.blast_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.blast_attack")));
	public static final RegistryObject<SoundEvent> L_WIND = SOUNDS.register("entity.windcaller.lift_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.lift_attack")));
}
