package com.infamous.dungeons_mobs.capabilities.animatedprops;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AnimatedPropsMessage;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = MODID)
public class AnimatedPropsEvents {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayer && target instanceof Vindicator) {
            AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability((Vindicator) target);
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new AnimatedPropsMessage(target.getId(), cap));
        }
    }
}
