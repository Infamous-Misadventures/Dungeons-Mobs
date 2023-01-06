package com.infamous.dungeons_mobs.network.message;

import java.util.function.Supplier;

import com.infamous.dungeons_mobs.capabilities.ancient.Ancient;
import com.infamous.dungeons_mobs.capabilities.ancient.AncientHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class AncientMessage {
	private final int entityId;
	private final boolean ancient;

	public AncientMessage(int entityId, boolean ancient) {
		this.entityId = entityId;
		this.ancient = ancient;
	}

	public static AncientMessage decode(FriendlyByteBuf buffer) {
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
					Ancient cap = AncientHelper.getAncientCapability(entity);
					cap.setAncient(message.ancient);
					entity.refreshDimensions();
				}
			});
		}
		return true;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeBoolean(ancient);
	}
}
