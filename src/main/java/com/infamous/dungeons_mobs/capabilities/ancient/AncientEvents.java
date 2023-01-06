package com.infamous.dungeons_mobs.capabilities.ancient;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import java.util.ArrayList;
import java.util.List;

import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AncientMessage;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = MODID)
public class AncientEvents {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayer) {
            Ancient cap = AncientHelper.getAncientCapability(target);
            if(cap.isAncient()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new AncientMessage(target.getId(), cap.isAncient()));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event){
        LivingEntity entityLiving = event.getEntity();
        if(!entityLiving.level.isClientSide) {
            Ancient cap = AncientHelper.getAncientCapability(entityLiving);
            if(cap.isAncient() &&  cap.getBossInfo() != null){
                List<ServerPlayer> nearbyEntities = entityLiving.level.getNearbyEntities(ServerPlayer.class, TargetingConditions.forNonCombat().range(20.0D).ignoreInvisibilityTesting(), entityLiving, entityLiving.getBoundingBox().inflate(20D, 10D, 20D));
                nearbyEntities.forEach(playerEntity ->
                    cap.getBossInfo().addPlayer(playerEntity)
                );
                ArrayList<ServerPlayer> trackingPlayers = new ArrayList<>(cap.getBossInfo().getPlayers());
                List<ServerPlayer> furtherEntities = entityLiving.level.getNearbyEntities(ServerPlayer.class, TargetingConditions.forNonCombat().range(50.0D).ignoreInvisibilityTesting(), entityLiving, entityLiving.getBoundingBox().inflate(50D, 20D, 50D));
                trackingPlayers.forEach(playerEntity -> {
                    if(!furtherEntities.contains(playerEntity)){
                        cap.getBossInfo().removePlayer(playerEntity);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingEvent.LivingTickEvent event){
        LivingEntity livingEntity = event.getEntity();
        Ancient cap = AncientHelper.getAncientCapability(livingEntity);
        if(cap.isAncient() && cap.getBossInfo() != null) {
            cap.getBossInfo().setProgress(livingEntity.getHealth() / livingEntity.getMaxHealth());
        }
    }
}
