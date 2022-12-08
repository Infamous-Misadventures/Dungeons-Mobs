package com.infamous.dungeons_mobs.entities.redstone;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
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
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public class RedstoneGolemEntity extends Raider implements IAnimatable {
    private int attackTimer;
    private int mineAttackCooldown;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    public static final byte MINE_ATTACK = 2;
    private static final EntityDataAccessor<Boolean> SUMMONING_MINES = SynchedEntityData.defineId(RedstoneGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData.defineId(RedstoneGolemEntity.class, EntityDataSerializers.BOOLEAN);

    public RedstoneGolemEntity(Level worldIn){
        super(ModEntityTypes.REDSTONE_GOLEM.get(), worldIn);
    }

    AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
	public int soundLoopTick;

    public RedstoneGolemEntity(EntityType<? extends RedstoneGolemEntity> type, Level worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.25F;
        this.xpReward = 40;
        this.mineAttackCooldown = 10 * 20;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONING_MINES, false);
        this.entityData.define(MELEEATTACKING, false);
    }

    public boolean isSummoningMines() {
        return this.entityData.get(SUMMONING_MINES);
    }

    public void setSummoningMines(boolean summoningMines){
        this.entityData.set(SUMMONING_MINES, summoningMines);
    }

    public boolean isMeleeAttacking() {
        return this.entityData.get(MELEEATTACKING);
    }

    public void setMeleeAttacking(boolean attacking) {
        this.entityData.set(MELEEATTACKING, attacking);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		Vec3 velocity = this.getDeltaMovement();
		float groundSpeed = Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
        if (this.isSummoningMines()) {
        	event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_golem.summon", PLAY_ONCE));
        } else if (isMeleeAttacking()) {
        	event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_golem.attack", PLAY_ONCE));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
        	event.getController().setAnimationSpeed(groundSpeed * 10);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_golem.walk", LOOP));
        } else {
        	event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_golem.general", LOOP));
        }
        return PlayState.CONTINUE;
    }



    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RedstoneGolemEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new RedstoneGolemEntity.AttackGoal(this, 1.3D));
        this.goalSelector.addGoal(4, new RedstoneGolemEntity.SummonRedstoneMinesGoal());
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
    protected SoundEvent getAmbientSound() {
    	return ModSoundEvents.REDSTONE_GOLEM_IDLE.get();
    }
    
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(ModSoundEvents.REDSTONE_GOLEM_STEP.get(), 1.0F, 1.0F);
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.REDSTONE_GOLEM_HURT.get();
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.REDSTONE_GOLEM_DEATH.get();
    }
    
    @Override
    public void baseTick() {
    	super.baseTick();
    	
    	this.soundLoopTick ++;
        if (this.soundLoopTick % 30 == 0) {
        	this.playSound(ModSoundEvents.REDSTONE_GOLEM_IDLE_PULSE_LOOP.get(), 0.75F, 1.0F);
        }
        
    	if (!this.level.isClientSide && this.random.nextInt(100) == 0) {
    		this.playSound(ModSoundEvents.REDSTONE_GOLEM_SPARK.get(), 0.25F, this.getVoicePitch());
    		this.level.broadcastEntityEvent(this, (byte) 4);
    	}
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
        if (!this.level.isClientSide && this.mineAttackCooldown > 0) {
            --this.mineAttackCooldown;
        }
        this.handleLeafCollision();
        this.handleSteppingOnBlocks();
    }

    private void handleSteppingOnBlocks() {
        if (this.getDeltaMovement().horizontalDistanceSqr() > (double)2.5000003E-7F && this.random.nextInt(5) == 0) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY() - (double)0.2F);
            int k = Mth.floor(this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = this.level.getBlockState(pos);
            if (!blockstate.isAir()) {
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    private void handleLeafCollision() {
        if (this.isAlive()) {

            if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                boolean destroyedLeafBlock = false;
                AABB axisalignedbb = this.getBoundingBox().inflate(0.2D);

                for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(axisalignedbb.minX), Mth.floor(axisalignedbb.minY), Mth.floor(axisalignedbb.minZ), Mth.floor(axisalignedbb.maxX), Mth.floor(axisalignedbb.maxY), Mth.floor(axisalignedbb.maxZ))) {
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
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D) // 2x Golem Health
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D) // >= Golem Attack
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D) // 2x Ravager knockback
                .add(Attributes.FOLLOW_RANGE, 25.0D); 
    }

    private float getAttackKnockback() {
        return (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
    }
    /**
     * Decrements the entity's air supply when underwater
     */

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (!this.level.isClientSide && this.attackID == 0) {
            this.attackID = MELEE_ATTACK;
        }
        return true;
    }

    public boolean checkSpawnObstruction(LevelReader worldIn) {
        BlockPos golemPos = this.blockPosition();
        BlockPos posBeneathGolem = golemPos.below();
        BlockState blockstateBeneathGolem = worldIn.getBlockState(posBeneathGolem);
        if (!blockstateBeneathGolem.entityCanStandOn(worldIn, posBeneathGolem, this)) {
            return false;
        } else {
            for(int i = 1; i < 4; ++i) {
                BlockPos posAboveGolem = golemPos.above(i);
                BlockState blockstateAboveGolem = worldIn.getBlockState(posAboveGolem);
                if (!NaturalSpawner
                        .isValidEmptySpawnBlock(worldIn,
                                posAboveGolem,
                                blockstateAboveGolem,
                                blockstateAboveGolem.getFluidState(),
                                ModEntityTypes.REDSTONE_GOLEM.get())) {
                    return false;
                }
            }

            return NaturalSpawner
                    .isValidEmptySpawnBlock(worldIn,
                            golemPos,
                            worldIn.getBlockState(golemPos),
                            Fluids.EMPTY.defaultFluidState(),
                            ModEntityTypes.REDSTONE_GOLEM.get())
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
        } else if (id == 4) {
    		for (int i = 0; i < 5; i++) {
    			this.level.addParticle(ModParticleTypes.REDSTONE_SPARK.get(), this.getRandomX(1.1D), this.getRandomY(), this.getRandomZ(1.1D), -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D);
    		}
        } else {
            super.handleEntityEvent(id);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.875F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    // NAVIGATION

    protected PathNavigation createNavigation(Level worldIn) {
        return new RedstoneGolemEntity.Navigator(this, worldIn);
    }

    static class Navigator extends GroundPathNavigation {
        public Navigator(Mob mobEntity, Level world) {
            super(mobEntity, world);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new RedstoneGolemEntity.Processor();
            return new PathFinder(this.nodeEvaluator, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeEvaluator {
        private Processor() {
        }

        protected BlockPathTypes evaluateBlockPathType(BlockGetter blockReader, boolean canBreakDoors, boolean canWalkThroughDoorways, BlockPos blockPos, BlockPathTypes pathNodeType) {
            return pathNodeType == BlockPathTypes.LEAVES ? BlockPathTypes.OPEN : super.evaluateBlockPathType(blockReader, canBreakDoors, canWalkThroughDoorways, blockPos, pathNodeType);
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

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == MobType.ILLAGER
                || entityIn instanceof Raider) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }


    class AttackGoal extends MeleeAttackGoal {
        private int maxAttackTimer = 20;
        private final double moveSpeed;
        private int delayCounter;
        private int attackTimer;

        public AttackGoal(PathfinderMob creatureEntity, double moveSpeed) {
            super(creatureEntity, moveSpeed, true);
            this.moveSpeed = moveSpeed;
        }

        @Override
        public boolean canUse() {
            return RedstoneGolemEntity.this.getTarget() != null && RedstoneGolemEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            RedstoneGolemEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = RedstoneGolemEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            RedstoneGolemEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + RedstoneGolemEntity.this.getRandom().nextInt(7);
                RedstoneGolemEntity.this.getNavigation().moveTo(livingentity, (double)this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, RedstoneGolemEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || RedstoneGolemEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                RedstoneGolemEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            RedstoneGolemEntity.this.getNavigation().stop();
            if (RedstoneGolemEntity.this.getTarget() == null) {
                RedstoneGolemEntity.this.setAggressive(false);
            }
        }

        public AttackGoal setMaxAttackTick(int max) {
            this.maxAttackTimer = max;
            return this;
        }
    }

    class MeleeGoal extends Goal {
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return RedstoneGolemEntity.this.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 15;
        }

        @Override
        public void start() {
            setAttackID(MELEE_ATTACK);
            setMeleeAttacking(true);
        }

        @Override
        public void tick() {
           if(RedstoneGolemEntity.this.getTarget() != null && RedstoneGolemEntity.this.getTarget().isAlive()) {
               RedstoneGolemEntity.this.getLookControl().setLookAt(RedstoneGolemEntity.this.getTarget(), 30.0F, 30.0F);
               if(attackTimer == 8) {
                   RedstoneGolemEntity.this.playSound(ModSoundEvents.REDSTONE_GOLEM_ATTACK.get(),1,1);
               }
               if (attackTimer == 9) {
                   float attackKnockback = RedstoneGolemEntity.this.getAttackKnockback();
                   LivingEntity attackTarget = RedstoneGolemEntity.this.getTarget();
                   double ratioX = (double) Mth.sin(RedstoneGolemEntity.this.getYRot() * ((float) Math.PI / 180F));
                   double ratioZ = (double) (-Mth.cos(RedstoneGolemEntity.this.getYRot() * ((float) Math.PI / 180F)));
                   double knockbackReduction = 0.5D;
                   attackTarget.hurt(DamageSource.mobAttack(RedstoneGolemEntity.this), (float) RedstoneGolemEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                   this.forceKnockback(attackTarget,attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                   RedstoneGolemEntity.this.setDeltaMovement(RedstoneGolemEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
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
                Vec3 vector3d = attackTarget.getDeltaMovement();
                Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }


        @Override
        public void stop() {
            setMeleeAttacking(false);
            setAttackID(0);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }


    // MINES

    class SummonRedstoneMinesGoal extends Goal{
        static final int MINE_ATTACK_COOLDOWN = 10 * 20;


        SummonRedstoneMinesGoal(){
        }

        @Override
        public boolean canUse() {
            return RedstoneGolemEntity.this.canSummonMines();
        }

        @Override
        public boolean canContinueToUse() {
            return attackTimer < 100;
        }

        @Override
        public void start() {
            RedstoneGolemEntity.this.setSummoningMines(true);
            // Play the summoning sound
            setAttackID(MINE_ATTACK);
            RedstoneGolemEntity.this.playSound(ModSoundEvents.REDSTONE_GOLEM_SUMMON_MINES.get(),1.5F,1);
        }

        @Override
        public void tick() {
            RedstoneGolemEntity.this.getNavigation().stop();
            if(attackTimer == 12){
                BlockPos centerPos = RedstoneGolemEntity.this.blockPosition();
                for (int i = 0; i < 14; i++) {
                    double randomNearbyX = centerPos.getX() + (RedstoneGolemEntity.this.random.nextGaussian() * 10.0D);
                    //double randomNearbyY = RedstoneGolemEntity.this.getPosY() + (double)(RedstoneGolemEntity.this.rand.nextInt(4) - 2);
                    double randomNearbyZ = centerPos.getZ() + (RedstoneGolemEntity.this.random.nextGaussian() * 10.0D);
                    int j =  RedstoneMineEntity.LIFE_TIME + 4 * i;
                    BlockPos randomBlockPos = new BlockPos(randomNearbyX, centerPos.getY(), randomNearbyZ);
                    RedstoneGolemEntity.this.createSpellEntity(randomBlockPos.getX(), randomBlockPos.getZ(), randomBlockPos.getY(), randomBlockPos.getY() + 1, j);

                }
            }
        }

        @Override
        public void stop() {
            setAttackID(0);
            RedstoneGolemEntity.this.mineAttackCooldown = MINE_ATTACK_COOLDOWN;
            RedstoneGolemEntity.this.setSummoningMines(false);
        }
    }

    private boolean canSummonMines() {
        return RedstoneGolemEntity.this.mineAttackCooldown <= 0
                && RedstoneGolemEntity.this.getTarget() != null
                && RedstoneGolemEntity.this.getTarget().isAlive()
                && RedstoneGolemEntity.this.isOnGround()
                && RedstoneGolemEntity.this.attackID == 0;
    }

    private void createSpellEntity(double x, double z, double minY, double maxY, int delay) {
        BlockPos blockpos = new BlockPos(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = RedstoneGolemEntity.this.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(RedstoneGolemEntity.this.level, blockpos1, Direction.UP)) {
                if (!RedstoneGolemEntity.this.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = RedstoneGolemEntity.this.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(RedstoneGolemEntity.this.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            RedstoneGolemEntity.this.level.addFreshEntity(new RedstoneMineEntity(RedstoneGolemEntity.this.level, x, (double)blockpos.getY() + d0, z, delay,RedstoneGolemEntity.this));
        }

    }

}
