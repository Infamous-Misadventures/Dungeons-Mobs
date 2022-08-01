package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.entities.summonables.TornadoEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class WindcallerEntity extends SpellcastingIllagerEntity implements IAnimatable {

    public static final DataParameter<Integer> TIMER = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.INT);
    public static final DataParameter<Boolean> CAN_MELEE = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> IS_MELEE = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> IS_LEFT = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> MELEE_CD = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> LIFT_CD = EntityDataManager.defineId(WindcallerEntity.class, DataSerializers.INT);

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAN_MELEE,false);
        this.entityData.define(IS_MELEE,false);
        this.entityData.define(IS_LEFT,false);
        this.entityData.define(TIMER,0);
        this.entityData.define(MELEE_CD,0);
        this.entityData.define(LIFT_CD,0);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.entityData.get(IS_LEFT)) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.lift_attack", false));
        } else if (this.entityData.get(IS_MELEE)) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.blast_attack", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().animationSpeed = 1.25;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.move", true));
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.windcaller.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
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
        this.goalSelector.addGoal(2, new WindcallerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new WindcallerEntity.LiftGoal());
        this.goalSelector.addGoal(0, new WindcallerEntity.MeleeGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 0.8D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, IronGolemEntity.class, 8.0F, 0.6D, 0.8D));

        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
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

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public void targetInRange(Entity p_70652_1_) {
        if (p_70652_1_ != null && this.distanceTo(p_70652_1_) < 16.0D) {
            this.entityData.set(CAN_MELEE, true);
        }else
            this.entityData.set(CAN_MELEE, false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getTarget() != null)
            this.targetInRange(this.getTarget());

        if (this.entityData.get(LIFT_CD) > 0) {
            this.entityData.set(LIFT_CD, this.entityData.get(LIFT_CD) - 1);
        }

        if (this.entityData.get(MELEE_CD) > 0) {
            this.entityData.set(MELEE_CD, this.entityData.get(MELEE_CD) - 1);
        }

    }

    class CastingSpellGoal extends CastingASpellGoal {
        public WindcallerEntity v = WindcallerEntity.this;
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

    class LiftGoal extends Goal {

        public WindcallerEntity windcallerEntity = WindcallerEntity.this;

        public LiftGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return windcallerEntity.getTarget() != null &&
                    windcallerEntity.entityData.get(LIFT_CD) <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return windcallerEntity.entityData.get(TIMER) < 43;
        }

        @Override
        public void start() {
            windcallerEntity.entityData.set(TIMER, 0);
        }

        @Override
        public void stop() {
            windcallerEntity.entityData.set(LIFT_CD, 185);
            windcallerEntity.entityData.set(TIMER, 0);
            windcallerEntity.entityData.set(IS_LEFT,false);
            windcallerEntity.entityData.set(IS_MELEE,false);
            if (windcallerEntity.getTarget() == null) {
                windcallerEntity.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            windcallerEntity.entityData.set(TIMER, windcallerEntity.entityData.get(TIMER) +1);
            windcallerEntity.getNavigation().stop();

            if (windcallerEntity.getTarget() != null && windcallerEntity.getTarget().isAlive() && windcallerEntity.distanceToSqr(windcallerEntity.getTarget()) > 30 + windcallerEntity.getTarget().getBbWidth()) {
                windcallerEntity.getLookControl().setLookAt(windcallerEntity.getTarget(), 30.0F, 30.0F);
                if (windcallerEntity.entityData.get(TIMER) == 18 ) {
                    TornadoEntity tornadoEntity = new TornadoEntity(WindcallerEntity.this.level, WindcallerEntity.this, WindcallerEntity.this.getTarget(), 12);
                    WindcallerEntity.this.level.addFreshEntity(tornadoEntity);
                }
                windcallerEntity.entityData.set(IS_LEFT,true);
            }

        }

        @Override
        public boolean isInterruptable() {
            return false;
        }
    }

    class MeleeGoal extends Goal {

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public WindcallerEntity v = WindcallerEntity.this;

        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null &&
                    v.entityData.get(TIMER) <= 0 &&
                    v.entityData.get(MELEE_CD) == 0 &&
                    v.entityData.get(CAN_MELEE) &&
                    v.distanceToSqr(v.getTarget()) <= 30 + v.getTarget().getBbWidth() &&
                    !v.entityData.get(IS_LEFT) ||
                    (v.getTarget() != null &&
                            v.hurtTime > 0);
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return v.entityData.get(TIMER) < 36 &&
                    !v.entityData.get(IS_LEFT) &&
                    v.getTarget() != null &&
                    v.getTarget().isAlive();
        }

        @Override
        public void start() {
            v.entityData.set(IS_MELEE, true);
        }

        @Override
        public void stop() {
            v.entityData.set(IS_MELEE, false);
            v.entityData.set(CAN_MELEE, false);
            if (v.getTarget() == null) {
                v.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            v.entityData.set(TIMER, v.entityData.get(TIMER) + 1);
            if (v.getTarget() != null && v.getTarget().isAlive()) {
                if (v.distanceToSqr(v.getTarget()) <= 30 + v.getTarget().getBbWidth()) {
                    float attackKnockback = 5;
                    if (v.entityData.get(TIMER) >= 18 && v.entityData.get(TIMER) <= 21) {
                        v.entityData.set(MELEE_CD,65);
                        LivingEntity attackTarget = v.getTarget();
                        double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 180F));
                        double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 180F)));
                        double knockbackReduction = 0.5D;
                        this.forceKnockback(attackTarget, attackKnockback, ratioX, ratioZ, knockbackReduction);
                        v.setDeltaMovement(v.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                    }
                    if (v.entityData.get(TIMER) < 18 ) {
                        v.getLookControl().setLookAt(v.getTarget(),30,30);
                    }else {
                        if ( v.entityData.get(TIMER) <= 21) {
                            attackKnockback = 5.01F;
                            List<Entity> list = Lists.newArrayList(WindcallerEntity.this.level.getEntities(v, v.getBoundingBox().inflate(3, 0.5, 3)));
                            for (Entity entity : list) {
                                if (entity instanceof LivingEntity) {
                                    LivingEntity attackTarget = v.getTarget();
                                    double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 180F));
                                    double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 180F)));
                                    double knockbackReduction = 0.5D;
                                    this.forceKnockback(attackTarget, attackKnockback, ratioX, ratioZ, knockbackReduction);
                                    v.setDeltaMovement(v.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                                }
                            }
                        }
                    }
                    v.entityData.set(IS_MELEE, true);
                }
            }

        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if (event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float) ((double) strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double) strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }


}