package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
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

import java.util.EnumSet;

public class WhispererEntity extends MonsterEntity implements IMagicUser, IAnimatable {
    // Required to make use of IMagicUser
    private static final DataParameter<Byte> USE_MAGIC = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.BYTE);
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    private static final DataParameter<Boolean> IS_SPELL = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_MELEE = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> C_MELEE = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TIMER = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> S_TIMER = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.INT);

    AnimationFactory factory = new AnimationFactory(this);

    public WhispererEntity(World worldIn) {
        super(ModEntityTypes.WHISPERER.get(), worldIn);
    }

    public WhispererEntity(EntityType<? extends WhispererEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.addMovementBehaviors();
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, WhispererEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    protected void addMovementBehaviors() {
        this.goalSelector.addGoal(1, new UsingMagicGoal<>(this));
        this.goalSelector.addGoal(0, new MeleeGoal());
        this.goalSelector.addGoal(3, new WhispererEntity.SummonVinesGoal());
        this.goalSelector.addGoal(3, new WhispererEntity.SummonTrapsGoal());
        this.goalSelector.addGoal(2, new WhispererEntity.AttackGoal());
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VineEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.6D));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.378D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        if (!this.entityData.get(C_MELEE) && this.level.isClientSide) {
            this.entityData.set(C_MELEE, true);
        }
        return true;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {

        if (this.entityData.get(IS_SPELL) && this.entityData.get(USE_MAGIC) == (byte) 3) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.whisperer_trap.summoned", false));
            return PlayState.CONTINUE;

        } else if (this.entityData.get(IS_SPELL) && this.entityData.get(USE_MAGIC) == (byte) 1) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.whisperer.summon_poisonquill", false));
            return PlayState.CONTINUE;

        } else if (this.entityData.get(IS_SPELL) && this.entityData.get(USE_MAGIC) == (byte) 2) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.whisperer.summon", false));
            return PlayState.CONTINUE;

        } else if (this.entityData.get(IS_MELEE)) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.whisperer.basic_attack", false));
            return PlayState.CONTINUE;

        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.whisperer.walk", true));
            return PlayState.CONTINUE;

        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.whisperer.idle", true));
            return PlayState.CONTINUE;
        }
    }

    class AttackGoal extends MeleeAttackGoal{
        private AttackGoal() {
            super(WhispererEntity.this, 1.0D, false);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null && WhispererEntity.this.distanceToSqr(targetEntity) < 30.0D) {
                return super.canUse();
            }
            return false;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || WhispererEntity.this.getBoundingBox().intersects(enemy.getBoundingBox()))) {
                WhispererEntity.this.entityData.set(C_MELEE, true);
            }
        }

        @Override
        public void tick() {
            LivingEntity livingentity = WhispererEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }
            super.tick();
            this.checkAndPerformAttack(livingentity, WhispererEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }
    }

    class MeleeGoal extends Goal {

        public WhispererEntity entity = WhispererEntity.this;

        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return entity.getTarget() != null && entity.entityData.get(C_MELEE);
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return entity.entityData.get(TIMER) < 21 && (entity.getTarget() != null && entity.getTarget().isAlive());
        }

        @Override
        public void start() {
            entity.entityData.set(IS_MELEE, true);
            entity.entityData.set(TIMER, 0);
        }

        @Override
        public void stop() {
            entity.entityData.set(TIMER, 0);
            entity.entityData.set(IS_MELEE, false);
            entity.entityData.set(C_MELEE, false);
            if (entity.getTarget() == null) {
                entity.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            entity.entityData.set(TIMER, entity.entityData.get(TIMER) + 1);
            if (entity.getTarget() != null && entity.getTarget().isAlive()) {
                entity.getLookControl().setLookAt(entity.getTarget(), 30.0F, 30.0F);
                //v.setDeltaMovement(v.getDeltaMovement().multiply(1D, 0.35D, 1D));
                if (entity.entityData.get(TIMER) == 12 && entity.distanceToSqr(entity.getTarget()) <= 6.0D+ entity.getTarget().getBbWidth()) {
                    float attackKnockback = (float) entity.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    LivingEntity attackTarget = entity.getTarget();
                    double ratioX = (double) MathHelper.sin(entity.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(entity.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(entity), ((float)(entity.getAttributeValue(Attributes.ATTACK_DAMAGE))));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                }
            }

        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if(event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }

    class SummonVinesGoal extends UseMagicGoal<WhispererEntity> {

        private float s;
        private SummonVinesGoal() {
            super(WhispererEntity.this);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null && WhispererEntity.this.distanceToSqr(targetEntity) >= 30.0D) {
                return super.canUse();
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            this.s = WhispererEntity.this.getRandom().nextFloat();
            WhispererEntity.this.entityData.set(IS_SPELL, true);
            if(this.s < 0.25F) {
                WhispererEntity.this.entityData.set(USE_MAGIC, (byte) 1);
                WhispererEntity.this.entityData.set(S_TIMER, 26);
            } else {
                WhispererEntity.this.entityData.set(USE_MAGIC, (byte) 2);
                WhispererEntity.this.entityData.set(S_TIMER, 54);
            }
        }

        @Override
        public void stop() {
            super.stop();
            WhispererEntity.this.entityData.set(USE_MAGIC, (byte) 0);
            WhispererEntity.this.entityData.set(IS_SPELL, false);
        }

        protected int getMagicUseTime() {
            return 22;
        }

        protected int getMagicUseInterval() {
            return 140;
        }

        protected void useMagic() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null) {
                if(this.s < 0.25F){
                    GeomancyHelper.summonOffensiveVine(WhispererEntity.this, WhispererEntity.this, WhispererEntity.this.getPoisonVineType());
                }
                else{
                    int[] rowToRemove = Util.getRandom(GeomancyHelper.CONFIG_1_ROWS, WhispererEntity.this.getRandom());
                    //GeomancyHelper.summonAreaDenialVineTrap(targetEntity, targetEntity, WhispererEntity.this.getQuickGrowingVineType(), rowToRemove);
                }
            }
        }


        protected SoundEvent getMagicPrepareSound() {
            return null;
        }

        protected MagicType getMagicType() {
            return null;
        }
    }

    class SummonTrapsGoal extends UseMagicGoal<WhispererEntity> {

        private float s;
        private SummonTrapsGoal() {
            super(WhispererEntity.this);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null && WhispererEntity.this.distanceToSqr(targetEntity) >= 30.0D) {
                return super.canUse();
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            WhispererEntity.this.entityData.set(IS_SPELL, true);
            WhispererEntity.this.entityData.set(USE_MAGIC, (byte) 3);
            WhispererEntity.this.entityData.set(S_TIMER, 53);
        }

        @Override
        public void stop() {
            super.stop();
            WhispererEntity.this.entityData.set(USE_MAGIC, (byte) 0);
            WhispererEntity.this.entityData.set(IS_SPELL, false);
        }

        protected int getMagicUseTime() {
            return 32;
        }

        protected int getMagicUseInterval() {
            return 240;
        }

        protected void useMagic() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null) {
                targetEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 10, false, false));
            }
        }


        protected SoundEvent getMagicPrepareSound() {
            return null;
        }

        protected MagicType getMagicType() {
            return null;
        }
    }
    protected EntityType<? extends QuickGrowingVineEntity> getQuickGrowingVineType() {
        return ModEntityTypes.QUICK_GROWING_VINE.get();
    }

    protected EntityType<? extends PoisonQuillVineEntity> getPoisonVineType() {
        return ModEntityTypes.POISON_QUILL_VINE.get();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGIC, (byte)0);
        this.entityData.define(USE_MAGIC, (byte)0);
        this.entityData.define(TIMER, 0);
        this.entityData.define(S_TIMER, 0);
        this.entityData.define(IS_MELEE, false);
        this.entityData.define(C_MELEE, false);
        this.entityData.define(IS_SPELL, false);
    }

    @Override
    public void tick() {
        super.tick();
        IMagicUser.spawnMagicParticles(this);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.magicUseTicks > 0) {
            --this.magicUseTicks;
        }
        if (this.entityData.get(S_TIMER) > 0) {
            this.entityData.set(S_TIMER, this.entityData.get(S_TIMER) - 1);
        }
    }


    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.magicUseTicks = compound.getInt("MagicUseTicks");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MagicUseTicks", this.magicUseTicks);
    }

    // IMAGICUSER METHODS
    @Override
    public boolean isUsingMagic() {
        if (this.level.isClientSide) {
            return this.entityData.get(MAGIC) > 0;
        } else {
            return this.magicUseTicks > 0;
        }
    }
    @Override
    public int getMagicUseTicks() {
        return this.magicUseTicks;
    }

    @Override
    public void setMagicUseTicks(int magicUseTicksIn) {
        this.magicUseTicks = magicUseTicksIn;
    }

    @Override
    public MagicType getMagicType() {
        return !this.level.isClientSide ? this.activeMagic : MagicType.getFromId(this.entityData.get(MAGIC));
    }

    @Override
    public void setMagicType(MagicType magicType) {
        this.activeMagic = magicType;
    }

    @Override
    public SoundEvent getMagicSound() {
        return null;
    }
}