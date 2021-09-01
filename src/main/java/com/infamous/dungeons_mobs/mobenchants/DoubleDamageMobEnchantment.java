package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;
import static com.infamous.dungeons_mobs.mobenchants.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.DOUBLE_DAMAGE;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.PROTECTION;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class DoubleDamageMobEnchantment extends MobEnchantment {

    public DoubleDamageMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        executeIfPresent(attacker, DOUBLE_DAMAGE.get(), () -> {
            if (event.getAmount() == 0) {
                event.setAmount(1);
            } else {
                event.setAmount(event.getAmount() * 2);
            }
        });
    }
}
