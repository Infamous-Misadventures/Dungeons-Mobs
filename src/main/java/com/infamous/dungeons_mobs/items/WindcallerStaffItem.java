package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactUseContext;
import com.infamous.dungeons_libraries.network.BreakItemMessage;
import com.infamous.dungeons_mobs.entities.projectiles.WindcallerBlastProjectileEntity;
import com.infamous.dungeons_mobs.entities.summonables.WindcallerTornadoEntity;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.network.NetworkHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class WindcallerStaffItem extends ArtifactItem implements IHasInventorySprite {
	public WindcallerStaffItem(Properties properties) {
		super(properties);
		procOnItemUse = true;
	}

	public InteractionResultHolder<ItemStack> procArtifact(ArtifactUseContext itemUseContext) {
		Level world = itemUseContext.getLevel();
		if (world.isClientSide) {
			return InteractionResultHolder.success(itemUseContext.getItemStack());
		} else {
			ItemStack itemUseContextItem = itemUseContext.getItemStack();
			Player player = itemUseContext.getPlayer();
			BlockPos itemUseContextPos = itemUseContext.getClickedPos();

			if (player != null) {
				shoot(player, itemUseContextPos.getX(), itemUseContextPos.getY() + 0.5, itemUseContextPos.getZ());
				itemUseContextItem.hurtAndBreak(1, player,
						(entity) -> NetworkHandler.INSTANCE.send(
								PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
								new BreakItemMessage(entity.getId(), itemUseContextItem)));
				ArtifactItem.putArtifactOnCooldown(player, itemUseContextItem.getItem());
			}
			return InteractionResultHolder.consume(itemUseContextItem);
		}
	}

	@Override
	public int getCooldownInSeconds() {
		return 20;
	}

	@Override
	public int getDurationInSeconds() {
		return 0;
	}

	private void shoot(Player mob, double targetX, double targetY, double targetZ) {
		double d1 = targetX - mob.getX();
		double d2 = targetY - mob.getY(0.5D);
		double d3 = targetZ - mob.getZ();
		WindcallerBlastProjectileEntity smallfireballentity = new WindcallerBlastProjectileEntity(mob.level, mob, d1, 0,
				d3);
		smallfireballentity.setPos(mob.getX(), mob.getY(0.25D), mob.getZ());
		mob.level.addFreshEntity(smallfireballentity);
		WindcallerTornadoEntity tornado = ModEntityTypes.TORNADO.get().create(mob.level);
		tornado.moveTo(mob.blockPosition(), 0, 0);
		tornado.playSound(ModSoundEvents.WINDCALLER_BLAST_WIND.get(), 1.5F, 1.0F);
		tornado.setBlast(true);
		tornado.setYRot(-mob.yHeadRot - 90);
		((ServerLevel) mob.level).addFreshEntityWithPassengers(tornado);
	}

}
