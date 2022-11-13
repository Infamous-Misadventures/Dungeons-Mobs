package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.capabilities.properties.IMobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.CHILLING;

//@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ChillingMobEnchant extends MobEnchant {

    public ChillingMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        executeIfPresentWithLevel(entity, CHILLING.get(), (level) -> {
            IMobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
            if(comboCap == null) return;
            int freezeNearbyTimer = comboCap.getFreezeNearbyTimer();
            if(freezeNearbyTimer <= 0){
                PROXY.spawnParticles(entity, ParticleTypes.ITEM_SNOWBALL);
                applyToNearbyEntities(entity, 1.5F,
                        getCanApplyToEnemyPredicate(entity), (LivingEntity nearbyEntity) -> {
                            freezeEnemy(1, nearbyEntity, level);
                            PROXY.spawnParticles(nearbyEntity, ParticleTypes.ITEM_SNOWBALL);
                        }
                );
                comboCap.setFreezeNearbyTimer(40);
            }
            else{
                comboCap.setFreezeNearbyTimer(freezeNearbyTimer - 1);
            }
        });
    }

    private static void freezeEnemy(int amplifier, LivingEntity nearbyEntity, int durationInSeconds) {
        EffectInstance slowness = new EffectInstance(Effects.MOVEMENT_SLOWDOWN, durationInSeconds * 20, amplifier);
        EffectInstance fatigue = new EffectInstance(Effects.DIG_SLOWDOWN, durationInSeconds * 20, Math.max(0, amplifier * 2 - 1));
        nearbyEntity.addEffect(slowness);
        nearbyEntity.addEffect(fatigue);
        PROXY.spawnParticles(nearbyEntity, ParticleTypes.ITEM_SNOWBALL);
    }
}