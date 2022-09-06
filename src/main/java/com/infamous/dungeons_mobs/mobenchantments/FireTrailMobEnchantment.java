package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.mod.ModBlocks;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.FIRE_TRAIL;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class FireTrailMobEnchantment extends MobEnchantment {

    public FireTrailMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        executeIfPresent(entity, FIRE_TRAIL.get(), () -> {
            if (entity.level.isEmptyBlock(entity.blockPosition()) && !entity.level.isEmptyBlock(entity.blockPosition().below())) {
                entity.level.setBlock(entity.blockPosition(), ModBlocks.CORRUPTED_PYRE_BLOCK.get().defaultBlockState(), 3);
            }
        });
    }
}
