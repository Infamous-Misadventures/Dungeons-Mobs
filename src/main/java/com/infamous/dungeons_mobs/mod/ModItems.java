package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.items.*;
import com.infamous.dungeons_mobs.items.armor.WindcallerClothesArmorGear;
import com.infamous.dungeons_mobs.items.shield.RoyalGuardShieldItem;
import com.infamous.dungeons_mobs.items.shield.SkeletonVanguardShieldItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.infamous.dungeons_mobs.DungeonsMobs.DUNGEONS_MOBS;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static net.minecraft.item.ArmorMaterial.DIAMOND;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final Item.Properties ARMOR_PROPERTIES = DungeonsMobsConfig.COMMON.ENABLE_ITEM_TAB.get() ?
            new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS) : new Item.Properties();

    // SHIELD
    public static final RegistryObject<Item> ROYAL_GUARD_SHIELD = ITEMS.register("royal_guard_shield",
            () -> new RoyalGuardShieldItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).durability(336)));

    public static final RegistryObject<Item> SKELETON_VANGUARD_SHIELD = ITEMS.register("skeleton_vanguard_shield",
            () -> new SkeletonVanguardShieldItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).durability(336)));

    // HELMETS
    public static final RegistryObject<Item> GOLD_PILLAGER_HELMET = ITEMS.register("gold_pillager_helmet",
            () -> new PillagerHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS), false));

    public static final RegistryObject<Item> DIAMOND_PILLAGER_HELMET = ITEMS.register("diamond_pillager_helmet",
            () -> new PillagerHelmetItem(ArmorMaterial.DIAMOND, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS), true));

    public static final RegistryObject<Item> GOLD_VINDICATOR_HELMET = ITEMS.register("gold_vindicator_helmet",
            () -> new VindicatorHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS), false));

    public static final RegistryObject<Item> DIAMOND_VINDICATOR_HELMET = ITEMS.register("diamond_vindicator_helmet",
            () -> new VindicatorHelmetItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS), true));

    public static final RegistryObject<Item> NETHERITE_PIGLIN_HELMET = ITEMS.register("netherite_piglin_helmet",
            () -> new PiglinHelmetItem(CustomArmorMaterial.PURE_NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));
    public static final RegistryObject<Item> CRACKED_NETHERITE_PIGLIN_HELMET = ITEMS.register("cracked_netherite_piglin_helmet",
            () -> new PiglinHelmetItem(CustomArmorMaterial.PURE_NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));
    public static final RegistryObject<Item> GOLD_PIGLIN_HELMET = ITEMS.register("gold_piglin_helmet",
            () -> new PiglinHelmetItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));
    public static final RegistryObject<Item> CRACKED_GOLD_PIGLIN_HELMET = ITEMS.register("cracked_gold_piglin_helmet",
            () -> new PiglinHelmetItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));

    public static final ArmorSet DROWNED_NECROMANCER_ROBES = registerArmorSet("drowned_necromancer_armor", "drowned_necromancer_crown", "drowned_necromancer_cloak", "drowned_necromancer_belt", null);
    public static final ArmorSet GEOMANCER_CLOTHES = registerArmorSet("geomancer_clothes", "geomancer_beads", "geomancer_robes", null, null);
    public static final ArmorSet ICEOLOGER_CLOTHES = registerArmorSet("iceologer_clothes", "iceologer_hood", "iceologer_robes", "iceologer_pants", "iceologer_shoes");
    public static final ArmorSet ILLUSIONER_CLOTHES = registerArmorSet("illusioner_clothes", "illusioner_hood", "illusioner_robes", "illusioner_pants", "illusioner_shoes");
    public static final ArmorSet NECROMANCER_ROBES = registerArmorSet("necromancer_armor", "necromancer_crown", "necromancer_cloak", "necromancer_belt", null);
    public static final ArmorSet NETHERPLATE_ARMOR = registerArmorSet("netherplate_armor", "netherplate_helmet", null, null, null);
    public static final ArmorSet ROYAL_GUARD_ARMOR = registerArmorSet("royal_guard_armor", "royal_guard_helmet", "royal_guard_chestplate", "royal_guard_legs", "royal_guard_sabatons");
    public static final ArmorSet VANGUARD_ARMOR = registerArmorSet("vanguard_armor", "vanguard_helmet", "vanguard_chestplate", "vanguard_legs", null);
    public static final ArmorSet WINDCALLER_CLOTHES = registerArmorSetWindcallerClothes("windcaller_armor", "wind_wig", "windcaller_robes", null, null);
    public static final ArmorSet MOUNTAINEER_ARMOR = registerArmorSet("mountaineer_armor", "moutaineer_hood", "mountaineer_jacket", "mountaineer_pants", "mountaineer_boots");
    public static final ArmorSet ARMORED_MOUNTAINEER_ARMOR = registerArmorSet("armoured_mountaineer_armor", "armored_mountaineer_hood", "armored_mountaineer_jacket", "armored_mountaineer_pants", "armored_mountaineer_boots");
    public static final ArmorSet STRONG_ARMORED_MOUNTAINEER_ARMOR = registerArmorSet("strong_armoured_mountaineer_armor", "strong_armored_mountaineer_hood", "strong_armored_mountaineer_jacket", "strong_armored_mountaineer_pants", "strong_armored_mountaineer_boots");
    public static final ArmorSet ENCHANTER_ARMOR = registerArmorSet("enchanter_armor", "enchanter_hat", "enchanter_robes", null, null);
    public static final ArmorSet FUNGUS_THROWER_ARMOR = registerArmorSet("fungus_thrower_armor", "fungus_thrower_headband", "fungus_thrower_pack", null, null);
    public static final ArmorSet MAGE_ARMOR = registerArmorSet("mage_armor", "mage_hood", "mage_robes", "mage_pants", "mage_shoes");
    public static final ArmorSet VINDICATOR_CHEF_ARMOR = registerArmorSet("vindicator_chef_armor", "chef_hat", "chef_apron", null, null);

    // SOUL FIRE CHARGE
    public static final RegistryObject<WraithFireChargeItem> WRAITH_FIRE_CHARGE = ITEMS.register("wraith_fire_charge",
            () -> new WraithFireChargeItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));

    // SPATULA
    public static final RegistryObject<Item> SPATULA = ITEMS.register("spatula",
            () -> new SpatulaItem(ItemTier.WOOD, 0.5F, (2.0F-4.0F), new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));

    // MOUNTAINEER AXES
    public static final RegistryObject<Item> MOUNTAINEER_AXE = ITEMS.register("mountaineer_axe",
            () -> new MountaineerAxeItem(ItemTier.IRON, 1, (1.2F-4.0F), new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));

    public static final RegistryObject<Item> GOLD_MOUNTAINEER_AXE = ITEMS.register("gold_mountaineer_axe",
            () -> new MountaineerAxeItem(ItemTier.IRON, 1, (1.2F-4.0F), new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));

    public static final RegistryObject<Item> DIAMOND_MOUNTAINEER_AXE = ITEMS.register("diamond_mountaineer_axe",
            () -> new MountaineerAxeItem(ItemTier.DIAMOND, 1, (1.2F-4.0F), new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)));

    // STAFFS
    public static final RegistryObject<Item> WINDCALLER_STAFF = ITEMS.register("windcaller_staff",
            () -> new WindcallerStaffItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).durability(64)));

    public static final RegistryObject<Item> GEOMANCER_STAFF = ITEMS.register("geomancer_staff",
            () -> new GeomancerStaffItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).durability(64)));

    public static final RegistryObject<Item> NECROMANCER_STAFF = ITEMS.register("necromancer_staff",
            () -> new NecromancerStaffItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).durability(64)));

    public static final RegistryObject<Item> NECROMANCER_TRIDENT = ITEMS.register("necromancer_trident",
            () -> new NecromancerTridentItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).durability(64)));

    public static final RegistryObject<Item> BLUE_NETHERSHROOM = ITEMS.register("blue_nethershroom",
            () -> new BlueNethershroomItem(new Item.Properties().tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS).stacksTo(16)));

    public static final RegistryObject<Item> YELLOW_TRIDENT = ITEMS.register("yellow_trident",
            () -> new ColoredTridentItem((new Item.Properties().durability(250).tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)), DyeColor.YELLOW));

    public static final RegistryObject<Item> PURPLE_TRIDENT = ITEMS.register("purple_trident",
            () -> new ColoredTridentItem((new Item.Properties().durability(250).tab(DungeonsMobs.DUNGEONS_MOBS_ITEMS)), DyeColor.PURPLE));



    private static ArmorSet registerArmorSet(String armorSetId, String helmetId, String chestId, String legsId, String bootsId, boolean animated) {
        ResourceLocation armorSet = new ResourceLocation(MODID, armorSetId);
        ResourceLocation modelLocation = new ResourceLocation(MODID, "geo/armor/"+armorSetId+".geo.json");
        ResourceLocation textureLocation = new ResourceLocation(MODID, "textures/models/armor/"+armorSetId+".png");
        ResourceLocation animationFileLocation = animated ? new ResourceLocation(MODID, "animations/armor/" + armorSetId + ".animation.json") : new ResourceLocation(DungeonsLibraries.MODID, "animations/armor/armor_default.animation.json");
        return new ArmorSet(
                armorSetId,
                registerArmor(helmetId, () -> new ArmorGear(EquipmentSlotType.HEAD, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation)),
                registerArmor(chestId, () -> new ArmorGear(EquipmentSlotType.CHEST, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation)),
                registerArmor(legsId, () -> new ArmorGear(EquipmentSlotType.LEGS, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation)),
                registerArmor(bootsId, () -> new ArmorGear(EquipmentSlotType.FEET, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation))
        );
    }

    private static ArmorSet registerArmorSet(String armorSetId, String helmetId, String chestId, String legsId, String bootsId) {
        return registerArmorSet(armorSetId, helmetId, chestId, legsId, bootsId, false);
    }

    private static ArmorSet registerArmorSetWindcallerClothes(String armorSetId, String helmetId, String chestId, String legsId, String bootsId) {
        return registerArmorSetWindcallerClothes(armorSetId, helmetId, chestId, legsId, bootsId, false);
    }

    private static ArmorSet registerArmorSetWindcallerClothes(String armorSetId, String helmetId, String chestId, String legsId, String bootsId, boolean animated) {
        ResourceLocation armorSet = new ResourceLocation(MODID, armorSetId);
        ResourceLocation modelLocation = new ResourceLocation(MODID, "geo/armor/"+armorSetId+".geo.json");
        ResourceLocation textureLocation = new ResourceLocation(MODID, "textures/models/armor/"+armorSetId+".png");
        ResourceLocation animationFileLocation = animated ? new ResourceLocation(MODID, "animations/armor/" + armorSetId + ".animation.json") : new ResourceLocation(DungeonsLibraries.MODID, "animations/armor/armor_default.animation.json");
        return new ArmorSet(
                armorSetId,
                registerArmor(helmetId, () -> new WindcallerClothesArmorGear(EquipmentSlotType.HEAD, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation)),
                registerArmor(chestId, () -> new WindcallerClothesArmorGear(EquipmentSlotType.CHEST, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation)),
                registerArmor(legsId, () -> new WindcallerClothesArmorGear(EquipmentSlotType.LEGS, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation)),
                registerArmor(bootsId, () -> new WindcallerClothesArmorGear(EquipmentSlotType.FEET, ARMOR_PROPERTIES, armorSet, modelLocation, textureLocation, animationFileLocation))
        );
    }

    private static RegistryObject<Item> registerArmor(String armorId, Supplier<Item> itemSupplier) {
        if(armorId == null) return null;
        return ITEMS.register(armorId, itemSupplier);
    }
}
