package com.infamous.dungeons_mobs.entities.ender;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EndersentEntity extends AbstractEnderlingEntity implements IAnimatable {

    public static final EntityDataAccessor<Integer> TELEPORTING = SynchedEntityData.defineId(EndersentEntity.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setCreateWorldFog(true).setPlayBossMusic(true);
    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public EndersentEntity(EntityType<? extends EndersentEntity> p_i50210_1_, Level p_i50210_2_) {
        super(p_i50210_1_, p_i50210_2_);
        this.xpReward = 50;
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0.85D).add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 14.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    //public void load(CompoundNBT p_70020_1_) {
    //	super.load(p_70020_1_);
    //
    //	this.setMusicTicks(30);
    //}

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EndersentEntity.AttackGoal(1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, AbstractEnderlingEntity.class).setAlertOthers().setUnseenMemoryTicks(500));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(500));
        this.targetSelector.addGoal(1, new AbstractEnderlingEntity.FindPlayerGoal(this, null));


        //this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, AbstractEndermanVariant.class, true, false));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TELEPORTING, 0);
    }

    public int isTeleporting() {
        return this.entityData.get(TELEPORTING);
    }

    public void setTeleporting(int p_189794_1_) {

        if (p_189794_1_ == 15) {
            if (this.getTarget() != null) {
                this.setPos(this.getTarget().getX() - 5 + this.random.nextInt(10), this.getTarget().getY(), this.getTarget().getZ() - 5 + this.random.nextInt(10));
                this.level.playSound(null, this.xo, this.yo, this.zo, ModSoundEvents.ENDERSENT_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(ModSoundEvents.ENDERSENT_TELEPORT.get(), 1.0F, 1.0F);
            } else {
                this.setPos(this.getX() - 20 + this.random.nextInt(40), this.getY(), this.getZ() - 20 + this.random.nextInt(40));
                this.level.playSound(null, this.xo, this.yo, this.zo, ModSoundEvents.ENDERSENT_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(ModSoundEvents.ENDERSENT_TELEPORT.get(), 1.0F, 1.0F);
            }
        }

        this.entityData.set(TELEPORTING, p_189794_1_);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 100) {
            this.remove(RemovalReason.KILLED);

            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
            }
        }

    }

    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }

    }

    public void setCustomName(@Nullable Component p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.ENDERSENT_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.ENDERSENT_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ENDERSENT_DEATH.get();
    }

    public boolean doHurtTarget(Entity p_70652_1_) {
        this.playSound(ModSoundEvents.ENDERSENT_ATTACK.get(), 1.0F, 1.0F);
        return super.doHurtTarget(p_70652_1_);
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(this.getStepSound(), 1.25F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 2.0F;
    }

    protected SoundEvent getStepSound() {
        return ModSoundEvents.ENDERSENT_STEP.get();
    }

    public void setTarget(LivingEntity p_70624_1_) {
        //if (p_70624_1_ != null && p_70624_1_ != this.getTarget()) {
        //	this.teleportTo(p_70624_1_.getX() - 3 + this.random.nextInt(6), p_70624_1_.getY(), p_70624_1_.getZ() - 3 + this.random.nextInt(6));
        //}
        super.setTarget(p_70624_1_);
    }

    public void baseTick() {
        super.baseTick();

        if (this.isTeleporting() > 0) {
            this.setTeleporting(this.isTeleporting() - 1);
        }

        //System.out.print("\r\n" + this.tickCount);

        //Player player = this.level.getNearestPlayer(this, EnderlingConfig.endersent_patrol_distance.get());


        if (this.random.nextInt(500) == 0 && this.getTarget() == null) {
            this.setTeleporting(50);
        } else if (this.random.nextInt(200) == 0 && this.getTarget() != null) {
            this.setTeleporting(50);
        }

        if (this.isTeleporting() > 0) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F);
        }

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void startSeenByPlayer(ServerPlayer p_184178_1_) {
        super.startSeenByPlayer(p_184178_1_);
        this.bossEvent.addPlayer(p_184178_1_);
    }

    public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
        super.stopSeenByPlayer(p_184203_1_);
        this.bossEvent.removePlayer(p_184203_1_);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.deathTime > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_death", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        } else if (this.isTeleporting() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_teleport", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.isAttacking() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_attack", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected boolean teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 32.0D;
            double d1 = this.getY() + (double) (this.random.nextInt(8) - 4);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 32.0D;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    private boolean teleportTowards(Entity p_70816_1_) {
        Vec3 vector3d = new Vec3(this.getX() - p_70816_1_.getX(), this.getY(0.5D) - p_70816_1_.getEyeY(), this.getZ() - p_70816_1_.getZ());
        vector3d = vector3d.normalize();
        double d0 = 16.0D;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * 16.0D;
        double d2 = this.getY() + (double) (this.random.nextInt(16) - 8) - vector3d.y * 16.0D;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * 16.0D;
        return this.teleport(d1, d2, d3);
    }

    protected boolean teleport(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(p_70825_1_, p_70825_3_, p_70825_5_);

        while (blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, p_70825_1_, p_70825_3_, p_70825_5_);
            if (event.isCanceled()) return false;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.level.playSound(null, this.xo, this.yo, this.zo, ModSoundEvents.ENDERSENT_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(ModSoundEvents.ENDERSENT_TELEPORT.get(), 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }

    class AttackGoal extends MeleeAttackGoal {

        public AttackGoal(double speed) {
            super(EndersentEntity.this, speed, true);
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected double getAttackReachSqr(LivingEntity p_179512_1_) {
            return this.mob.getBbWidth() * 5.0F * this.mob.getBbWidth() * 5.0F + p_179512_1_.getBbWidth();
        }

        public void tick() {
            super.tick();

            EndersentEntity.this.setRunning(10);
        }


        protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
            double d0 = this.getAttackReachSqr(p_190102_1_);
            if (p_190102_2_ <= d0 && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(p_190102_1_);
            } else if (p_190102_2_ <= d0 * 1.5D) {
                if (this.isTimeToAttack()) {
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 30) {
                    EndersentEntity.this.setAttacking(30);
                }
            } else {
                this.resetAttackCooldown();
            }
        }
    }

}
