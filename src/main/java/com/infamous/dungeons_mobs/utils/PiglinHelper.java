package com.infamous.dungeons_mobs.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;

public class PiglinHelper {

    public static void stopAdmiringItem(PiglinEntity piglin) {
        if (piglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM) && !piglin.getOffhandItem().isEmpty()) {
            piglin.spawnAtLocation(piglin.getOffhandItem());
            piglin.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    public static <T extends MobEntity> void zombify(EntityType<T> convertToType, PiglinEntity piglin) {
        T convertTo = piglin.convertTo(convertToType, true);
        if (convertTo != null) {
            convertTo.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(piglin, convertTo);
        }
    }
}
