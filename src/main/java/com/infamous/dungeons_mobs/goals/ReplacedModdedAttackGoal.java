package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedProps;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedPropsHelper;
import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AnimatedPropsMessage;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ReplacedModdedAttackGoal<T extends Mob> extends Goal {

    public T mob;
    @Nullable
    public LivingEntity target;
    private SoundEvent soundEvent;
    private int cooldown;
    private long lastUseTime;

    public ReplacedModdedAttackGoal(T mob, SoundEvent soundEvent, int cooldown) {
        this.soundEvent = soundEvent;
        this.cooldown = cooldown;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        this.mob = mob;
        this.target = mob.getTarget();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        long i = this.mob.level.getGameTime();
        if (i - this.lastUseTime < cooldown) {
            return false;
        } else {
            target = mob.getTarget();
            return target != null && !mob.isBlocking() && mob.distanceTo(target) <= getAttackReachSqr(target) && animationsUseable()
                    && mob.hasLineOfSight(target);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && !animationsUseable();
    }

    @Override
    public void start() {
        AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(mob);
        cap.setAttackAnimationTick(cap.getAttackAnimationLength());
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> mob), new AnimatedPropsMessage(mob.getId(), cap));
        lastUseTime = this.mob.level.getGameTime();
        mob.level.broadcastEntityEvent(mob, (byte) 4);
    }

    @Override
    public void tick() {
        target = mob.getTarget();
        AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(mob);
        if (cap.getAttackAnimationTick() == cap.getAttackAnimationActionPoint() && soundEvent != null) {
            mob.playSound(soundEvent, 1.0F, 1.0F);
        }

        if (target != null && mob.distanceTo(target) < 4
                && cap.getAttackAnimationTick() == cap.getAttackAnimationActionPoint()) {
            mob.doHurtTarget(target);
        }
    }

    @Override
    public void stop() {
        if (target != null && !isShieldDisabled(mob) && shouldBlockForTarget(target)
                && mob.getOffhandItem().canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK) && mob.getRandom().nextInt(4) == 0) {
            mob.startUsingItem(InteractionHand.OFF_HAND);
        }
    }

    public boolean isShieldDisabled(Mob shieldUser) {
        if (shieldUser instanceof IShieldUser && ((IShieldUser) shieldUser).isShieldDisabled()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldBlockForTarget(LivingEntity target) {
        if (target instanceof Mob && ((Mob) target).getTarget() != mob) {
            return false;
        } else {
            return true;
        }
    }

    public boolean animationsUseable() {
        AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(mob);
        return cap.getAttackAnimationTick() <= 0;
    }

    public double getAttackReachSqr(LivingEntity p_179512_1_) {
        return (double) (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_179512_1_.getBbWidth());
    }

}
