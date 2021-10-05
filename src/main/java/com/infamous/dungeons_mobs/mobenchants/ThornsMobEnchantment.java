package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.THORNS;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ThornsMobEnchantment extends MobEnchantment {

    public ThornsMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        executeIfPresent(defender, THORNS.get(), () -> {
            // TODO Make Config Option
            float thornsDamage = event.getAmount() * 0.33F;
            if(thornsDamage > 0.5F) {
                Entity attacker = event.getSource().getEntity();
                if(attacker != null && attacker.isAlive()) {
                    attacker.hurt(DamageSource.thorns(defender), thornsDamage);
                }
            }
        });
    }
}