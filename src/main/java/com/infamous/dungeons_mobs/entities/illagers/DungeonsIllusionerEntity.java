package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.ModProjectileHelper;
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
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

public class DungeonsIllusionerEntity extends SpellcastingIllagerEntity implements IAnimatable,IRangedAttackMob {

   public static final DataParameter<Integer> SHOT_TICK = EntityDataManager.defineId(DungeonsIllusionerEntity.class, DataSerializers.INT);
   public static final DataParameter<Integer> SUMMON_CLONE_TICK = EntityDataManager.defineId(DungeonsIllusionerEntity.class, DataSerializers.INT);
   public static final DataParameter<Boolean> SHOT_OR_SUMMON_BOOLEAN = EntityDataManager.defineId(DungeonsIllusionerEntity.class, DataSerializers.BOOLEAN);

   public int liftInterval = 0;
   public int duplicateInterval = 0;

   AnimationFactory factory = new AnimationFactory(this);

   public DungeonsIllusionerEntity(World world){
      super(ModEntityTypes.ILLUSIONER.get(), world);
   }

   public DungeonsIllusionerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
      super(type, world);
   }

   @Override
   public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
      if (!(p_70097_1_.getDirectEntity() == this) && !(p_70097_1_.getDirectEntity() instanceof IllusionerCloneEntity) && !(p_70097_1_.isExplosion())) {
         this.duplicateInterval = (this.duplicateInterval - 10 - this.getRandom().nextInt(20));
         return super.hurt(p_70097_1_, p_70097_2_);
      }else
         return false;
   }

   @Override
   public boolean canBeLeader() {
      return false;
   }

   protected void registerGoals() {
      super.registerGoals();
      this.targetSelector.addGoal(2, new FindTargetGoal(this, 10F));
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(5, new DungeonsIllusionerEntity.RunAvoidGoal(this,1));
      this.goalSelector.addGoal(1, new DungeonsIllusionerEntity.CastingSpellGoal());
      //this.goalSelector.addGoal(1, new DungeonsIllusionerEntity.RunAvoidGoal(this, 1.1));
      this.goalSelector.addGoal(2, new DungeonsIllusionerEntity.SummonClonesGoal());
      this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 5.0F, 1.0D, 1.0D));
      this.goalSelector.addGoal(4, new DungeonsIllusionerEntity.ShotGoal());
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

      //this.setAttackType(this.getTarget() != null);
      //System.out.print("\r\n" + this.isAttackType());

      if (this.liftInterval > 0) {
         this.liftInterval --;
      }

      if (this.duplicateInterval > 0) {
         this.duplicateInterval --;
      }

      //if (this.level.isClientSide) {
      //	System.out.print("\r\n" + this.isAggressive());
      //}

      if (this.getShotTick() > 0) {
         this.setShotTick(this.getShotTick() - 1);
      }

      if (this.getSummonCloneTick() > 0) {
         this.setSummonCloneTick(this.getSummonCloneTick() - 1);
      }
   }

   @Override
   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
   }

   private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
      if (this.isAttackType()&&this.getSummonCloneTick() >0) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.teleport_in_short", true));
      } else if (this.isAttackType()&&this.getShotTick() >0) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.shoot2_clone", true));
      } else if (this.getSummonCloneTick() > 0) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.teleport_out", true));
      } else if (this.getShotTick() > 0) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.bow_action_clone", true));
      } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_walk", true));
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.idle", true));
      }
      return this.level.isClientSide || this.deathTime > 0 ? PlayState.CONTINUE :
              PlayState.STOP;
   }

   @Override
   public AnimationFactory getFactory() {
      return factory;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SHOT_TICK, 0);
      this.entityData.define(SUMMON_CLONE_TICK, 0);
      this.entityData.define(SHOT_OR_SUMMON_BOOLEAN, false);
   }

   public int getShotTick() {
      return this.entityData.get(SHOT_TICK);
   }
   public void setShotTick(int p_189794_1_) {
      this.entityData.set(SHOT_TICK, p_189794_1_);
   }

   public int getSummonCloneTick() {
      return this.entityData.get(SUMMON_CLONE_TICK);
   }

   public void setSummonCloneTick(int p_189794_1_) {
      this.entityData.set(SUMMON_CLONE_TICK, p_189794_1_);
   }

   public boolean isAttackType() {
      return this.entityData.get(SHOT_OR_SUMMON_BOOLEAN);
   }

   public void setAttackType(boolean p_189794_1_) {
      this.entityData.set(SHOT_OR_SUMMON_BOOLEAN, p_189794_1_);
   }


   public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
      return MonsterEntity.createMonsterAttributes()
              .add(Attributes.MOVEMENT_SPEED, 0.25D)
              .add(Attributes.FOLLOW_RANGE, 32.0D)
              .add(Attributes.MAX_HEALTH, 30.0D)
              .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
      return SoundEvents.ILLUSIONER_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ILLUSIONER_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.ILLUSIONER_HURT;
   }

   @Override
   protected SoundEvent getCastingSoundEvent() {
      return SoundEvents.ILLUSIONER_CAST_SPELL;
   }

   @Override
   public SoundEvent getCelebrateSound() {
      return SoundEvents.ILLUSIONER_AMBIENT;
   }

   @Nullable
   @Override
   public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
      this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
      return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
   }

   @Override
   public void performRangedAttack(LivingEntity target, float p_82196_2_) {
      {
         ItemStack fireworkRocket = ModProjectileHelper.createRocket(DyeColor.PINK);
         FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(this.level, fireworkRocket, this, this.getX(), this.getEyeY() - (double) 0.15F, this.getZ(), true);
         double xDifference = target.getX() - this.getX();
         double yDifference = target.getY(0.3333333333333333D) - fireworkrocketentity.getY();
         double zDifference = target.getZ() - this.getZ();
         double horizontalDifference = (double) MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference);
         fireworkrocketentity.shoot(xDifference, yDifference, zDifference, 1.6F, (float) (18 - this.level.getDifficulty().getId() * 7.5));
         this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
         this.level.addFreshEntity(fireworkrocketentity);
      }
   }

   class CastingSpellGoal extends CastingASpellGoal {
      private CastingSpellGoal() {
      }

      /**
       * Keep ticking a continuous task that has already been started
       */
      public void tick() {
         if (DungeonsIllusionerEntity.this.getTarget() != null) {
            DungeonsIllusionerEntity.this.getLookControl().setLookAt(DungeonsIllusionerEntity.this.getTarget(), (float) DungeonsIllusionerEntity.this.getMaxHeadYRot(), (float) DungeonsIllusionerEntity.this.getMaxHeadXRot());
         }

      }
   }

   class ShotGoal extends Goal {

      private boolean d;
      @Override
      public boolean isInterruptable() {
         return false;
      }


      public ShotGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      }

      public boolean canUse() {
         return DungeonsIllusionerEntity.this.getTarget() != null && DungeonsIllusionerEntity.this.getShotTick() == 0 && DungeonsIllusionerEntity.this.liftInterval <= 0 && DungeonsIllusionerEntity.this.random.nextInt(20) == 0;
      }

      public boolean canContinueToUse() {
         return DungeonsIllusionerEntity.this.getTarget() != null && DungeonsIllusionerEntity.this.getShotTick() > 0;
      }

      public void start() {
         super.start();
         DungeonsIllusionerEntity.this.liftInterval = DungeonsIllusionerEntity.this.getRandom().nextInt(35) + 21;
         this.d =true;
         DungeonsIllusionerEntity.this.setAttackType(false);
         DungeonsIllusionerEntity.this.setShotTick(21 + DungeonsIllusionerEntity.this.getRandom().nextInt(3));
      }

      public void tick() {
         DungeonsIllusionerEntity.this.getLookControl().setLookAt(DungeonsIllusionerEntity.this.getTarget(), (float) DungeonsIllusionerEntity.this.getMaxHeadYRot(), (float) DungeonsIllusionerEntity.this.getMaxHeadXRot());
         LivingEntity target = DungeonsIllusionerEntity.this.getTarget();
         DungeonsIllusionerEntity mob = DungeonsIllusionerEntity.this;

         mob.getNavigation().stop();

         mob.getNavigation().stop();

         if (mob.getShotTick() == 1 && this.d) {
            DungeonsIllusionerEntity.this.setShotTick(14);
            DungeonsIllusionerEntity.this.setAttackType(true);
            this.d =false;
            mob.performRangedAttack(target, 0);
         }
      }

      public void stop() {
         super.stop();
         DungeonsIllusionerEntity.this.setAttackType(false);
         DungeonsIllusionerEntity.this.setShotTick(0);
      }
   }

   class SummonClonesGoal extends Goal {

      private boolean e;
      private int o;
      public SummonClonesGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      }

      public boolean canUse() {
         return DungeonsIllusionerEntity.this.getTarget() != null && DungeonsIllusionerEntity.this.getSummonCloneTick() == 0  && DungeonsIllusionerEntity.this.getShotTick() == 0 && DungeonsIllusionerEntity.this.duplicateInterval <= 0;
      }

      public boolean canContinueToUse() {
         return DungeonsIllusionerEntity.this.getTarget() != null && DungeonsIllusionerEntity.this.getSummonCloneTick() > 0;
      }

      @Override
      public boolean isInterruptable() {
         return false;
      }

      public void start() {
         super.start();
         this.e = false;
         this.o = 0;

         DungeonsIllusionerEntity.this.setInvulnerable(true);
         DungeonsIllusionerEntity.this.setAttackType(false);

         DungeonsIllusionerEntity.this.duplicateInterval = 180;


         DungeonsIllusionerEntity.this.setSummonCloneTick(20);

         DungeonsIllusionerEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, DungeonsIllusionerEntity.this.getSoundVolume(), DungeonsIllusionerEntity.this.getVoicePitch());
      }

      public void tick() {
         DungeonsIllusionerEntity.this.getLookControl().setLookAt(DungeonsIllusionerEntity.this.getTarget(), (float) DungeonsIllusionerEntity.this.getMaxHeadYRot(), (float) DungeonsIllusionerEntity.this.getMaxHeadXRot());
         LivingEntity target = DungeonsIllusionerEntity.this.getTarget();

         DungeonsIllusionerEntity mob = DungeonsIllusionerEntity.this;

         mob.getNavigation().stop();

         if (mob.getSummonCloneTick() == 1 && !this.e) {
            this.summonIllusionerClones();
            this.e = true;
            DungeonsIllusionerEntity.this.setInvulnerable(false);
            DungeonsIllusionerEntity.this.setSummonCloneTick(33);
            DungeonsIllusionerEntity.this.setAttackType(true);
            DungeonsIllusionerEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, DungeonsIllusionerEntity.this.getSoundVolume(), DungeonsIllusionerEntity.this.getVoicePitch());
            BlockPos blockpos = DungeonsIllusionerEntity.this.getTarget().blockPosition().offset(-5 + DungeonsIllusionerEntity.this.getRandom().nextInt(10) * (target.getBbWidth() / 2 +1), 0, -5 + DungeonsIllusionerEntity.this.getRandom().nextInt(10) * (target.getBbWidth() / 2 +1));
            this.o = 0;
            do {
               if (mob.level.isEmptyBlock(blockpos) && !mob.level.isEmptyBlock(blockpos.offset(0,-1,0))) {
                  mob.moveTo(blockpos,0,0);
                  break;
               }else if (!mob.level.isEmptyBlock(blockpos)) {
                  this.o++;
                  blockpos = blockpos.offset(0, 1, 0);
               }else {
                  this.o++;
                  blockpos = blockpos.offset(0, -1, 0);
               }
            }while (this.o < 20);
         }
      }

      private void summonIllusionerClones(){
         int difficultyAsInt = DungeonsIllusionerEntity.this.level.getDifficulty().getId();
         int mobsToSummon = difficultyAsInt * 3 + 6;
         LivingEntity target = DungeonsIllusionerEntity.this.getTarget();
         for(int i = 0; i < mobsToSummon; ++i) {
            BlockPos blockpos = DungeonsIllusionerEntity.this.getTarget().blockPosition().offset(-5 + DungeonsIllusionerEntity.this.getRandom().nextInt(10) * (target.getBbWidth() / 2 +1), 0, -5 + DungeonsIllusionerEntity.this.getRandom().nextInt(10) * (target.getBbWidth() / 2 +1));

            IllusionerCloneEntity illusionerCloneEntity = new IllusionerCloneEntity(DungeonsIllusionerEntity.this.level, DungeonsIllusionerEntity.this, 150 +  DungeonsIllusionerEntity.this.getRandom().nextInt(15), DungeonsIllusionerEntity.this.getTarget());
            DifficultyInstance difficultyForLocation = DungeonsIllusionerEntity.this.level.getCurrentDifficultyAt(blockpos);
            int ou = 0;
            do {
               if (illusionerCloneEntity.level.isEmptyBlock(blockpos) && !illusionerCloneEntity.level.isEmptyBlock(blockpos.offset(0,-1,0))) {
                  illusionerCloneEntity.moveTo(DungeonsIllusionerEntity.this.getTarget().blockPosition().offset(-4+illusionerCloneEntity.getRandom().nextInt(8),0,-4+illusionerCloneEntity.getRandom().nextInt(8)), 0.0F, 0.0F);
                  illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                  illusionerCloneEntity.setHealth(DungeonsIllusionerEntity.this.getHealth());
                  illusionerCloneEntity.setTarget(DungeonsIllusionerEntity.this.getTarget());
                  illusionerCloneEntity.setSummonCloneTick(33);
                  illusionerCloneEntity.setAttackType(true);
                  illusionerCloneEntity.liftInterval = DungeonsIllusionerEntity.this.getRandom().nextInt(60);
                  illusionerCloneEntity.setItemSlot(EquipmentSlotType.MAINHAND, DungeonsIllusionerEntity.this.getItemBySlot(EquipmentSlotType.MAINHAND));
                  illusionerCloneEntity.setItemSlot(EquipmentSlotType.OFFHAND, DungeonsIllusionerEntity.this.getItemBySlot(EquipmentSlotType.OFFHAND));
                  illusionerCloneEntity.setItemSlot(EquipmentSlotType.HEAD, DungeonsIllusionerEntity.this.getItemBySlot(EquipmentSlotType.HEAD));
                  illusionerCloneEntity.setItemSlot(EquipmentSlotType.CHEST, DungeonsIllusionerEntity.this.getItemBySlot(EquipmentSlotType.CHEST));
                  illusionerCloneEntity.setItemSlot(EquipmentSlotType.LEGS, DungeonsIllusionerEntity.this.getItemBySlot(EquipmentSlotType.LEGS));
                  illusionerCloneEntity.setItemSlot(EquipmentSlotType.FEET, DungeonsIllusionerEntity.this.getItemBySlot(EquipmentSlotType.FEET));
                  DungeonsIllusionerEntity.this.level.addFreshEntity(illusionerCloneEntity);
                  ICloneable cloneable = CloneableHelper.getCloneableCapability(DungeonsIllusionerEntity.this);
                  if(cloneable != null){
                     cloneable.addClone(illusionerCloneEntity.getUUID());
                  }
                  illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
                  break;
               }else {
                  if (!illusionerCloneEntity.level.isEmptyBlock(blockpos)) {
                     ou++;
                     blockpos = blockpos.offset(0, 1, 0);
                  }else {
                     ou++;
                     blockpos = blockpos.offset(0, -1, 0);
                  }
               }
            }while (ou < 20);
         }
      }

      public void stop() {
         super.stop();
         DungeonsIllusionerEntity.this.setInvulnerable(false);
         DungeonsIllusionerEntity.this.setSummonCloneTick(0);
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

   class RunAvoidGoal extends MeleeAttackGoal {
      private int maxAttackTimer = 15;
      private final double moveSpeed;
      private int delayCounter;
      private int attackTimer;

      public DungeonsIllusionerEntity v = DungeonsIllusionerEntity.this;

      public RunAvoidGoal(CreatureEntity creatureEntity, double moveSpeed) {
         super(creatureEntity, moveSpeed, true);
         this.moveSpeed = moveSpeed;
      }

      @Override
      public boolean canUse() {
         return v.getTarget() != null && v.getTarget().isAlive();
      }

      @Override
      public void start() {
         v.setAggressive(true);
         this.delayCounter = 0;
      }

      @Override
      public void tick() {
         LivingEntity livingentity = v.getTarget();
         if (livingentity == null) {
            v.setAggressive(false);
            return;
         }
         v.setAggressive(true);
         if (--this.delayCounter <= 0) {
            this.delayCounter = 8 + v.getRandom().nextInt(5);
            {
               if (v.distanceToSqr(livingentity) >= 80) {
                  v.getNavigation().moveTo(livingentity,
                          (double) this.moveSpeed
                  );
               }
            }
            if (v.distanceToSqr(livingentity) >= 60 && v.distanceToSqr(livingentity) <= 70) {
               v.getNavigation().stop();
            }
         }
      }

      @Override
      public void stop() {
         v.getNavigation().stop();
         if (v.getTarget() == null) {
            v.setAggressive(false);
         }
      }

      public DungeonsIllusionerEntity.RunAvoidGoal setMaxAttackTick(int max) {
         this.maxAttackTimer = max;
         return this;
      }
   }
}
