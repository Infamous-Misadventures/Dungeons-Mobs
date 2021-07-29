package com.infamous.dungeons_mobs.entities.golem;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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

import javax.annotation.Nullable;

public class SquallGolemEntity extends AbstractRaiderEntity{
    private int attackTimer;

    public SquallGolemEntity(World worldIn){
        super(ModEntityTypes.SQUALL_GOLEM.get(), worldIn);
    }

    public SquallGolemEntity(EntityType<? extends SquallGolemEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
        this.xpReward = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));

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
                .add(Attributes.MAX_HEALTH, 100.0D) // 1x Golem Health
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D) // 1x Golem Attack
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D); // 1x Ravager knockback
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
        float adjustedAttackDamage = (int)attackDamage > 0 ? attackDamage / 2.0F + (float)this.random.nextInt((int)attackDamage) : attackDamage;
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), adjustedAttackDamage);
        if (flag) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(0.0D, (double)0.4F, 0.0D));
            this.doEnchantDamageEffects(this, entityIn);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
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
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
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
        return new SquallGolemEntity.Navigator(this, worldIn);
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new SquallGolemEntity.Processor();
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
}
