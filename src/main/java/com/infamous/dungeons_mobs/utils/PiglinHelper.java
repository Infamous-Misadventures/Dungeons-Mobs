package com.infamous.dungeons_mobs.utils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;

public class PiglinHelper {

    public static void stopAdmiringItem(Piglin piglin) {
        if (piglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM) && !piglin.getOffhandItem().isEmpty()) {
            piglin.spawnAtLocation(piglin.getOffhandItem());
            piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    public static <T extends Mob> void zombify(EntityType<T> convertToType, Piglin piglin) {
        T convertTo = piglin.convertTo(convertToType, true);
        if (convertTo != null) {
            convertTo.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(piglin, convertTo);
        }
    }
}
