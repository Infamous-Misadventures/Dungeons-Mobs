package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
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
import java.util.function.Predicate;

import static com.infamous.dungeons_mobs.entities.SpawnArmoredHelper.equipArmorSet;

public class MageEntity extends AbstractIllager implements IAnimatable, SpawnArmoredMob {

    public int attackAnimationTick;
    public int attackAnimationLength = 50;

    public int vanishAnimationTick;
    public int vanishAnimationLength = 23;

    public int appearAnimationTick;
    public int appearAnimationLength = 25;

    public int appearDelay = 0;

    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MageEntity(EntityType<? extends MageEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new MageEntity.RemainStationaryGoal());
        this.goalSelector.addGoal(1, new MageEntity.CreateIllusionsGoal(this));
        this.goalSelector.addGoal(2, new MageEntity.LevitateTargetAttackGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, AbstractVillager.class, 5.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 5.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, IronGolem.class, 5.0F, 1.3D, 1.15D));
        this.goalSelector.addGoal(4, new ApproachTargetGoal(this, 14, 1.0D, true));
        this.goalSelector.addGoal(5, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false).setUnseenMemoryTicks(600));
    }

    public boolean shouldBeStationary() {
        return this.appearAnimationTick > 0 || this.appearDelay > 0;
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.attackAnimationTick = attackAnimationLength;
        } else if (p_28844_ == 6) {
            this.appearDelay = 11;
        } else if (p_28844_ == 7) {
            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
            }
        } else if (p_28844_ == 8) {
            this.vanishAnimationTick = vanishAnimationLength;
        } else if (p_28844_ == 9) {
            this.appearAnimationTick = appearAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();

        if (this.appearDelay > 0) {
            this.appearDelay--;
        }

        if (!this.level.isClientSide && this.appearDelay == 1) {
            this.appearAnimationTick = appearAnimationLength;
            this.level.broadcastEntityEvent(this, (byte) 9);
        }
    }

    public void tickDownAnimTimers() {
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        if (this.vanishAnimationTick > 0) {
            this.vanishAnimationTick--;
        }

        if (this.appearAnimationTick > 0) {
            this.appearAnimationTick--;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.appearAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_appear", EDefaultLoopTypes.LOOP));
        } else if (this.vanishAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_vanish", EDefaultLoopTypes.LOOP));
        } else if (this.attackAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_throw", EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_walk", EDefaultLoopTypes.LOOP));
        } else {
            if (this.isCelebrating()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_celebrate", EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_idle", EDefaultLoopTypes.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_180481_1_) {
        super.populateDefaultEquipmentSlots(random, p_180481_1_);
        equipArmorSet(ModItems.MAGE_ARMOR, this);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_213386_1_, DifficultyInstance p_213386_2_, MobSpawnType p_213386_3_, @Nullable SpawnGroupData p_213386_4_, @Nullable CompoundTag p_213386_5_) {
        SpawnGroupData iLivingEntityData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        this.populateDefaultEquipmentSlots(this.getRandom(), p_213386_2_);
        this.populateDefaultEquipmentEnchantments(this.getRandom(), p_213386_2_);
        return iLivingEntityData;
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 30.0D).add(Attributes.MAX_HEALTH, 40.0D);
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getMobType() == MobType.ILLAGER) {
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
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ILLUSIONER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ENCHANTER_HURT.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.MAGE_ARMOR;
    }

    class CreateIllusionsGoal extends Goal {
        public MageEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        private final Predicate<Entity> MAGE_CLONE = (p_33346_) -> {
            return p_33346_ instanceof MageCloneEntity && ((MageCloneEntity) p_33346_).getOwner() != null && ((MageCloneEntity) p_33346_).getOwner() == mob;
        };

        public CreateIllusionsGoal(MageEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return mob.shouldBeStationary();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            int nearbyClones = mob.level.getEntities(mob, mob.getBoundingBox().inflate(30.0D), MAGE_CLONE)
                    .size();

            return target != null && mob.tickCount >= this.nextUseTime && mob.random.nextInt(10) == 0 && mob.hasLineOfSight(target) && nearbyClones <= 0 && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, 1.0F, 1.0F);
            mob.vanishAnimationTick = mob.vanishAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 8);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            mob.getNavigation().stop();

            if (target != null) {
                mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            }

            if (target != null && mob.vanishAnimationTick == 1) {
                SummonSpotEntity summonSpot = ModEntityTypes.SUMMON_SPOT.get().create(mob.level);
                summonSpot.moveTo(target.blockPosition().offset(-12.5 + mob.random.nextInt(25), 0, -12.5 + mob.random.nextInt(25)), 0.0F, 0.0F);
                summonSpot.setSummonType(3);
                ((ServerLevel) mob.level).addFreshEntityWithPassengers(summonSpot);
                PositionUtils.moveToCorrectHeight(summonSpot);

                mob.level.broadcastEntityEvent(mob, (byte) 7);
                mob.moveTo(summonSpot.blockPosition(), 0.0F, 0.0F);
                mob.setYBodyRot(mob.random.nextInt(360));
                mob.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(mob.getX(), mob.getEyeY(), mob.getZ()));
                mob.appearDelay = 11;
                mob.level.broadcastEntityEvent(mob, (byte) 6);
                mob.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
                PositionUtils.moveToCorrectHeight(mob);

                if (target instanceof Mob) {
                    ((Mob) target).setTarget(null);
                    target.setLastHurtByMob(null);
                    if (target instanceof NeutralMob) {
                        ((NeutralMob) target).stopBeingAngry();
                        ((NeutralMob) target).setLastHurtByMob(null);
                        ((NeutralMob) target).setTarget(null);
                        ((NeutralMob) target).setPersistentAngerTarget(null);
                    }
                }

                int clonesByDifficulty = mob.level.getCurrentDifficultyAt(mob.blockPosition()).getDifficulty().getId();

                for (int i = 0; i < clonesByDifficulty * 4; i++) {
                    SummonSpotEntity cloneSummonSpot = ModEntityTypes.SUMMON_SPOT.get().create(mob.level);
                    cloneSummonSpot.moveTo(target.blockPosition().offset(-12.5 + mob.random.nextInt(25), 0, -12.5 + mob.random.nextInt(25)), 0.0F, 0.0F);
                    cloneSummonSpot.setSummonType(3);
                    cloneSummonSpot.mobSpawnRotation = mob.random.nextInt(360);
                    ((ServerLevel) mob.level).addFreshEntityWithPassengers(cloneSummonSpot);
                    PositionUtils.moveToCorrectHeight(cloneSummonSpot);

                    MageCloneEntity clone = ModEntityTypes.MAGE_CLONE.get().create(mob.level);
                    clone.finalizeSpawn(((ServerLevel) mob.level), mob.level.getCurrentDifficultyAt(cloneSummonSpot.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    clone.setOwner(mob);
                    clone.setHealth(mob.getHealth());
                    for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
                        ItemStack itemstack = mob.getItemBySlot(equipmentslottype);
                        if (!itemstack.isEmpty()) {
                            clone.setItemSlot(equipmentslottype, itemstack.copy());
                            clone.setDropChance(equipmentslottype, 0.0F);
                        }
                    }
                    clone.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(mob.getX(), mob.getEyeY(), mob.getZ()));
                    clone.setDelayedAppear(true);
                    cloneSummonSpot.summonedEntity = clone;
                    cloneSummonSpot.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
                }
            }
        }

        public boolean animationsUseable() {
            return mob.vanishAnimationTick <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.nextUseTime = mob.tickCount + 60;
        }

    }

    class LevitateTargetAttackGoal extends Goal {
        public MageEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime = 0;

        public boolean slammedTarget = false;

        public LevitateTargetAttackGoal(MageEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return mob.shouldBeStationary();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && !mob.shouldBeStationary() && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) <= 16 && mob.hasLineOfSight(target) && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !mob.shouldBeStationary() && !animationsUseable();
        }

        @Override
        public void start() {
            this.slammedTarget = false;
            mob.playSound(ModSoundEvents.NECROMANCER_PREPARE_SUMMON.get(), 1.0F, mob.getVoicePitch());
            mob.attackAnimationTick = mob.attackAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 4);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            mob.getNavigation().stop();

            if (target != null) {
                mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            }

            if (target != null) {
                target.hurtMarked = true;
                if (mob.attackAnimationTick >= mob.attackAnimationLength - 32) {
                    if (target.getY() < mob.getY() + 7) {
                        target.push(0, 0.1, 0);
                        if (target.verticalCollision && !target.isOnGround()) {
                            target.hurt(DamageSource.FLY_INTO_WALL, 10.0F);
                        }
                    } else {
                        target.setDeltaMovement(target.getDeltaMovement().x * 0.5, 0, target.getDeltaMovement().z * 0.5);
                    }
                } else {
                    if (!this.slammedTarget) {
                        target.fallDistance = 0;
                        target.push(0, -0.5, 0);
                        if (target.verticalCollision) {
                            this.slammedTarget = true;
                            target.hurt(DamageSource.FLY_INTO_WALL, 10.0F);
                        }
                    }
                }
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.slammedTarget = false;
            this.nextUseTime = mob.tickCount + 80 + mob.random.nextInt(120);
        }

        public boolean animationsUseable() {
            return mob.attackAnimationTick <= 0;
        }

    }

    class RemainStationaryGoal extends Goal {

        public RemainStationaryGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return MageEntity.this.shouldBeStationary();
        }
    }
}
