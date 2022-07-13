package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.capabilities.properties.IMobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.BURNING;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class BurningMobEnchantment extends MobEnchantment {

    public BurningMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        executeIfPresent(entity, BURNING.get(), () -> {
            IMobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
            if(comboCap == null) return;
            int burnNearbyTimer = comboCap.getBurnNearbyTimer();
            if(burnNearbyTimer <= 0){
                applyToNearbyEntities(entity, 1.5F,
                        getCanApplyToEnemyPredicate(entity), (LivingEntity nearbyEntity) -> {
                            nearbyEntity.hurt(DamageSource.ON_FIRE, 1F);
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