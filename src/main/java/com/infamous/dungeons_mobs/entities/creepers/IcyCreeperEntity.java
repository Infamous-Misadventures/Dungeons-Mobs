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
        return canMonsterSpawnInLight(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    @Override
    public void livingTick() {

        if (this.world.isRemote) {
            this.world.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getPosXRandom(0.5D), this.getPosYRandom() - 0.25D, this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }
        super.livingTick();
    }

    public IcyCreeperEntity(EntityType<? extends CreeperEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return CreeperEntity.registerAttributes();
    }
}
