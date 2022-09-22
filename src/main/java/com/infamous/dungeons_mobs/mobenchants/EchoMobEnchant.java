package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.utils.EchoDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.ECHO;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class EchoMobEnchant extends MobEnchant {

    private static float ECHO_CHANCE = 0.25f;

    public EchoMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity defender = (LivingEntity) event.getEntity();
        Entity entity = event.getSource().getEntity();
        if(entity instanceof LivingEntity && isMelee(event.getSource()) && !(event.getSource() instanceof EchoDamageSource)) {
            LivingEntity attacker = (LivingEntity) entity;
            executeIfPresentWithLevel(attacker, ECHO.get(), (level) -> {
                if(attacker.getRandom().nextFloat() <= ECHO_CHANCE*level){
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