package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.RUSH;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class RushMobEnchant extends MobEnchant {

    public RushMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        executeIfPresentWithLevel(defender, RUSH.get(), (level) -> {
            defender.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 10+20*level, 3, false, false));
        });

    }
}