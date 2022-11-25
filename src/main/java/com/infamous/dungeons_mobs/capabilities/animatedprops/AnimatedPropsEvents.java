package com.infamous.dungeons_mobs.capabilities.animatedprops;

import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AnimatedPropsMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class AnimatedPropsEvents {

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayerEntity && target instanceof VindicatorEntity) {
            AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability((VindicatorEntity) target);
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new AnimatedPropsMessage(target.getId(), cap));
        }
    }
}
