package com.infamous.dungeons_mobs.entities.redstone;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.util.*;
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

import javax.annotation.Nullable;

public class RedstoneGolemEntity extends AbstractRaiderEntity {
    private int attackTimer;
    private int mineAttackCooldown;
    private static final DataParameter<Boolean> SUMMONING_MINES = EntityDataManager.defineId(RedstoneGolemEntity.class, DataSerializers.BOOLEAN);

    public RedstoneGolemEntity(World worldIn){
        super(ModEntityTypes.REDSTONE_GOLEM.get(), worldIn);
    }

    public RedstoneGolemEntity(EntityType<? extends RedstoneGolemEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookControl = new GolemLookController(this);
        this.moveControl = new GolemMovementController(this);
        this.maxUpStep = 1.0F;
        this.xpReward = 40;
        this.mineAttackCooldown = 10 * 20;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONING_MINES, false);
    }

    public boolean isSummoningMines() {
        return this.entityData.get(SUMMONING_MINES);
    }

    public void setSummoningMines(boolean summoningMines){
        this.entityData.set(SUMMONING_MINES, summoningMines);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new RedstoneGolemEntity.AttackGoal());
        this.goalSelector.addGoal(5, new RedstoneGolemEntity.SummonRedstoneMinesGoal());
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }
        if (!this.level.isClientSide && this.mineAttackCooldown > 0) {
            --this.mineAttackCooldown;
        }
        this.handleLeafCollision();
        this.handleSteppingOnBlocks();
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


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D) // 2x Golem Health
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 30.0D) // 2x Golem Attack
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D); // 2x Ravager knockback
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
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
        this.attackTimer = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        float attackDamage = this.getAttackDamage();
        float attackKnockback = this.getAttackKnockback();
        float adjustedAttackDamage = (int)attackDamage > 0 ? attackDamage / 2.0F + (float)this.random.nextInt((int)attackDamage) : attackDamage;
        boolean didAttack = entityIn.hurt(DamageSource.mobAttack(this), adjustedAttackDamage);
        if (didAttack) {
            if (attackKnockback > 0.0F && entityIn instanceof LivingEntity) {
                LivingEntity attackTarget = (LivingEntity)entityIn;
                double ratioX = (double)MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                double ratioZ = (double)(-MathHelper.cos(this.yRot * ((float)Math.PI / 180F)));
                double knockbackReduction = 0.5D;
                this.forceKnockback(attackTarget,
                        attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
            this.doEnchantDamageEffects(this, entityIn);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return didAttack;
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

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (flag) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }

        return flag;
    }

    public boolean checkSpawnObstruction(IWorldReader worldIn) {
        BlockPos golemPos = this.blockPosition();
        BlockPos posBeneathGolem = golemPos.below();
        BlockState blockstateBeneathGolem = worldIn.getBlockState(posBeneathGolem);
        if (!blockstateBeneathGolem.entityCanStandOn(worldIn, posBeneathGolem, this)) {
            return false;
        } else {
            for(int i = 1; i < 4; ++i) {
                BlockPos posAboveGolem = golemPos.above(i);
                BlockState blockstateAboveGolem = worldIn.getBlockState(posAboveGolem);
                if (!WorldEntitySpawner
                        .isValidEmptySpawnBlock(worldIn,
                                posAboveGolem,
                                blockstateAboveGolem,
                                blockstateAboveGolem.getFluidState(),
                                ModEntityTypes.REDSTONE_GOLEM.get())) {
                    return false;
                }
            }

            return WorldEntitySpawner
                    .isValidEmptySpawnBlock(worldIn,
                            golemPos,
                            worldIn.getBlockState(golemPos),
                            Fluids.EMPTY.defaultFluidState(),
                            ModEntityTypes.REDSTONE_GOLEM.get())
                    && worldIn.isUnobstructed(this);
        }
    }/**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        }
        else if (id == 5) {
            // Play the summoning sound
            this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(id);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
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
        return new RedstoneGolemEntity.Navigator(this, worldIn);
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new RedstoneGolemEntity.Processor();
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

    static class GolemLookController extends LookController {
        RedstoneGolemEntity redstoneGolemEntity;
        GolemLookController(RedstoneGolemEntity mob) {
            super(mob);
            this.redstoneGolemEntity = mob;
        }

        @Override
        public void tick() {
            if(!this.redstoneGolemEntity.isSummoningMines()){
                super.tick();
            }
        }
    }

    static class GolemMovementController extends MovementController{
        RedstoneGolemEntity redstoneGolemEntity;
        GolemMovementController(RedstoneGolemEntity mob) {
            super(mob);
            this.redstoneGolemEntity = mob;
        }

        @Override
        public void tick() {
            if(!this.redstoneGolemEntity.isSummoningMines()){
                super.tick();
            }
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
        AttackGoal() {
            super(RedstoneGolemEntity.this, 1.0D, true);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !RedstoneGolemEntity.this.canSummonMines();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !RedstoneGolemEntity.this.canSummonMines();
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            float adjustedAttackerWidth = RedstoneGolemEntity.this.getBbWidth() - 0.8F;
            float attackerWidthSquaredTimes4 = adjustedAttackerWidth * 2.0F * adjustedAttackerWidth * 2.0F;
            return (double)(attackerWidthSquaredTimes4 + attackTarget.getBbWidth());
        }
    }

    // MINES

    class SummonRedstoneMinesGoal extends Goal{
        static final int WARM_UP_TICKS = 5 * 20;
        static final int MINE_ATTACK_COOLDOWN = 10 * 20;
        private int warmUpTicks;

        SummonRedstoneMinesGoal(){
            this.warmUpTicks = WARM_UP_TICKS;
        }

        @Override
        public boolean canUse() {
            return this.warmUpTicks > 0
                    && RedstoneGolemEntity.this.canSummonMines();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            RedstoneGolemEntity.this.getNavigation().stop();
            RedstoneGolemEntity.this.setSummoningMines(true);
            // Play the summoning sound
            RedstoneGolemEntity.this.level.broadcastEntityEvent(RedstoneGolemEntity.this, (byte) 5);
            RedstoneGolemEntity.this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
        }

        @Override
        public void tick() {
            this.warmUpTicks--;
            if(this.warmUpTicks <= 0){
                BlockPos centerPos = RedstoneGolemEntity.this.blockPosition();
                for(int i = 0; i < 14; i++){
                    double randomNearbyX = centerPos.getX() + (RedstoneGolemEntity.this.random.nextGaussian() * 10.0D);
                    //double randomNearbyY = RedstoneGolemEntity.this.getPosY() + (double)(RedstoneGolemEntity.this.rand.nextInt(4) - 2);
                    double randomNearbyZ = centerPos.getZ() + (RedstoneGolemEntity.this.random.nextGaussian() * 10.0D);
                    BlockPos randomBlockPos = new BlockPos(randomNearbyX, centerPos.getY(), randomNearbyZ);
                    if(GeomancyHelper.canAllowBlockEntitySpawn(RedstoneGolemEntity.this, randomBlockPos)){
                        RedstoneMineEntity redstoneMineEntity = new RedstoneMineEntity(RedstoneGolemEntity.this.level, randomBlockPos.getX(), randomBlockPos.getY(), randomBlockPos.getZ(), RedstoneGolemEntity.this);
                        RedstoneGolemEntity.this.level.addFreshEntity(redstoneMineEntity);
                    }
                }
            }
        }

        @Override
        public void stop() {
            this.warmUpTicks = WARM_UP_TICKS;
            RedstoneGolemEntity.this.mineAttackCooldown = MINE_ATTACK_COOLDOWN;
            RedstoneGolemEntity.this.setSummoningMines(false);
        }
    }

    private boolean canSummonMines() {
        return RedstoneGolemEntity.this.mineAttackCooldown <= 0
                && RedstoneGolemEntity.this.getTarget() != null
                && RedstoneGolemEntity.this.getTarget().isAlive()
                && RedstoneGolemEntity.this.isOnGround();
    }
}
