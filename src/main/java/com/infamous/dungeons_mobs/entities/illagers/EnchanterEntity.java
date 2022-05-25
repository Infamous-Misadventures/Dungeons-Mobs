package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.network.MobEnchantmentMessage;
import com.infamous.dungeons_libraries.utils.AreaOfEffectHelper;
import com.infamous.dungeons_mobs.mobenchantments.MobEnchantmentEvents;
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
import net.minecraft.world.raid.Raid;
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
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.*;
import static com.infamous.dungeons_mobs.network.datasync.ModDataSerializers.UUID_LIST;

public class EnchanterEntity extends SpellcastingIllagerEntity implements IAnimatable {

    public static final DataParameter<Integer> ATTACK_TICKS = EntityDataManager.defineId(EnchanterEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> ENCHANT_TICKS = EntityDataManager.defineId(EnchanterEntity.class, DataSerializers.INT);
    public static final DataParameter<List<UUID>> ENCHANTMENT_TARGETS = EntityDataManager.defineId(EnchanterEntity.class, UUID_LIST);

    public MonsterEntity enchantingTargetEntity = null;
    public int enchantmentCooldown;
    public int timer;

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
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MAX_HEALTH, 24.0D);
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
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.0D, 1.0D));
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

        if (this.getAttackTicks() > 0) {
            this.setAttackTicks(this.getAttackTicks() - 1);
        }

        if (this.getEnchantTicks() > 0) {
            this.setEnchantTicks(this.getEnchantTicks() - 1);
        }

        if (this.enchantmentCooldown > 0) {
            this.enchantmentCooldown --;
        }

        List<MonsterEntity> validEnchantmentTargets = this.enchantmentTargets.stream().filter(this::isValidEnchantmentTarget).collect(Collectors.toList());
        this.enchantmentTargets.stream().filter(monsterEntity -> !validEnchantmentTargets.contains(monsterEntity)).filter(LivingEntity::isAlive).forEach(this::clearEntityMobEnchantments);
        setEnchantmentTargets(validEnchantmentTargets);

        this.timer ++;
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

    class EnchantTargetGoal extends Goal{

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity v = EnchanterEntity.this.getTarget();
            if (v != null && v.isAlive())
                if (v.getMobType() == CreatureAttribute.ILLAGER) {
                    if (EnchanterEntity.this.distanceToSqr(EnchanterEntity.this.getTarget()) < 80.0D) {
                        EnchanterEntity.this.enchantingTargetEntity = (MonsterEntity) v;
                }else if (v.getMobType() == CreatureAttribute.UNDEAD) {
                        if (EnchanterEntity.this.distanceToSqr(EnchanterEntity.this.getTarget()) < 80.0D) {
                            EnchanterEntity.this.enchantingTargetEntity = (MonsterEntity) v;
                        }
                }
            }
            super.tick();
        }
    }

    class EnchantingMobsGoal extends Goal {

        public int enchantmentNumber;

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        @Override
        public boolean canUse() {
            if (EnchanterEntity.this.enchantingTargetEntity != null)
                if (EnchanterEntity.this.enchantmentTargets.get(1) == null) {
                    this.enchantmentNumber = 1;
                    return EnchanterEntity.this.enchantmentCooldown == 0 &&
                            EnchanterEntity.this.getEnchantTicks() == 0;
                } else if (EnchanterEntity.this.enchantmentTargets.get(2) == null) {
                    this.enchantmentNumber = 2;
                    return EnchanterEntity.this.enchantmentCooldown == 0 &&
                            EnchanterEntity.this.getEnchantTicks() == 0;
                } else {
                    return false;
                }else {
                    return false;
            }
        }

        @Override
        public boolean isInterruptable() {
            return EnchanterEntity.this.getEnchantTicks() > 0;
        }

        @Override
        public void tick() {
            if (EnchanterEntity.this.getEnchantTicks() == 35)
            if (this.enchantmentNumber == 1){
                EnchanterEntity.this.enchantmentCooldown = 240;
                if (EnchanterEntity.this.distanceToSqr(EnchanterEntity.this.enchantingTargetEntity) < 80.0D) {
                    MonsterEntity v = EnchanterEntity.this.enchantingTargetEntity;
                    EnchanterEntity.this.enchantmentTargets.set(1, EnchanterEntity.this.enchantingTargetEntity);
                    getEnchantableCapabilityLazy(v).ifPresent(cap ->{
                        cap.addEnchantment(DOUBLE_DAMAGE.get());
                        cap.addEnchantment(PROTECTION.get());
                        cap.addEnchantment(REGENERATION.get());
                        cap.addEnchantment(HEALS_ALLIES.get());
                        cap.addEnchantment(THORNS.get());
                        v.refreshDimensions();
                        NetworkHandler.INSTANCE.send((PacketDistributor.TRACKING_ENTITY.with(() -> v)), new MobEnchantmentMessage(v.getId(), cap.getEnchantments()));
                    });
                }

            }else if (this.enchantmentNumber == 2){
                EnchanterEntity.this.enchantmentCooldown = 240;
                if (EnchanterEntity.this.distanceToSqr(EnchanterEntity.this.enchantingTargetEntity) < 80.0D) {
                    MonsterEntity v = EnchanterEntity.this.enchantingTargetEntity;
                    EnchanterEntity.this.enchantmentTargets.set(1, EnchanterEntity.this.enchantingTargetEntity);
                    getEnchantableCapabilityLazy(v).ifPresent(cap ->{
                        int f = EnchanterEntity.this.getRandom().nextInt(6);
                        if(f==1)
                        cap.addEnchantment(DOUBLE_DAMAGE.get());
                        if(f==2)
                        cap.addEnchantment(PROTECTION.get());
                        if(f==3)
                        cap.addEnchantment(REGENERATION.get());
                        if(f==4)
                        cap.addEnchantment(HEALS_ALLIES.get());
                        if(f==5)
                        cap.addEnchantment(THORNS.get());
                        if(f==0)
                        cap.addEnchantment(ECHO.get());
                        v.refreshDimensions();
                        NetworkHandler.INSTANCE.send((PacketDistributor.TRACKING_ENTITY.with(() -> v)), new MobEnchantmentMessage(v.getId(), cap.getEnchantments()));
                    });
                }
            }
            super.tick();
        }

        @Override
        public void stop() {
            EnchanterEntity.this.setEnchantTicks(0);
            this.enchantmentNumber = 0;
            super.stop();
        }

        @Override
        public void start() {
            addEnchantmentTarget(EnchanterEntity.this.enchantingTargetEntity);
            EnchanterEntity.this.setEnchantTicks(45);
            super.start();
        }

    }

    class AttackGoal extends Goal {

        @Override
        public boolean canUse() {
            return false;
        }

    }

}