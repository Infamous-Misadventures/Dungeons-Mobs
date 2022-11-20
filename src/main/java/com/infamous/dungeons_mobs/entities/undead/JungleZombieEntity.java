package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Random;

public class JungleZombieEntity extends Zombie {

    public JungleZombieEntity(Level worldIn) {
        super(worldIn);
    }

    public JungleZombieEntity(EntityType<? extends JungleZombieEntity> p_i48549_1_, Level p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    public static boolean canJungleZombieSpawn(EntityType<JungleZombieEntity> entityType, ServerLevelAccessor iWorld, MobSpawnType spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == MobSpawnType.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Zombie.createAttributes();
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
                    ((LivingEntity)targetEntity).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
