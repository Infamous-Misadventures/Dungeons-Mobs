package com.infamous.dungeons_mobs.entities.golem;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

public class SquallGolemEntity extends AbstractRaiderEntity implements IAnimatable {
    private int attackTimer;
    private int attackID;
    public int cd;
    public static final byte STOMP_ATTACK = 1;
    public static final byte GOLEM_ACTIVATE = 2;
    public static final byte GOLEM_DEACTIVATE = 3;
    private int timeWithoutTarget;
    private static final DataParameter<Boolean> ACTIVATE = EntityDataManager.defineId(SquallGolemEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(SquallGolemEntity.class, DataSerializers.BOOLEAN);

    public SquallGolemEntity(World world){
        super(ModEntityTypes.SQUALL_GOLEM.get(), world);
    }

    public SquallGolemEntity(EntityType<? extends AbstractRaiderEntity> type, World world) {
        super(type, world);
        this.xpReward = 20;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 125.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 21.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {

        if (this.attackID == GOLEM_ACTIVATE) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.activate", false));
            return PlayState.CONTINUE;

        } else if (this.attackID == GOLEM_DEACTIVATE) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.deactivate", false));
            return PlayState.CONTINUE;

        } else if (!this.getActivate()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.deactivated", true));
            return PlayState.CONTINUE;

        }else if (this.isMeleeAttacking()&& this.isAlive()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.attack", false));
            return PlayState.CONTINUE;

        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().animationSpeed = 1.35;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.walk", true));
            return PlayState.CONTINUE;

        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new SquallGolemEntity.AttackGoal());
        this.goalSelector.addGoal(0, new SquallGolemEntity.MeleeGoal());
        this.goalSelector.addGoal(1, new SquallGolemEntity.DoNothingGoal());
        this.goalSelector.addGoal(0, new SquallGolemEntity.Deactivate());
        this.goalSelector.addGoal(0, new SquallGolemEntity.Activate());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.6D));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTIVATE, false);
        this.entityData.define(MELEEATTACKING, false);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("activate", getActivate());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setActivate(compound.getBoolean("activate"));
    }

    public void setActivate(boolean isActivate) {
        this.entityData.set(ACTIVATE, isActivate);
    }

    public boolean getActivate() {
        return this.entityData.get(ACTIVATE);
    }

    public boolean isMeleeAttacking() {
        return this.entityData.get(MELEEATTACKING);
    }

    public void setMeleeAttacking(boolean attacking) {
        this.entityData.set(MELEEATTACKING, attacking);
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
        this.setDeltaMovement(SquallGolemEntity.this.getDeltaMovement().x, SquallGolemEntity.this.getDeltaMovement().y-2.5, SquallGolemEntity.this.getDeltaMovement().z);
        this.handleLeafCollision();
        this.handleSteppingOnBlocks();
    }

    public void tick() {
        super.tick();

        if(this.attackID == STOMP_ATTACK) {
            if (this.attackTimer == 30) {
                Attackparticle(40,0.5f,2.6f,0.5f);
                Attackparticle(40,0.5f,2.4f,-1f);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5f, 1F + this.getRandom().nextFloat() * 0.1F);
            }
        }
        LivingEntity target = this.getTarget();
        if (!level.isClientSide) {
            timeWithoutTarget++;
            if (target != null) {
                timeWithoutTarget = 0;
                if(!this.getActivate()) {
                    this.setActivate(true);
                    this.attackID = GOLEM_ACTIVATE;
                }
            }
            if (this.getCurrentRaid() != null && this.getCurrentRaid().isActive()) {
                timeWithoutTarget = 0;
                if(!this.getActivate()) {
                    this.setActivate(true);
                    this.attackID = GOLEM_ACTIVATE;
                }
            }

            if (timeWithoutTarget > 200 && this.getActivate() && target == null) {
                timeWithoutTarget = 0;
                this.setActivate(false);
                this.attackID = GOLEM_DEACTIVATE;

            }
        }

        if (this.cd > 0) {
            this.cd--;
        }

    }

    private void Attackparticle(int paticle,float circle, float vec, float math) {
        if (this.level.isClientSide) {
            for (int i1 = 0; i1 < paticle; i1++) {
                double DeltaMovementX = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementY = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementZ = getRandom().nextGaussian() * 0.07D;
                float angle = (0.01745329251F * this.yBodyRot) + i1;
                float f = MathHelper.cos(this.yRot * ((float)Math.PI / 180F)) ;
                float f1 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F)) ;
                double extraX = circle * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = circle * MathHelper.cos(angle);
                double theta = (yBodyRot) * (Math.PI / 180);
                theta += Math.PI / 2;
                double vecX = Math.cos(theta);
                double vecZ = Math.sin(theta);
                int hitX = MathHelper.floor(getX() + vec * vecX+ extraX);
                int hitY = MathHelper.floor(getY());
                int hitZ = MathHelper.floor(getZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = level.getBlockState(hit.below());
                this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);

            }
        }
    }


    private void handleSteppingOnBlocks() {
        if (getHorizontalDistanceSqr(this.getDeltaMovement()) > (double)2.5000003E-7F && this.random.nextInt(5) == 0) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY() - (double)0.2F);
            int k = MathHelper.floor(this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = this.level.getBlockState(pos);
            if (!blockstate.isAir(this.level, pos)) {
                this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    private void handleLeafCollision() {
        if (this.isAlive()) {

            if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                boolean destroyedLeafBlock = false;
                AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(0.2D);

                for(BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (block instanceof LeavesBlock) {
                        destroyedLeafBlock = this.level.destroyBlock(blockpos, true, this) || destroyedLeafBlock;
                    }
                }

                if (!destroyedLeafBlock && this.onGround) {
                    this.jumpFromGround();
                }
            }
        }
    }


    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.level.isClientSide && this.attackID == 0) {
            this.attackID = STOMP_ATTACK;
        }
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = false;
        if (!this.getActivate()) {
            this.playSound(SoundEvents.IRON_GOLEM_HURT, 1.0F, 0.4F);
            flag = super.hurt(source,0);
        }else {
            flag = super.hurt(source, amount);
        }
        return flag;
    }

    public boolean checkSpawnwObstruction(IWorldReader worldIn) {
        BlockPos golemPos = this.blockPosition();
        BlockPos posBeneathGolem = golemPos.below();
        BlockState blockstateBeneathGolem = worldIn.getBlockState(posBeneathGolem);
        if (!blockstateBeneathGolem.entityCanStandOn(worldIn, posBeneathGolem, this)) {
            return false;
        } else {
            for(int i = 1; i < 3; ++i) {
                BlockPos posAboveGolem = golemPos.above(i);
                BlockState blockstateAboveGolem = worldIn.getBlockState(posAboveGolem);
                if (!WorldEntitySpawner
                        .isValidEmptySpawnBlock(worldIn,
                                posAboveGolem,
                                blockstateAboveGolem,
                                blockstateAboveGolem.getFluidState(),
                                ModEntityTypes.SQUALL_GOLEM.get())) {
                    return false;
                }
            }

            return WorldEntitySpawner
                    .isValidEmptySpawnBlock(worldIn,
                            golemPos,
                            worldIn.getBlockState(golemPos),
                            Fluids.EMPTY.defaultFluidState(),
                            ModEntityTypes.SQUALL_GOLEM.get())
                    && worldIn.isUnobstructed(this);
        }
    }/**
     * Handler for {link WorldsetEntityState}
     */
    private void setAttackID(int id) {
        this.attackID = id;
        this.attackTimer = 0;
        this.level.broadcastEntityEvent(this, (byte)-id);
    }


    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.attackID = Math.abs(id);
            this.attackTimer = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, (double)(0.875F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public SoundCategory getSoundSource() {
        return SoundCategory.HOSTILE;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    // NAVIGATION

    protected PathNavigator createNavigation(World worldIn) {
        return new Navigator(this, worldIn);
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new Processor();
            return new PathFinder(this.nodeEvaluator, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeProcessor {
        private Processor() {
        }

        protected PathNodeType evaluateBlockPathType(IBlockReader blockReader, boolean canBreakDoors, boolean canWalkThroughDoorways, BlockPos blockPos, PathNodeType pathNodeType) {
            return pathNodeType == PathNodeType.LEAVES ? PathNodeType.OPEN : super.evaluateBlockPathType(blockReader, canBreakDoors, canWalkThroughDoorways, blockPos, pathNodeType);
        }
    }

    // RAIDER METHODS
    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.IRON_GOLEM_REPAIR;
    }

    public boolean canBeLeader() {
        return false;
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(SquallGolemEntity.this, 1.025D, false);
        }

        protected double getAttackReachSqr(LivingEntity p_179512_1_) {
            float f = SquallGolemEntity.this.getBbWidth() - 0.1F;
            return f * 1.8F * f * 1.8F + p_179512_1_.getBbWidth();
        }
    }

    class DoNothingGoal extends Goal {
        public DoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return !SquallGolemEntity.this.getActivate();
        }

        @Override
        public void tick() {
            SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
        }
    }

    class Activate extends Goal {
        public Activate() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return attackID == GOLEM_ACTIVATE;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 60;
        }

        @Override
        public void start() {
            setAttackID(GOLEM_ACTIVATE);
        }

        @Override
        public void tick() {
            SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
        }

        @Override
        public void stop() {
            setAttackID(0);
        }
    }

    class Deactivate extends Goal {
        public Deactivate() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return attackID == GOLEM_DEACTIVATE;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 80;
        }

        @Override
        public void start() {
            setAttackID(GOLEM_DEACTIVATE);
        }

        @Override
        public void tick() {
            SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
        }

        @Override
        public void stop() {
            setAttackID(0);
        }
    }

    class MeleeGoal extends Goal {
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return attackID == STOMP_ATTACK && (SquallGolemEntity.this.cd <= 0);
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 34;
        }

        @Override
        public void start() {
            if (SquallGolemEntity.this.getTarget() != null)
                SquallGolemEntity.this.getLookControl().setLookAt(SquallGolemEntity.this.getTarget(),30,30);
            setMeleeAttacking(true);
            setAttackID(STOMP_ATTACK);
        }

        @Override
        public void tick() {
            SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
            LivingEntity target = SquallGolemEntity.this.getTarget();
            if (SquallGolemEntity.this.attackTimer < 15 && target != null) {
                SquallGolemEntity.this.lookAt(target, 15.0F, 15.0F);
            } else {
                SquallGolemEntity.this.yRot = SquallGolemEntity.this.yRotO;

            }
            if (SquallGolemEntity.this.attackTimer == 30){
                AreaAttack(5,5,5,5,60,1.15F + (SquallGolemEntity.this.getRandom().nextFloat() / 3F));
            }
        }

        private void AreaAttack(float range,float X,float Y, float Z,float arc ,float damage) {
            for (LivingEntity entityHit : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(X, Y, Z))) {
                float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - SquallGolemEntity.this.getZ(), entityHit.getX() - SquallGolemEntity.this.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = SquallGolemEntity.this.yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - SquallGolemEntity.this.getZ()) * (entityHit.getZ() - SquallGolemEntity.this.getZ()) + (entityHit.getX() - SquallGolemEntity.this.getX()) * (entityHit.getX() - SquallGolemEntity.this.getX()));
                if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                    if (!isAlliedTo(entityHit) && !(entityHit == SquallGolemEntity.this)) {
                        entityHit.hurt(DamageSource.mobAttack(SquallGolemEntity.this), (float) SquallGolemEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);

                        SquallGolemEntity v = SquallGolemEntity.this;
                        float attackKnockback = (float) SquallGolemEntity.this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                        double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 180F));
                        double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 180F)));
                        double knockbackReduction = 0.5D;
                        entityHit.hurt(DamageSource.mobAttack(v), damage);
                        this.forceKnockback(entityHit, attackKnockback * 0.8F, ratioX, ratioZ, knockbackReduction);
                        entityHit.setDeltaMovement(entityHit.getDeltaMovement().add(0,0.3333333,0));
                    }
                }
            }
        }
        @Override
        public void stop() {
            setMeleeAttacking(false);
            setAttackID(0);
            SquallGolemEntity.this.cd = 25;
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

}