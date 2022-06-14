package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
import javax.xml.soap.Text;
import java.util.EnumSet;
import java.util.Map;

public class ArmoredMountaineerEntity extends AbstractIllagerEntity implements IAnimatable {
    private boolean isJohnny;
    private int attackTimer;
    private int T;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(ArmoredMountaineerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(ArmoredMountaineerEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);


    public ArmoredMountaineerEntity(World world) {
        super(ModEntityTypes.ARMORED_MOUNTAINEER.get(), world);
    }

    public ArmoredMountaineerEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
        this.entityData.define(MELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.21D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.MAX_HEALTH, 48.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.25D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            if (this.isDiamond()) {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get()));
            } else {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.GOLD_MOUNTAINEER_AXE.get()));
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
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.isDiamond()) {
            compound.putBoolean("Diamond", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Diamond", 99)) {
            this.setDiamond(compound.getBoolean("Diamond"));
        }
        if (compound.contains("Johnny", 99)) {
            this.isJohnny = compound.getBoolean("Johnny");
        }
    }

    public boolean isDiamond() {
        return this.entityData.get(IS_DIAMOND);
    }


    public void setDiamond(boolean isDiamond) {
        this.entityData.set(IS_DIAMOND, isDiamond);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        populateDefaultEquipmentSlots(difficultyIn);

        float diamondChance = random.nextFloat();
        if (diamondChance < 0.25F) {
            this.setDiamond(true);
        }

        if (this.isDiamond()) {
            this.applyDiamondArmorBoosts();
            if (reason != SpawnReason.SPAWN_EGG && reason != SpawnReason.SPAWNER) {
                for (int i = 0; i < 5; ++i) {
                    ArmoredMountaineerEntity v = this;
                    BlockPos vvf = v.blockPosition();
                    int o = v.getRandom().nextInt(6);
                    {
                        MountaineerEntity vv = new MountaineerEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.populateDefaultEquipmentSlots(difficultyIn);
                        vv.setCurrentRaid(v.getCurrentRaid());
                        vv.setCanJoinRaid(true);
                        v.level.addFreshEntity(vv);
                    }
                }
            }
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    private void applyDiamondArmorBoosts() {
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Diamond health boost", 12.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("Diamond attack knockback boost", 1.75D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Diamond armor boost", 4.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Diamond knockback resistance boost", 2.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Diamond attack boost", 3.0D, AttributeModifier.Operation.ADDITION));
    }



    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = this.isDiamond() ?
                new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get()) :
                new ItemStack(ModItems.GOLD_MOUNTAINEER_AXE.get());

        int enchantmentLevel = (int) (4 + (waveAmount / 1.5));
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

        boolean applyEnchant = this.random.nextFloat() + 0.15 <= raid.getEnchantOdds();
        if (applyEnchant) {
            float e = this.getRandom().nextFloat();

            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, enchantmentLevel);

            if (e <= 0.05) {
                enchantmentIntegerMap.put(Enchantments.VANISHING_CURSE, 1);
            }

            if (e <= 0.65) {
                enchantmentIntegerMap.put(Enchantments.KNOCKBACK, (int) (enchantmentLevel / 2.5));
            }

            if (e <= 0.35) {
                enchantmentIntegerMap.put(Enchantments.FIRE_ASPECT, (int) (enchantmentLevel / 2.5));
            }

            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
        }
        if (this.isDiamond()) {
            for (int i = 0; i < 8; ++i) {
                ArmoredMountaineerEntity v = this;
                BlockPos vvf = v.blockPosition();
                int o = v.getRandom().nextInt(6);
                {
                    MountaineerEntity vv = new MountaineerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.setCanJoinRaid(true);
                    vv.setCurrentRaid(v.getCurrentRaid());
                    vv.applyRaidBuffs(waveAmount, b);
                    v.level.addFreshEntity(vv);
                }
            }
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 12.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.5D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 3.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 3.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.EASY) && !(waveAmount > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 3.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 4.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 10.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 2.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR_TOUGHNESS).addPermanentModifier(new AttributeModifier("armor toughness boost", 2.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.35D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 8.0D, AttributeModifier.Operation.ADDITION));
        }

        if (this.getRandom().nextInt(100) <= 6){
            for (int e = 0; e < 7; e++) {
                MountaineerEntity vv = new MountaineerEntity(this.level);
                vv.moveTo(this.blockPosition(), 0F, 0F);
                vv.setCanJoinRaid(true);
                vv.setCurrentRaid(this.getCurrentRaid());
                vv.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
                this.level.addFreshEntity(vv);
            }
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);

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
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (isMeleeAttacking()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack_run", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                event.getController().animationSpeed = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            } else {
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
            event.getController().animationSpeed = 1;
            if (!(this.getYHeadRot() <= 10 && this.getYHeadRot() >= -10) && !this.getLookControl().isHasWanted())
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
            else
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel_look_around", true));
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
            return ArmoredMountaineerEntity.this.getTarget() != null && ArmoredMountaineerEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            ArmoredMountaineerEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = ArmoredMountaineerEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            ArmoredMountaineerEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + ArmoredMountaineerEntity.this.getRandom().nextInt(7);
                ArmoredMountaineerEntity.this.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, ArmoredMountaineerEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || ArmoredMountaineerEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                ArmoredMountaineerEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            ArmoredMountaineerEntity.this.getNavigation().stop();
            if (ArmoredMountaineerEntity.this.getTarget() == null) {
                ArmoredMountaineerEntity.this.setAggressive(false);
            }
        }

        public ArmoredMountaineerEntity.AttackGoal setMaxAttackTick(int max) {
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
        return SoundEvents.VINDICATOR_HURT;
    }

    class MeleeGoal extends Goal {
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            ArmoredMountaineerEntity v = ArmoredMountaineerEntity.this;
            return v.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            ArmoredMountaineerEntity v = ArmoredMountaineerEntity.this;
            //animation tick
            return v.attackTimer < 21 && (v.getTarget() != null && v.getTarget().isAlive());
        }

        @Override
        public void start() {
            ArmoredMountaineerEntity v = ArmoredMountaineerEntity.this;
            v.setAttackID(MELEE_ATTACK);
            v.setMeleeAttacking(true);
        }

        @Override
        public void stop() {
            ArmoredMountaineerEntity v = ArmoredMountaineerEntity.this;
            v.setAttackID(0);
            v.setMeleeAttacking(false);
            v.attackTimer = 0;
            if (ArmoredMountaineerEntity.this.getTarget() == null) {
                v.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            ArmoredMountaineerEntity v = ArmoredMountaineerEntity.this;
            if (v.getTarget() != null && v.getTarget().isAlive()) {
                v.getNavigation().moveTo(v.getTarget(), 2.3);
                v.getLookControl().setLookAt(v.getTarget(), 30.0F, 30.0F);
                if (v.attackTimer == 15 && v.distanceToSqr(v.getTarget()) <= 6.0D) {
                    float attackKnockback = v.getAttackKnockback();
                    LivingEntity attackTarget = v.getTarget();
                    double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    v.setDeltaMovement(v.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }

        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if(event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
        if (this.T != 200 && this.getYHeadRot() == 0) {
            ++this.T;
        }else {
            this.T = 0;
        }
    }

    @Override
    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        if (!this.isJohnny && p_200203_1_ != null && p_200203_1_.getString().equals("Johnny")) {
            this.isJohnny = true;
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new PatrollerEntity.PatrolGoal<>(this,1.42,1.3));
        this.targetSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(6, new AbstractRaiderEntity.FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(0, new ArmoredMountaineerEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new ArmoredMountaineerEntity.AttackGoal(this, 1.667D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.3D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(5, (new JohnnyAttackGoal(this)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public JohnnyAttackGoal(ArmoredMountaineerEntity p_i47345_1_) {
            super(p_i47345_1_, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean canUse() {
            return ((ArmoredMountaineerEntity) this.mob).isJohnny && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }

}