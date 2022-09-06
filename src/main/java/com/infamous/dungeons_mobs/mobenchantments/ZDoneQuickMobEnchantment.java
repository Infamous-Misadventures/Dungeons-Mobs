package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.mod.ModMobEnchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ZDoneQuickMobEnchantment extends MobEnchantment {
	   
    public ZDoneQuickMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        executeIfPresent(entity, ModMobEnchantments.QUICK.get(), () -> {
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 10, 1, false, false));
        });
    }
}
