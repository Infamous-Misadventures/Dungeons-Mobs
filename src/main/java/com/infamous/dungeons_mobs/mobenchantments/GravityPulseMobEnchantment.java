package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
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

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.GRAVITY_PULSE;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class GravityPulseMobEnchantment extends MobEnchantment {

    public static final double PULL_IN_SPEED_FACTOR = 0.15;

    public GravityPulseMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        executeIfPresent(entity, GRAVITY_PULSE.get(), () -> {
            IMobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
            if(comboCap == null) return;
            int gravityPulseTimer = comboCap.getGravityPulseTimer();
            if(gravityPulseTimer <= 0){
                applyToNearbyEntities(entity, 4.5F,
                        getCanApplyToEnemyPredicate(entity), (LivingEntity nearbyEntity) -> {
                            pullVictimTowardsTarget(entity, nearbyEntity, ParticleTypes.PORTAL);
                        }
                );
                comboCap.setGravityPulseTimer(100);
            }
            else{
                comboCap.setGravityPulseTimer(gravityPulseTimer - 1);
            }
        });
    }

    public static void pullVictimTowardsTarget(LivingEntity target, LivingEntity nearbyEntity, BasicParticleType particleType) {
        double motionX = target.getX() - (nearbyEntity.getX());
        double motionY = target.getY() - (nearbyEntity.getY());
        double motionZ = target.getZ() - (nearbyEntity.getZ());
        Vector3d vector3d = new Vector3d(motionX, motionY, motionZ).scale(PULL_IN_SPEED_FACTOR);

        nearbyEntity.setDeltaMovement(vector3d);
        PROXY.spawnParticles(nearbyEntity, particleType);
    }
}