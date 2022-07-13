package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.DungeonsIllusionerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;

public class DungeonsVindicatorEntity extends AbstractIllagerEntity implements IAnimatable {
    private boolean isJohnny;
    private int attackTimer;
    private int T;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(DungeonsVindicatorEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        if (!this.isJohnny && p_200203_1_ != null && p_200203_1_.getString().equals("Johnny")) {
            this.isJohnny = true;
        }
    }

    public DungeonsVindicatorEntity(World world) {
        super(ModEntityTypes.VINDICATOR.get(), world);
    }

    public DungeonsVindicatorEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.273D)
                .add(Attributes.FOLLOW_RANGE, 36D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
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
        ItemStack mainhandWeapon = new ItemStack(Items.IRON_AXE);
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = (int) Math.min((1 + (waveAmount / 2.5)),3);

        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
        boolean applyEnchant = this.random.nextFloat() <= raid.getEnchantOdds();
        if (applyEnchant) {
            float e = this.getRandom().nextFloat();

            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, enchantmentLevel);

            if (e <= 0.05) {
                enchantmentIntegerMap.put(Enchantments.VANISHING_CURSE, 1);
            }

            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
        }

        if (waveAmount > raid.getNumGroups(Difficulty.EASY) && !(waveAmount > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 12.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.5D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 2.0D, AttributeModifier.Operation.ADDITION));
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

    public void applyEnchantment() {
        float o = this.getRandom().nextFloat();

        if (this.getRandom().nextInt(100) <= 10 || (this.level.getDifficulty().getId() == 2 && this.getRandom().nextInt(100) <= 40) || (this.level.getDifficulty().getId() == 3 && this.getRandom().nextInt(100) <= 75)) {
            ItemStack weapon = this.getMainHandItem();
            this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

            if ((this.getCurrentRaid() == null && o <= 0.05)) {
                Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
                weaponEnchantmentMap.put(Enchantments.SHARPNESS, this.getRandom().nextInt(3) + 1);
                if (o <= 0.05) {
                    weaponEnchantmentMap.put(Enchantments.KNOCKBACK, this.getRandom().nextInt(2) + 1);
                }
                EnchantmentHelper.setEnchantments(weaponEnchantmentMap, weapon);
            }
        }
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

    public boolean g;

    public void setG(boolean G) {
        this.g = G;
    }

    public boolean getG() {
        return this.g;
    }
    public boolean h;

    public void setH(boolean G) {
        this.h = G;
    }

    public boolean getH() {
        return this.h;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "f", 12, this::fre));
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (isMeleeAttacking()) {
            this.setG(false);
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack_run", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                this.setG(false);
                event.getController().animationSpeed = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            } else {
                this.setG(false);
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
            if ((this.getH())) {
                this.setG(false);
                event.getController().animationSpeed = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
            }
            else {
                event.getController().animationSpeed = 1;
                this.setG(true);
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel_look_around", true));
            }
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState fre(AnimationEvent<P> event) {
        if (this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("facialexpression.angry", true));
        }else if (this.getH()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("facialexpression.see", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("facialexpression.null", true));
        }
        return PlayState.CONTINUE;
    }

    class AttackGoal extends MeleeAttackGoal {
        private int maxAttackTimer = 30;
        private final double moveSpeed;
        private int delayCounter;
        private int sr;
        private int attackTimer;

        public DungeonsVindicatorEntity v = DungeonsVindicatorEntity.this;

        public AttackGoal(CreatureEntity creatureEntity, double moveSpeed) {
            super(creatureEntity, moveSpeed, true);
            this.moveSpeed = moveSpeed;
        }

        @Override
        public boolean canUse() {
            return DungeonsVindicatorEntity.this.getTarget() != null && DungeonsVindicatorEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            DungeonsVindicatorEntity.this.setAggressive(true);
            this.delayCounter = 0;
            //this.attackTimer = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = DungeonsVindicatorEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }
                if (this.attackTimer <= 0) {
                    DungeonsVindicatorEntity.this.setAggressive(true);
                    DungeonsVindicatorEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

                    if (--this.delayCounter <= 0) {
                        this.delayCounter = 4 + DungeonsVindicatorEntity.this.getRandom().nextInt(7);
                        DungeonsVindicatorEntity.this.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
                    }

                    this.attackTimer = Math.max(this.attackTimer - 1, 0);
                    this.checkAndPerformAttack(livingentity, DungeonsVindicatorEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
                } else {
                    if (--this.delayCounter <= 0) {
                        v.setAggressive(false);
                        this.delayCounter = 4 + DungeonsVindicatorEntity.this.getRandom().nextInt(7);
                        v.getNavigation().moveTo(
                                livingentity.getX() + (v.getRandom().nextInt(3) + 6) * (v.getRandom().nextBoolean() ? 6.14 : -6.14),
                                livingentity.getY(),
                                livingentity.getZ() + (v.getRandom().nextInt(3) + 6) * (v.getRandom().nextBoolean() ? 6.14 : -6.14),
                                (double) this.moveSpeed * 0.851
                        );
                        if (--this.sr <= 0) {
                                if (v.distanceToSqr(livingentity) >= 15 + livingentity.getBbWidth()) {
                                    this.attackTimer = 0;
                                }
                                v.getNavigation().stop();
                        }
                    }
                    this.sr = (int) Math.max(this.sr - 1, 0);
                    this.attackTimer = (int) Math.max(this.attackTimer - 1, 0);
                }
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || DungeonsVindicatorEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = 45;
                this.sr = 2;
                this.delayCounter = 0;
                DungeonsVindicatorEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            DungeonsVindicatorEntity.this.getNavigation().stop();
            if (DungeonsVindicatorEntity.this.getTarget() == null) {
                DungeonsVindicatorEntity.this.setAggressive(false);
            }
        }

        public DungeonsVindicatorEntity.AttackGoal setMaxAttackTick(int max) {
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

        public DungeonsVindicatorEntity v = DungeonsVindicatorEntity.this;

        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null && v.attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return v.attackTimer < 21 && (v.getTarget() != null && v.getTarget().isAlive());
        }

        @Override
        public void start() {
            v.setAttackID(MELEE_ATTACK);
            v.setMeleeAttacking(true);
            v.playSound(ModSoundEvents.VINDICATOR_ATTACK.get(), v.getSoundVolume(), v.getVoicePitch());
        }

        @Override
        public void stop() {
            v.setAttackID(0);
            v.setMeleeAttacking(false);
            v.attackTimer = 0;
            if (v.getTarget() == null) {
                v.setAggressive(false);
            }
            v.setDeltaMovement(v.getDeltaMovement().multiply(-1D, 1.0D, -1D));
        }

        @Override
        public void tick() {
            if (v.getTarget() != null && v.getTarget().isAlive()) {
                v.getNavigation().moveTo(v.getTarget(), 2.1);
                v.getLookControl().setLookAt(v.getTarget(), 30.0F, 30.0F);
                //v.setDeltaMovement(v.getDeltaMovement().multiply(1D, 0.35D, 1D));
                if (v.attackTimer == 15 && v.distanceToSqr(v.getTarget()) <= 6.0D+ v.getTarget().getBbWidth()) {
                    float attackKnockback = v.getAttackKnockback();
                    LivingEntity attackTarget = v.getTarget();
                    double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    v.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, v.getSoundVolume(), v.getVoicePitch());
                    attackTarget.hurt(DamageSource.mobAttack(v), ((float)(
                            v.getAttributeValue(Attributes.ATTACK_DAMAGE) +
                            EnchantmentHelper.getDamageBonus(v.getMainHandItem(), ((LivingEntity)attackTarget).getMobType()))) * (1 + (v.getRandom().nextFloat() / 2))
                    );
                    attackTarget.setSecondsOnFire((int) (EnchantmentHelper.getFireAspect(v) * 5.5));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
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

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.applyEnchantment();

        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    @Override
    public boolean canJoinPatrol() {
        return true;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new DungeonsVindicatorEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new DungeonsVindicatorEntity.AttackGoal(this, 1.28D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, (new DungeonsVindicatorEntity.JohnnyAttackGoal(this)));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new FindTargetGoal(this,10F));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public JohnnyAttackGoal(DungeonsVindicatorEntity p_i47345_1_) {
            super(p_i47345_1_, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean canUse() {
            return ((DungeonsVindicatorEntity) this.mob).isJohnny && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }

}