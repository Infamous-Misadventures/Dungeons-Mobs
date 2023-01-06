package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.entities.summonables.WraithFireEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class WraithEntity extends Monster implements IAnimatable {

    public int summonFireAttackAnimationTick;
    public int summonFireAttackAnimationLength = 40;
    public int summonFireAttackAnimationActionPoint = 20;

    public int teleportAnimationTick;
    public int teleportAnimationLength = 40;
    public int teleportAnimationActionPoint = 18;

    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public WraithEntity(EntityType<? extends WraithEntity> type, Level world) {
        super(type, world);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RestrictSunGoal(this));
        this.goalSelector.addGoal(2, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new WraithEntity.TeleportGoal(this));
        this.goalSelector.addGoal(4, new WraithEntity.SummonFireAttackGoal(this));
        this.goalSelector.addGoal(5, new ApproachTargetGoal(this, 8, 1.2D, true));
        this.goalSelector.addGoal(6, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR).setUnseenMemoryTicks(300));
    }

    public boolean isSpellcasting() {
        return this.summonFireAttackAnimationTick > 0;
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.teleportAnimationTick = teleportAnimationLength;
        } else if (p_28844_ == 11) {
            this.summonFireAttackAnimationTick = summonFireAttackAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity
                && ((LivingEntity) entityIn).getMobType() == MobType.UNDEAD) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.tickDownAnimTimers();

        if (this.teleportAnimationTick > 0) {
            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(1), this.getY(), this.getRandomZ(1), this.random.nextGaussian() * 0.01, 0.1, this.random.nextGaussian() * 0.01);
        }
    }

    public void tickDownAnimTimers() {
        if (this.summonFireAttackAnimationTick > 0) {
            this.summonFireAttackAnimationTick--;
        }

        if (this.teleportAnimationTick > 0) {
            this.teleportAnimationTick--;
        }
    }

    public void aiStep() {

        if (!this.onGround && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.75D, 1.0D));
        }

        if (this.isAlive()) {
            boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setSecondsOnFire(8);
                }
            }
        }

        super.aiStep();
    }

    public boolean isSunSensitive() {
        return true;
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.4D).add(Attributes.FOLLOW_RANGE, 25.0D).add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.WRAITH_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.WRAITH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.WRAITH_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSoundEvents.WRAITH_FLY.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.summonFireAttackAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_attack", EDefaultLoopTypes.LOOP));
        } else if (this.teleportAnimationTick > 10) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_teleport", EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_fly", EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_idle", EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected boolean teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
            double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
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

    private boolean teleport(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
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
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), false);
            if (flag2 && !this.isSilent()) {
                this.level.playSound(null, this.xo, this.yo, this.zo, ModSoundEvents.WRAITH_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(ModSoundEvents.WRAITH_TELEPORT.get(), 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }

    class TeleportGoal extends Goal {
        public WraithEntity mob;
        @Nullable
        public LivingEntity target;

        public TeleportGoal(WraithEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && (mob.distanceTo(target) <= 4 || mob.distanceTo(target) >= 16) && mob.hasLineOfSight(target) && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(ModSoundEvents.WRAITH_IDLE.get(), 1.0F, mob.getVoicePitch());
            mob.teleportAnimationTick = mob.teleportAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 4);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            if (target != null) {
                mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            }

            int teleportAwayRange = 20;
            int teleportToRange = 10;

            if (target != null && mob.teleportAnimationTick == mob.teleportAnimationActionPoint) {
                if (mob.distanceTo(target) >= 16) {
                    for (int i = 0; i < 10; i++) {
                        mob.teleport(target.getX() - teleportToRange + mob.random.nextInt(teleportToRange * 2), target.getY(), target.getZ() - teleportToRange + mob.random.nextInt(teleportToRange * 2));
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        mob.teleport(target.getX() - teleportAwayRange + mob.random.nextInt(teleportAwayRange * 2), target.getY(), target.getZ() - teleportAwayRange + mob.random.nextInt(teleportAwayRange * 2));
                    }
                }

            }
        }

        public boolean animationsUseable() {
            return mob.teleportAnimationTick <= 0;
        }

    }

    class SummonFireAttackGoal extends Goal {
        public WraithEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime = 0;

        public SummonFireAttackGoal(WraithEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) <= 14 && mob.hasLineOfSight(target) && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(ModSoundEvents.WRAITH_ATTACK.get(), 1.0F, mob.getVoicePitch());
            mob.summonFireAttackAnimationTick = mob.summonFireAttackAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 11);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            mob.getNavigation().stop();

            if (target != null) {
                mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            }

            if (target != null && mob.summonFireAttackAnimationTick == mob.summonFireAttackAnimationActionPoint) {
                WraithFireEntity wraithFire = ModEntityTypes.WRAITH_FIRE.get().create(mob.level);
                wraithFire.owner = mob;
                wraithFire.moveTo(target.position());
                mob.level.addFreshEntity(wraithFire);
                PositionUtils.moveToCorrectHeight(wraithFire);
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.nextUseTime = mob.tickCount + 60 + mob.random.nextInt(60);
        }

        public boolean animationsUseable() {
            return mob.summonFireAttackAnimationTick <= 0;
        }

    }
}
