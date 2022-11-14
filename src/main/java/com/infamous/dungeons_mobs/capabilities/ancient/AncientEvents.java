package com.infamous.dungeons_mobs.capabilities.ancient;

import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AncientMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class AncientEvents {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayerEntity) {
            IAncient cap = AncientHelper.getAncientCapability(target);
            if(cap.isAncient()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new AncientMessage(target.getId(), cap.isAncient()));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if(!entityLiving.level.isClientSide) {
            IAncient cap = AncientHelper.getAncientCapability(entityLiving);
            if(cap.isAncient() &&  cap.getBossInfo() != null){
                List<ServerPlayerEntity> nearbyEntities = entityLiving.level.getNearbyEntities(ServerPlayerEntity.class, new EntityPredicate().range(20.0D).allowInvulnerable().allowNonAttackable().ignoreInvisibilityTesting(), entityLiving, entityLiving.getBoundingBox().inflate(20D, 10D, 20D));
                nearbyEntities.forEach(playerEntity ->
                    cap.getBossInfo().addPlayer(playerEntity)
                );
                ArrayList<ServerPlayerEntity> trackingPlayers = new ArrayList<>(cap.getBossInfo().getPlayers());
                List<ServerPlayerEntity> furtherEntities = entityLiving.level.getNearbyEntities(ServerPlayerEntity.class, new EntityPredicate().range(50.0D).allowInvulnerable().allowNonAttackable().ignoreInvisibilityTesting(), entityLiving, entityLiving.getBoundingBox().inflate(50D, 20D, 50D));
                trackingPlayers.forEach(playerEntity -> {
                    if(!furtherEntities.contains(playerEntity)){
                        cap.getBossInfo().removePlayer(playerEntity);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        IAncient cap = AncientHelper.getAncientCapability(livingEntity);
        if(cap.isAncient() && cap.getBossInfo() != null) {
            cap.getBossInfo().setPercent(livingEntity.getHealth() / livingEntity.getMaxHealth());
        }
    }
}
