package com.infamous.dungeons_mobs.goals.switchcombat;

import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.util.Hand;

public class ShieldAndMeleeAttackGoal<T extends CreatureEntity & IShieldUser> extends MeleeAttackGoal {
    private T hostCreature;
    private boolean inAttackMode;
    private int seeTime;

    public ShieldAndMeleeAttackGoal(T creatureEntity, double speedTowardsTarget, boolean useLongMemory) {
        super(creatureEntity, speedTowardsTarget, useLongMemory);
            this.hostCreature = creatureEntity;
            this.inAttackMode = false;
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
        if(this.hostCreature.getActiveHand() == Hand.OFF_HAND && this.hasShieldInOffhand()){
            this.hostCreature.stopActiveHand();
        }
    }

    @Override
    public boolean shouldExecute() {

        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled() && !this.inAttackMode){
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
        if(this.hasShieldInOffhand() && !this.hostCreature.isShieldDisabled() && !this.inAttackMode){
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
            this.stopUsingShield();
        }
        this.inAttackMode = false;
        super.resetTask();
    }

    @Override
    public void tick() {
        LivingEntity attackTarget = this.hostCreature.getAttackTarget();
        boolean villagerFlag = attackTarget instanceof AbstractVillagerEntity;
        if(this.hasShieldInOffhand() // check if we have a shield - if not, we must default to melee attack AI
                && !this.hostCreature.isShieldDisabled() // check if our shield is disabled - if so, we must default to melee attack AI
                && !this.inAttackMode // check if we are set in attack mode - if so, default to melee attack AI
                && attackTarget != null // check if our target exists - otherwise we will get a NPE below
                && !villagerFlag){ // check if the target is a villager - if so, default to melee attack AI since it cannot fight back
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

            this.hostCreature.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
            if (!canSee) {
                this.stopUsingShield();
                return;
            }

            if(!this.hostCreature.isActiveItemStackBlocking()){
                this.useShield();
            }
        } else{
            this.inAttackMode = true;
            super.tick();
        }
    }
}
