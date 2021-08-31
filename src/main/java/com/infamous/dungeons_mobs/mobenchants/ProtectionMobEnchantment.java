package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.DOUBLE_DAMAGE;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.PROTECTION;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ProtectionMobEnchantment extends MobEnchantment {

    public ProtectionMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        if (defender != null) {
            getEnchantableCapability(defender).ifPresent(cap -> {
                if(cap.hasEnchantment(PROTECTION.get())) {
                    event.setAmount(event.getAmount() / 2);
                }
            });
        }
    }
}