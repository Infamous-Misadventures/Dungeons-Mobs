package com.infamous.dungeons_mobs.entities.allcustomentity.illagers.bosses;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
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
import java.util.Map;

public class AxeWarriorEntity extends AbstractIllagerEntity implements IAnimatable {

    public int S;
    public int SS;
    public static final DataParameter<Integer> CASTING_TICKS = EntityDataManager.defineId(AxeWarriorEntity.class, DataSerializers.INT);
    private boolean isJohnny;
    private int attackTimer;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(AxeWarriorEntity.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> TPMELEEATTACKING = EntityDataManager.defineId(AxeWarriorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(AxeWarriorEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);


    public AxeWarriorEntity(World world) {
        super(ModEntityTypes.AXE_WARRIOR.get(), world);
    }

    public AxeWarriorEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
        this.entityData.define(CASTING_TICKS, 0);
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(TPMELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 5.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.21D)
                .add(Attributes.MAX_HEALTH, 648.8D)
                .add(Attributes.ATTACK_DAMAGE, 21.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get()));
        }
    }

    public boolean isMeleeAttacking() {
        return this.entityData.get(MELEEATTACKING);
    }

    public void setMeleeAttacking(boolean attacking) {
        this.entityData.set(MELEEATTACKING, attacking);
    }

    public boolean isTpMeleeAttacking() {
        return this.entityData.get(TPMELEEATTACKING);
    }

    public void setTpMeleeAttacking(boolean attacking) {
        this.entityData.set(TPMELEEATTACKING, attacking);
    }

    public int getCastTicks() {
        return this.entityData.get(CASTING_TICKS);
    }

    public void setCastTicks(int p_189794_1_) {
        this.entityData.set(CASTING_TICKS, p_189794_1_);
    }


    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    private ItemStack getWeaponBasedOnMod() {
        ItemStack mainhandWeapon;
        if(ModList.get().isLoaded("dungeons_gear")){
            Item WHIRLWIND = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "whirlwind"));

            ItemStack whirlwind = new ItemStack(WHIRLWIND);

            mainhandWeapon = whirlwind;
        }
        else{
            mainhandWeapon = new ItemStack(Items.DIAMOND_AXE);
        }
        return mainhandWeapon;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float p_70097_2_) {
        if (damageSource != DamageSource.LIGHTNING_BOLT && damageSource != DamageSource.IN_FIRE && damageSource != DamageSource.ON_FIRE) {
            return super.hurt(damageSource, p_70097_2_);
        }else {
            return false;
        }
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = this.getWeaponBasedOnMod();
        ItemStack helmet = new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get());
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = 4;

        Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
        Map<Enchantment, Integer> armorEnchantmentMap = Maps.newHashMap();
        weaponEnchantmentMap.put(Enchantments.SHARPNESS, enchantmentLevel + 1);
        armorEnchantmentMap.put(Enchantments.ALL_DAMAGE_PROTECTION, enchantmentLevel);
            weaponEnchantmentMap.put(Enchantments.KNOCKBACK, enchantmentLevel - 2);
            weaponEnchantmentMap.put(Enchantments.FIRE_ASPECT, enchantmentLevel - 1);
            armorEnchantmentMap.put(Enchantments.PROJECTILE_PROTECTION,  enchantmentLevel);
            armorEnchantmentMap.put(Enchantments.BLAST_PROTECTION,  enchantmentLevel);
            armorEnchantmentMap.put(Enchantments.THORNS,  enchantmentLevel - 2);
        EnchantmentHelper.setEnchantments(weaponEnchantmentMap, mainhandWeapon);
        EnchantmentHelper.setEnchantments(armorEnchantmentMap, helmet);

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        this.setItemSlot(EquipmentSlotType.HEAD, helmet);
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
        if (this.getCastTicks() > 0) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("tp", false));
        }else if (isTpMeleeAttacking()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("tp_attack", false));
        }else if (isMeleeAttacking()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_warrior_attack", false));
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
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
            return AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            AxeWarriorEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = AxeWarriorEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            AxeWarriorEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + AxeWarriorEntity.this.getRandom().nextInt(7);
                AxeWarriorEntity.this.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, AxeWarriorEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || AxeWarriorEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                AxeWarriorEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            AxeWarriorEntity.this.getNavigation().stop();
            if (AxeWarriorEntity.this.getTarget() == null) {
                AxeWarriorEntity.this.setAggressive(false);
            }
        }

        public AxeWarriorEntity.AttackGoal setMaxAttackTick(int max) {
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
            return AxeWarriorEntity.this.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 60 && AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getTarget().isAlive();
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
            AxeWarriorEntity.this.attackTimer = 0;
            if (AxeWarriorEntity.this.getTarget() == null) {
                AxeWarriorEntity.this.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            if (AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getTarget().isAlive()) {
                AxeWarriorEntity.this.getLookControl().setLookAt(AxeWarriorEntity.this.getTarget(), 30.0F, 30.0F);
                if (attackTimer == 15 && AxeWarriorEntity.this.distanceToSqr(AxeWarriorEntity.this.getTarget()) <= 6.0D) {
                    float attackKnockback = AxeWarriorEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = AxeWarriorEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(AxeWarriorEntity.this.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(AxeWarriorEntity.this.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(AxeWarriorEntity.this), (float) AxeWarriorEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    AxeWarriorEntity.this.setDeltaMovement(AxeWarriorEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
                if (attackTimer == 42 && AxeWarriorEntity.this.distanceToSqr(AxeWarriorEntity.this.getTarget()) <= 6.0D) {
                    float attackKnockback = AxeWarriorEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = AxeWarriorEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(AxeWarriorEntity.this.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(AxeWarriorEntity.this.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    AxeWarriorEntity.this.getNavigation().moveTo(AxeWarriorEntity.this, 2.3);
                    attackTarget.hurt(DamageSource.mobAttack(AxeWarriorEntity.this), (float) (AxeWarriorEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0));
                    this.forceKnockback(attackTarget, attackKnockback * 8.5F, ratioX, ratioZ, knockbackReduction);
                    AxeWarriorEntity.this.setDeltaMovement(AxeWarriorEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
                if (attackTimer == 24 && AxeWarriorEntity.this.distanceToSqr(AxeWarriorEntity.this.getTarget()) <= 6.0D) {
                    float attackKnockback = AxeWarriorEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = AxeWarriorEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(AxeWarriorEntity.this.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(AxeWarriorEntity.this.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(AxeWarriorEntity.this), (float) (AxeWarriorEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    AxeWarriorEntity.this.setDeltaMovement(AxeWarriorEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
                if (attackTimer < 34 && AxeWarriorEntity.this.getTarget() != null) {
                    AxeWarriorEntity.this.getNavigation().moveTo(AxeWarriorEntity.this.getTarget(), 2.3);
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

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
        if (this.S > 0) {
            --this.S;
        }
        if (this.SS > 0) {
            --this.SS;
        }
        if (this.SS <= 0) {
            SS = 5 + this.getRandom().nextInt(50);
            AxeWarriorEntity v = this;
            if (v.getTarget() != null) {
                BlockPos pos = v.getTarget().blockPosition();
                LightningBoltEntity l = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, v.level);
                l.moveTo(pos, 0, 0);
                v.level.addFreshEntity(l);
            }
        }
        if (this.getCastTicks() > 0) {
            this.setCastTicks(this.getCastTicks() - 1);
        }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.remove();
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    @Override
    public boolean canJoinPatrol() {
        return true;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new PatrolGoal<>(this,1.42,1.3));
        this.targetSelector.addGoal(2, new FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(0, new AxeWarriorEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new AxeWarriorEntity.AttackGoal(this, 1.667D));
        this.goalSelector.addGoal(4, new AxeWarriorEntity.TpGoal());
        this.goalSelector.addGoal(0, new AxeWarriorEntity.TpMeleeGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.3D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, (new AxeWarriorEntity.JohnnyAttackGoal(this)));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public JohnnyAttackGoal(AxeWarriorEntity p_i47345_1_) {
            super(p_i47345_1_, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean canUse() {
            return ((AxeWarriorEntity) this.mob).isJohnny && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }

    class TpGoal extends Goal {


        @Override
        public boolean isInterruptable() {
            return false;
        }

        public TpGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public boolean canUse() {
            return AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getCastTicks() == 0 && AxeWarriorEntity.this.S == 0 && AxeWarriorEntity.this.random.nextInt(20) == 0 && AxeWarriorEntity.this.getHealth() < AxeWarriorEntity.this.getMaxHealth() * 0.7;
        }

        public boolean canContinueToUse() {
            return AxeWarriorEntity.this.getCastTicks() > 0;
        }



        public void start() {
            super.start();
            AxeWarriorEntity.this.S = 600;
            AxeWarriorEntity.this.setCastTicks(57);
            AxeWarriorEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, AxeWarriorEntity.this.getSoundVolume(), AxeWarriorEntity.this.getVoicePitch());
        }

        public void tick() {
            if (AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getTarget().isAlive()) {
                if (AxeWarriorEntity.this.getCastTicks() == 15){
                    AxeWarriorEntity v = AxeWarriorEntity.this;
                    if (v.distanceToSqr(v.getTarget()) <= 15) {
                        v.getTarget().hurt(DamageSource.mobAttack(AxeWarriorEntity.this), (float) (AxeWarriorEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0));
                    }
                    if (v.distanceToSqr(v.getTarget()) <= 15) {
                        v.getTarget().addEffect(new EffectInstance(Effects.HARM, 25, 1,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 4,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.WITHER, 120, 4,(false),(false)));
                    }
                    BlockPos pos = v.blockPosition();
                    LightningBoltEntity l = new LightningBoltEntity(EntityType.LIGHTNING_BOLT,v.level);
                    l.moveTo(pos,0,0);
                    v.level.addFreshEntity(l);
                }
                if (AxeWarriorEntity.this.getCastTicks() == 19) {
                    LivingEntity v = AxeWarriorEntity.this.getTarget();
                    AxeWarriorEntity.this.moveTo(v.getX(),v.getY(),v.getZ());
                }
            }
        }

        public void stop() {
            super.stop();
            AxeWarriorEntity.this.setCastTicks(0);
        }
    }


    class TpMeleeGoal extends Goal {
        public TpMeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return AxeWarriorEntity.this.getTarget() != null && attackID == 2;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 27;
        }

        @Override
        public void start() {
            setAttackID(2);
            setMeleeAttacking(true);
        }

        @Override
        public void stop() {
            setAttackID(0);
            setMeleeAttacking(false);
            AxeWarriorEntity.this.attackTimer = 0;
        }

        @Override
        public void tick() {
            if (AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getTarget().isAlive()) {
                if (attackTimer == 12){
                    AxeWarriorEntity v = AxeWarriorEntity.this;
                    if (v.distanceToSqr(v.getTarget()) <= 15) {
                        v.getTarget().hurt(DamageSource.mobAttack(AxeWarriorEntity.this), (float) (AxeWarriorEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.5));
                    }
                    BlockPos pos = v.blockPosition();
                    LightningBoltEntity l = new LightningBoltEntity(EntityType.LIGHTNING_BOLT,v.level);
                    l.moveTo(pos,0,0);
                    if (v.distanceToSqr(v.getTarget()) <= 15) {
                        v.getTarget().addEffect(new EffectInstance(Effects.HARM, 25, 0,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 200, 5,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 4,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.WITHER, 100, 5,(false),(false)));
                        v.getTarget().hurt(DamageSource.LIGHTNING_BOLT, 18);
                    }
                }else if (attackTimer < 10) {
                    LivingEntity v = AxeWarriorEntity.this.getTarget();
                    AxeWarriorEntity.this.moveTo(v.getX(),v.getY(),v.getZ());
                }
            }
        }
    }

    class TpAxeGoal extends Goal {
        public TpAxeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return AxeWarriorEntity.this.getTarget() != null && attackID == 2;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 27;
        }

        @Override
        public void start() {
            setAttackID(2);
            setTpMeleeAttacking(true);
        }

        @Override
        public void stop() {
            setAttackID(0);
            setTpMeleeAttacking(false);
            AxeWarriorEntity.this.attackTimer = 0;
        }

        @Override
        public void tick() {
            if (AxeWarriorEntity.this.getTarget() != null && AxeWarriorEntity.this.getTarget().isAlive()) {
                if (attackTimer == 12){
                    AxeWarriorEntity v = AxeWarriorEntity.this;
                    if (v.distanceToSqr(v.getTarget()) <= 15) {
                        v.getTarget().hurt(DamageSource.mobAttack(AxeWarriorEntity.this), (float) (AxeWarriorEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5));
                    }
                    BlockPos pos = v.blockPosition();
                    LightningBoltEntity l = new LightningBoltEntity(EntityType.LIGHTNING_BOLT,v.level);
                    l.moveTo(pos,0,0);
                    if (v.distanceToSqr(v.getTarget()) <= 15) {
                        v.getTarget().addEffect(new EffectInstance(Effects.HARM, 25, 1,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 4,(false),(false)));
                        v.getTarget().addEffect(new EffectInstance(Effects.WITHER, 120, 4,(false),(false)));
                    }
                }else if (attackTimer == 10) {
                    LivingEntity v = AxeWarriorEntity.this.getTarget();
                    AxeWarriorEntity.this.moveTo(v.getX(),v.getY(),v.getZ());
                }
            }
        }
    }

}