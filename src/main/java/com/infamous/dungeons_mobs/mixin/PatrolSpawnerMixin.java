package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Random;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "spawnPatrolMember")
    private PatrollerEntity spawnPatroller(PatrollerEntity patrollerEntity, ServerWorld serverWorld, BlockPos blockPos, Random random, boolean makeLeader){
        Boolean armoredPillagersCanReplacePillagers = DungeonsMobsConfig.COMMON.ENABLE_ARMORED_PILLAGER_REPLACES_PILLAGER.get();
        if(!makeLeader && armoredPillagersCanReplacePillagers){
            int difficultyAsInt = serverWorld.getDifficulty().getId();
            double armoredPillagerChance = difficultyAsInt * 0.25D;
            if(serverWorld.random.nextDouble() < armoredPillagerChance){
                patrollerEntity = ModEntityTypes.ARMORED_PILLAGER.get().create(serverWorld);
            }
        }
        return patrollerEntity;
    }
}
