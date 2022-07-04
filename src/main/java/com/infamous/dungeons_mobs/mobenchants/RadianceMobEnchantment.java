package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanHealPredicate;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.RADIANCE;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class RadianceMobEnchantment extends MobEnchantment {

    public RadianceMobEnchantment(Rarity rarity) {
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
            executeIfPresent((LivingEntity) attacker, RADIANCE.get(), () -> {
                LivingEntity source = event.getSource().isProjectile() ? event.getEntityLiving() : (LivingEntity) attacker;
                applyToNearbyEntities(source, 1.5F,
                        getCanHealPredicate(source), (LivingEntity nearbyEntity) ->
                                nearbyEntity.heal(2)
                );
            });
    }
}