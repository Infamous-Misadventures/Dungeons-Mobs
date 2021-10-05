package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class BurningMobEnchantment extends MobEnchantment {

    public BurningMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

//    executeIfPresent(defender, BURNING.get(), () -> {
//        AreaOfEffectHelper.burnNearbyEnemies(player, 1.0F * burningLevel, 1.5F);
//        comboCap.setBurnNearbyTimer(10);
//    });
    }



//    ICombo comboCap = CapabilityHelper.getComboCapability(entity);
//            if(comboCap == null) return;
//    int burnNearbyTimer = comboCap.getBurnNearbyTimer();
//    if(burnNearbyTimer <= 0){
//        int burningLevel = EnchantmentHelper.getEnchantmentLevel(ArmorEnchantmentList.BURNING, player);
//        if(uniqueArmorFlag) burningLevel++;
//        AreaOfEffectHelper.burnNearbyEnemies(player, 1.0F * burningLevel, 1.5F);
//        comboCap.setBurnNearbyTimer(10);
//    }
//    else{
//        comboCap.setBurnNearbyTimer(burnNearbyTimer - 1);
//    }
//            else{
//        if(burnNearbyTimer != 10){
//            comboCap.setBurnNearbyTimer(10);
//        }
//    }
//    LivingEntity defender = (LivingEntity) event.getEntity();
//    executeIfPresent(defender, BURNING.get(), () -> {
//        AreaOfEffectHelper.burnNearbyEnemies(player, 1.0F * burningLevel, 1.5F);
//        comboCap.setBurnNearbyTimer(10);
//    });
}