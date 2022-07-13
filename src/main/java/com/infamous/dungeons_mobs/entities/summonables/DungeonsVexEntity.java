package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsVindicatorEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
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
import java.util.List;

public class DungeonsVexEntity extends MonsterEntity implements IAnimatable {

   AnimationFactory factory = new AnimationFactory(this);
   protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.BYTE);
   protected static final DataParameter<Boolean> MELEEING = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.BOOLEAN);
   protected static final DataParameter<Boolean> RCMELEEING = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.BOOLEAN);
   protected static final DataParameter<Boolean> CMELEEING = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.BOOLEAN);
   protected static final DataParameter<Integer> MID = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.INT);
   protected static final DataParameter<Integer> CD = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.INT);
   protected static final DataParameter<Integer> MCD = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.INT);
   protected static final DataParameter<Integer> TIMER = EntityDataManager.defineId(DungeonsVexEntity.class, DataSerializers.INT);
   private MobEntity owner;
   @Nullable
   private BlockPos boundOrigin;
   private boolean hasLimitedLife;
   private int limitedLifeTicks;

   public DungeonsVexEntity(World world){
      super(ModEntityTypes.VEX.get(), world);
   }

   public DungeonsVexEntity(EntityType<? extends DungeonsVexEntity> p_i50190_1_, World p_i50190_2_) {
      super(p_i50190_1_, p_i50190_2_);
      this.moveControl = new DungeonsVexEntity.MoveHelperController(this);
      this.xpReward = 3;
   }

   public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
      super.move(p_213315_1_, p_213315_2_);
      this.checkInsideBlocks();
   }

   @Override
   public void aiStep() {
      if (this.entityData.get(MELEEING) || this.entityData.get(RCMELEEING)) {
         this.entityData.set(TIMER, this.entityData.get(TIMER) + 1);
      }
      if (this.entityData.get(CD) > 0) {
         this.entityData.set(CD, this.entityData.get(CD) - 1);
      }
      if (this.entityData.get(MCD) > 0) {
         this.entityData.set(MCD, this.entityData.get(MCD) - 1);
      }
      super.aiStep();
   }

   public void tick() {
      super.tick();
      this.noPhysics = false;
      this.setNoGravity(true);
      if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
         this.limitedLifeTicks = 20;
         this.hurt(DamageSource.STARVE, 1F);
      }

   }

   @Override
   public AnimationFactory getFactory() {
      return factory;
   }

   @Override
   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 20, this::predicate));
   }

   private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
      if (this.entityData.get(MELEEING)) {
         event.getController().animationSpeed = 1.5;
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vex.attack", true));
      } else if (this.entityData.get(RCMELEEING)) {
         event.getController().animationSpeed = 1;
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vex.chargeattack", true));
      } else if (this.entityData.get(CMELEEING)) {
         event.getController().animationSpeed = 1;
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vex.charge", true));
      } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
         event.getController().animationSpeed = 1;
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vex.walk", true));
      } else {
         event.getController().animationSpeed = 1;
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.vex.idle", true));
      }
      return PlayState.CONTINUE;
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(4, new DungeonsVexEntity.ChargeAttackGoal());
      this.goalSelector.addGoal(8, new DungeonsVexEntity.MoveRandomGoal());
      this.goalSelector.addGoal(4, new DungeonsVexEntity.AttackGoal());
      this.goalSelector.addGoal(4, new DungeonsVexEntity.CMeleeGoal());
      this.goalSelector.addGoal(8, new DungeonsVexEntity.MeleeGoal());
      this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
      this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
      this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
      this.targetSelector.addGoal(2, new DungeonsVexEntity.CopyOwnerTargetGoal(this));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
   }

   public static AttributeModifierMap.MutableAttribute createAttributes() {
      return MonsterEntity.createMonsterAttributes()
              .add(Attributes.MAX_HEALTH, 10.0D)
              .add(Attributes.ATTACK_DAMAGE, 6.0D);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_FLAGS_ID, (byte)0);
      this.entityData.define(MELEEING, false);
      this.entityData.define(RCMELEEING, false);
      this.entityData.define(CMELEEING, false);
      this.entityData.define(CD, 0);
      this.entityData.define(MCD, 0);
      this.entityData.define(MID, 0);
      this.entityData.define(TIMER, 0);
   }

   public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      if (p_70037_1_.contains("BoundX")) {
         this.boundOrigin = new BlockPos(p_70037_1_.getInt("BoundX"), p_70037_1_.getInt("BoundY"), p_70037_1_.getInt("BoundZ"));
      }

      if (p_70037_1_.contains("LifeTicks")) {
         this.setLimitedLife(p_70037_1_.getInt("LifeTicks"));
      }

   }

   public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
      super.addAdditionalSaveData(p_213281_1_);
      if (this.boundOrigin != null) {
         p_213281_1_.putInt("BoundX", this.boundOrigin.getX());
         p_213281_1_.putInt("BoundY", this.boundOrigin.getY());
         p_213281_1_.putInt("BoundZ", this.boundOrigin.getZ());
      }

      if (this.hasLimitedLife) {
         p_213281_1_.putInt("LifeTicks", this.limitedLifeTicks);
      }

   }

   public MobEntity getOwner() {
      return this.owner;
   }

   @Nullable
   public BlockPos getBoundOrigin() {
      return this.boundOrigin;
   }

   public void setBoundOrigin(@Nullable BlockPos p_190651_1_) {
      this.boundOrigin = p_190651_1_;
   }

   private boolean getVexFlag(int p_190656_1_) {
      int i = this.entityData.get(DATA_FLAGS_ID);
      return (i & p_190656_1_) != 0;
   }

   private void setVexFlag(int p_190660_1_, boolean p_190660_2_) {
      int i = this.entityData.get(DATA_FLAGS_ID);
      if (p_190660_2_) {
         i = i | p_190660_1_;
      } else {
         i = i & ~p_190660_1_;
      }

      this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
   }

   public boolean isCharging() {
      return this.getVexFlag(1);
   }

   public void setIsCharging(boolean p_190648_1_) {
      this.setVexFlag(1, p_190648_1_);
   }

   public void setOwner(MobEntity p_190658_1_) {
      this.owner = p_190658_1_;
   }

   public void setLimitedLife(int p_190653_1_) {
      this.hasLimitedLife = true;
      this.limitedLifeTicks = p_190653_1_;
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.VEX_AMBIENT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.VEX_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.VEX_HURT;
   }

   public float getBrightness() {
      return 1.0F;
   }

   @Nullable
   public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
      this.populateDefaultEquipmentSlots(p_213386_2_);
      this.populateDefaultEquipmentEnchantments(p_213386_2_);
      return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
   }

   protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
      this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
      this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
   }

   @Override
   public boolean doHurtTarget(Entity p_70652_1_) {
      if (!this.entityData.get(MELEEING) && this.entityData.get(MID) == 1){
         this.entityData.set(MELEEING, true);
      }else {
         this.entityData.set(RCMELEEING, true);
      }
      return true;
   }

   class ChargeAttackGoal extends Goal {

      DungeonsVexEntity v = DungeonsVexEntity.this;

      @Override
      public boolean isInterruptable() {
         return false;
      }

      public ChargeAttackGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      }

      public boolean canUse() {
         return DungeonsVexEntity.this.entityData.get(CD) <= 0 &&
                 v.getTarget() != null &&
                 v.getTarget().isAlive();
      }

      public boolean canContinueToUse() {
         return v.entityData.get(TIMER) < 30 &&
                 DungeonsVexEntity.this.getTarget() != null &&
                 DungeonsVexEntity.this.getTarget().isAlive();
      }

      public void start() {
         v.entityData.set(CD, 250);
         v.entityData.set(CMELEEING, true);
         DungeonsVexEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
      }

      public void tick() {
         LivingEntity livingentity = DungeonsVexEntity.this.getTarget();
         v.getLookControl().setLookAt(livingentity, 30, 30);
         if (v.entityData.get(TIMER) == 28) {
            v.entityData.set(RCMELEEING,true);
            v.entityData.set(CMELEEING, false);
            DungeonsVexEntity.this.entityData.set(MID, 2);
            v.doHurtTarget(livingentity);
         }
      }
   }

   class AttackGoal extends Goal {
      public AttackGoal() {
      }

      public boolean canUse() {
         if (DungeonsVexEntity.this.getTarget() != null && !DungeonsVexEntity.this.getMoveControl().hasWanted() && DungeonsVexEntity.this.random.nextInt(7) == 0) {
            return DungeonsVexEntity.this.distanceToSqr(DungeonsVexEntity.this.getTarget()) > 4.0D;
         } else {
            return false;
         }
      }

      public boolean canContinueToUse() {
         return DungeonsVexEntity.this.getMoveControl().hasWanted() && DungeonsVexEntity.this.getTarget() != null && DungeonsVexEntity.this.getTarget().isAlive();
      }

      public void start() {
         LivingEntity livingentity = DungeonsVexEntity.this.getTarget();
         Vector3d vector3d = livingentity.getEyePosition(1.0F);
         DungeonsVexEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
      }

      public void tick() {
         LivingEntity livingentity = DungeonsVexEntity.this.getTarget();
         if (DungeonsVexEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
            DungeonsVexEntity.this.entityData.set(MCD, 30);
            DungeonsVexEntity.this.entityData.set(MELEEING,true);
            DungeonsVexEntity.this.entityData.set(MID, 1);
            DungeonsVexEntity.this.doHurtTarget(livingentity);
         }
         DungeonsVexEntity.this.getLookControl().setLookAt(DungeonsVexEntity.this.getTarget(), 30.0F, 30.0F);
         Vector3d vector3d = livingentity.getEyePosition(1.0F);
         DungeonsVexEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 0.8D);
      }
   }

   class CopyOwnerTargetGoal extends TargetGoal {
      private final EntityPredicate copyOwnerTargeting = (new EntityPredicate()).allowUnseeable().ignoreInvisibilityTesting();

      public CopyOwnerTargetGoal(CreatureEntity p_i47231_2_) {
         super(p_i47231_2_, false);
      }

      public boolean canUse() {
         return DungeonsVexEntity.this.owner != null && DungeonsVexEntity.this.owner.getTarget() != null && this.canAttack(DungeonsVexEntity.this.owner.getTarget(), this.copyOwnerTargeting);
      }

      public void start() {
         DungeonsVexEntity.this.setTarget(DungeonsVexEntity.this.owner.getTarget());
         super.start();
      }
   }

   class MoveHelperController extends MovementController {
      public MoveHelperController(DungeonsVexEntity p_i47230_2_) {
         super(p_i47230_2_);
      }

      public void tick() {
         if (this.operation == Action.MOVE_TO) {
            Vector3d vector3d = new Vector3d(this.wantedX - DungeonsVexEntity.this.getX(), this.wantedY - DungeonsVexEntity.this.getY(), this.wantedZ - DungeonsVexEntity.this.getZ());
            double d0 = vector3d.length();
            if (d0 < DungeonsVexEntity.this.getBoundingBox().getSize()) {
               this.operation = Action.WAIT;
               DungeonsVexEntity.this.setDeltaMovement(DungeonsVexEntity.this.getDeltaMovement().scale(0.5D));
            } else {
               DungeonsVexEntity.this.setDeltaMovement(DungeonsVexEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
               if (DungeonsVexEntity.this.getTarget() == null) {
                  Vector3d vector3d1 = DungeonsVexEntity.this.getDeltaMovement();
                  DungeonsVexEntity.this.yRot = -((float)MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                  DungeonsVexEntity.this.yBodyRot = DungeonsVexEntity.this.yRot;
               } else {
                  double d2 = DungeonsVexEntity.this.getTarget().getX() - DungeonsVexEntity.this.getX();
                  double d1 = DungeonsVexEntity.this.getTarget().getZ() - DungeonsVexEntity.this.getZ();
                  DungeonsVexEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                  DungeonsVexEntity.this.yBodyRot = DungeonsVexEntity.this.yRot;
               }
            }

         }
      }
   }

   class MoveRandomGoal extends Goal {
      public MoveRandomGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      public boolean canUse() {
         return !DungeonsVexEntity.this.getMoveControl().hasWanted() && DungeonsVexEntity.this.random.nextInt(7) == 0;
      }

      public boolean canContinueToUse() {
         return false;
      }

      public void tick() {
         BlockPos blockpos = DungeonsVexEntity.this.getBoundOrigin();
         if (blockpos == null) {
            blockpos = DungeonsVexEntity.this.blockPosition();
         }

         for(int i = 0; i < 3; ++i) {
            BlockPos blockpos1 = blockpos.offset(DungeonsVexEntity.this.random.nextInt(15) - 7, DungeonsVexEntity.this.random.nextInt(11) - 5, DungeonsVexEntity.this.random.nextInt(15) - 7);
            if (DungeonsVexEntity.this.level.isEmptyBlock(blockpos1)) {
               DungeonsVexEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
               if (DungeonsVexEntity.this.getTarget() == null) {
                  DungeonsVexEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
               }
               break;
            }
         }

      }
   }

   class MeleeGoal extends Goal {

      public DungeonsVexEntity v = DungeonsVexEntity.this;

      public MeleeGoal() {
         this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         return v.getTarget() != null && v.entityData.get(MELEEING);
      }

      @Override
      public boolean canContinueToUse() {
         //animation tick
         return v.entityData.get(TIMER) <= 30 / 1.5 && (v.getTarget() != null && v.getTarget().isAlive());
      }

      @Override
      public void start() {
         v.entityData.set(MID,1);
         v.entityData.set(MELEEING,true);
         v.entityData.set(TIMER,0);
      }

      @Override
      public void stop() {
         v.entityData.set(MID,0);
         v.entityData.set(MELEEING,false);
         v.entityData.set(TIMER,0);
         if (v.getTarget() == null) {
            v.setAggressive(false);
         }
      }

      @Override
      public void tick() {
         if (v.getTarget() != null && v.getTarget().isAlive()) {
            v.getLookControl().setLookAt(v.getTarget(), 30.0F, 30.0F);
            if (v.entityData.get(TIMER) <= 20 / 1.5 && v.distanceToSqr(v.getTarget()) <= 6.0D+ v.getTarget().getBbWidth()) {
               float attackKnockback = 0.05F;
               LivingEntity attackTarget = v.getTarget();
               double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 360F));
               double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 360F)));
               double knockbackReduction = 0.5D;
               attackTarget.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE) +
                       EnchantmentHelper.getDamageBonus(v.getMainHandItem(), ((LivingEntity)attackTarget).getMobType()) );
               attackTarget.setSecondsOnFire(EnchantmentHelper.getFireAspect(v) * 5);
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

   class CMeleeGoal extends Goal {

      public DungeonsVexEntity v = DungeonsVexEntity.this;

      public CMeleeGoal() {
         this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         return v.getTarget() != null && v.entityData.get(RCMELEEING);
      }

      @Override
      public boolean canContinueToUse() {
         //animation tick
         return v.entityData.get(TIMER) <= 50 && (v.getTarget() != null && v.getTarget().isAlive());
      }

      @Override
      public void start() {
         v.entityData.set(MID,1);
         v.entityData.set(RCMELEEING,true);
         v.entityData.set(TIMER,0);
      }

      @Override
      public void stop() {
         v.entityData.set(MID,0);
         v.entityData.set(RCMELEEING,false);
         v.entityData.set(TIMER,0);
         if (v.getTarget() == null) {
            v.setAggressive(false);
         }
      }

      @Override
      public void tick() {
         if (v.getTarget() != null && v.getTarget().isAlive()) {
            v.getLookControl().setLookAt(v.getTarget(), 30.0F, 30.0F);
            LivingEntity livingentity = DungeonsVexEntity.this.getTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            DungeonsVexEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.5D);
            if (v.entityData.get(TIMER) >= 10 && v.distanceToSqr(v.getTarget()) <= 6.0D+ v.getTarget().getBbWidth()) {
               float attackKnockback = 0.05F;
               LivingEntity attackTarget = v.getTarget();
               double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 360F));
               double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 360F)));
               double knockbackReduction = 0.5D;
               attackTarget.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE) +
                       EnchantmentHelper.getDamageBonus(v.getMainHandItem(), ((LivingEntity)attackTarget).getMobType()) );
               attackTarget.setSecondsOnFire(EnchantmentHelper.getFireAspect(v) * 5);
               this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
               v.setDeltaMovement(v.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
            if (v.entityData.get(TIMER) >= 10) {
               List<Entity> list = Lists.newArrayList(v.level.getEntities(v, v.getBoundingBox().inflate(2, 0.5, 2)));
               for(Entity entity : list) {
                  if(entity instanceof LivingEntity){
                     LivingEntity livingEntity = (LivingEntity)entity;
                     float attackKnockback = 0.05F;
                     double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 360F));
                     double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 360F)));
                     double knockbackReduction = 0.5D;
                     livingEntity.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE) +
                             EnchantmentHelper.getDamageBonus(v.getMainHandItem(), ((LivingEntity) livingEntity).getMobType()) );
                     livingEntity.setSecondsOnFire(EnchantmentHelper.getFireAspect(v) * 5);
                     this.forceKnockback(livingEntity, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                     v.setDeltaMovement(v.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                  }
               }
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
}
