package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.ender.BlastlingEntity;
import com.infamous.dungeons_mobs.entities.ender.EndersentEntity;
import com.infamous.dungeons_mobs.entities.ender.SnarelingEntity;
import com.infamous.dungeons_mobs.entities.ender.WatchlingEntity;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.piglin.ArmoredPiglinEntity;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.piglin.ZombifiedArmoredPiglinEntity;
import com.infamous.dungeons_mobs.entities.piglin.ZombifiedFungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneCubeEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.slime.ConjuredSlimeEntity;
import com.infamous.dungeons_mobs.entities.undead.*;
import com.infamous.dungeons_mobs.entities.undead.horseman.SkeletonHorsemanEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.entities.water.*;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypeAttributes {

    @SubscribeEvent
    public static void initEntityTypeAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.ARMORED_ZOMBIE.get(), ArmoredZombieEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.JUNGLE_ZOMBIE.get(), JungleZombieEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.FROZEN_ZOMBIE.get(), FrozenZombieEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.ARMORED_SKELETON.get(), ArmoredSkeletonEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.MOSSY_SKELETON.get(), MossySkeletonEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.SKELETON_VANGUARD.get(), SkeletonVanguardEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.SKELETON_HORSEMAN.get(), SkeletonHorsemanEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.NECROMANCER.get(), NecromancerEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.ARMORED_VINDICATOR.get(), ArmoredVindicatorEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ARMORED_PILLAGER.get(), ArmoredPillagerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ROYAL_GUARD.get(), RoyalGuardEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.VINDICATOR_CHEF.get(), VindicatorChefEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.MOUNTAINEER.get(), MountaineerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ARMORED_MOUNTAINEER.get(), ArmoredMountaineerEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.ICEOLOGER.get(), IceologerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.GEOMANCER.get(), GeomancerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ILLUSIONER.get(), DungeonsIllusionerEntity.createAttributes().build());
        event.put(ModEntityTypes.ILLUSIONER_CLONE.get(), IllusionerCloneEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.WINDCALLER.get(), WindcallerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ENCHANTER.get(), EnchanterEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.ICY_CREEPER.get(), IcyCreeperEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.WRAITH.get(), WraithEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.CONJURED_SLIME.get(), ConjuredSlimeEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.REDSTONE_CUBE.get(), RedstoneCubeEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.REDSTONE_GOLEM.get(), RedstoneGolemEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.WHISPERER.get(), WhispererEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.LEAPLEAF.get(), LeapleafEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.QUICK_GROWING_VINE.get(), QuickGrowingVineEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.POISON_QUILL_VINE.get(), PoisonQuillVineEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.SQUALL_GOLEM.get(), SquallGolemEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.ARMORED_PIGLIN.get(), ArmoredPiglinEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.FUNGUS_THROWER.get(), PiglinEntity.createAttributes().build());
        event.put(ModEntityTypes.ZOMBIFIED_ARMORED_PIGLIN.get(), ZombifiedArmoredPiglinEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(), ZombifiedPiglinEntity.createAttributes().build());

        event.put(ModEntityTypes.ARMORED_DROWNED.get(), ArmoredDrownedEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.DROWNED_NECROMANCER.get(), DrownedNecromancerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.SUNKEN_SKELETON.get(), SunkenSkeletonEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ARMORED_SUNKEN_SKELETON.get(), ArmoredSunkenSkeletonEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.WAVEWHISPERER.get(), WavewhispererEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.QUICK_GROWING_ANEMONE.get(), QuickGrowingAnemoneEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.POISON_ANEMONE.get(), PoisonAnemoneEntity.setCustomAttributes().build());

        event.put(ModEntityTypes.ENDERSENT.get(), EndersentEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.BLASTLING.get(), BlastlingEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.WATCHLING.get(), WatchlingEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.SNARELING.get(), SnarelingEntity.setCustomAttributes().build());

    }

    @SubscribeEvent
    public static void changeAttributes(EntityAttributeModificationEvent event){
        // Tougher Husks
        if(DungeonsMobsConfig.COMMON.ENABLE_STRONGER_HUSKS.get()){
            event.add(EntityType.HUSK, Attributes.ARMOR, 10.0D);
            event.add(EntityType.HUSK, Attributes.MOVEMENT_SPEED, 0.17D);
            event.add(EntityType.HUSK, Attributes.KNOCKBACK_RESISTANCE, 0.6D);
        }
    }
}
