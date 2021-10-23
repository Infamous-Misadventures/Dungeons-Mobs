package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanHealPredicate;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.HEALS_ALLIES;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class HealsAlliesMobEnchantment extends MobEnchantment {

    private static float HEAL_PERCENTAGE = 0.25F;

    public HealsAlliesMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving();
        executeIfPresent(defender, HEALS_ALLIES.get(), () -> {
            applyToNearbyEntities(defender, 1.5F,
                    getCanHealPredicate(defender), (LivingEntity nearbyEntity) ->
                            nearbyEntity.heal(event.getAmount() * HEAL_PERCENTAGE)
            );
        });
    }
}