package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.DOUBLE_DAMAGE;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ZDoneDoubleDamageMobEnchantment extends MobEnchantment {

    public ZDoneDoubleDamageMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        Entity attacker;
        if (!event.getSource().isProjectile()) {
            attacker = event.getSource().getDirectEntity();
        } else {
            attacker = event.getSource().getEntity();
        }
        if (attacker instanceof LivingEntity)
            executeIfPresent((LivingEntity) attacker, DOUBLE_DAMAGE.get(), () -> {
                if (event.getAmount() == 0) {
                    event.setAmount(1);
                } else {
                    event.setAmount(event.getAmount() * 2);
                }
            });
    }
}
