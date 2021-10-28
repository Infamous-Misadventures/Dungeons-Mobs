package com.infamous.dungeons_mobs.network;

import com.infamous.dungeons_mobs.capabilities.ancient.properties.AncientHelper;
import com.infamous.dungeons_mobs.network.message.AncientMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;


@Mod.EventBusSubscriber(modid = MODID)
public class MessageEvents {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayerEntity)
            AncientHelper.getAncientCapabilityLazy(target).ifPresent(cap -> {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new AncientMessage(target.getId(), cap.isAncient()));
            });
    }
}
