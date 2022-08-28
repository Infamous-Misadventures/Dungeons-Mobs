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
}
