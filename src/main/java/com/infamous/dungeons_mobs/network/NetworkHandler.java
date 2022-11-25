package com.infamous.dungeons_mobs.network;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.network.message.AncientMessage;
import com.infamous.dungeons_mobs.network.message.AnimatedPropsMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
        INSTANCE.messageBuilder(AncientMessage.class, incrementAndGetPacketCounter())
                .encoder(AncientMessage::encode).decoder(AncientMessage::decode)
                .consumer(AncientMessage::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(AnimatedPropsMessage.class, incrementAndGetPacketCounter())
                .encoder(AnimatedPropsMessage::encode).decoder(AnimatedPropsMessage::decode)
                .consumer(AnimatedPropsMessage::onPacketReceived)
                .add();
    }

    public static int incrementAndGetPacketCounter() {
        return PACKET_COUNTER++;
    }
}
