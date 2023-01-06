package com.infamous.dungeons_mobs.entities.illagers;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import com.infamous.dungeons_libraries.utils.AreaOfEffectHelper;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static baguchan.enchantwithmob.registry.MobEnchants.PROTECTION;
import static baguchan.enchantwithmob.registry.MobEnchants.STRONG;
import static com.infamous.dungeons_mobs.network.datasync.ModDataSerializers.UUID_LIST;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class EnchanterEntity extends SpellcasterIllager implements IAnimatable {

    public static final EntityDataAccessor<Integer> ATTACK_TICKS = SynchedEntityData.defineId(EnchanterEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ENCHANT_TICKS = SynchedEntityData.defineId(EnchanterEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<List<UUID>> ENCHANTMENT_TARGETS = SynchedEntityData.defineId(EnchanterEntity.class, UUID_LIST.get());

    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private Monster enchantmentTarget;
    private List<Monster> enchantmentTargets = new ArrayList<>();

    public EnchanterEntity(Level world) {
        super(ModEntityTypes.GEOMANCER.get(), world);
    }

    public EnchanterEntity(EntityType<? extends SpellcasterIllager> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MAX_HEALTH, 14.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_TICKS, 0);
        this.entityData.define(ENCHANT_TICKS, 0);
        this.entityData.define(ENCHANTMENT_TARGETS, new ArrayList<>());
    }

    public int getAttackTicks() {
        return this.entityData.get(ATTACK_TICKS);
    }

    public void setAttackTicks(int p_189794_1_) {
        this.entityData.set(ATTACK_TICKS, p_189794_1_);
    }

    public int getEnchantTicks() {
        return this.entityData.get(ENCHANT_TICKS);
    }

    public void setEnchantTicks(int p_189794_1_) {
        this.entityData.set(ENCHANT_TICKS, p_189794_1_);
    }

    public void addEnchantmentTarget(Monster monsterEntity) {
        enchantmentTargets.add(monsterEntity);
        this.entityData.set(ENCHANTMENT_TARGETS, enchantmentTargets.stream().map(Entity::getUUID).collect(Collectors.toList()));
    }

    public void setEnchantmentTargets(List<Monster> monsterEntities) {
        enchantmentTargets = monsterEntities;
        this.entityData.set(ENCHANTMENT_TARGETS, enchantmentTargets.stream().map(Entity::getUUID).collect(Collectors.toList()));
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EnchanterEntity.AttackingGoal());
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EnchanterEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new EnchanterEntity.EnchantSpellGoal());
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    @Nullable
    private Monster getEnchantmentTarget() {
        return this.enchantmentTarget;
    }

    private void setEnchantmentTarget(@Nullable Monster p_190748_1_) {
        this.enchantmentTarget = p_190748_1_;
    }

    public void baseTick() {
        super.baseTick();
        if (this.level.getNearestPlayer(this, 2) != null && this.getTarget() != null && this.level.getNearestPlayer(this, 2) == this.getTarget()) {
            if (this.getAttackTicks() == 0 && this.random.nextInt(10) == 0) {
                this.setAttackTicks(40);
                this.playSound(ModSoundEvents.ENCHANTER_PRE_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            } else if (this.getAttackTicks() == 12) {
                this.doHurtTarget(this.level.getNearestPlayer(this, 2));
                this.playSound(ModSoundEvents.ENCHANTER_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            }
        }

        if (this.getAttackTicks() > 0) {
            this.setAttackTicks(this.getAttackTicks() - 1);
        }

        if (this.getEnchantTicks() > 0) {
            this.setEnchantTicks(this.getEnchantTicks() - 1);
        }

        List<Monster> validEnchantmentTargets = this.enchantmentTargets.stream().filter(this::isValidEnchantmentTarget).collect(Collectors.toList());
        this.enchantmentTargets.stream().filter(monsterEntity -> !validEnchantmentTargets.contains(monsterEntity)).filter(LivingEntity::isAlive).forEach(this::clearEntityMobEnchantments);
        setEnchantmentTargets(validEnchantmentTargets);
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        this.enchantmentTargets.forEach(this::clearEntityMobEnchantments);
        this.setEnchantmentTargets(new ArrayList<>());
    }

    private void clearEntityMobEnchantments(Monster entity) {
        entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(enchantableCapability -> {
            enchantableCapability.removeAllMobEnchant(entity);
            entity.refreshDimensions();
        });
    }

    private boolean isValidEnchantmentTarget(Monster monsterEntity) {
        return monsterEntity.isAlive() && monsterEntity.distanceTo(this) <= 30;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getEnchantTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_enchant", LOOP));
        } else if (this.getAttackTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_attack", LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_walk", LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_idle", LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
        return ModSoundEvents.ENCHANTER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ENCHANTER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ENCHANTER_HURT.get();
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return ModSoundEvents.ENCHANTER_BEAM.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.ENCHANTER_IDLE.get();
    }

    @Override
    public IllagerArmPose getArmPose() {
        IllagerArmPose illagerArmPose = super.getArmPose();
        if (illagerArmPose == IllagerArmPose.CROSSED) {
            return IllagerArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    class CastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (EnchanterEntity.this.getEnchantmentTarget() != null) {
                EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getEnchantmentTarget(), (float) EnchanterEntity.this.getMaxHeadYRot(), (float) EnchanterEntity.this.getMaxHeadXRot());
            }

        }
    }

    public class EnchantSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
//        private final EntityPredicate wololoTargeting = (new EntityPredicate()).range(16.0D).selector();

        public boolean canUse() {
            if (EnchanterEntity.this.getTarget() == null) {
                return false;
            } else if (EnchanterEntity.this.isCastingSpell()) {
                return false;
            } else if (EnchanterEntity.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else if (EnchanterEntity.this.enchantmentTargets.size() > 1) {
                return false;
            } else {
                List<LivingEntity> list = AreaOfEffectHelper.getNearbyEnemies(EnchanterEntity.this, 16, EnchanterEntity.this.level, livingEntity -> {
                    if (livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).isPresent()) {
                        MobEnchantCapability mobEnchantCapability = livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).resolve().get();
                        return !mobEnchantCapability.hasEnchant() && livingEntity instanceof Monster;
                    } else {
                        return false;
                    }
                });
                if (list.isEmpty()) {
                    return false;
                } else {
                    EnchanterEntity.this.setEnchantmentTarget((Monster) list.get(EnchanterEntity.this.random.nextInt(list.size())));
                    EnchanterEntity.this.setEnchantTicks(45);
                    return true;
                }
            }
        }

        public boolean canContinueToUse() {
            return EnchanterEntity.this.getEnchantmentTarget() != null && this.attackWarmupDelay > 0;
        }

        public void stop() {
            super.stop();
            EnchanterEntity.this.setEnchantmentTarget(null);
        }

        protected void performSpellCasting() {
            Monster selectedMonsterEntity = EnchanterEntity.this.getEnchantmentTarget();
            if (selectedMonsterEntity != null && selectedMonsterEntity.isAlive()) {
                selectedMonsterEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
                    cap.addMobEnchant(selectedMonsterEntity, STRONG.get(), 2);
                    cap.addMobEnchant(selectedMonsterEntity, PROTECTION.get(), 2);
                    selectedMonsterEntity.refreshDimensions();
                });
                EnchanterEntity.this.addEnchantmentTarget(selectedMonsterEntity);
            }
        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 45;
        }

        protected int getCastingInterval() {
            return 140;
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSoundEvents.ENCHANTER_SPELL.get();
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.NONE;
        }
    }

    class AttackingGoal extends Goal {
        public AttackingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return EnchanterEntity.this.getAttackTicks() > 0 && EnchanterEntity.this.getTarget() != null;
        }

        public void tick() {
            EnchanterEntity.this.getNavigation().stop();
            EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getTarget().getX(), EnchanterEntity.this.getTarget().getEyeY(), EnchanterEntity.this.getTarget().getZ());
        }
    }

}