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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.func_234289_eI_();
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.WINDCALLER_STAFF.get()));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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
        return SoundEvents.ENTITY_EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_EVOKER_HURT;
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_EVOKER_CELEBRATE;
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (WindcallerEntity.this.getAttackTarget() != null) {
                WindcallerEntity.this.getLookController().setLookPositionWithEntity(WindcallerEntity.this.getAttackTarget(), (float) WindcallerEntity.this.getHorizontalFaceSpeed(), (float) WindcallerEntity.this.getVerticalFaceSpeed());
            }

        }
    }

    class SummonTornadoGoal extends UseSpellGoal {
        private SummonTornadoGoal() {
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity attackTarget = WindcallerEntity.this.getAttackTarget();
            if(attackTarget != null){
                boolean targetOnGround = attackTarget.isOnGround();
                boolean targetCanBeSeen = WindcallerEntity.this.canEntityBeSeen(attackTarget);
                return super.shouldExecute() && targetOnGround && targetCanBeSeen;
            }
            return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity attackTarget = WindcallerEntity.this.getAttackTarget();
            if(attackTarget != null){
                boolean targetOnGround = attackTarget.isOnGround();
                boolean targetCanBeSeen = WindcallerEntity.this.canEntityBeSeen(attackTarget);
                return super.shouldContinueExecuting() && targetOnGround && targetCanBeSeen;
            }
            return false;
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void castSpell() {
            LivingEntity attackTarget = WindcallerEntity.this.getAttackTarget();
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

            TornadoEntity tornadoEntity = new TornadoEntity(WindcallerEntity.this.world, WindcallerEntity.this, livingEntity);
            tornadoEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100,1));
            tornadoEntity.setDuration(100);
            WindcallerEntity.this.world.addEntity(tornadoEntity);
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        protected SpellType getSpellType() {
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
