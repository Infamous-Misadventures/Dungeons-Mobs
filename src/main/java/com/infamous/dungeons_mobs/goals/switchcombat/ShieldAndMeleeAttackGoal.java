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
        return this.hostCreature.getHeldItemOffhand().isShield(this.hostCreature); // using the Forge ItemStack-sensitive version
    }

    private void useShield(){
        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled()){
            this.hostCreature.setActiveHand(Hand.OFF_HAND);
        }
    }

    private void stopUsingShield(){
        if(this.hostCreature.isActiveItemStackBlocking()){
            this.hostCreature.stopActiveHand();
        }
    }

    @Override
    public boolean shouldExecute() {

        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled()){
            LivingEntity attackTarget = this.hostCreature.getAttackTarget();
            return attackTarget != null && attackTarget.isAlive();
        }
        else{
            return super.shouldExecute();
        }
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled()){
            return this.shouldExecute();
        }
        else{
            return super.shouldContinueExecuting();
        }
    }

    @Override
    public void resetTask() {
        if(this.hasShieldInOffhand() && this.hostCreature.isActiveItemStackBlocking()){
            this.seeTime = 0;
            this.closeQuartersShieldUseCounter = 0;
            this.stopUsingShield();
        }
        super.resetTask();
    }

    @Override
    public void tick() {
        LivingEntity attackTarget = this.hostCreature.getAttackTarget();
        boolean dontShieldAgainst = attackTarget != null && attackTarget.getType().isContained(CustomTags.DONT_SHIELD_AGAINST);
        boolean shieldDisabled = this.hostCreature.isShieldDisabled();
        boolean hasShield = this.hasShieldInOffhand();
        if(hasShield // check if we have a shield - if not, we must default to melee attack AI
                && !shieldDisabled // check if our shield is disabled - if so, we must default to melee attack AI
                && attackTarget != null // check if our target exists - otherwise we will get a NPE below
                && !dontShieldAgainst){ // check if the target is a villager - if so, default to melee attack AI since it cannot fight back
            double hostDistanceSq = this.hostCreature.getDistanceSq(attackTarget.getPosX(), attackTarget.getPosY(), attackTarget.getPosZ());
            double detectRange = this.hostCreature.getAttributeValue(Attributes.FOLLOW_RANGE);
            double detectRangeSq = detectRange * detectRange;
            boolean canSee = this.hostCreature.getEntitySenses().canSee(attackTarget);
            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (hostDistanceSq <= detectRangeSq && this.seeTime >= 5) {
                this.hostCreature.getNavigator().clearPath();
            }

            boolean closeQuarters = hostDistanceSq <= this.closeQuartersRangeSq;

            if(closeQuarters && this.hostCreature.isActiveItemStackBlocking()){
                this.closeQuartersShieldUseCounter++;
            }

            if(this.closeQuartersShieldUseCounter >= this.maxCloseQuartersShieldUseTime){
                this.closeQuartersShieldUseCounter = 0;
                this.stopUsingShield();
                this.hostCreature.setShieldCooldownTime(this.attackWindowTime);
                return;
            }

            this.hostCreature.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
            if (!canSee) {
                this.stopUsingShield();
                return;
            }

            if(!this.hostCreature.isActiveItemStackBlocking()){
                this.useShield();
            }
        } else if(attackTarget != null){
            this.closeQuartersShieldUseCounter = 0;
            super.tick();
        }
    }
}
