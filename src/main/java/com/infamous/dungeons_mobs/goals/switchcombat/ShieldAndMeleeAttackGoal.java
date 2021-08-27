package com.infamous.dungeons_mobs.goals.switchcombat;

import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.tags.CustomTags;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class ShieldAndMeleeAttackGoal<T extends CreatureEntity & IShieldUser> extends MeleeAttackGoal {
    private final T hostCreature;
    private final int maxCloseQuartersShieldUseTime;
    private final int attackWindowTime;
    private int seeTime;
    private int closeQuartersShieldUseCounter;
    private final double closeQuartersRangeSq;

    public ShieldAndMeleeAttackGoal(T creatureEntity, double speedTowardsTarget, boolean useLongMemory, double closeQuartersRange, int maxCloseQuartersShieldUseTime, int attackWindowTime) {
        super(creatureEntity, speedTowardsTarget, useLongMemory);
        this.hostCreature = creatureEntity;
        this.closeQuartersRangeSq = closeQuartersRange * closeQuartersRange;
        this.maxCloseQuartersShieldUseTime = maxCloseQuartersShieldUseTime;
        this.attackWindowTime = attackWindowTime;
        this.closeQuartersShieldUseCounter = 0;
    }

    private boolean hasShieldInOffhand(){
        return this.hostCreature.getOffhandItem().isShield(this.hostCreature); // using the Forge ItemStack-sensitive version
    }

    private void useShield(){
        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled()){
            this.hostCreature.startUsingItem(Hand.OFF_HAND);
        }
    }

    private void stopUsingShield(){
        if(this.hostCreature.isBlocking()){
            this.hostCreature.releaseUsingItem();
        }
    }

    @Override
    public boolean canUse() {

        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled()){
            LivingEntity attackTarget = this.hostCreature.getTarget();
            return attackTarget != null && attackTarget.isAlive();
        }
        else{
            return super.canUse();
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled()){
            return this.canUse();
        }
        else{
            return super.canContinueToUse();
        }
    }

    @Override
    public void stop() {
        if(this.hasShieldInOffhand() && this.hostCreature.isBlocking()){
            this.seeTime = 0;
            this.closeQuartersShieldUseCounter = 0;
            this.stopUsingShield();
        }
        super.stop();
    }

    @Override
    public void tick() {
        LivingEntity attackTarget = this.hostCreature.getTarget();
        boolean dontShieldAgainst = attackTarget != null && attackTarget.getType().is(CustomTags.DONT_SHIELD_AGAINST);
        boolean shieldDisabled = this.hostCreature.isShieldDisabled();
        boolean hasShield = this.hasShieldInOffhand();
        if(hasShield // check if we have a shield - if not, we must default to melee attack AI
                && !shieldDisabled // check if our shield is disabled - if so, we must default to melee attack AI
                && attackTarget != null // check if our target exists - otherwise we will get a NPE below
                && !dontShieldAgainst){ // check if the target is a villager - if so, default to melee attack AI since it cannot fight back
            double hostDistanceSq = this.hostCreature.distanceToSqr(attackTarget.getX(), attackTarget.getY(), attackTarget.getZ());
            double detectRange = this.hostCreature.getAttributeValue(Attributes.FOLLOW_RANGE);
            double detectRangeSq = detectRange * detectRange;
            boolean canSee = this.hostCreature.getSensing().canSee(attackTarget);
            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (hostDistanceSq <= detectRangeSq && this.seeTime >= 5) {
                this.hostCreature.getNavigation().stop();
            }

            boolean closeQuarters = hostDistanceSq <= this.closeQuartersRangeSq;

            if(closeQuarters && this.hostCreature.isBlocking()){
                this.closeQuartersShieldUseCounter++;
            }

            if(this.closeQuartersShieldUseCounter >= this.maxCloseQuartersShieldUseTime){
                this.closeQuartersShieldUseCounter = 0;
                this.stopUsingShield();
                this.hostCreature.setShieldCooldownTime(this.attackWindowTime);
                return;
            }

            this.hostCreature.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
            if (!canSee) {
                this.stopUsingShield();
                return;
            }

            if(!this.hostCreature.isBlocking()){
                this.useShield();
            }
        } else if(attackTarget != null){
            this.closeQuartersShieldUseCounter = 0;
            super.tick();
        }
    }
}
