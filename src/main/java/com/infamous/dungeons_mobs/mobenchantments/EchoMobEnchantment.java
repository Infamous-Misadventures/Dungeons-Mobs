package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.utils.EchoDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.ECHO;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class EchoMobEnchantment extends MobEnchantment {

    private static float ECHO_CHANCE = 0.25f;

    public EchoMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        Entity entity = event.getSource().getEntity();
        if(entity instanceof LivingEntity && isMelee(event.getSource()) && !(event.getSource() instanceof EchoDamageSource)) {
            LivingEntity attacker = (LivingEntity) entity;
            executeIfPresent(attacker, ECHO.get(), () -> {
                if(attacker.getRandom().nextFloat() <= ECHO_CHANCE){
                    defender.hurt(new EchoDamageSource(attacker), event.getAmount());
                    defender.invulnerableTime = 0;
                }
            });
        }
    }

    private static boolean isMelee(DamageSource source) {
        return !source.isProjectile() && !source.isExplosion() && !source.isMagic() && !source.isFire();
    }
}