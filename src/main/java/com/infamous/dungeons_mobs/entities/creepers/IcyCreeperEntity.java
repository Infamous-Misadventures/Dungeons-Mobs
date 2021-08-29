package com.infamous.dungeons_mobs.entities.creepers;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;


public class IcyCreeperEntity extends CreeperEntity {

    public IcyCreeperEntity(World worldIn) {
        super(ModEntityTypes.ICY_CREEPER.get(), worldIn);
    }

    public static boolean canIcyCreeperSpawn(EntityType<IcyCreeperEntity> entityType, IServerWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    @Override
    public void aiStep() {

        if (this.level.isClientSide) {
            this.level.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }
        super.aiStep();
    }

    public IcyCreeperEntity(EntityType<? extends CreeperEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return CreeperEntity.createAttributes();
    }
}
