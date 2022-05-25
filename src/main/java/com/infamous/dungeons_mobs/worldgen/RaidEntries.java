package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mixin.RaidMixin;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.raid.RaidManager;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.fml.ModList;

public class RaidEntries {

    public static void initWaveMemberEntries(){
        /*
      VINDICATOR(EntityType.VINDICATOR, new int[]{0, 0, 2, 0, 1, 4, 2, 5}),
      EVOKER(EntityType.EVOKER, new int[]{0, 0, 0, 0, 0, 1, 1, 2}),
      PILLAGER(EntityType.PILLAGER, new int[]{0, 4, 3, 3, 4, 4, 4, 2}),
      WITCH(EntityType.WITCH, new int[]{0, 0, 0, 0, 3, 0, 0, 1}),
      RAVAGER(EntityType.RAVAGER, new int[]{0, 0, 0, 1, 0, 1, 0, 2});
         */

        // If you enable all illager join raid.
        // You will encounter difficult raids.

   /*
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        recommend config difficult
        Esay:none
        Common:Armored vindicator,Armored pillager,geomancer,mage
        Hard:Armored vindicator,Armored pillager,geomancer,mage,iceologer,mountaineer,windcaller,vindicator chef,royal guard.
        Hardcore:ALL
         ////////////////||||||||\\\\\\\\\\\\\\\\\\
        ///Ps : Hard and Hardcore may have bugs !\\\
        \\\\\\\\\\\\\\\\\||||||||///////////////////
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   */

        // WARRIOR
        if(DungeonsMobsConfig.COMMON.ENABLE_MOUNTAINEERS_IN_RAIDS.get()){
            Raid.WaveMember.create("mountaineer", ModEntityTypes.MOUNTAINEER.get(), new int[]{4, 4, 6, 7, 7, 8, 8, 8});
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_VINDICATOR_CHEFS_IN_RAIDS.get()){
            Raid.WaveMember.create("vindicator_chef", ModEntityTypes.VINDICATOR_CHEF.get(), new int[]{0, 0, 2, 0, 1, 4, 2, 5});
        }
        // ARMORED WARRIOR
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_VINDICATORS_IN_RAIDS.get()){
            Raid.WaveMember.create("armored_vindicator", ModEntityTypes.ARMORED_VINDICATOR.get(), new int[]{0, 0, 2, 2, 3, 4, 4, 5});

            Raid.WaveMember.create("vindicator", ModEntityTypes.VINDICATOR.get(), new int[]{0, 0, 5, 5, 6, 7, 8, 8});

            Raid.WaveMember.create("vindicator_raid_captain", ModEntityTypes.VINDICATOR_RAID_CAPTAIN.get(), new int[]{0, 0, 1, 0, 0, 1, 1, 2});
        }
        
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_MOUNTAINEERS_IN_RAIDS.get()){
            Raid.WaveMember.create("armored_mountaineer", ModEntityTypes.ARMORED_MOUNTAINEER.get(), new int[]{2, 2, 4, 3, 3, 7, 6, 7});

            Raid.WaveMember.create("rampart_captain", ModEntityTypes.RAMPART_CAPTAIN.get(), new int[]{0, 0, 1, 0, 0, 1, 1, 2});
        }

        if(DungeonsMobsConfig.COMMON.ENABLE_ROYAL_GUARDS_IN_RAIDS.get()){
            Raid.WaveMember.create("royal_guard", ModEntityTypes.ROYAL_GUARD.get(), new int[]{0, 0, 2, 4, 4, 6, 6, 8});

            Raid.WaveMember.create("tower_guard", ModEntityTypes.TOWER_GUARD.get(), new int[]{0, 0, 1, 3, 3, 4, 4, 6});

            Raid.WaveMember.create("powerful_royal_guard", ModEntityTypes.POWERFUL_ROYAL_GUARD.get(), new int[]{0, 0, 0, 1, 1, 2, 2, 3});
        }
        
        // SPELLCASTER
        if(DungeonsMobsConfig.COMMON.ENABLE_GEOMANCERS_IN_RAIDS.get()){
            Raid.WaveMember.create("geomancer", ModEntityTypes.GEOMANCER.get(), new int[] {0, 1, 1, 0, 1, 2, 4, 5});
        }
        
        if(DungeonsMobsConfig.COMMON.ENABLE_ILLUSIONERS_IN_RAIDS.get()){
            Raid.WaveMember.create("illusioner", ModEntityTypes.ILLUSIONER.get(), new int[] {0, 0, 0, 0, 0, 0, 0, 1});
        }
        
        if(DungeonsMobsConfig.COMMON.ENABLE_MAGES_IN_RAIDS.get()){
            Raid.WaveMember.create("mage", ModEntityTypes.MAGE.get(), new int[] {0, 1, 1, 0, 0, 1, 1, 1});
        }
        
        if(DungeonsMobsConfig.COMMON.ENABLE_ICEOLOGERS_IN_RAIDS.get()){           
            Raid.WaveMember.create("iceologer", ModEntityTypes.ICEOLOGER.get(), new int[] {0, 0, 1, 0, 1, 1, 1, 2});
        }

        if(DungeonsMobsConfig.COMMON.ENABLE_WINDCALLERS_IN_RAIDS.get()){
            Raid.WaveMember.create("windcaller", ModEntityTypes.WINDCALLER.get(), new int[] {0, 0, 1, 0, 1, 1, 1, 2});
        }
        
        // ARMORED RANGER
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_PILLAGERS_IN_RAIDS.get()){            
            Raid.WaveMember.create("armored_pillager", ModEntityTypes.ARMORED_PILLAGER.get(), new int[]{0, 3, 2, 2, 3, 3, 3, 4});
        }

        // BEAST / GOLEM
        if(DungeonsMobsConfig.COMMON.ENABLE_SQUALL_GOLEMS_IN_RAIDS.get()){
            Raid.WaveMember.create("squall_golem", ModEntityTypes.SQUALL_GOLEM.get(), new int[]{0, 0, 0, 1, 0, 1, 1, 2});
        }
        
        // BOSS
        if(DungeonsMobsConfig.COMMON.ENABLE_REDSTONE_GOLEMS_IN_RAIDS.get()){            
            Raid.WaveMember.create("redstone_golem", ModEntityTypes.REDSTONE_GOLEM.get(), new int[]{0, 0, 0, 0, 0, 1, 1, 2});
        }

        Raid.WaveMember.create("illager_warrior", ModEntityTypes.ILLAGER_WARRIOR.get(), new int[]{0, 0, 0, 0, 0, 0, 0, 1});


    }
}
