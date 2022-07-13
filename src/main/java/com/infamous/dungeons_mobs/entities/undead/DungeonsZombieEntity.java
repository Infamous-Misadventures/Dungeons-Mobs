package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
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

import java.util.EnumSet;

public class DungeonsZombieEntity extends MonsterEntity implements IAnimatable {
    private int attackTimer;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(DungeonsZombieEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> HURT_TIME_TIMER = EntityDataManager.defineId(DungeonsZombieEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IS_VILLAGER = EntityDataManager.defineId(DungeonsZombieEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public boolean isBaby() {
        return false;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public DungeonsZombieEntity(World world) {
        super(ModEntityTypes.ZOMBIE.get(), world);
    }

    public DungeonsZombieEntity(EntityType<? extends MonsterEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(IS_VILLAGER, false);
        this.entityData.define(HURT_TIME_TIMER, 0);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.16D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        this.setHurtTimeTimer(8);
        return super.hurt(p_70097_1_, p_70097_2_);
    }

    public boolean isMeleeAttacking() {
        return this.entityData.get(MELEEATTACKING);
    }

    public void setMeleeAttacking(boolean attacking) {
        this.entityData.set(MELEEATTACKING, attacking);
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

    public boolean getIsVillager() {
        return this.entityData.get(IS_VILLAGER);
    }

    public void setIsVillager(boolean t) {
        this.entityData.set(IS_VILLAGER, t);
    }
    public int gethurt() {
        return this.entityData.get(HURT_TIME_TIMER);
    }

    public void setHurtTimeTimer(int t) {
        this.entityData.set(HURT_TIME_TIMER, t);
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.zombie.attack", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
                event.getController().animationSpeed = 0.75;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("zombie_walk", true));
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("zombie.idel", true));
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState hurte(AnimationEvent<P> event) {
        if (this.gethurt() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("zombie.hurt", true));
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
            return DungeonsZombieEntity.this.getTarget() != null && DungeonsZombieEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            DungeonsZombieEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = DungeonsZombieEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            DungeonsZombieEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + DungeonsZombieEntity.this.getRandom().nextInt(7);
                DungeonsZombieEntity.this.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, DungeonsZombieEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || DungeonsZombieEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                DungeonsZombieEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            DungeonsZombieEntity.this.getNavigation().stop();
            if (DungeonsZombieEntity.this.getTarget() == null) {
                DungeonsZombieEntity.this.setAggressive(false);
            }
        }

        public DungeonsZombieEntity.AttackGoal setMaxAttackTick(int max) {
            this.maxAttackTimer = max;
            return this;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ZOMBIE_HURT;
    }

    class MeleeGoal extends Goal {
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return DungeonsZombieEntity.this.getTarget() != null && attackID == MELEE_ATTACK;
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
            DungeonsZombieEntity.this.attackTimer = 0;
            if (DungeonsZombieEntity.this.getTarget() == null) {
                DungeonsZombieEntity.this.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            if (DungeonsZombieEntity.this.getTarget() != null && DungeonsZombieEntity.this.getTarget().isAlive()) {
                DungeonsZombieEntity.this.getLookControl().setLookAt(DungeonsZombieEntity.this.getTarget(), 30.0F, 30.0F);
                if (attackTimer == 15 && DungeonsZombieEntity.this.distanceToSqr(DungeonsZombieEntity.this.getTarget()) <= 6.0D) {
                    float attackKnockback = DungeonsZombieEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = DungeonsZombieEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(DungeonsZombieEntity.this.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(DungeonsZombieEntity.this.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(DungeonsZombieEntity.this), (float) DungeonsZombieEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    DungeonsZombieEntity.this.setDeltaMovement(DungeonsZombieEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                    /*
                    if (!DungeonsZombieEntity.this.getTarget().isAlive() && DungeonsZombieEntity.this.getTarget() instanceof AbstractVillagerEntity) {
                        BlockPos b = DungeonsZombieEntity.this.getTarget().blockPosition();
                        DungeonsZombieEntity vv = new DungeonsZombieEntity(DungeonsZombieEntity.this.level);
                        vv.moveTo(b, 0F, 0F);
                        vv.setIsVillager(true);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        DungeonsZombieEntity.this.level.addFreshEntity(vv);
                    }
                     */

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

    protected boolean isSunSensitive() {
        return true;
    }

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                            this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setSecondsOnFire(8);
                }
            }
        }
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
        if (this.gethurt() > 0) {
            this.setHurtTimeTimer(this.gethurt() - 1);
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new DungeonsZombieEntity.MeleeGoal());
        this.goalSelector.addGoal(5, new DungeonsZombieEntity.AttackGoal(this, 1.15D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglinEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

}