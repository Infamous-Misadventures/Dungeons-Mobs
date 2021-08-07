package com.infamous.dungeons_mobs.network;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(DungeonsMobs.MODID, "network"))
            .clientAcceptedVersions("1"::equals)
            .serverAcceptedVersions("1"::equals)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    protected static int PACKET_COUNTER = 0;

    public NetworkHandler() {
    }

    public static void init() {
    }

    public static int incrementAndGetPacketCounter() {
        return PACKET_COUNTER++;
    }
}
