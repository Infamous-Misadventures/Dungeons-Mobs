package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.network.MobEnchantmentMessage;
import com.infamous.dungeons_libraries.utils.AreaOfEffectHelper;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;
import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapabilityLazy;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.DOUBLE_DAMAGE;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.PROTECTION;
import static com.infamous.dungeons_mobs.network.datasync.ModDataSerializers.UUID_LIST;

public class EnchanterEntity extends SpellcastingIllagerEntity implements IAnimatable {

    public static final DataParameter<Integer> ATTACK_TICKS = EntityDataManager.defineId(EnchanterEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> ENCHANT_TICKS = EntityDataManager.defineId(EnchanterEntity.class, DataSerializers.INT);
    public static final DataParameter<List<UUID>> ENCHANTMENT_TARGETS = EntityDataManager.defineId(EnchanterEntity.class, UUID_LIST);

    AnimationFactory factory = new AnimationFactory(this);

    private MonsterEntity enchantmentTarget;
    private List<MonsterEntity> enchantmentTargets = new ArrayList<>();

    public EnchanterEntity(World world) {
        super(ModEntityTypes.ENCHANTER.get(), world);
    }

    public EnchanterEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MAX_HEALTH, 14.0D);
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

    public void addEnchantmentTarget(MonsterEntity monsterEntity){
        enchantmentTargets.add(monsterEntity);
        this.entityData.set(ENCHANTMENT_TARGETS, enchantmentTargets.stream().map(Entity::getUUID).collect(Collectors.toList()));
    }

    public void setEnchantmentTargets(List<MonsterEntity> monsterEntities){
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
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EnchanterEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new EnchanterEntity.EnchantSpellGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    @Nullable
    private MonsterEntity getEnchantmentTarget() {
        return this.enchantmentTarget;
    }

    private void setEnchantmentTarget(@Nullable MonsterEntity p_190748_1_) {
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

        List<MonsterEntity> validEnchantmentTargets = this.enchantmentTargets.stream().filter(this::isValidEnchantmentTarget).collect(Collectors.toList());
        this.enchantmentTargets.stream().filter(monsterEntity -> !validEnchantmentTargets.contains(monsterEntity)).filter(LivingEntity::isAlive).forEach(this::clearEntityMobEnchantments);
        setEnchantmentTargets(validEnchantmentTargets);
    }

    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);
        this.enchantmentTargets.forEach(this::clearEntityMobEnchantments);
        this.setEnchantmentTargets(new ArrayList<>());
    }

    private void clearEntityMobEnchantments(MonsterEntity entity) {
        IEnchantable enchantableCapability = getEnchantableCapability(entity);
        enchantableCapability.clearAllEnchantments();
        entity.refreshDimensions();
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), enchantableCapability.getEnchantments()));
    }

    private boolean isValidEnchantmentTarget(MonsterEntity monsterEntity) {
        return monsterEntity.isAlive() && monsterEntity.distanceTo(this) <= 30;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getEnchantTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_enchant", true));
        } else if (this.getAttackTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_attack", true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_idle", true));
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
        } else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getMobType() == CreatureAttribute.ILLAGER) {
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
    public ArmPose getArmPose() {
        ArmPose illagerArmPose = super.getArmPose();
        if (illagerArmPose == ArmPose.CROSSED) {
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    class CastingSpellGoal extends SpellcastingIllagerEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (EnchanterEntity.this.getEnchantmentTarget() != null) {
                EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getEnchantmentTarget(), (float) EnchanterEntity.this.getMaxHeadYRot(), (float) EnchanterEntity.this.getMaxHeadXRot());
            }

        }
    }

    public class EnchantSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
//        private final EntityPredicate wololoTargeting = (new EntityPredicate()).range(16.0D).allowInvulnerable().selector();

        public boolean canUse() {
            if (EnchanterEntity.this.getTarget() == null) {
                return false;
            } else if (EnchanterEntity.this.isCastingSpell()) {
                return false;
            } else if (EnchanterEntity.this.tickCount < this.nextAttackTickCount) {
                return false;
            }else if(EnchanterEntity.this.enchantmentTargets.size() > 1){
                return false;
            } else {
                List<LivingEntity> list = AreaOfEffectHelper.getNearbyEnemies(EnchanterEntity.this, 16, EnchanterEntity.this.level, livingEntity -> {
                    IEnchantable enchantableCapability = getEnchantableCapability(livingEntity);
                    return !enchantableCapability.hasEnchantment() && livingEntity instanceof MonsterEntity;
                });
                if (list.isEmpty()) {
                    return false;
                } else {
                    EnchanterEntity.this.setEnchantmentTarget((MonsterEntity) list.get(EnchanterEntity.this.random.nextInt(list.size())));
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
            EnchanterEntity.this.setEnchantmentTarget((MonsterEntity) null);
        }

        protected void performSpellCasting() {
            MonsterEntity selectedMonsterEntity = EnchanterEntity.this.getEnchantmentTarget();
            if (selectedMonsterEntity != null && selectedMonsterEntity.isAlive()) {
                getEnchantableCapabilityLazy(selectedMonsterEntity).ifPresent(cap -> {
                    cap.addEnchantment(DOUBLE_DAMAGE.get());
                    cap.addEnchantment(PROTECTION.get());
                    selectedMonsterEntity.refreshDimensions();
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> selectedMonsterEntity), new MobEnchantmentMessage(selectedMonsterEntity.getId(), cap.getEnchantments()));
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

        protected SpellcastingIllagerEntity.SpellType getSpell() {
            return SpellcastingIllagerEntity.SpellType.NONE;
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