package com.infamous.dungeons_mobs.datagen;

import com.infamous.dungeons_mobs.tags.EntityTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.function.Consumer;

public class ModNetherAdvancements implements Consumer<Consumer<Advancement>> {
   private static final EntityPredicate.Composite DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE = EntityPredicate.Composite.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of(Items.GOLDEN_HELMET).build()).build())).invert().build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().chest(ItemPredicate.Builder.item().of(Items.GOLDEN_CHESTPLATE).build()).build())).invert().build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().legs(ItemPredicate.Builder.item().of(Items.GOLDEN_LEGGINGS).build()).build())).invert().build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of(Items.GOLDEN_BOOTS).build()).build())).invert().build());

   public void accept(Consumer<Advancement> consumer) {
      Advancement netherRoot = Advancement.Builder.advancement().display(Blocks.RED_NETHER_BRICKS, Component.translatable("advancements.nether.root.title"), Component.translatable("advancements.nether.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/nether.png"), FrameType.TASK, false, false, false).addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(consumer, "nether/root");
      Advancement.Builder.advancement().parent(netherRoot).requirements(RequirementsStrategy.OR).display(Items.GOLD_INGOT, Component.translatable("advancements.nether.distract_piglin.title"), Component.translatable("advancements.nether.distract_piglin.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("distract_piglin", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE, ItemPredicate.Builder.item().of(ItemTags.PIGLIN_LOVED).build(), EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().of(EntityTags.PIGLINS).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build()).build()))).addCriterion("distract_piglin_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE, ItemPredicate.Builder.item().of(PiglinAi.BARTERING_ITEM), EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().of(EntityTags.PIGLINS).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build()).build()))).save(consumer, "nether/distract_piglin");
   }
}