package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.mobenchants.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.PROTECTION;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.REGENERATION;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class RegenerationMobEnchantment extends MobEnchantment {

    public RegenerationMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        executeIfPresent(entity, REGENERATION.get(), () -> {
            if (entity.getHealth() < entity.getMaxHealth() && entity.tickCount % 20 == 0) {
                entity.heal(1.0F);
            }
        });
    }
}