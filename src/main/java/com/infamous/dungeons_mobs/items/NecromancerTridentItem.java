package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactUseContext;
import com.infamous.dungeons_libraries.network.BreakItemMessage;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class NecromancerTridentItem extends ArtifactItem implements IHasInventorySprite {
    public NecromancerTridentItem(Properties properties) {
        super(properties);
    }

    public int tridentSummonRange = 5;

    public InteractionResultHolder<ItemStack> procArtifact(ArtifactUseContext itemUseContext) {
        Level world = itemUseContext.getLevel();
        if (world.isClientSide) {
            return InteractionResultHolder.success(itemUseContext.getItemStack());
        } else {
            ItemStack itemUseContextItem = itemUseContext.getItemStack();
            Player itemUseContextPlayer = itemUseContext.getPlayer();
            BlockPos itemUseContextPos = itemUseContext.getClickedPos();

            if(itemUseContextPlayer != null){
                for(int i = 0; i < 8; i++){
                    TridentStormEntity tridentStorm = ModEntityTypes.TRIDENT_STORM.get().create(itemUseContextPlayer.level);
                    tridentStorm.owner = itemUseContextPlayer;
                    tridentStorm.moveTo(new BlockPos(itemUseContextPos.getX() - tridentSummonRange + itemUseContextPlayer.getRandom().nextInt(tridentSummonRange * 2), itemUseContextPos.getY(), itemUseContextPos.getZ() - tridentSummonRange + itemUseContextPlayer.getRandom().nextInt(tridentSummonRange * 2)), 0, 0);
                    tridentStorm.setYRot(itemUseContextPlayer.getRandom().nextInt(360));
                    itemUseContextPlayer.level.addFreshEntity(tridentStorm);
                    PositionUtils.moveToCorrectHeight(tridentStorm);
                }
                itemUseContextItem.hurtAndBreak(1, itemUseContextPlayer, (entity) -> NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new BreakItemMessage(entity.getId(), itemUseContextItem)));
                ArtifactItem.putArtifactOnCooldown(itemUseContextPlayer, itemUseContextItem.getItem());
            }
            return InteractionResultHolder.consume(itemUseContextItem);
        }
    }

    @Override
    public int getCooldownInSeconds() {
        return 10;
    }

    @Override
    public int getDurationInSeconds() {
        return 0;
    }
}
