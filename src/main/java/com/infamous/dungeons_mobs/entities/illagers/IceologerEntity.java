package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
import java.util.function.Predicate;

import static com.infamous.dungeons_mobs.entities.SpawnArmoredHelper.equipArmorSet;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class IceologerEntity extends AbstractIllager implements IAnimatable, SpawnArmoredMob {

    public int summonAnimationTick;
    public int summonAnimationLength = 60;
    public int summonAnimationActionPoint = 40;

    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public IceologerEntity(Level world) {
        super(ModEntityTypes.ICEOLOGER.get(), world);
    }

    public IceologerEntity(EntityType<? extends IceologerEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new IceologerEntity.SummonIceChunkGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractVillager.class, 3.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 3.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolem.class, 3.0F, 1.3D, 1.15D));
        this.goalSelector.addGoal(2, new ApproachTargetGoal(this, 10, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false).setUnseenMemoryTicks(600));
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.summonAnimationTick = summonAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();
    }

    public void tickDownAnimTimers() {
        if (this.summonAnimationTick > 0) {
            this.summonAnimationTick--;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.summonAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_summon", LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_walk", LOOP));
        } else {
            if (this.isCelebrating()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_celebrate", LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_idle", LOOP));
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
        equipArmorSet(ModItems.ICEOLOGER_ARMOR, this);
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
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 18D).add(Attributes.MAX_HEALTH, 20.0D);
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
        return ModSoundEvents.ICEOLOGER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ICEOLOGER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ICEOLOGER_HURT.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.ICEOLOGER_ATTACK.get();
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.ICEOLOGER_ARMOR;
    }

    class SummonIceChunkGoal extends Goal {
        public IceologerEntity mob;
        @Nullable
        public LivingEntity target;

        private final Predicate<Entity> ICE_CHUNK = (p_33346_) -> {
            return p_33346_ instanceof IceCloudEntity && ((IceCloudEntity) p_33346_).owner != null && ((IceCloudEntity) p_33346_).owner == mob;
        };

        public SummonIceChunkGoal(IceologerEntity mob) {
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
            int nearbyChunks = mob.level.getEntities(mob, mob.getBoundingBox().inflate(20.0D), ICE_CHUNK)
                    .size();

            return target != null && mob.random.nextInt(20) == 0 && mob.distanceTo(target) <= 12 && nearbyChunks <= 0 && mob.hasLineOfSight(target) && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(ModSoundEvents.ICEOLOGER_ATTACK.get(), 1.0F, mob.getVoicePitch());
            mob.summonAnimationTick = mob.summonAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 4);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            if (target != null) {
                mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            }

            if (target != null && mob.summonAnimationTick == mob.summonAnimationActionPoint) {
                IceCloudEntity.spawn(mob, target);
            }
        }

        public boolean animationsUseable() {
            return mob.summonAnimationTick <= 0;
        }

    }
}