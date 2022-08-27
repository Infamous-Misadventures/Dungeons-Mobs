package com.infamous.dungeons_mobs.goals.magic;

import com.infamous.dungeons_mobs.capabilities.teamable.TeamableHelper;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class SummonMobsGoal extends Goal {
    private final NecromancerEntity mobEntity;
    private final EntityPredicate entityPredicate = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

    public int timer;

    public SummonMobsGoal(NecromancerEntity MobEntity) {
        mobEntity = MobEntity;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mobEntity.getTarget() != null && mobEntity.cd <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer < 35;
    }

    @Override
    public void start() {
        super.start();
        this.timer = 0;
        mobEntity.setIsSummoning(true);
    }

    @Override
    public void stop() {
        mobEntity.cd = 168;
        mobEntity.setIsSummoning(false);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.timer++;
        mobEntity.getNavigation().stop();
        if (mobEntity.getTarget() != null) {
            mobEntity.getLookControl().setLookAt(mobEntity.getTarget(), 30, 30);
        }
        if (this.timer == 16) {
            useMagic();
        }
    }

    protected void useMagic() {
        {
            summonUndead();
        }
    }

    private void summonUndead() {

        int difficultyAsInt = mobEntity.level.getDifficulty().getId();
        for (int i = 0; i < difficultyAsInt; ++i) {
            BlockPos blockpos = mobEntity.blockPosition().offset(-2 + mobEntity.getRandom().nextInt(5), 1, -2 + mobEntity.getRandom().nextInt(5));
            boolean summonedMobFromConfig = summonMobFromConfig(blockpos);
            if (!summonedMobFromConfig) {
                summonZombie(blockpos);
            }
        }
    }

    private boolean summonMobFromConfig(BlockPos blockpos) {
        List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.NECROMANCER_MOB_SUMMONS.get();
        if (necromancerMobSummons.isEmpty()) return false;
        Collections.shuffle(necromancerMobSummons);

        int randomIndex = mobEntity.getRandom().nextInt(necromancerMobSummons.size());
        String randomMobID = necromancerMobSummons.get(randomIndex);
        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
        if (entityType == null) return false;

        Entity entity = entityType.create(mobEntity.level);
        if (!(entity instanceof MobEntity)) return false;

        MobEntity mobEntity = (MobEntity) entity;
        DifficultyInstance difficultyForLocation = mobEntity.level.getCurrentDifficultyAt(blockpos);
        mobEntity.moveTo(blockpos, 0.0F, 0.0F);
        ModifiableAttributeInstance spawnReinforcementsAttribute = mobEntity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        if (spawnReinforcementsAttribute != null) {
            spawnReinforcementsAttribute.setBaseValue(0);
        }
        mobEntity.finalizeSpawn((IServerWorld) mobEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
        TeamableHelper.makeTeammates(mobEntity, mobEntity);
        mobEntity.setTarget(this.mobEntity.getTarget());
        return mobEntity.level.addFreshEntity(mobEntity);
    }

    private void summonZombie(BlockPos blockpos) {
        ZombieEntity zombieEntity = EntityType.ZOMBIE.create(mobEntity.level);
        if (zombieEntity != null) {
            DifficultyInstance difficultyForLocation = mobEntity.level.getCurrentDifficultyAt(blockpos);
            zombieEntity.moveTo(blockpos, 0.0F, 0.0F);
            ModifiableAttributeInstance spawnReinforcementsAttribute = zombieEntity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
            if (spawnReinforcementsAttribute != null) {
                spawnReinforcementsAttribute.setBaseValue(0);
            }
            zombieEntity.finalizeSpawn((IServerWorld) mobEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            TeamableHelper.makeTeammates(zombieEntity, mobEntity);
            zombieEntity.setTarget(mobEntity.getTarget());
            mobEntity.level.addFreshEntity(zombieEntity);
        }
    }
}
