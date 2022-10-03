package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.capabilities.properties.IMobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.GRAVITY_PULSE;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class GravityPulseMobEnchant extends MobEnchant {

    public static final double PULL_IN_SPEED_FACTOR = 0.1;

    public GravityPulseMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        executeIfPresentWithLevel(entity, GRAVITY_PULSE.get(), (level) -> {
            IMobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
            if(comboCap == null) return;
            int gravityPulseTimer = comboCap.getGravityPulseTimer();
            if(gravityPulseTimer <= 0){
                PROXY.spawnParticles(entity, ParticleTypes.PORTAL);
                applyToNearbyEntities(entity, 5F,
                        getCanApplyToEnemyPredicate(entity), (LivingEntity nearbyEntity) -> {
                            pullVictimTowardsTarget(entity, nearbyEntity, ParticleTypes.PORTAL, level);
                        }
                );
                comboCap.setGravityPulseTimer(100);
            }
            else{
                comboCap.setGravityPulseTimer(gravityPulseTimer - 1);
            }
        });
    }

    public static void pullVictimTowardsTarget(LivingEntity target, LivingEntity nearbyEntity, BasicParticleType particleType, Integer level) {
        double motionX = target.getX() - (nearbyEntity.getX());
        double motionY = target.getY() - (nearbyEntity.getY());
        double motionZ = target.getZ() - (nearbyEntity.getZ());
        Vector3d vector3d = new Vector3d(motionX, motionY, motionZ).scale(PULL_IN_SPEED_FACTOR * level);

        nearbyEntity.setDeltaMovement(vector3d);
        PROXY.spawnParticles(nearbyEntity, particleType);
    }
}