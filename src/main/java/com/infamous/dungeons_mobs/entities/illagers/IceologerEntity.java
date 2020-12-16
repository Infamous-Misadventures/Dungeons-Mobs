package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class IceologerEntity extends SpellcastingIllagerEntity {

    public double prevChasingPosX;
    public double prevChasingPosY;
    public double prevChasingPosZ;
    public double chasingPosX;
    public double chasingPosY;
    public double chasingPosZ;
    public float prevCameraYaw;
    public float cameraYaw;

    public IceologerEntity(World world){
        super(ModEntityTypes.ICEOLOGER.get(), world);
    }

    public IceologerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new IceologerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new IceologerEntity.SummonIceBlocksGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.func_234289_eI_();
    }



    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn) {
        if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void applyWaveBonus(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
    }

    class CastingSpellGoal extends SpellcastingIllagerEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (IceologerEntity.this.getAttackTarget() != null) {
                IceologerEntity.this.getLookController().setLookPositionWithEntity(IceologerEntity.this.getAttackTarget(), (float)IceologerEntity.this.getHorizontalFaceSpeed(), (float)IceologerEntity.this.getVerticalFaceSpeed());
            }

        }
    }

    class SummonIceBlocksGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private SummonIceBlocksGoal() {
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void castSpell() {
            LivingEntity attackTarget = IceologerEntity.this.getAttackTarget();
            summonIceCloud(attackTarget);
            //summonIceBlocks(attackTarget);
        }

        private void summonIceCloud(LivingEntity livingEntity){
            IceCloudEntity iceCloudEntity = new IceCloudEntity(world, IceologerEntity.this, livingEntity);
            world.addEntity(iceCloudEntity);
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingIllagerEntity.SpellType getSpellType() {
            return SpellcastingIllagerEntity.SpellType.SUMMON_VEX;
        }
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateCape();
    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        this.prevCameraYaw = this.cameraYaw;
        this.cameraYaw = 0.0F;
    }

    private void updateCape() {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double xDifference = this.getPosX() - this.chasingPosX;
        double yDifference = this.getPosY() - this.chasingPosY;
        double zDifference = this.getPosZ() - this.chasingPosZ;
        double maxDelta = 10.0D;
        if (xDifference > maxDelta) {
            this.chasingPosX = this.getPosX();
            this.prevChasingPosX = this.chasingPosX;
        }

        if (zDifference > maxDelta) {
            this.chasingPosZ = this.getPosZ();
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (yDifference > maxDelta) {
            this.chasingPosY = this.getPosY();
            this.prevChasingPosY = this.chasingPosY;
        }

        if (xDifference < -maxDelta) {
            this.chasingPosX = this.getPosX();
            this.prevChasingPosX = this.chasingPosX;
        }

        if (zDifference < -maxDelta) {
            this.chasingPosZ = this.getPosZ();
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (yDifference < -maxDelta) {
            this.chasingPosY = this.getPosY();
            this.prevChasingPosY = this.chasingPosY;
        }

        this.chasingPosX += xDifference * 0.25D;
        this.chasingPosZ += zDifference * 0.25D;
        this.chasingPosY += yDifference * 0.25D;
    }

}
