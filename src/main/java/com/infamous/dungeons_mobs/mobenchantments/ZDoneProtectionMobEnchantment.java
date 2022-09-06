package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.PROTECTION;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ZDoneProtectionMobEnchantment extends MobEnchantment {

    public ZDoneProtectionMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        executeIfPresent(defender, PROTECTION.get(), () -> {
            event.setAmount(event.getAmount() / 2);
        });

    }
}