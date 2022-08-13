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

    public static final RegistryObject<Item> ICEOLOGER_HOOD = ITEMS.register("iceologer_hood",
        () -> new IceologerClothesItem(CosmeticArmorMaterial.INSTANCE, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> ICEOLOGER_ROBES = ITEMS.register("iceologer_robes",
        () -> new IceologerClothesItem(CosmeticArmorMaterial.INSTANCE, EquipmentSlotType.CHEST, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> ICEOLOGER_PANTS = ITEMS.register("iceologer_pants",
        () -> new IceologerClothesItem(CosmeticArmorMaterial.INSTANCE, EquipmentSlotType.LEGS, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> ICEOLOGER_SHOES = ITEMS.register("iceologer_shoes",
            () -> new IceologerClothesItem(CosmeticArmorMaterial.INSTANCE, EquipmentSlotType.FEET, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> GEOMANCER_BEADS = ITEMS.register("geomancer_beads",
            () -> new GeomancerClothesItem(CosmeticArmorMaterial.INSTANCE, EquipmentSlotType.HEAD, new Item.Properties().tab(DUNGEONS_MOBS)));

    public static final RegistryObject<Item> GEOMANCER_ROBES = ITEMS.register("geomancer_robes",
            () -> new GeomancerClothesItem(CosmeticArmorMaterial.INSTANCE, EquipmentSlotType.CHEST, new Item.Properties().tab(DUNGEONS_MOBS)));

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
