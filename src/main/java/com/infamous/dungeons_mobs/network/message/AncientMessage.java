package com.infamous.dungeons_mobs.network.message;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentsRegistry;
import com.infamous.dungeons_mobs.capabilities.ancient.properties.AncientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapabilityLazy;

public class AncientMessage {
    private int entityId;
    private boolean ancient;

    public AncientMessage(int entityId, boolean ancient) {
        this.entityId = entityId;
        this.ancient = ancient;
    }

    public static AncientMessage decode(PacketBuffer buffer) {
        int entityId = buffer.readInt();
        boolean ancient = buffer.readBoolean();

        return new AncientMessage(entityId, ancient);
    }

    public static boolean onPacketReceived(AncientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
                if (entity instanceof LivingEntity) {
                    AncientHelper.getAncientCapabilityLazy(entity).ifPresent(ancientCap -> {
                        ancientCap.setAncient(message.ancient);
                        entity.refreshDimensions();
                    });
                }
            });
        }
        return true;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(ancient);
    }
}
