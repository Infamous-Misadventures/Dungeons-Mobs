package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TowerGuardEntity extends AbstractIllagerEntity implements IAnimatable {
    private int attackTimer;
    private int attackID;
    private int attackType;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(TowerGuardEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);


    public TowerGuardEntity(World world) {
        super(ModEntityTypes.TOWER_GUARD.get(), world);
    }

    public TowerGuardEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {

        if(ModList.get().isLoaded("dungeons_gear")){

            if (this.getCurrentRaid() == null) {
                Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));
                Item ROYAL_GUARD_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_helmet"));
                Item ROYAL_GUARD_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_chestplate"));
                ItemStack royalGuardHelmet = new ItemStack(ROYAL_GUARD_HELMET);
                ItemStack royalGuardChestplate = new ItemStack(ROYAL_GUARD_CHESTPLATE);
                ItemStack mace = new ItemStack(MACE);
                this.setItemSlot(EquipmentSlotType.HEAD, royalGuardHelmet);
                this.setItemSlot(EquipmentSlotType.CHEST, royalGuardChestplate);
                this.setItemSlot(EquipmentSlotType.MAINHAND, mace);
                this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.ROYAL_GUARD_SHIELD.get()));
            }
        }
        else{
            if (this.getCurrentRaid() == null) {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.ROYAL_GUARD_SHIELD.get()));
            }
        }
    }

    public boolean isMeleeAttacking() {
        return this.entityData.get(MELEEATTACKING);
    }

    public void setMeleeAttacking(boolean attacking) {
        this.entityData.set(MELEEATTACKING, attacking);
    }


    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }


    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack royalGuardHelmet = new ItemStack(Items.IRON_HELMET);
        ItemStack royalGuardChestplate = new ItemStack(Items.IRON_CHESTPLATE);
        ItemStack mainhandWeapon = new ItemStack(Items.IRON_AXE);
        ItemStack offhandWeapon = new ItemStack(ModItems.ROYAL_GUARD_SHIELD.get());
        if(ModList.get().isLoaded("dungeons_gear")){
            Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));
            Item ROYAL_GUARD_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_helmet"));
            Item ROYAL_GUARD_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_chestplate"));

            royalGuardHelmet = new ItemStack(ROYAL_GUARD_HELMET);
            royalGuardChestplate = new ItemStack(ROYAL_GUARD_CHESTPLATE);
            mainhandWeapon = new ItemStack(MACE);
        }
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = (int) (3 + (waveAmount / 2.5));

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        this.setItemSlot(EquipmentSlotType.OFFHAND, offhandWeapon);
        this.setItemSlot(EquipmentSlotType.HEAD, royalGuardHelmet);
        this.setItemSlot(EquipmentSlotType.CHEST, royalGuardChestplate);
    }

    private float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (!this.level.isClientSide && this.attackID == 0) {
            this.attackID = MELEE_ATTACK;
        }
        return true;
    }


    private float getAttackKnockback() {
        return (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
    }


    private void setAttackID(int id) {
        this.attackID = id;
        this.attackTimer = 0;
        this.level.broadcastEntityEvent(this, (byte) -id);
    }


    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.attackID = Math.abs(id);
            this.attackTimer = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getMobType() == CreatureAttribute.ILLAGER
                || entityIn instanceof AbstractRaiderEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller2", 0, this::predicate2));
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (isMeleeAttacking()) {
            if (this.attackType <= 1) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_attack", false));
            }else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_attack_2", false));
            }
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_walk", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_walk", true));
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_idel", true));
        }
        return PlayState.CONTINUE;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private <P extends IAnimatable> PlayState predicate2(AnimationEvent<P> event) {
        if (this.getOffhandItem().isShield(this)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_shield", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_no_shield", true));
        }
        return PlayState.CONTINUE;
    }

    class AttackGoal extends MeleeAttackGoal {
        private int maxAttackTimer = 20;
        private final double moveSpeed;
        private int delayCounter;
        private int attackTimer;

        public AttackGoal(CreatureEntity creatureEntity, double moveSpeed) {
            super(creatureEntity, moveSpeed, true);
            this.moveSpeed = moveSpeed;
        }

        @Override
        public boolean canUse() {
            return TowerGuardEntity.this.getTarget() != null && TowerGuardEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            TowerGuardEntity.this.setAggressive(true);
            this.delayCounter = 0;
            TowerGuardEntity.this.attackType = TowerGuardEntity.this.getRandom().nextInt(5);
        }

        @Override
        public void tick() {
            LivingEntity livingentity = TowerGuardEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            TowerGuardEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + TowerGuardEntity.this.getRandom().nextInt(7);
                TowerGuardEntity.this.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, TowerGuardEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || TowerGuardEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                TowerGuardEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            TowerGuardEntity.this.getNavigation().stop();
            if (TowerGuardEntity.this.getTarget() == null) {
                TowerGuardEntity.this.setAggressive(false);
            }
        }

        public TowerGuardEntity.AttackGoal setMaxAttackTick(int max) {
            this.maxAttackTimer = max;
            return this;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (this.getOffhandItem().isShield(this)) {
                return SoundEvents.SHIELD_BLOCK;
        }else {
            return SoundEvents.VINDICATOR_HURT;
        }
    }

    class MeleeGoal extends Goal {
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return TowerGuardEntity.this.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 25;
        }

        @Override
        public void start() {
            setAttackID(MELEE_ATTACK);
            setMeleeAttacking(true);
        }

        @Override
        public void stop() {
            setAttackID(0);
            setMeleeAttacking(false);
            TowerGuardEntity.this.attackTimer = 0;
        }

        @Override
        public void tick() {
            if (TowerGuardEntity.this.getTarget() != null && TowerGuardEntity.this.getTarget().isAlive()) {
                TowerGuardEntity.this.getLookControl().setLookAt(TowerGuardEntity.this.getTarget(), 30.0F, 30.0F);
                if (attackTimer == 15 && TowerGuardEntity.this.distanceToSqr(TowerGuardEntity.this.getTarget()) <= 15.0+ TowerGuardEntity.this.getTarget().getBbWidth()) {
                    float attackKnockback = TowerGuardEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = TowerGuardEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(TowerGuardEntity.this.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(TowerGuardEntity.this.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(TowerGuardEntity.this), (float) TowerGuardEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    TowerGuardEntity.this.setDeltaMovement(TowerGuardEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }

        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if (event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float) ((double) strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double) strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
        if (this.getOffhandItem().isShield(this)){
            this.startUsingItem(Hand.OFF_HAND);
        }else {
            this.releaseUsingItem();
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new PatrollerEntity.PatrolGoal<>(this,1.42,1.3));
        this.targetSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(0, new TowerGuardEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new TowerGuardEntity.AttackGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.ARMOR_EQUIP_IRON, 0.6f, 1);
    }

    @Override
    protected void hurtCurrentlyUsedShield(float amount) {
        if (this.useItem.isShield(this)) {
            if (amount >= 3.0F) {
                int i = (int) (1 + MathHelper.floor(amount) * 10);
                Hand hand = this.getUsedItemHand();
                this.useItem.hurtAndBreak(i, this, (royalGuardEntity) -> {
                    royalGuardEntity.broadcastBreakEvent(hand);
                    // Forge would have called onPlayerDestroyItem here
                });
                if (this.useItem.isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                    }

                    this.useItem = ItemStack.EMPTY;
                    this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
                }
            }
        }
    }
}