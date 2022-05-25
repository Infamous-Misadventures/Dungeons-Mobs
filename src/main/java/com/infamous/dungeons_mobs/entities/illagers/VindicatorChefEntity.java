package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import java.util.EnumSet;
import java.util.Map;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;
import net.minecraft.world.spawner.WorldEntitySpawner;
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

public class VindicatorChefEntity extends AbstractIllagerEntity implements IAnimatable {
    private boolean isJohnny;
    private int attackTimer;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(VindicatorChefEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(VindicatorChefEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);

    public VindicatorChefEntity(World worldIn){
        super(ModEntityTypes.VINDICATOR_CHEF.get(), worldIn);
    }

    public VindicatorChefEntity(EntityType<? extends VindicatorChefEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
        this.entityData.define(MELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.21D)
                .add(Attributes.MAX_HEALTH, 28.8D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
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
        ItemStack mainhandWeapon = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = 1;
        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL)) {
            enchantmentLevel = 2;
        }

        boolean applyEnchant = this.random.nextFloat() <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, Integer.valueOf(enchantmentLevel));
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
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

    public boolean checkSpawnObstruction(IWorldReader worldIn) {
        BlockPos golemPos = this.blockPosition();
        BlockPos posBeneathGolem = golemPos.below();
        BlockState blockstateBeneathGolem = worldIn.getBlockState(posBeneathGolem);
        if (!blockstateBeneathGolem.entityCanStandOn(worldIn, posBeneathGolem, this)) {
            return false;
        } else {
            for (int i = 1; i < 4; ++i) {
                BlockPos posAboveGolem = golemPos.above(i);
                BlockState blockstateAboveGolem = worldIn.getBlockState(posAboveGolem);
                if (!WorldEntitySpawner
                        .isValidEmptySpawnBlock(worldIn,
                                posAboveGolem,
                                blockstateAboveGolem,
                                blockstateAboveGolem.getFluidState(),
                                ModEntityTypes.MOUNTAINEER.get())) {
                    return false;
                }
            }

            return WorldEntitySpawner
                    .isValidEmptySpawnBlock(worldIn,
                            golemPos,
                            worldIn.getBlockState(golemPos),
                            Fluids.EMPTY.defaultFluidState(),
                            ModEntityTypes.MOUNTAINEER.get())
                    && worldIn.isUnobstructed(this);
        }
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
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
            return VindicatorChefEntity.this.getTarget() != null && VindicatorChefEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            VindicatorChefEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = VindicatorChefEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            VindicatorChefEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + VindicatorChefEntity.this.getRandom().nextInt(7);
                VindicatorChefEntity.this.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, VindicatorChefEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || VindicatorChefEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                VindicatorChefEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            VindicatorChefEntity.this.getNavigation().stop();
            if (VindicatorChefEntity.this.getTarget() == null) {
                VindicatorChefEntity.this.setAggressive(false);
            }
        }

        public VindicatorChefEntity.AttackGoal setMaxAttackTick(int max) {
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
            return VindicatorChefEntity.this.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 28;
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
            VindicatorChefEntity.this.attackTimer = 0;
        }

        @Override
        public void tick() {
            if (VindicatorChefEntity.this.getTarget() != null && VindicatorChefEntity.this.getTarget().isAlive()) {
                VindicatorChefEntity.this.getLookControl().setLookAt(VindicatorChefEntity.this.getTarget(), 30.0F, 30.0F);
                if (attackTimer == 15) {
                    float attackKnockback = VindicatorChefEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = VindicatorChefEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(VindicatorChefEntity.this.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(VindicatorChefEntity.this.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(VindicatorChefEntity.this), (float) VindicatorChefEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    VindicatorChefEntity.this.setDeltaMovement(VindicatorChefEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
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
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new VindicatorChefEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new VindicatorChefEntity.AttackGoal(this, 1.667D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public JohnnyAttackGoal(VindicatorChefEntity p_i47345_1_) {
            super(p_i47345_1_, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean canUse() {
            return ((VindicatorChefEntity) this.mob).isJohnny && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }

}
