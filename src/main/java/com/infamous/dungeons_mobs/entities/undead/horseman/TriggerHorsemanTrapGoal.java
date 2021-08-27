package com.infamous.dungeons_mobs.entities.undead.horseman;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class TriggerHorsemanTrapGoal extends Goal {
   private final SkeletonHorsemanEntity leaderSkeletonHorseman;

   TriggerHorsemanTrapGoal(SkeletonHorsemanEntity horsemanIn) {
      this.leaderSkeletonHorseman = horsemanIn;
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean canUse() {
      PlayerEntity closestPlayer = this.leaderSkeletonHorseman.level
              .getNearestPlayer(this.leaderSkeletonHorseman.getX(),
                      this.leaderSkeletonHorseman.getY(),
                      this.leaderSkeletonHorseman.getZ(),
                      10.0D,
                      true);
      return closestPlayer != null && this.leaderSkeletonHorseman.canSee(closestPlayer);
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      // Using Forge PR #7509 that fixes MC-206338
      ServerWorld serverWorld = (ServerWorld) this.leaderSkeletonHorseman.level;
      DifficultyInstance difficultyinstance = serverWorld.getCurrentDifficultyAt(this.leaderSkeletonHorseman.blockPosition());
      serverWorld.getServer().tell(new net.minecraft.util.concurrent.TickDelayedTask(serverWorld.getServer().getTickCount(), () -> this.leaderSkeletonHorseman.setTrap(false)));

      this.createLightningBolt(this.leaderSkeletonHorseman);

      SkeletonHorseEntity leaderSkeletonHorse = this.createHorse(difficultyinstance);
      this.leaderSkeletonHorseman.startRiding(leaderSkeletonHorse);
      leaderSkeletonHorse.makeMad();

      for(int i = 0; i < 3; ++i) {
         SkeletonHorseEntity additionalSkeletonHorse = this.createHorse(difficultyinstance);
         SkeletonHorsemanEntity additionalSkeletonHorseman = this.createSkeletonHorseman(difficultyinstance, additionalSkeletonHorse);
         additionalSkeletonHorseman.startRiding(additionalSkeletonHorse);
         additionalSkeletonHorse.makeMad();
         additionalSkeletonHorse.push(this.leaderSkeletonHorseman.getRandom().nextGaussian() * 0.5D, 0.0D, this.leaderSkeletonHorseman.getRandom().nextGaussian() * 0.5D);
         this.createLightningBolt(this.leaderSkeletonHorseman);
      }
   }

   private void createLightningBolt(SkeletonHorsemanEntity skeletonHorsemanEntity) {
      LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(skeletonHorsemanEntity.level);
      if (lightningboltentity != null) {
         lightningboltentity.moveTo(skeletonHorsemanEntity.getX(), skeletonHorsemanEntity.getY(), skeletonHorsemanEntity.getZ());
         lightningboltentity.setVisualOnly(true);
         skeletonHorsemanEntity.level.addFreshEntity(lightningboltentity);
      }
   }

   private SkeletonHorseEntity createHorse(DifficultyInstance difficultyInstance) {
      SkeletonHorseEntity skeletonHorseEntity = EntityType.SKELETON_HORSE.create(this.leaderSkeletonHorseman.level);
      if (skeletonHorseEntity != null) {
         skeletonHorseEntity.finalizeSpawn((IServerWorld) this.leaderSkeletonHorseman.level, difficultyInstance, SpawnReason.TRIGGERED, (ILivingEntityData)null, (CompoundNBT)null);
         skeletonHorseEntity.setPos(this.leaderSkeletonHorseman.getX(), this.leaderSkeletonHorseman.getY(), this.leaderSkeletonHorseman.getZ());
         skeletonHorseEntity.invulnerableTime = 60;
         skeletonHorseEntity.setTamed(true);
         skeletonHorseEntity.setPersistenceRequired();
         skeletonHorseEntity.setAge(0);

         Inventory horseChest = ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, skeletonHorseEntity, "field_110296_bG");
         if (horseChest != null) {
            horseChest.setItem(0, new ItemStack(Items.SADDLE));
         }
         //skeletonHorseEntity.goalSelector.addGoal(0, new MountedRoamingGoal(skeletonHorseEntity, 1.2D));
         skeletonHorseEntity.level.addFreshEntity(skeletonHorseEntity);
      }
      return skeletonHorseEntity;
   }

   private SkeletonHorsemanEntity createSkeletonHorseman(DifficultyInstance difficultyInstance, AbstractHorseEntity horse) {
      SkeletonHorsemanEntity skeletonHorsemanEntity = ModEntityTypes.SKELETON_HORSEMAN.get().create(horse.level);
      if (skeletonHorsemanEntity != null) {
         skeletonHorsemanEntity.finalizeSpawn((IServerWorld) horse.level, difficultyInstance, SpawnReason.TRIGGERED, (ILivingEntityData)null, (CompoundNBT)null);
         skeletonHorsemanEntity.setPos(horse.getX(), horse.getY(), horse.getZ());
         skeletonHorsemanEntity.invulnerableTime = 60;
         skeletonHorsemanEntity.setPersistenceRequired();
         skeletonHorsemanEntity.level.addFreshEntity(skeletonHorsemanEntity);
      }
      return skeletonHorsemanEntity;
   }
}