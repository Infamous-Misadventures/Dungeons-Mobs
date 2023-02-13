package com.infamous.dungeons_mobs.entities;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static com.infamous.dungeons_mobs.mod.ModEffects.ENSNARED;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientEntityEvents {

    @SubscribeEvent
    public static void preventExtraMovement(MovementInputUpdateEvent event) {
        Player owner = event.getEntity();

        if (owner.hasEffect(ENSNARED.get())) {
            event.getInput().getMoveVector().scale(0);
            if (event.getInput().jumping) event.getInput().jumping = false;
        }
    }
}
