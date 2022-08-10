package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.entities.summonables.Tornado2Entity;
import com.infamous.dungeons_mobs.entities.summonables.TornadoEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
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
    protected float getSoundVolume() {
        return 3.0F;
    }
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

    public float h;
    public float d;

    public void spawnIceExplosionCloud(){
        float radius = (float) ((this.getBbWidth() * 2) / Math.PI);
        if (this.level.isClientSide) {
            IParticleData iparticledata = ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
            if (true) {
                if (this.random.nextBoolean()) {
                    for (int i = 0; i < 2; ++i) {
                        float randWithin2Pi = this.random.nextFloat() * ((float)Math.PI * 2F);
                        float adjustedSqrtFloat = MathHelper.sqrt(this.random.nextFloat()) * radius;
                        float adjustedCosine = MathHelper.cos(randWithin2Pi) * adjustedSqrtFloat;
                        float adjustedSine = MathHelper.sin(randWithin2Pi) * adjustedSqrtFloat;
                        this.level.addAlwaysVisibleParticle(iparticledata, true,
                                this.getX() + (double) adjustedCosine,
                                this.getY(0.1),
                                this.getZ() + (double) adjustedSine,
                                0.0D,
                                -0.01D,
                                0.0D);
                    }
                }
            }
        }
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
        return this.level.isClientSide || this.deathTime > 0 ? PlayState.CONTINUE :
                PlayState.STOP;
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
        return ModSoundEvents.WINDCALLER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.WINDCALLER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.WINDCALLER_HURT.get();
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.WINDCALLER_IDLE.get();
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public void ik(Entity p_70652_1_) {
        if (p_70652_1_ != null && this.distanceTo(p_70652_1_) < 16.0D) {
            this.entityData.set(CAN_MELEE, true);
        }else
            this.entityData.set(CAN_MELEE, false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        spawnIceExplosionCloud();
        if (this.getTarget() != null)
            this.ik(this.getTarget());

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

        public WindcallerEntity v = WindcallerEntity.this;

        public LiftGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null &&
                    v.entityData.get(LIFT_CD) <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return v.entityData.get(TIMER) < 43;
        }

        @Override
        public void start() {
            v.entityData.set(TIMER, 0);
        }

        @Override
        public void stop() {
            v.entityData.set(LIFT_CD, 185);
            v.entityData.set(TIMER, 0);
            v.entityData.set(IS_LEFT,false);
            v.entityData.set(IS_MELEE,false);
            if (v.getTarget() == null) {
                v.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            v.entityData.set(TIMER, v.entityData.get(TIMER) +1);
            v.getNavigation().stop();

            if (v.getTarget() != null && v.getTarget().isAlive() && v.distanceToSqr(v.getTarget()) > 30 + v.getTarget().getBbWidth()) {
                v.getLookControl().setLookAt(v.getTarget(), 30.0F, 30.0F);
                if (v.entityData.get(TIMER) == 14) {
                    v.playSound(ModSoundEvents.WINDCALLER_LIFT_ATTACK.get(), 3.5f, 1);
                }
                if (v.entityData.get(TIMER) == 18 ) {
                    TornadoEntity tornadoEntity = new TornadoEntity(WindcallerEntity.this.level, WindcallerEntity.this, WindcallerEntity.this.getTarget(), 12);
                    WindcallerEntity.this.level.addFreshEntity(tornadoEntity);
                }
                v.entityData.set(IS_LEFT,true);
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
                v.getLookControl().setLookAt(v.getTarget(),5,5);
                if (v.entityData.get(TIMER) == 14) {
                    v.playSound(ModSoundEvents.WINDCALLER_BLAST_ATTACK.get(), 3.5f, 1);
                }
                if (v.entityData.get(TIMER) == 17) {
                    Tornado2Entity vv = new Tornado2Entity(v.level);
                    double d2 = 1.25D;
                    float f = (float) MathHelper.atan2(v.getTarget().getZ() - v.getZ(), v.getTarget().getX() - v.getX());
                    double x = v.getX() + Math.cos(f) * d2;
                    double z = v.getZ() + Math.sin(f) * d2;
                    BlockPos blockpos = new BlockPos(x, v.getY(1.0), z);
                    vv.moveTo(v.blockPosition(),0,0);
                    vv.setCaster(v);
                    vv.setTargetr(v.getTarget());
                    vv.xRot = -v.xRot;
                    //this.yHeadRot = -v.yRot;
                    vv.yBodyRot = -v.yHeadRot;
                    vv.yRot = -v.yHeadRot;
                    vv.yRotO = vv.yRot;
                    vv.xRotO = vv.xRot;

                    v.level.addFreshEntity(vv);
                }
            }

        }

    }



}