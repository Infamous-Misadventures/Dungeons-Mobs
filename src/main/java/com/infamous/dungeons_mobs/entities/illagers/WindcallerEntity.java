package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.summonables.TornadoEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;
import net.minecraft.entity.monster.SpellcastingIllagerEntity.CastingASpellGoal;
import net.minecraft.entity.monster.SpellcastingIllagerEntity.SpellType;
import net.minecraft.entity.monster.SpellcastingIllagerEntity.UseSpellGoal;

public class WindcallerEntity extends SpellcastingIllagerEntity {

    public double prevChasingPosX;
    public double prevChasingPosY;
    public double prevChasingPosZ;
    public double chasingPosX;
    public double chasingPosY;
    public double chasingPosZ;
    public float prevCameraYaw;
    public float cameraYaw;

    public WindcallerEntity(World world){
        super(ModEntityTypes.WINDCALLER.get(), world);
    }

    public WindcallerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new WindcallerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new SummonTornadoGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.createAttributes();
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.WINDCALLER_STAFF.get()));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (WindcallerEntity.this.getTarget() != null) {
                WindcallerEntity.this.getLookControl().setLookAt(WindcallerEntity.this.getTarget(), (float) WindcallerEntity.this.getMaxHeadYRot(), (float) WindcallerEntity.this.getMaxHeadXRot());
            }

        }
    }

    class SummonTornadoGoal extends UseSpellGoal {
        private SummonTornadoGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity attackTarget = WindcallerEntity.this.getTarget();
            if(attackTarget != null){
                boolean targetOnGround = attackTarget.isOnGround();
                boolean targetCanBeSeen = WindcallerEntity.this.canSee(attackTarget);
                return super.canUse() && targetOnGround && targetCanBeSeen;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity attackTarget = WindcallerEntity.this.getTarget();
            if(attackTarget != null){
                boolean targetOnGround = attackTarget.isOnGround();
                boolean targetCanBeSeen = WindcallerEntity.this.canSee(attackTarget);
                return super.canContinueToUse() && targetOnGround && targetCanBeSeen;
            }
            return false;
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void performSpellCasting() {
            LivingEntity attackTarget = WindcallerEntity.this.getTarget();
            if (attackTarget != null) {
                summonTornado(attackTarget);
            }
        }

        private void summonTornado(LivingEntity livingEntity){
            /*
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(WindcallerEntity.this.world, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ());
            areaEffectCloudEntity.setOwner(WindcallerEntity.this);
            areaEffectCloudEntity.setParticleData(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
            areaEffectCloudEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100,1));
            areaEffectCloudEntity.setDuration(100);
            WindcallerEntity.this.world.addEntity(areaEffectCloudEntity);
             */

            TornadoEntity tornadoEntity = new TornadoEntity(WindcallerEntity.this.level, WindcallerEntity.this, livingEntity);
            tornadoEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100,1));
            tornadoEntity.setDuration(100);
            WindcallerEntity.this.level.addFreshEntity(tornadoEntity);
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellType getSpell() {
            return SpellType.SUMMON_VEX;
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
    public void rideTick() {
        super.rideTick();
        this.prevCameraYaw = this.cameraYaw;
        this.cameraYaw = 0.0F;
    }

    private void updateCape() {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double xDifference = this.getX() - this.chasingPosX;
        double yDifference = this.getY() - this.chasingPosY;
        double zDifference = this.getZ() - this.chasingPosZ;
        double maxDelta = 10.0D;
        if (xDifference > maxDelta) {
            this.chasingPosX = this.getX();
            this.prevChasingPosX = this.chasingPosX;
        }

        if (zDifference > maxDelta) {
            this.chasingPosZ = this.getZ();
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (yDifference > maxDelta) {
            this.chasingPosY = this.getY();
            this.prevChasingPosY = this.chasingPosY;
        }

        if (xDifference < -maxDelta) {
            this.chasingPosX = this.getX();
            this.prevChasingPosX = this.chasingPosX;
        }

        if (zDifference < -maxDelta) {
            this.chasingPosZ = this.getZ();
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (yDifference < -maxDelta) {
            this.chasingPosY = this.getY();
            this.prevChasingPosY = this.chasingPosY;
        }

        this.chasingPosX += xDifference * 0.25D;
        this.chasingPosZ += zDifference * 0.25D;
        this.chasingPosY += yDifference * 0.25D;
    }

}
