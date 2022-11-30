package com.infamous.dungeons_mobs.network.message;

import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedProps;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedPropsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AnimatedPropsMessage {
    private int entityId;
    private AnimatedProps cap;

    public AnimatedPropsMessage(int entityId, AnimatedProps cap) {
        this.entityId = entityId;
        this.cap = cap;
    }

    public static AnimatedPropsMessage decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        AnimatedProps cap = new AnimatedProps();
        cap.setAttackAnimationTick(buffer.readInt());
        cap.setAttackAnimationLength(buffer.readInt());
        cap.setAttackAnimationActionPoint(buffer.readInt());
        return new AnimatedPropsMessage(entityId, cap);
    }

    public static boolean onPacketReceived(AnimatedPropsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
                if (entity instanceof Mob) {
                    AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability((Mob) entity);
                    cap.setAttackAnimationTick(message.cap.getAttackAnimationTick());
                    cap.setAttackAnimationLength(message.cap.getAttackAnimationLength());
                    cap.setAttackAnimationActionPoint(message.cap.getAttackAnimationActionPoint());
                }
            });
        }
        return true;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(cap.getAttackAnimationTick());
        buffer.writeInt(cap.getAttackAnimationLength());
        buffer.writeInt(cap.getAttackAnimationActionPoint());
    }
}
