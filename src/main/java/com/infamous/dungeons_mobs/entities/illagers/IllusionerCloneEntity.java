package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class IllusionerCloneEntity extends SpellcastingIllagerEntity implements IAnimatable,IRangedAttackMob {

    public static final DataParameter<Integer> SHOT_TICK = EntityDataManager.defineId(IllusionerCloneEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> SUMMON_CLONE_TICK = EntityDataManager.defineId(IllusionerCloneEntity.class, DataSerializers.INT);
    public static final DataParameter<Boolean> SHOT_OR_SUMMON_BOOLEAN = EntityDataManager.defineId(IllusionerCloneEntity.class, DataSerializers.BOOLEAN);

    public int liftInterval = 0;
    public int duplicateInterval = 0;


    private LivingEntity caster;
    private UUID casterUuid;
    private int lifeTicks;
    AnimationFactory factory = new AnimationFactory(this);

    public int getSummonCloneTick() {
        return this.entityData.get(SUMMON_CLONE_TICK);
    }

    public void setSummonCloneTick(int p_189794_1_) {
        this.entityData.set(SUMMON_CLONE_TICK, p_189794_1_);
    }


    public IllusionerCloneEntity(World world){
        super(ModEntityTypes.ILLUSIONER_CLONE.get(), world);
    }

    public IllusionerCloneEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    public IllusionerCloneEntity(World worldIn, LivingEntity caster, int lifeTicks,LivingEntity target) {
        this(ModEntityTypes.ILLUSIONER_CLONE.get(), worldIn);
        this.setCaster(caster);
        this.lifeTicks = lifeTicks;
    }

    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUUID();
    }

    @Override
    public boolean canJoinRaid() {
        return false;
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    @Override
    public void onAddedToWorld() {
        if(this.level.isClientSide){
            this.spawnPoofCloud();
        }
        super.onAddedToWorld();
    }

    @Override
    public void onRemovedFromWorld() {
        this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
        if(this.level.isClientSide){
            this.spawnPoofCloud();
        }
        super.onRemovedFromWorld();
    }

    private void spawnPoofCloud(){
        for(int i = 0; i < 50; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.WITCH, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    public void aiStep() {
        super.aiStep();
        if(!this.level.isClientSide){
            this.lifeTicks--;
            if(this.lifeTicks <= 0 || this.getCaster() == null || !this.getCaster().isAlive()  || ((DungeonsIllusionerEntity)this.getCaster()).getDuplicateInterval() <= 15 ){
                this.remove();
            }
        }
        if (this.getSummonCloneTick() > 0) {
            this.setSummonCloneTick(this.getSummonCloneTick() - 1);
        }
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if (!this.isInvulnerable()) {
            this.remove();
        }
        return false;
    }


    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new IllusionerCloneEntity.RunAvoidGoal(this,1));
        this.goalSelector.addGoal(1, new IllusionerCloneEntity.CastingSpellGoal());
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new IllusionerCloneEntity.ShotGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    class RunAvoidGoal extends MeleeAttackGoal {
        private int maxAttackTimer = 15;
        private final double moveSpeed;
        private int delayCounter;
        private int attackTimer;

        public IllusionerCloneEntity v = IllusionerCloneEntity.this;

        public RunAvoidGoal(CreatureEntity creatureEntity, double moveSpeed) {
            super(creatureEntity, moveSpeed, true);
            this.moveSpeed = moveSpeed;
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null && v.getTarget().isAlive();
        }

        @Override
        public void start() {
            v.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = v.getTarget();
            if (livingentity == null) {
                v.setAggressive(false);
                return;
            }
            v.setAggressive(true);
            if (--this.delayCounter <= 0) {
                this.delayCounter = 8 + v.getRandom().nextInt(5);
                {
                    if (v.distanceToSqr(livingentity) >= 80) {
                        v.getNavigation().moveTo(livingentity,
                                (double) this.moveSpeed
                        );
                    }
                }
                if (v.distanceToSqr(livingentity) >= 60 && v.distanceToSqr(livingentity) <= 70) {
                    v.getNavigation().stop();
                }
            }
        }

        @Override
        public void stop() {
            v.getNavigation().stop();
            if (v.getTarget() == null) {
                v.setAggressive(false);
            }
        }

        public IllusionerCloneEntity.RunAvoidGoal setMaxAttackTick(int max) {
            this.maxAttackTimer = max;
            return this;
        }
    }

    public void baseTick() {
        super.baseTick();

        if (this.getTarget() != null) {
            //this.getNavigation().moveTo(this.getTarget(), this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.getLookControl().setLookAt(this.getTarget(), (float)this.getMaxHeadYRot(), (float)this.getMaxHeadXRot());
        }

        //this.setAttackType(this.getTarget() != null);
        //System.out.print("\r\n" + this.getType());

        if (this.liftInterval > 0) {
            this.liftInterval --;
        }

        if (this.duplicateInterval > 0) {
            this.duplicateInterval --;
        }

        //if (this.level.isClientSide) {
        //	System.out.print("\r\n" + this.isAggressive());
        //}

        if (this.getShotTick() > 0) {
            this.setShotTick(this.getShotTick() - 1);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getAttackType()&&this.getSummonCloneTick() >0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.teleport_in_short", true));
        } else if (this.getAttackType()&&this.getShotTick() >0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.shoot2_clone", true));
        } else if (this.getShotTick() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.bow_action_clone", true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOT_TICK, 0);
        this.entityData.define(SUMMON_CLONE_TICK, 0);
        this.entityData.define(SHOT_OR_SUMMON_BOOLEAN, false);
    }

    public int getShotTick() {
        return this.entityData.get(SHOT_TICK);
    }

    public void setShotTick(int p_189794_1_) {
        this.entityData.set(SHOT_TICK, p_189794_1_);
    }

    public boolean getAttackType() {
        return this.entityData.get(SHOT_OR_SUMMON_BOOLEAN);
    }

    public void setAttackType(boolean p_189794_1_) {
        this.entityData.set(SHOT_OR_SUMMON_BOOLEAN, p_189794_1_);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
        int waveAmount = p_213660_1_;
        boolean applyEnchant = this.random.nextFloat() <= raid.getEnchantOdds();
        if (waveAmount > raid.getNumGroups(Difficulty.EASY) && !(waveAmount > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 8.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 4.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 4.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 16.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 2.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 8.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR_TOUGHNESS).addPermanentModifier(new AttributeModifier("armor toughness boost", 2.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.35D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 8.0D, AttributeModifier.Operation.ADDITION));
        }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, itemstack, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double xDifference = target.getX() - this.getX();
        double yDifference = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double zDifference = target.getZ() - this.getZ();
        double horizontalDistance = (double) MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference);
        abstractarrowentity.shoot(xDifference, yDifference + horizontalDistance * (double)0.2F, zDifference, 1.6F, (float)(18 - this.level.getDifficulty().getId() * 5));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (IllusionerCloneEntity.this.getTarget() != null) {
                IllusionerCloneEntity.this.getLookControl().setLookAt(IllusionerCloneEntity.this.getTarget(), (float) IllusionerCloneEntity.this.getMaxHeadYRot(), (float) IllusionerCloneEntity.this.getMaxHeadXRot());
            }

        }
    }

    class ShotGoal extends Goal {

        private boolean d;

        public ShotGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public boolean canUse() {
            return IllusionerCloneEntity.this.getTarget() != null && IllusionerCloneEntity.this.getShotTick() == 0 && IllusionerCloneEntity.this.liftInterval == 0 && IllusionerCloneEntity.this.random.nextInt(20) == 0;
        }

        public boolean canContinueToUse() {
            return IllusionerCloneEntity.this.getTarget() != null && IllusionerCloneEntity.this.getShotTick() > 0;
        }

        public void start() {
            super.start();
            IllusionerCloneEntity.this.liftInterval = 58;
            this.d =true;
            IllusionerCloneEntity.this.setShotTick(21);
            IllusionerCloneEntity.this.entityData.set(SHOT_OR_SUMMON_BOOLEAN,false);
        }

        public void tick() {
            IllusionerCloneEntity.this.getLookControl().setLookAt(IllusionerCloneEntity.this.getTarget(), (float) IllusionerCloneEntity.this.getMaxHeadYRot(), (float) IllusionerCloneEntity.this.getMaxHeadXRot());
            LivingEntity target = IllusionerCloneEntity.this.getTarget();
            IllusionerCloneEntity mob = IllusionerCloneEntity.this;

            mob.getNavigation().stop();

            mob.getNavigation().stop();

            if (mob.getShotTick() == 1 && this.d) {
                IllusionerCloneEntity.this.setShotTick(14);
                IllusionerCloneEntity.this.setAttackType(true);
                this.d =false;
                mob.performRangedAttack(target, (float) (target.getBbHeight() / Math.PI));
            }
        }

        public void stop() {
            super.stop();
            IllusionerCloneEntity.this.setAttackType(false);
            IllusionerCloneEntity.this.setShotTick(0);
        }

        @Override
        public boolean isInterruptable() {
            return false;
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

}