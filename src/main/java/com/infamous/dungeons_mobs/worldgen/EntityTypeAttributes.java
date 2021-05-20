package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneCubeEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.slime.ConjuredSlimeEntity;
import com.infamous.dungeons_mobs.entities.undead.*;
import com.infamous.dungeons_mobs.entities.undead.horseman.SkeletonHorsemanEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;

public class EntityTypeAttributes {

    public static void initEntityTypeAttributes() {
        GlobalEntityTypeAttributes.put(ModEntityTypes.ARMORED_ZOMBIE.get(), ArmoredZombieEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.JUNGLE_ZOMBIE.get(), JungleZombieEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.FROZEN_ZOMBIE.get(), FrozenZombieEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.ARMORED_SKELETON.get(), ArmoredSkeletonEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.MOSSY_SKELETON.get(), MossySkeletonEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.SKELETON_VANGUARD.get(), SkeletonVanguardEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.SKELETON_HORSEMAN.get(), SkeletonHorsemanEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.NECROMANCER.get(), NecromancerEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.ARMORED_VINDICATOR.get(), ArmoredVindicatorEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.ARMORED_PILLAGER.get(), ArmoredPillagerEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.ROYAL_GUARD.get(), RoyalGuardEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.VINDICATOR_CHEF.get(), VindicatorChefEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.MOUNTAINEER.get(), MountaineerEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.ARMORED_MOUNTAINEER.get(), ArmoredMountaineerEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.ICEOLOGER.get(), IceologerEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.GEOMANCER.get(), GeomancerEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.ILLUSIONER_CLONE.get(), IllusionerCloneEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.WINDCALLER.get(), WindcallerEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.ICY_CREEPER.get(), IcyCreeperEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.WRAITH.get(), WraithEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.CONJURED_SLIME.get(), ConjuredSlimeEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.REDSTONE_CUBE.get(), RedstoneCubeEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.REDSTONE_GOLEM.get(), RedstoneGolemEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.WHISPERER.get(), WhispererEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.LEAPLEAF.get(), LeapleafEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.QUICK_GROWING_VINE.get(), QuickGrowingVineEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.POISON_QUILL_VINE.get(), PoisonQuillVineEntity.setCustomAttributes().create());

        GlobalEntityTypeAttributes.put(ModEntityTypes.SQUALL_GOLEM.get(), SquallGolemEntity.setCustomAttributes().create());

        // Tougher Husks
        if(DungeonsMobsConfig.COMMON.ENABLE_STRONGER_HUSKS.get()){
            GlobalEntityTypeAttributes.put(EntityType.HUSK, HuskEntity.func_234342_eQ_()
                    .createMutableAttribute(Attributes.ARMOR, 10.0D)
                    .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.17D)
                    .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                    .create());
        }
    }
}
