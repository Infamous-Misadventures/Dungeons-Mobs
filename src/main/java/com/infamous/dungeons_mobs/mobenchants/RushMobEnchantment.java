package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.mobenchants.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.PROTECTION;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.RUSH;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class RushMobEnchantment extends MobEnchantment {

    public RushMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        executeIfPresent(defender, RUSH.get(), () -> {
            defender.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 70, 3, false, false));
        });

    }
}