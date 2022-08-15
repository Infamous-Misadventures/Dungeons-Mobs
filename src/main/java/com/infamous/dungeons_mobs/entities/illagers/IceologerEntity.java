package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class IceologerEntity extends SpellcastingIllagerEntity implements IAnimatable {

    public static final DataParameter<Integer> SUMMON_TICK = EntityDataManager.defineId(IceologerEntity.class, DataSerializers.INT);

    public boolean SpellAttacking;
    public int summonCloudCooldown = 0;
    public int timer = 0;

    AnimationFactory factory = new AnimationFactory(this);

    public IceologerEntity(World world){
        super(ModEntityTypes.ICEOLOGER.get(), world);
    }

    public IceologerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractVillagerEntity.class, 3.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 3.0F, 1.3D, 1.15D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new IceologerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(5, new IceologerEntity.SummonCloudGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public void baseTick() {
        super.baseTick();

        if (this.getTarget() != null) {
            this.getLookControl().setLookAt(this.getTarget(), (float)this.getMaxHeadYRot(), (float)this.getMaxHeadXRot());
        }

        if (this.summonCloudCooldown > 0) {
            this.summonCloudCooldown --;
        }

        if (this.timer > 0) {
            this.timer --;
        }

        if (this.getLiftTicks() > 0) {
            this.setLiftTicks(this.getLiftTicks() - 1);
        }

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getLiftTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iceologer.summon", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iceologer.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iceologer.idle", true));
        }
        return this.level.isClientSide || this.deathTime > 0 ? PlayState.CONTINUE :
                PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        super.populateDefaultEquipmentSlots(p_180481_1_);
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.ICEOLOGER_HOOD.get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.ICEOLOGER_ROBES.get()));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.ICEOLOGER_PANTS.get()));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.ICEOLOGER_SHOES.get()));
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData iLivingEntityData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.populateDefaultEquipmentEnchantments(p_213386_2_);
        return iLivingEntityData;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMON_TICK, 0);
    }


    public int getLiftTicks() {
        return this.entityData.get(SUMMON_TICK);
    }

    public void setLiftTicks(int p_189794_1_) {
        this.entityData.set(SUMMON_TICK, p_189794_1_);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MageEntity.setCustomAttributes();
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
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
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (IceologerEntity.this.getTarget() != null) {
                IceologerEntity.this.getLookControl().setLookAt(IceologerEntity.this.getTarget(), (float) IceologerEntity.this.getMaxHeadYRot(), (float) IceologerEntity.this.getMaxHeadXRot());
            }

        }
    }

    class SummonCloudGoal extends Goal {


        @Override
        public boolean isInterruptable() {
            return false;
        }

        public SummonCloudGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public boolean canUse() {
            return IceologerEntity.this.getTarget() != null && IceologerEntity.this.getLiftTicks() == 0 && IceologerEntity.this.summonCloudCooldown == 0 && IceologerEntity.this.random.nextInt(20) == 0;
        }

        public boolean canContinueToUse() {
            return IceologerEntity.this.getTarget() != null && IceologerEntity.this.getLiftTicks() > 0;
        }



        public void start() {
            super.start();
            IceologerEntity.this.SpellAttacking = false;
            IceologerEntity.this.summonCloudCooldown = 280 - IceologerEntity.this.getRandom().nextInt(80);
            IceologerEntity.this.setLiftTicks(44);
            IceologerEntity mob = IceologerEntity.this;

            IceologerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, IceologerEntity.this.getSoundVolume(), IceologerEntity.this.getVoicePitch());
            IceCloudEntity v = new IceCloudEntity(level, IceologerEntity.this, IceologerEntity.this.getTarget());
            v.moveTo(mob.getTarget().getX(), mob.getTarget().getY(mob.getBbHeight()), mob.getTarget().getZ());
            mob.level.addFreshEntity(v);
        }

        public void tick() {
            IceologerEntity.this.getLookControl().setLookAt(IceologerEntity.this.getTarget(), (float) IceologerEntity.this.getMaxHeadYRot(), (float) IceologerEntity.this.getMaxHeadXRot());

        }

        public void stop() {
            super.stop();
            IceologerEntity.this.setLiftTicks(0);
        }
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }
}