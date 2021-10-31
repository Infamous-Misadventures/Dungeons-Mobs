package com.infamous.dungeons_mobs.capabilities.ancient;

import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AncientMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class AncientEvents {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayerEntity) {
            AncientHelper.getAncientCapabilityLazy(target).ifPresent(cap -> {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new AncientMessage(target.getId(), cap.isAncient()));
                if(cap.isAncient() &&  cap.getBossInfo() != null){
                    cap.getBossInfo().addPlayer((ServerPlayerEntity) player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onStopEntityTracking(PlayerEvent.StopTracking event){
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayerEntity) {
            AncientHelper.getAncientCapabilityLazy(target).ifPresent(cap -> {
                if(cap.isAncient() &&  cap.getBossInfo() != null){
                    cap.getBossInfo().removePlayer((ServerPlayerEntity) player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        AncientHelper.getAncientCapabilityLazy(livingEntity).ifPresent(cap -> {
            if(cap.isAncient() && cap.getBossInfo() != null) {
                cap.getBossInfo().setPercent(livingEntity.getHealth() / livingEntity.getMaxHealth());
            }
        });
    }
}
