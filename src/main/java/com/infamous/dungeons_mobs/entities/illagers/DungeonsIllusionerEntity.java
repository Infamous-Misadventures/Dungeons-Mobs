package com.infamous.dungeons_mobs.entities.illagers;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.capabilities.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.ICloneable;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class DungeonsIllusionerEntity extends IllusionerEntity implements IRangedAttackMob {

   public DungeonsIllusionerEntity(EntityType<? extends DungeonsIllusionerEntity> entityType, World world) {
      super(entityType, world);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      // remove the original MirrorSpellGoal, since it's a package private inner class
      ((GoalSelectorAccessor)this.goalSelector)
              .getAvailableGoals()
              .removeIf(
                      pg -> pg.getPriority() == 4
                              && pg.getGoal() instanceof SpellcastingIllagerEntity.UseSpellGoal);
      this.goalSelector.addGoal(4, new DungeonsIllusionerEntity.MirrorSpellGoal());
   }

   @OnlyIn(Dist.CLIENT)
   public AbstractIllagerEntity.ArmPose getArmPose() {
      if (this.isCastingSpell()) {
         return AbstractIllagerEntity.ArmPose.SPELLCASTING;
      } else if(this.isAggressive()){
         if(this.isHolding(item -> item instanceof BowItem)){
            return AbstractIllagerEntity.ArmPose.BOW_AND_ARROW;
         } else{
            return AbstractIllagerEntity.ArmPose.ATTACKING;
         }
      } else{
         return ArmPose.CROSSED;
      }
   }

   @Override
   public boolean hurt(DamageSource damageSource, float amount) {
      boolean didHurt = super.hurt(damageSource, amount);
      if(didHurt){
         ICloneable cloneable = CloneableHelper.getCloneableCapability(this);
         if(cloneable != null && this.level instanceof ServerWorld){
            UUID[] cloneUUIDs = cloneable.getClones();
            for(int i = 0; i < cloneUUIDs.length; i++){
               UUID currentCloneUUID = cloneUUIDs[i];
               if(currentCloneUUID != null){
                  ServerWorld serverWorld = (ServerWorld) this.level;
                  Entity clone = serverWorld.getEntity(currentCloneUUID);
                  if(clone != null){
                     clone.remove();
                  }
                  cloneUUIDs[i] = null;
               }
            }
         }
      }
      return didHurt;
   }

   class MirrorSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
      private MirrorSpellGoal() {
      }

      public boolean canUse() {
         if (!super.canUse()) {
            return false;
         } else {
            return !DungeonsIllusionerEntity.this.hasEffect(Effects.INVISIBILITY);
         }
      }

      protected int getCastingTime() {
         return 20;
      }

      protected int getCastingInterval() {
         return 340;
      }

      protected void performSpellCasting() {
         DungeonsIllusionerEntity.this.addEffect(new EffectInstance(Effects.INVISIBILITY, 1200));
         this.summonIllusionerClones();
      }

      private void summonIllusionerClones(){
         int difficultyAsInt = DungeonsIllusionerEntity.this.level.getDifficulty().getId();
         int mobsToSummon = difficultyAsInt * 2 + 1; // 3 on easy, 5 on normal, 7 on hard
         for(int i = 0; i < mobsToSummon; ++i) {
            BlockPos blockpos = DungeonsIllusionerEntity.this.blockPosition().offset(-2 + DungeonsIllusionerEntity.this.getRandom().nextInt(5), 1, -2 + DungeonsIllusionerEntity.this.getRandom().nextInt(5));
            IllusionerCloneEntity illusionerCloneEntity = new IllusionerCloneEntity(DungeonsIllusionerEntity.this.level, DungeonsIllusionerEntity.this, 30 * 20);
            DifficultyInstance difficultyForLocation = DungeonsIllusionerEntity.this.level.getCurrentDifficultyAt(blockpos);
            illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
            illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
            DungeonsIllusionerEntity.this.level.addFreshEntity(illusionerCloneEntity);
            ICloneable cloneable = CloneableHelper.getCloneableCapability(DungeonsIllusionerEntity.this);
            if(cloneable != null){
               cloneable.addClone(illusionerCloneEntity.getUUID());
            }
         }
      }

      @Nullable
      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
      }

      protected SpellcastingIllagerEntity.SpellType getSpell() {
         return SpellcastingIllagerEntity.SpellType.DISAPPEAR;
      }
   }
}