package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.REGENERATION;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class RegenerationMobEnchant extends MobEnchant {

    public RegenerationMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        executeIfPresentWithLevel(entity, REGENERATION.get(), (level) -> {
            if (entity.getHealth() < entity.getMaxHealth() && entity.tickCount % getTickCountForLevel(level) == 0) {
                entity.heal(1.0F);
                PROXY.spawnParticles(entity, ParticleTypes.HEART);
            }
        });
    }

    private static int getTickCountForLevel(Integer level) {
        return 62 - level * 12;
    }
}