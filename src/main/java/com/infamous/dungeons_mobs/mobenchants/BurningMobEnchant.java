package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.capabilities.properties.IMobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.BURNING;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class BurningMobEnchant extends MobEnchant {

    public BurningMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        executeIfPresentWithLevel(entity, BURNING.get(), (level) -> {
            IMobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
            if(comboCap == null) return;
            int burnNearbyTimer = comboCap.getBurnNearbyTimer();
            if(burnNearbyTimer <= 0){
                PROXY.spawnParticles(entity, ParticleTypes.FLAME);
                applyToNearbyEntities(entity, 1.5F,
                        getCanApplyToEnemyPredicate(entity), (LivingEntity nearbyEntity) -> {
                            nearbyEntity.hurt(DamageSource.ON_FIRE, 0.5F * level);
                            PROXY.spawnParticles(nearbyEntity, ParticleTypes.FLAME);
                        }
                );
                comboCap.setBurnNearbyTimer(20);
            }
            else{
                comboCap.setBurnNearbyTimer(burnNearbyTimer - 1);
            }
        });
    }
}