package com.infamous.dungeons_mobs.entities.undead;

import java.util.Random;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class JungleZombieEntity extends ZombieEntity {

    public JungleZombieEntity(World worldIn) {
        super(worldIn);
    }

    public JungleZombieEntity(EntityType<? extends JungleZombieEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    public static boolean canJungleZombieSpawn(EntityType<JungleZombieEntity> entityType, IServerWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes();
    }
    
    protected boolean isSunSensitive() {
        return false;
     }
    
    protected float getSoundVolume() {
    	return 0.5F;
    }
    
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.JUNGLE_ZOMBIE_IDLE.get();
     }

     protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.JUNGLE_ZOMBIE_HURT.get();
     }

     protected SoundEvent getDeathSound() {
        return ModSoundEvents.JUNGLE_ZOMBIE_DEATH.get();
     }

     protected SoundEvent getStepSound() {
        return ModSoundEvents.JUNGLE_ZOMBIE_STEP.get();
     }

    @Override
    public boolean doHurtTarget(Entity targetEntity) {
        if (super.doHurtTarget(targetEntity)) {
            if (targetEntity instanceof LivingEntity) {
                int i = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    i = 4;
                } else if (this.level.getDifficulty() == Difficulty.HARD) {
                    i = 8;
                }

                if (i > 0) {
                    ((LivingEntity)targetEntity).addEffect(new EffectInstance(Effects.POISON, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
