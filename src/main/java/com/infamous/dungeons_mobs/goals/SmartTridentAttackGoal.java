package com.infamous.dungeons_mobs.goals;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.TridentItem;

public class SmartTridentAttackGoal extends RangedAttackGoal {
      private final MobEntity thrower;

      public SmartTridentAttackGoal(IRangedAttackMob rangedAttackMob, double speedModifier, int attackInterval, float attackRadius) {
         super(rangedAttackMob, speedModifier, attackInterval, attackRadius);
         this.thrower = (MobEntity)rangedAttackMob;
      }

      public boolean canUse() {
         return super.canUse() && this.thrower.isHolding(item -> item instanceof TridentItem);
      }

      public void start() {
         super.start();
         this.thrower.setAggressive(true);
         this.thrower.startUsingItem(ProjectileHelper.getWeaponHoldingHand(this.thrower, item -> item instanceof TridentItem));
      }

      public void stop() {
         super.stop();
         this.thrower.stopUsingItem();
         this.thrower.setAggressive(false);
      }
   }