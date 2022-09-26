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
	
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_IDLE = SOUNDS.register("entity.squall_golem.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.idle")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_HURT = SOUNDS.register("entity.squall_golem.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.hurt")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_DEATH = SOUNDS.register("entity.squall_golem.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.death")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_ATTACK = SOUNDS.register("entity.squall_golem.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.attack")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_OPEN = SOUNDS.register("entity.squall_golem.on", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.on")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_OFF = SOUNDS.register("entity.squall_golem.off", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.off")));
	public static final RegistryObject<SoundEvent> SQUALL_GOLEM_WALK = SOUNDS.register("entity.squall_golem.walk", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.squall_golem.walk")));
	
	public static final RegistryObject<SoundEvent> ROYAL_GUARD_ATTACK = SOUNDS.register("entity.royal_guard.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.royal_guard.attack")));
	public static final RegistryObject<SoundEvent> ROYAL_GUARD_STEP = SOUNDS.register("entity.royal_guard.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.royal_guard.step")));	
	
	public static final RegistryObject<SoundEvent> SNARELING_STEP = SOUNDS.register("entity.snareling.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.step")));
	public static final RegistryObject<SoundEvent> SNARELING_IDLE = SOUNDS.register("entity.snareling.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.idle")));
	public static final RegistryObject<SoundEvent> SNARELING_HURT = SOUNDS.register("entity.snareling.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.hurt")));
	public static final RegistryObject<SoundEvent> SNARELING_DEATH = SOUNDS.register("entity.snareling.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.death")));
	public static final RegistryObject<SoundEvent> SNARELING_ATTACK = SOUNDS.register("entity.snareling.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.attack")));
	public static final RegistryObject<SoundEvent> SNARELING_PREPARE_SHOOT = SOUNDS.register("entity.snareling.prepare_shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.prepare_shoot")));
	public static final RegistryObject<SoundEvent> SNARELING_SHOOT = SOUNDS.register("entity.snareling.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.shoot")));
	public static final RegistryObject<SoundEvent> SNARELING_GLOB_LAND = SOUNDS.register("entity.snareling.glob_land", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.snareling.glob_land")));

	public static final RegistryObject<SoundEvent> BLASTLING_IDLE = SOUNDS.register("entity.blastling.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.blastling.idle")));
	public static final RegistryObject<SoundEvent> BLASTLING_HURT = SOUNDS.register("entity.blastling.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.blastling.hurt")));
	public static final RegistryObject<SoundEvent> BLASTLING_DEATH = SOUNDS.register("entity.blastling.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.blastling.death")));
	public static final RegistryObject<SoundEvent> BLASTLING_STEP = SOUNDS.register("entity.blastling.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.blastling.step")));
	public static final RegistryObject<SoundEvent> BLASTLING_SHOOT = SOUNDS.register("entity.blastling.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.blastling.shoot")));
	public static final RegistryObject<SoundEvent> BLASTLING_BULLET_LAND = SOUNDS.register("entity.blastling.bullet_land", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.blastling.bullet_land")));
	
	public static final RegistryObject<SoundEvent> WATCHLING_IDLE = SOUNDS.register("entity.watchling.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.watchling.idle")));
	public static final RegistryObject<SoundEvent> WATCHLING_HURT = SOUNDS.register("entity.watchling.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.watchling.hurt")));
	public static final RegistryObject<SoundEvent> WATCHLING_DEATH = SOUNDS.register("entity.watchling.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.watchling.death")));
	public static final RegistryObject<SoundEvent> WATCHLING_STEP = SOUNDS.register("entity.watchling.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.watchling.step")));
	public static final RegistryObject<SoundEvent> WATCHLING_ATTACK = SOUNDS.register("entity.watchling.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.watchling.attack")));
	
	public static final RegistryObject<SoundEvent> ENDERSENT_STEP = SOUNDS.register("entity.endersent.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.step")));
	public static final RegistryObject<SoundEvent> ENDERSENT_IDLE = SOUNDS.register("entity.endersent.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.idle")));
	public static final RegistryObject<SoundEvent> ENDERSENT_HURT = SOUNDS.register("entity.endersent.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.hurt")));
	public static final RegistryObject<SoundEvent> ENDERSENT_DEATH = SOUNDS.register("entity.endersent.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.death")));
	public static final RegistryObject<SoundEvent> ENDERSENT_ATTACK = SOUNDS.register("entity.endersent.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.attack")));
	public static final RegistryObject<SoundEvent> ENDERSENT_IDLE_SMASH = SOUNDS.register("entity.endersent.idle_smash", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.idle_smash")));
	public static final RegistryObject<SoundEvent> ENDERSENT_TELEPORT = SOUNDS.register("entity.endersent.teleport", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.endersent.teleport")));
	
	public static final RegistryObject<SoundEvent> ICEOLOGER_ATTACK = SOUNDS.register("entity.iceologer.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.iceologer.attack")));
	public static final RegistryObject<SoundEvent> ICEOLOGER_IDLE = SOUNDS.register("entity.iceologer.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.iceologer.idle")));
	public static final RegistryObject<SoundEvent> ICEOLOGER_HURT = SOUNDS.register("entity.iceologer.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.iceologer.hurt")));
	public static final RegistryObject<SoundEvent> ICEOLOGER_DEATH = SOUNDS.register("entity.iceologer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.iceologer.death")));
	public static final RegistryObject<SoundEvent> ICE_CHUNK_IDLE_LOOP = SOUNDS.register("entity.ice_chunk.idle_loop", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.ice_chunk.idle_loop")));
	public static final RegistryObject<SoundEvent> ICE_CHUNK_SUMMONED = SOUNDS.register("entity.ice_chunk.summoned", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.ice_chunk.summoned")));
	public static final RegistryObject<SoundEvent> ICE_CHUNK_FALL = SOUNDS.register("entity.ice_chunk.fall", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.ice_chunk.fall")));
	public static final RegistryObject<SoundEvent> ICE_CHUNK_LAND = SOUNDS.register("entity.ice_chunk.land", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.ice_chunk.land")));
	
	public static final RegistryObject<SoundEvent> SKELETON_VANGUARD_ATTACK = SOUNDS.register("entity.skeleton_vanguard.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.skeleton_vanguard.attack")));
	public static final RegistryObject<SoundEvent> SKELETON_VANGUARD_IDLE = SOUNDS.register("entity.skeleton_vanguard.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.skeleton_vanguard.idle")));
	public static final RegistryObject<SoundEvent> SKELETON_VANGUARD_HURT = SOUNDS.register("entity.skeleton_vanguard.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.skeleton_vanguard.hurt")));
	public static final RegistryObject<SoundEvent> SKELETON_VANGUARD_DEATH = SOUNDS.register("entity.skeleton_vanguard.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.skeleton_vanguard.death")));
	public static final RegistryObject<SoundEvent> SKELETON_VANGUARD_STEP = SOUNDS.register("entity.skeleton_vanguard.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.skeleton_vanguard.step")));
	
	public static final RegistryObject<SoundEvent> ICY_CREEPER_EXPLODE = SOUNDS.register("entity.icy_creeper.explode", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.icy_creeper.explode")));
	
	public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_IDLE = SOUNDS.register("entity.jungle_zombie.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.jungle_zombie.idle")));
	public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_HURT = SOUNDS.register("entity.jungle_zombie.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.jungle_zombie.hurt")));
	public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_DEATH = SOUNDS.register("entity.jungle_zombie.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.jungle_zombie.death")));
	public static final RegistryObject<SoundEvent> JUNGLE_ZOMBIE_STEP = SOUNDS.register("entity.jungle_zombie.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.jungle_zombie.step")));
	
	public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_IDLE = SOUNDS.register("entity.frozen_zombie.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.frozen_zombie.idle")));
	public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_HURT = SOUNDS.register("entity.frozen_zombie.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.frozen_zombie.hurt")));
	public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_DEATH = SOUNDS.register("entity.frozen_zombie.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.frozen_zombie.death")));
	public static final RegistryObject<SoundEvent> FROZEN_ZOMBIE_SNOWBALL_LAND = SOUNDS.register("entity.frozen_zombie.snowball_land", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.frozen_zombie.snowball_land")));
	
	public static final RegistryObject<SoundEvent> MOSSY_SKELETON_IDLE = SOUNDS.register("entity.mossy_skeleton.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mossy_skeleton.idle")));
	public static final RegistryObject<SoundEvent> MOSSY_SKELETON_HURT = SOUNDS.register("entity.mossy_skeleton.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mossy_skeleton.hurt")));
	public static final RegistryObject<SoundEvent> MOSSY_SKELETON_DEATH = SOUNDS.register("entity.mossy_skeleton.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mossy_skeleton.death")));
	public static final RegistryObject<SoundEvent> MOSSY_SKELETON_SHOOT = SOUNDS.register("entity.mossy_skeleton.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mossy_skeleton.shoot")));
	public static final RegistryObject<SoundEvent> MOSSY_SKELETON_STEP = SOUNDS.register("entity.mossy_skeleton.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mossy_skeleton.step")));
	
	public static final RegistryObject<SoundEvent> NECROMANCER_IDLE = SOUNDS.register("entity.necromancer.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.idle")));
	public static final RegistryObject<SoundEvent> NECROMANCER_LAUGH = SOUNDS.register("entity.necromancer.laugh", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.laugh")));
	public static final RegistryObject<SoundEvent> NECROMANCER_HURT = SOUNDS.register("entity.necromancer.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.hurt")));
	public static final RegistryObject<SoundEvent> NECROMANCER_DEATH = SOUNDS.register("entity.necromancer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.death")));
	public static final RegistryObject<SoundEvent> NECROMANCER_SHOOT = SOUNDS.register("entity.necromancer.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.shoot")));
	public static final RegistryObject<SoundEvent> NECROMANCER_ORB_IMPACT = SOUNDS.register("entity.necromancer.orb_impact", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.orb_impact")));
	public static final RegistryObject<SoundEvent> NECROMANCER_STEP = SOUNDS.register("entity.necromancer.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.step")));
	public static final RegistryObject<SoundEvent> NECROMANCER_PREPARE_SUMMON = SOUNDS.register("entity.necromancer.prepare_summon", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.prepare_summon")));
	public static final RegistryObject<SoundEvent> NECROMANCER_SUMMON = SOUNDS.register("entity.necromancer.summon", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.necromancer.summon")));
	
	public static final RegistryObject<SoundEvent> WINDCALLER_IDLE = SOUNDS.register("entity.windcaller.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.idle")));
	public static final RegistryObject<SoundEvent> WINDCALLER_HURT = SOUNDS.register("entity.windcaller.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.hurt")));
	public static final RegistryObject<SoundEvent> WINDCALLER_DEATH = SOUNDS.register("entity.windcaller.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.death")));
	public static final RegistryObject<SoundEvent> WINDCALLER_LIFT_WIND = SOUNDS.register("entity.windcaller.lift_wind", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.lift_wind")));
	public static final RegistryObject<SoundEvent> WINDCALLER_BLAST_WIND = SOUNDS.register("entity.windcaller.blast_wind", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.blast_wind")));
	public static final RegistryObject<SoundEvent> WINDCALLER_LIFT_VOCAL = SOUNDS.register("entity.windcaller.lift_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.lift_vocal")));
	public static final RegistryObject<SoundEvent> WINDCALLER_BLAST_VOCAL = SOUNDS.register("entity.windcaller.blast_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.blast_vocal")));
	public static final RegistryObject<SoundEvent> WINDCALLER_FLY_LOOP = SOUNDS.register("entity.windcaller.fly_loop", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.windcaller.fly_loop")));
	
	public static final RegistryObject<SoundEvent> ILLUSIONER_DEATH = SOUNDS.register("entity.illusioner.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.illusioner.death")));
	public static final RegistryObject<SoundEvent> ILLUSIONER_CLONE_ARROW_HIT = SOUNDS.register("entity.illusioner.clone_arrow_hit", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.illusioner.clone_arrow_hit")));
	
	public static final RegistryObject<SoundEvent> MOUNTAINEER_IDLE = SOUNDS.register("entity.mountaineer.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mountaineer.idle")));
	public static final RegistryObject<SoundEvent> MOUNTAINEER_HURT = SOUNDS.register("entity.mountaineer.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mountaineer.hurt")));
	public static final RegistryObject<SoundEvent> MOUNTAINEER_DEATH = SOUNDS.register("entity.mountaineer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.mountaineer.death")));
	
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_IDLE = SOUNDS.register("entity.redstone_golem.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.idle")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_HURT = SOUNDS.register("entity.redstone_golem.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.hurt")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_DEATH = SOUNDS.register("entity.redstone_golem.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.death")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_STEP = SOUNDS.register("entity.redstone_golem.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.step")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_ATTACK = SOUNDS.register("entity.redstone_golem.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.attack")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_SUMMON_MINES = SOUNDS.register("entity.redstone_golem.summon_mines", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.summon_mines")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_IDLE_PULSE_LOOP = SOUNDS.register("entity.redstone_golem.idle_pulse_loop", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.idle_pulse_loop")));
	public static final RegistryObject<SoundEvent> REDSTONE_GOLEM_SPARK = SOUNDS.register("entity.redstone_golem.spark", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.redstone_golem.spark")));
	
	public static final RegistryObject<SoundEvent> WHISPERER_IDLE_VOCAL = SOUNDS.register("entity.whisperer.idle_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.idle_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_HURT_VOCAL = SOUNDS.register("entity.whisperer.hurt_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.hurt_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_STEP_VOCAL = SOUNDS.register("entity.whisperer.step_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.step_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_ATTACK_VOCAL = SOUNDS.register("entity.whisperer.attack_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.attack_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_SUMMON_PQV_VOCAL = SOUNDS.register("entity.whisperer.summon_pqv_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.summon_pqv_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_SUMMON_QGV_VOCAL = SOUNDS.register("entity.whisperer.summon_qgv_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.summon_qgv_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_GRAPPLE_VOCAL = SOUNDS.register("entity.whisperer.grapple_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.grapple_vocal")));
	public static final RegistryObject<SoundEvent> WHISPERER_IDLE_FOLEY = SOUNDS.register("entity.whisperer.idle_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.idle_foley")));
	public static final RegistryObject<SoundEvent> WHISPERER_HURT_FOLEY = SOUNDS.register("entity.whisperer.hurt_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.hurt_foley")));
	public static final RegistryObject<SoundEvent> WHISPERER_STEP_FOLEY = SOUNDS.register("entity.whisperer.step_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.step_foley")));
	public static final RegistryObject<SoundEvent> WHISPERER_ATTACK_FOLEY = SOUNDS.register("entity.whisperer.attack_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.attack_foley")));
	public static final RegistryObject<SoundEvent> WHISPERER_SUMMON_PQV_FOLEY = SOUNDS.register("entity.whisperer.summon_pqv_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.summon_pqv_foley")));
	public static final RegistryObject<SoundEvent> WHISPERER_SUMMON_QGV_FOLEY = SOUNDS.register("entity.whisperer.summon_qgv_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.summon_qgv_foley")));
	public static final RegistryObject<SoundEvent> WHISPERER_GRAPPLE_FOLEY = SOUNDS.register("entity.whisperer.grapple_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.grapple_foley")));
	
	public static final RegistryObject<SoundEvent> WHISPERER_DEATH = SOUNDS.register("entity.whisperer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.whisperer.death")));
	
	public static final RegistryObject<SoundEvent> LEAPLEAF_IDLE_VOCAL = SOUNDS.register("entity.leapleaf.idle_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.idle_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_HURT_VOCAL = SOUNDS.register("entity.leapleaf.hurt_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.hurt_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_STEP_VOCAL = SOUNDS.register("entity.leapleaf.step_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.step_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_ATTACK_VOCAL = SOUNDS.register("entity.leapleaf.attack_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.attack_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_PREPARE_LEAP_VOCAL = SOUNDS.register("entity.leapleaf.prepare_leap_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.prepare_leap_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_LEAP_VOCAL = SOUNDS.register("entity.leapleaf.leap_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.leap_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_REST_VOCAL = SOUNDS.register("entity.leapleaf.rest_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.rest_vocal")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_IDLE_FOLEY = SOUNDS.register("entity.leapleaf.idle_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.idle_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_HURT_FOLEY = SOUNDS.register("entity.leapleaf.hurt_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.hurt_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_STEP_FOLEY = SOUNDS.register("entity.leapleaf.step_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.step_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_ATTACK_FOLEY = SOUNDS.register("entity.leapleaf.attack_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.attack_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_PREPARE_LEAP_FOLEY = SOUNDS.register("entity.leapleaf.prepare_leap_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.prepare_leap_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_LEAP_FOLEY = SOUNDS.register("entity.leapleaf.leap_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.leap_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_REST_FOLEY = SOUNDS.register("entity.leapleaf.rest_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.rest_foley")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_DEATH = SOUNDS.register("entity.leapleaf.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.death")));
	public static final RegistryObject<SoundEvent> LEAPLEAF_LAND = SOUNDS.register("entity.leapleaf.land", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.leapleaf.land")));

	
	public static final RegistryObject<SoundEvent> QUICK_GROWING_VINE_BURST = SOUNDS.register("entity.quick_growing_vine.burst", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_vine.burst")));
	public static final RegistryObject<SoundEvent> QUICK_GROWING_VINE_BURST_DOWN = SOUNDS.register("entity.quick_growing_vine.burst_down", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_vine.burst_down")));
	public static final RegistryObject<SoundEvent> QUICK_GROWING_VINE_HURT = SOUNDS.register("entity.quick_growing_vine.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_vine.hurt")));
	public static final RegistryObject<SoundEvent> QUICK_GROWING_VINE_DEATH = SOUNDS.register("entity.quick_growing_vine.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_vine.death")));
	
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_BURST = SOUNDS.register("entity.poison_quill_vine.burst", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.burst")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_IDLE = SOUNDS.register("entity.poison_quill_vine.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.idle")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_HURT_VOCAL = SOUNDS.register("entity.poison_quill_vine.hurt_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.hurt_vocal")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_HURT_FOLEY = SOUNDS.register("entity.poison_quill_vine.hurt_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.hurt_foley")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_DEATH = SOUNDS.register("entity.poison_quill_vine.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.death")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_OPEN = SOUNDS.register("entity.poison_quill_vine.open", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.open")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_CLOSE = SOUNDS.register("entity.poison_quill_vine.close", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.close")));
	public static final RegistryObject<SoundEvent> POISON_QUILL_VINE_SHOOT = SOUNDS.register("entity.poison_quill_vine.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_quill_vine.shoot")));
	
	public static final RegistryObject<SoundEvent> FUNGUS_THROWER_THROW = SOUNDS.register("entity.fungus_thrower.throw", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.fungus_thrower.throw")));
	public static final RegistryObject<SoundEvent> FUNGUS_THROWER_FUNGUS_LAND = SOUNDS.register("entity.fungus_thrower.fungus_land", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.fungus_thrower.fungus_land")));
	
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_IDLE = SOUNDS.register("entity.wavewhisperer.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.idle")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_HURT = SOUNDS.register("entity.wavewhisperer.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.hurt")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_DEATH = SOUNDS.register("entity.wavewhisperer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.death")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_STEP = SOUNDS.register("entity.wavewhisperer.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.step")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_ATTACK = SOUNDS.register("entity.wavewhisperer.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.attack")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_SUMMON_PA_FOLEY = SOUNDS.register("entity.wavewhisperer.summon_pa_foley", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.summon_pa_foley")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_SUMMON_PA_VOCAL = SOUNDS.register("entity.wavewhisperer.summon_pa_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.summon_pa_vocal")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_SUMMON_QGK = SOUNDS.register("entity.wavewhisperer.summon_qgk", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.summon_qgk")));
	public static final RegistryObject<SoundEvent> WAVEWHISPERER_GRAPPLE = SOUNDS.register("entity.wavewhisperer.grapple", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wavewhisperer.grapple")));
	
	public static final RegistryObject<SoundEvent> QUICK_GROWING_KELP_BURST = SOUNDS.register("entity.quick_growing_kelp.burst", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_kelp.burst")));
	public static final RegistryObject<SoundEvent> QUICK_GROWING_KELP_BURST_DOWN = SOUNDS.register("entity.quick_growing_kelp.burst_down", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_kelp.burst_down")));
	public static final RegistryObject<SoundEvent> QUICK_GROWING_KELP_HURT = SOUNDS.register("entity.quick_growing_kelp.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_kelp.hurt")));
	public static final RegistryObject<SoundEvent> QUICK_GROWING_KELP_DEATH = SOUNDS.register("entity.quick_growing_kelp.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.quick_growing_kelp.death")));
	
	public static final RegistryObject<SoundEvent> POISON_ANEMONE_BURST = SOUNDS.register("entity.poison_anemone.burst", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_anemone.burst")));
	public static final RegistryObject<SoundEvent> POISON_ANEMONE_IDLE = SOUNDS.register("entity.poison_anemone.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_anemone.idle")));
	public static final RegistryObject<SoundEvent> POISON_ANEMONE_HURT = SOUNDS.register("entity.poison_anemone.hurt_vocal", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_anemone.hurt")));
	public static final RegistryObject<SoundEvent> POISON_ANEMONE_DEATH = SOUNDS.register("entity.poison_anemone.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_anemone.death")));
	public static final RegistryObject<SoundEvent> POISON_ANEMONE_CLOSE = SOUNDS.register("entity.poison_anemone.close", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_anemone.close")));
	public static final RegistryObject<SoundEvent> POISON_ANEMONE_SHOOT = SOUNDS.register("entity.poison_anemone.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.poison_anemone.shoot")));
	
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_IDLE = SOUNDS.register("entity.drowned_necromancer.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.idle")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_HURT = SOUNDS.register("entity.drowned_necromancer.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.hurt")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_DEATH = SOUNDS.register("entity.drowned_necromancer.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.death")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_SWIM = SOUNDS.register("entity.drowned_necromancer.swim", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.swim")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_ATTACK = SOUNDS.register("entity.drowned_necromancer.attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.attack")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_STRONG_ATTACK = SOUNDS.register("entity.drowned_necromancer.strong_attack", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.strong_attack")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_SHOOT = SOUNDS.register("entity.drowned_necromancer.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.shoot")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_SUMMON = SOUNDS.register("entity.drowned_necromancer.summon", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.summon")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_STEAM_MISSILE = SOUNDS.register("entity.drowned_necromancer.steam_missile", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.steam_missile")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_STEAM_MISSILE_IMPACT = SOUNDS.register("entity.drowned_necromancer.steam_missile_impact", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.steam_missile_impact")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_PREPARE_TRIDENT_STORM = SOUNDS.register("entity.drowned_necromancer.prepare_trident_storm", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.prepare_trident_storm")));
	public static final RegistryObject<SoundEvent> DROWNED_NECROMANCER_TRIDENT_STORM_HIT = SOUNDS.register("entity.drowned_necromancer.trident_storm_hit", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.drowned_necromancer.trident_storm_hit")));
	
	public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_IDLE = SOUNDS.register("entity.sunken_skeleton.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.sunken_skeleton.idle")));
	public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_HURT = SOUNDS.register("entity.sunken_skeleton.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.sunken_skeleton.hurt")));
	public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_DEATH = SOUNDS.register("entity.sunken_skeleton.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.sunken_skeleton.death")));
	public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_STEP = SOUNDS.register("entity.sunken_skeleton.step", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.sunken_skeleton.step")));
	public static final RegistryObject<SoundEvent> SUNKEN_SKELETON_SHOOT = SOUNDS.register("entity.sunken_skeleton.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.sunken_skeleton.shoot")));
	
	public static final RegistryObject<SoundEvent> WILDFIRE_IDLE = SOUNDS.register("entity.wildfire.idle", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.idle")));
	public static final RegistryObject<SoundEvent> WILDFIRE_IDLE_LOOP = SOUNDS.register("entity.wildfire.idle_loop", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.idle_loop")));
	public static final RegistryObject<SoundEvent> WILDFIRE_HURT = SOUNDS.register("entity.wildfire.hurt", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.hurt")));
	public static final RegistryObject<SoundEvent> WILDFIRE_DEATH = SOUNDS.register("entity.wildfire.death", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.death")));
	public static final RegistryObject<SoundEvent> WILDFIRE_MOVE = SOUNDS.register("entity.wildfire.move", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.move")));
	public static final RegistryObject<SoundEvent> WILDFIRE_SHOOT = SOUNDS.register("entity.wildfire.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.shoot")));
	public static final RegistryObject<SoundEvent> WILDFIRE_PROJECTILE_HIT = SOUNDS.register("entity.wildfire.projectile_hit", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.projectile_hit")));
	public static final RegistryObject<SoundEvent> WILDFIRE_SHOCKWAVE = SOUNDS.register("entity.wildfire.shockwave", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.shockwave")));
	public static final RegistryObject<SoundEvent> WILDFIRE_SHIELD_BREAK = SOUNDS.register("entity.wildfire.shield_break", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.wildfire.shield_break")));
	
	public static final RegistryObject<SoundEvent> SPIDER_PREPARE_SHOOT = SOUNDS.register("entity.spider.prepare_shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.spider.prepare_shoot")));
	public static final RegistryObject<SoundEvent> SPIDER_SHOOT = SOUNDS.register("entity.spider.shoot", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.spider.shoot")));
	public static final RegistryObject<SoundEvent> SPIDER_WEB_IMPACT = SOUNDS.register("entity.spider.web_impact", () -> new SoundEvent(new ResourceLocation(DungeonsMobs.MODID, "entity.spider.web_impact")));
}
