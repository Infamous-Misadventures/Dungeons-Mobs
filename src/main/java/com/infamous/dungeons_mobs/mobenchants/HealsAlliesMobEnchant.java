package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanHealPredicate;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.HEALS_ALLIES;

//@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class HealsAlliesMobEnchant extends MobEnchant {

    private static float HEAL_PERCENTAGE = 0.10F;

    public HealsAlliesMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = event.getEntityLiving();
        executeIfPresentWithLevel(defender, HEALS_ALLIES.get(), (level) -> {
            applyToNearbyEntities(defender, 1.5F,
                    getCanHealPredicate(defender), (LivingEntity nearbyEntity) ->
                            nearbyEntity.heal(event.getAmount() * HEAL_PERCENTAGE*level)
            );
        });
    }
}