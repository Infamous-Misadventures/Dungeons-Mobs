package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.CombatEvent;
import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class MountaineerEntity extends VindicatorEntity implements IAnimatable {

    public static final DataParameter<Boolean> CAN_MELEE_ATTACK = EntityDataManager.defineId(MountaineerEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> MELEE_ATTACK = EntityDataManager.defineId(MountaineerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(MountaineerEntity.class, DataSerializers.BYTE);

    private int timer;

    AnimationFactory factory = new AnimationFactory(this);
    public MountaineerEntity(World worldIn){
        super(ModEntityTypes.MOUNTAINEER.get(), worldIn);
    }

    public MountaineerEntity(EntityType<? extends MountaineerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        ((GoalSelectorAccessor)this.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 4 && pg.getGoal() instanceof MeleeAttackGoal);

        this.goalSelector.addGoal(3, new MountaineerAttackGoal(this, 1.05D));
        this.goalSelector.addGoal(0, new BasicAttackGoal(this));

    }

    protected PathNavigator createNavigation(World p_175447_1_) {
        return new ClimberPathNavigator(this, p_175447_1_);
     }

     protected void defineSynchedData() {
        super.defineSynchedData();
         this.entityData.define(DATA_FLAGS_ID, (byte)0);
         this.entityData.define(MELEE_ATTACK, false);
         this.entityData.define(CAN_MELEE_ATTACK, false);
     }

     public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
           this.setClimbing(this.horizontalCollision);
        }

     }
     
     public boolean onClimbable() {
         return this.isClimbing();
      }
     
     public boolean isClimbing() {
         return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
      }

      public void setClimbing(boolean p_70839_1_) {
         byte b0 = this.entityData.get(DATA_FLAGS_ID);
         if (p_70839_1_) {
            b0 = (byte)(b0 | 1);
         } else {
            b0 = (byte)(b0 & -2);
         }

         this.entityData.set(DATA_FLAGS_ID, b0);
      }
      
    protected float getSoundVolume() {
    	return 0.5F;
    }
    
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
     }
    
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
     }

     protected SoundEvent getDeathSound() {
        return ModSoundEvents.MOUNTAINEER_DEATH.get();
     }

     protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.MOUNTAINEER_HURT.get();
     }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getHead().get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getChest().get()));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getLegs().get()));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getFeet().get()));
        if(this.getCurrentRaid() == null){
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
        }
    }

    public void applyRaidBuffs(int waveNumber, boolean bool) {
        ItemStack itemStack = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (raid != null && waveNumber > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = false;
        if (raid != null) {
            flag = this.random.nextFloat() <= raid.getEnchantOdds();
        }
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemStack);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, itemStack);
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 6, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimationSpeed(1);

        Vector3d velocity = this.getDeltaMovement();
        float groundSpeed = MathHelper.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));

        if (this.isMeleeAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack", true));
        } else if (this.isClimbing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("climbing", true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (this.isAggressive()) {
                event.getController().setAnimationSpeed(groundSpeed * 10);
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }else {
                event.getController().setAnimationSpeed(groundSpeed * 20);
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        this.entityData.set(CAN_MELEE_ATTACK, true);
        return true;
    }

    static class MountaineerAttackGoal extends MeleeAttackGoal {
        private final double moveSpeed;
        private int delayCounter;
        private int sr;
        private int attackTimer;

        public MountaineerAttackGoal(CreatureEntity p_i1636_1_, double p_i1636_2_) {
            super(p_i1636_1_, p_i1636_2_, true);
            this.moveSpeed = p_i1636_2_;
        }

        @Override
        public void start() {
            this.mob.setAggressive(true);
            this.delayCounter = 0;
            //this.attackTimer = 0;
        }
        @Override
        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return;
            }
            this.sr = (int) Math.max(this.sr - 1, 0);
            if (this.attackTimer <= 0) {
                this.mob.setAggressive(true);
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);

                if (--this.delayCounter <= 0) {
                    this.delayCounter = 4 + this.mob.getRandom().nextInt(7);
                    this.mob.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
                }

                this.attackTimer = Math.max(this.attackTimer - 1, 0);
                this.checkAndPerformAttack(livingentity, this.mob.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
            } else {
                if (--this.delayCounter <= 0) {
                    this.mob.setAggressive(false);
                    this.delayCounter = 35 + this.mob.getRandom().nextInt(10);
                    this.mob.getNavigation().moveTo(
                            livingentity.getX() + (this.mob.getRandom().nextInt(3) + 6) * (this.mob.getRandom().nextBoolean() ? 2.14 : -2.14),
                            livingentity.getY(),
                            livingentity.getZ() + (this.mob.getRandom().nextInt(3) + 6) * (this.mob.getRandom().nextBoolean() ? 2.14 : -2.14),
                            (double) this.moveSpeed * 0.9
                    );
                    if (this.sr > 0) {
                        if (this.mob.distanceToSqr(livingentity) >= 15 + livingentity.getBbWidth()) {
                            this.attackTimer = 0;
                            if (this.mob.distanceToSqr(livingentity) <= 50 + livingentity.getBbWidth()) {
                                this.mob.doHurtTarget(livingentity);
                            }
                        }
                        this.mob.getNavigation().stop();
                        this.delayCounter = 0;
                    }
                }
                if (this.mob.hurtTime > 0) {
                    this.attackTimer = 0;
                }
                // this.sr = (int) Math.max(this.sr - 1, 0);
                this.attackTimer = (int) Math.max(this.attackTimer - 1, 0);
                if (this.attackTimer == 0) {
                    this.delayCounter = 0;
                }
            }
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr+5 <= this.getAttackReachSqr(enemy) || this.mob.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = 50;
                this.sr = 15;
                this.delayCounter = 0;
                ((MountaineerEntity)this.mob).setCanMeleeAttack(true);
            }
        }
    }

    static class BasicAttackGoal extends Goal {

        public MountaineerEntity mob;
        @Nullable
        public LivingEntity target;

        public BasicAttackGoal(MountaineerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();
            return target != null && mob.canMeleeAttack()
                    && mob.canSee(target);
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && mob.timer <= 23;
        }

        @Override
        public void start() {
            mob.timer = 0;
            mob.setMeleeAttack(true);
            mob.setCanMeleeAttack(false);
            mob.level.broadcastEntityEvent(mob, (byte) 4);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            mob.timer ++;
            if (mob.timer == 17) {
                mob.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,1,1);
                CombatEvent.AreaAttack(mob, 3.2f,3.2f,1f,3.2f,120f,1.0f,0.0f,0.675f);
            }
        }

        @Override
        public void stop() {
            if (target != null && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob) && mob.random.nextInt(6) == 0) {
                mob.startUsingItem(Hand.OFF_HAND);
            }
            mob.setMeleeAttack(false);
            mob.setCanMeleeAttack(false);
        }

    }

    public boolean canMeleeAttack() {
        return this.entityData.get(CAN_MELEE_ATTACK);
    }

    public void setCanMeleeAttack(boolean v) {
        this.entityData.set(CAN_MELEE_ATTACK, v);
    }

    public boolean isMeleeAttack() {
        return this.entityData.get(MELEE_ATTACK);
    }

    public void setMeleeAttack(boolean v) {
        this.entityData.set(MELEE_ATTACK, v);
    }
}
