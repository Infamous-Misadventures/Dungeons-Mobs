package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.capabilities.properties.MobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.GRAVITY_PULSE;

public class GravityPulseMobEnchant extends MobEnchant {

    public static final double PULL_IN_SPEED_FACTOR = 0.1;

    public GravityPulseMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        executeIfPresentWithLevel(entity, GRAVITY_PULSE.get(), (level) -> {
            MobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
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

    public static void pullVictimTowardsTarget(LivingEntity target, LivingEntity nearbyEntity, SimpleParticleType particleType, Integer level) {
        double motionX = target.getX() - (nearbyEntity.getX());
        double motionY = target.getY() - (nearbyEntity.getY());
        double motionZ = target.getZ() - (nearbyEntity.getZ());
        Vec3 vector3d = new Vec3(motionX, motionY, motionZ).scale(PULL_IN_SPEED_FACTOR * level);

        nearbyEntity.setDeltaMovement(vector3d);
        PROXY.spawnParticles(nearbyEntity, particleType);
    }
}