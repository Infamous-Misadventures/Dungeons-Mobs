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
   public boolean shouldExecute() {
      PlayerEntity closestPlayer = this.leaderSkeletonHorseman.world
              .getClosestPlayer(this.leaderSkeletonHorseman.getPosX(),
                      this.leaderSkeletonHorseman.getPosY(),
                      this.leaderSkeletonHorseman.getPosZ(),
                      10.0D,
                      true);
      return closestPlayer != null && this.leaderSkeletonHorseman.canEntityBeSeen(closestPlayer);
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      // Using Forge PR #7509 that fixes MC-206338
      ServerWorld serverWorld = (ServerWorld) this.leaderSkeletonHorseman.world;
      DifficultyInstance difficultyinstance = serverWorld.getDifficultyForLocation(this.leaderSkeletonHorseman.getPosition());
      serverWorld.getServer().enqueue(new net.minecraft.util.concurrent.TickDelayedTask(serverWorld.getServer().getTickCounter(), () -> this.leaderSkeletonHorseman.setTrap(false)));

      this.createLightningBolt(this.leaderSkeletonHorseman);

      SkeletonHorseEntity leaderSkeletonHorse = this.createHorse(difficultyinstance);
      this.leaderSkeletonHorseman.startRiding(leaderSkeletonHorse);
      leaderSkeletonHorse.makeMad();

      for(int i = 0; i < 3; ++i) {
         SkeletonHorseEntity additionalSkeletonHorse = this.createHorse(difficultyinstance);
         SkeletonHorsemanEntity additionalSkeletonHorseman = this.createSkeletonHorseman(difficultyinstance, additionalSkeletonHorse);
         additionalSkeletonHorseman.startRiding(additionalSkeletonHorse);
         additionalSkeletonHorse.makeMad();
         additionalSkeletonHorse.addVelocity(this.leaderSkeletonHorseman.getRNG().nextGaussian() * 0.5D, 0.0D, this.leaderSkeletonHorseman.getRNG().nextGaussian() * 0.5D);
         this.createLightningBolt(this.leaderSkeletonHorseman);
      }
   }

   private void createLightningBolt(SkeletonHorsemanEntity skeletonHorsemanEntity) {
      LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(skeletonHorsemanEntity.world);
      if (lightningboltentity != null) {
         lightningboltentity.moveForced(skeletonHorsemanEntity.getPosX(), skeletonHorsemanEntity.getPosY(), skeletonHorsemanEntity.getPosZ());
         lightningboltentity.setEffectOnly(true);
         skeletonHorsemanEntity.world.addEntity(lightningboltentity);
      }
   }

   private SkeletonHorseEntity createHorse(DifficultyInstance difficultyInstance) {
      SkeletonHorseEntity skeletonHorseEntity = EntityType.SKELETON_HORSE.create(this.leaderSkeletonHorseman.world);
      if (skeletonHorseEntity != null) {
         skeletonHorseEntity.onInitialSpawn((IServerWorld) this.leaderSkeletonHorseman.world, difficultyInstance, SpawnReason.TRIGGERED, (ILivingEntityData)null, (CompoundNBT)null);
         skeletonHorseEntity.setPosition(this.leaderSkeletonHorseman.getPosX(), this.leaderSkeletonHorseman.getPosY(), this.leaderSkeletonHorseman.getPosZ());
         skeletonHorseEntity.hurtResistantTime = 60;
         skeletonHorseEntity.setHorseTamed(true);
         skeletonHorseEntity.enablePersistence();
         skeletonHorseEntity.setGrowingAge(0);

         Inventory horseChest = ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, skeletonHorseEntity, "field_110296_bG");
         if (horseChest != null) {
            horseChest.setInventorySlotContents(0, new ItemStack(Items.SADDLE));
         }
         //skeletonHorseEntity.goalSelector.addGoal(0, new MountedRoamingGoal(skeletonHorseEntity, 1.2D));
         skeletonHorseEntity.world.addEntity(skeletonHorseEntity);
      }
      return skeletonHorseEntity;
   }

   private SkeletonHorsemanEntity createSkeletonHorseman(DifficultyInstance difficultyInstance, AbstractHorseEntity horse) {
      SkeletonHorsemanEntity skeletonHorsemanEntity = ModEntityTypes.SKELETON_HORSEMAN.get().create(horse.world);
      if (skeletonHorsemanEntity != null) {
         skeletonHorsemanEntity.onInitialSpawn((IServerWorld) horse.world, difficultyInstance, SpawnReason.TRIGGERED, (ILivingEntityData)null, (CompoundNBT)null);
         skeletonHorsemanEntity.setPosition(horse.getPosX(), horse.getPosY(), horse.getPosZ());
         skeletonHorsemanEntity.hurtResistantTime = 60;
         skeletonHorsemanEntity.enablePersistence();
         skeletonHorsemanEntity.world.addEntity(skeletonHorsemanEntity);
      }
      return skeletonHorsemanEntity;
   }
}