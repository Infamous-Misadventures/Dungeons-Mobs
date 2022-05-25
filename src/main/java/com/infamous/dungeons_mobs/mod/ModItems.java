package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.items.*;
import com.infamous.dungeons_mobs.items.shield.RoyalGuardShieldItem;
import com.infamous.dungeons_mobs.items.shield.SkeletonVanguardShieldItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_mobs.DungeonsMobs.DUNGEONS_MOBS;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // ZOMBIES
    public static final RegistryObject<ModSpawnEggItem> ARMORED_ZOMBIE_SPAWN_EGG = ITEMS.register("armored_zombie_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_ZOMBIE,
                    44975, 7969893,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> JUNGLE_ZOMBIE_SPAWN_EGG = ITEMS.register("jungle_zombie_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.JUNGLE_ZOMBIE,
                    44975, 7969893,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> FROZEN_ZOMBIE_SPAWN_EGG = ITEMS.register("frozen_zombie_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.FROZEN_ZOMBIE,
                    44975, 7969893,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // SKELETONS
    public static final RegistryObject<ModSpawnEggItem> ARMORED_SKELETON_SPAWN_EGG = ITEMS.register("armored_skeleton_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_SKELETON,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> MOSSY_SKELETON_SPAWN_EGG = ITEMS.register("mossy_skeleton_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.MOSSY_SKELETON,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> SKELETON_VANGUARD_SPAWN_EGG = ITEMS.register("skeleton_vanguard_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.SKELETON_VANGUARD,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> SKELETON_HORSEMAN_SPAWN_EGG = ITEMS.register("skeleton_horseman_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.SKELETON_HORSEMAN,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> NECROMANCER_SPAWN_EGG = ITEMS.register("necromancer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.NECROMANCER,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));


    // ILLAGERS
    public static final RegistryObject<ModSpawnEggItem> ARMORED_VINDICATOR_SPAWN_EGG = ITEMS.register("armored_vindicator_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_VINDICATOR,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> VINDICATOR_RAID_CAPTAIN_SPAWN_EGG = ITEMS.register("vindicator_raid_captain_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.VINDICATOR_RAID_CAPTAIN,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> RAMPART_CAPTAIN_SPAWN_EGG = ITEMS.register("rampart_captain_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.RAMPART_CAPTAIN,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ARMORED_PILLAGER_SPAWN_EGG = ITEMS.register("armored_pillager_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_PILLAGER,
                    5451574, 9804699,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ROYAL_GUARD_SPAWN_EGG = ITEMS.register("royal_guard_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ROYAL_GUARD,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> POWERFUL_ROYAL_GUARD_SPAWN_EGG = ITEMS.register("powerful_royal_guard_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.POWERFUL_ROYAL_GUARD,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> TOWER_GUARD_SPAWN_EGG = ITEMS.register("tower_guard_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.TOWER_GUARD,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> EVOKER_SPAWN_EGG = ITEMS.register("evoker_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.DUNGEONS_EVOKER,
                    9804699, 1973274,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ILLAGER_WARRIOR_SPAWN_EGG = ITEMS.register("illager_warrior_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ILLAGER_WARRIOR,
                    9804699, 1973274,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ILLUSIONER_SPAWN_EGG = ITEMS.register("illusioner_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ILLUSIONER,
                    9804699, 1973274,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ICEOLOGER_SPAWN_EGG = ITEMS.register("iceologer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ICEOLOGER,
                    9804699, 1973274,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> GEOMANCER_SPAWN_EGG = ITEMS.register("geomancer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.GEOMANCER,
                    0x373b3b, 0x8b5ea3,
                    new Item.Properties().tab(DUNGEONS_MOBS)));
    
    public static final RegistryObject<ModSpawnEggItem> MAGE_SPAWN_EGG = ITEMS.register("mage_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.MAGE,
                    0x951f75, 0xe3ab58,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> WINDCALLER_SPAWN_EGG = ITEMS.register("windcaller_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.WINDCALLER,
                    9804699, 1973274,
                    new Item.Properties().tab(DUNGEONS_MOBS)));
    
    public static final RegistryObject<ModSpawnEggItem> ENCHANTER_SPAWN_EGG = ITEMS.register("enchanter_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ENCHANTER,
                    0x62162c, 0xfbd600,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> VINDICATOR_CHEF_SPAWN_EGG = ITEMS.register("vindicator_chef_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.VINDICATOR_CHEF,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> MOUNTAINEER_SPAWN_EGG = ITEMS.register("mountaineer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.MOUNTAINEER,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ARMORED_MOUNTAINEER_SPAWN_EGG = ITEMS.register("armored_mountaineer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_MOUNTAINEER,
                    9804699, 2580065,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // CREEPERS
    public static final RegistryObject<ModSpawnEggItem> ICY_CREEPER_SPAWN_EGG = ITEMS.register("icy_creeper_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ICY_CREEPER,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // WRAITH
    public static final RegistryObject<ModSpawnEggItem> WRAITH_SPAWN_EGG = ITEMS.register("wraith_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.WRAITH,
                    0x1a1862, 0x2586d9,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // SLIME
    public static final RegistryObject<ModSpawnEggItem> CONJURED_SLIME_SPAWN_EGG = ITEMS.register("conjured_slime_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.CONJURED_SLIME,
                    5349438, 8306542,
                    new Item.Properties().tab(DUNGEONS_MOBS)));
    // REDSTONE
    public static final RegistryObject<ModSpawnEggItem> REDSTONE_CUBE_SPAWN_EGG = ITEMS.register("redstone_cube_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.REDSTONE_CUBE,
                    10489616, 12040119,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> REDSTONE_GOLEM_SPAWN_EGG = ITEMS.register("redstone_golem_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.REDSTONE_GOLEM,
                    12040119, 10489616,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // JUNGLE
    public static final RegistryObject<ModSpawnEggItem> WHISPERER_SPAWN_EGG = ITEMS.register("whisperer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.WHISPERER,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> LEAPLEAF_SPAWN_EGG = ITEMS.register("leapleaf_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.LEAPLEAF,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> POISON_QUILL_VINE_SPAWN_EGG = ITEMS.register("poison_quill_vine_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.POISON_QUILL_VINE,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> QUICK_GROWING_VINE_SPAWN_EGG = ITEMS.register("quick_growing_vine_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.QUICK_GROWING_VINE,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // GOLEM
    public static final RegistryObject<ModSpawnEggItem> SQUALL_GOLEM_SPAWN_EGG = ITEMS.register("squall_golem_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.SQUALL_GOLEM,
                    13552826, 7632531,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // PIGLIN
    public static final RegistryObject<ModSpawnEggItem> ARMORED_PIGLIN_SPAWN_EGG = ITEMS.register("armored_piglin_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_PIGLIN,
                    5843472, 16380836,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ARMORED_ZOMBIFIED_PIGLIN_SPAWN_EGG = ITEMS.register("zombified_armored_piglin_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ZOMBIFIED_ARMORED_PIGLIN,
                    15373203, 5009705,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> FUNGUS_THROWER_SPAWN_EGG = ITEMS.register("fungus_thrower_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.FUNGUS_THROWER,
                    5843472, 16380836,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ZOMBIFIED_FUNGUS_THROWER_SPAWN_EGG = ITEMS.register("zombified_fungus_thrower_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER,
                    15373203, 5009705,
                    new Item.Properties().tab(DUNGEONS_MOBS)));


    public static final RegistryObject<ModSpawnEggItem> WAVEWHISPERER_SPAWN_EGG = ITEMS.register("wavewhisperer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.WAVEWHISPERER,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> POISON_ANEMONE_SPAWN_EGG = ITEMS.register("poison_anemone_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.POISON_ANEMONE,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> QUICK_GROWING_ANEMONE_SPAWN_EGG = ITEMS.register("quick_growing_anemone_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.QUICK_GROWING_ANEMONE,
                    894731, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ARMORED_SUNKEN_SKELETON_SPAWN_EGG = ITEMS.register("armored_sunken_skeleton_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_SUNKEN_SKELETON,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> SUNKEN_SKELETON_SPAWN_EGG = ITEMS.register("sunken_skeleton_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.SUNKEN_SKELETON,
                    12698049, 4802889,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> ARMORED_DROWNED_SPAWN_EGG = ITEMS.register("armored_drowned_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ARMORED_DROWNED,
                    44975, 7969893,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> DROWNED_NECROMANCER_SPAWN_EGG = ITEMS.register("drowned_necromancer_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.DROWNED_NECROMANCER,
                    44975, 7969893,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // END MOBS


    public static final RegistryObject<ModSpawnEggItem> ENDERSENT_SPAWN_EGG = ITEMS.register("endersent_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.ENDERSENT,
                    1447446, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> BLASTLING_SPAWN_EGG = ITEMS.register("blastling_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.BLASTLING,
                    1447446, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> WATCHLING_SPAWN_EGG = ITEMS.register("watchling_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.WATCHLING,
                    1447446, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<ModSpawnEggItem> SNARELING_SPAWN_EGG = ITEMS.register("snareling_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.SNARELING,
                    1447446, 0,
                    new Item.Properties().tab(DUNGEONS_MOBS)));

    // SHIELD
    public static final RegistryObject<Item> ROYAL_GUARD_SHIELD = ITEMS.register("royal_guard_shield",
            () -> new RoyalGuardShieldItem(new Item.Properties().tab(DUNGEONS_MOBS).durability(336)));

    public static final RegistryObject<Item> SKELETON_VANGUARD_SHIELD = ITEMS.register("skeleton_vanguard_shield",
            () -> new SkeletonVanguardShieldItem(new Item.Properties().tab(DUNGEONS_MOBS).durability(336)));

    // HELMETS
    public static final RegistryObject<Item> GOLD_PILLAGER_HELMET = ITEMS.register("gold_pillager_helmet",
            () -> new PillagerHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS), false));

    public static final RegistryObject<Item> DIAMOND_PILLAGER_HELMET = ITEMS.register("diamond_pillager_helmet",
            () -> new PillagerHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS), true));

    public static final RegistryObject<Item> GOLD_VINDICATOR_HELMET = ITEMS.register("gold_vindicator_helmet",
            () -> new VindicatorHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS), false));

    public static final RegistryObject<Item> DIAMOND_VINDICATOR_HELMET = ITEMS.register("diamond_vindicator_helmet",
            () -> new VindicatorHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS), true));

    public static final RegistryObject<Item> SKELETON_VANGUARD_HELMET = ITEMS.register("skeleton_vanguard_helmet",
            () -> new SkeletonVanguardHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> CHEF_HAT = ITEMS.register("chef_hat",
            () -> new ChefHatItem(ArmorMaterial.LEATHER, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> NETHERITE_PIGLIN_HELMET = ITEMS.register("netherite_piglin_helmet",
            () -> new PiglinHelmetItem(CustomArmorMaterial.PURE_NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));
    public static final RegistryObject<Item> CRACKED_NETHERITE_PIGLIN_HELMET = ITEMS.register("cracked_netherite_piglin_helmet",
            () -> new PiglinHelmetItem(CustomArmorMaterial.PURE_NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));
    public static final RegistryObject<Item> GOLD_PIGLIN_HELMET = ITEMS.register("gold_piglin_helmet",
            () -> new PiglinHelmetItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));
    public static final RegistryObject<Item> CRACKED_GOLD_PIGLIN_HELMET = ITEMS.register("cracked_gold_piglin_helmet",
            () -> new PiglinHelmetItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));

    // SOUL FIRE CHARGE
    public static final RegistryObject<WraithFireChargeItem> WRAITH_FIRE_CHARGE = ITEMS.register("wraith_fire_charge",
            () -> new WraithFireChargeItem(new Item.Properties().tab(DUNGEONS_MOBS)));

    // SPATULA
    public static final RegistryObject<Item> SPATULA = ITEMS.register("spatula",
            () -> new SpatulaItem(ItemTier.WOOD, 0.5F, (2.0F-4.0F), new Item.Properties().tab(DUNGEONS_MOBS)));

    // MOUNTAINEER AXES
    public static final RegistryObject<Item> MOUNTAINEER_AXE = ITEMS.register("mountaineer_axe",
            () -> new MountaineerAxeItem(ItemTier.IRON, 1, (1.2F-4.0F), new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> GOLD_MOUNTAINEER_AXE = ITEMS.register("gold_mountaineer_axe",
            () -> new MountaineerAxeItem(ItemTier.IRON, 1, (1.2F-4.0F), new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> DIAMOND_MOUNTAINEER_AXE = ITEMS.register("diamond_mountaineer_axe",
            () -> new MountaineerAxeItem(ItemTier.DIAMOND, 1, (1.2F-4.0F), new Item.Properties().tab(DUNGEONS_MOBS)));

    // STAFFS
    public static final RegistryObject<Item> WINDCALLER_STAFF = ITEMS.register("windcaller_staff",
            () -> new WindcallerStaffItem(new Item.Properties().tab(DUNGEONS_MOBS).durability(64)));

    public static final RegistryObject<Item> GEOMANCER_STAFF = ITEMS.register("geomancer_staff",
            () -> new GeomancerStaffItem(new Item.Properties().tab(DUNGEONS_MOBS).durability(64)));

    public static final RegistryObject<Item> NECROMANCER_STAFF = ITEMS.register("necromancer_staff",
            () -> new NecromancerStaffItem(new Item.Properties().tab(DUNGEONS_MOBS).durability(64)));

    public static final RegistryObject<Item> NECROMANCER_TRIDENT = ITEMS.register("necromancer_trident",
            () -> new NecromancerTridentItem(new Item.Properties().tab(DUNGEONS_MOBS).durability(64)));

    public static final RegistryObject<Item> BLUE_NETHERSHROOM = ITEMS.register("blue_nethershroom",
            () -> new BlueNethershroomItem(new Item.Properties().tab(DUNGEONS_MOBS).stacksTo(16)));

    public static final RegistryObject<Item> YELLOW_TRIDENT = ITEMS.register("yellow_trident",
            () -> new ColoredTridentItem((new Item.Properties().durability(250).tab(DUNGEONS_MOBS)), DyeColor.YELLOW));

    public static final RegistryObject<Item> PURPLE_TRIDENT = ITEMS.register("purple_trident",
            () -> new ColoredTridentItem((new Item.Properties().durability(250).tab(DUNGEONS_MOBS)), DyeColor.PURPLE));
}
