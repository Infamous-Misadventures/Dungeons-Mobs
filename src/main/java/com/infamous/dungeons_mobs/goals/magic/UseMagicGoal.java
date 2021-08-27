package com.infamous.dungeons_mobs.goals.magic;

import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

public abstract class UseMagicGoal<T extends MobEntity & IMagicUser> extends Goal {
    private int magicWarmup;
    private int magicCooldown;
    private T hostMobEntity;

    protected UseMagicGoal(T magicUserMob) {
        this.hostMobEntity = magicUserMob;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.hostMobEntity.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            if (this.hostMobEntity.isUsingMagic()) {
                return false;
            } else {
               return this.hostMobEntity.tickCount >= this.magicCooldown;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.hostMobEntity.getTarget();
        return livingentity != null && livingentity.isAlive() && this.magicWarmup > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.magicWarmup = this.getMagicWarmupTime();
        this.hostMobEntity.setMagicUseTicks(this.getMagicUseTime());
        this.magicCooldown = this.hostMobEntity.tickCount + this.getMagicUseInterval();
        SoundEvent soundevent = this.getMagicPrepareSound();
        if (soundevent != null) {
            this.hostMobEntity.playSound(soundevent, 1.0F, 1.0F);
        }

        this.hostMobEntity.setMagicType(this.getMagicType());
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        --this.magicWarmup;
        if (this.magicWarmup == 0) {
            this.useMagic();
            this.hostMobEntity.playSound(this.hostMobEntity.getMagicSound(), 1.0F, 1.0F);
        }
    }

    protected abstract void useMagic();

    private int getMagicWarmupTime() {
         return 20;
    }

    protected abstract int getMagicUseTime();

    protected abstract int getMagicUseInterval();

    @Nullable
    protected abstract SoundEvent getMagicPrepareSound();

    protected abstract MagicType getMagicType();
}