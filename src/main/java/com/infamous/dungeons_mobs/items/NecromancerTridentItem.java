package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactUseContext;
import com.infamous.dungeons_libraries.network.BreakItemMessage;
import com.infamous.dungeons_libraries.summon.SummonHelper;
import com.infamous.dungeons_libraries.utils.SoundHelper;
import com.infamous.dungeons_mobs.entities.projectiles.DrownedNecromancerOrbEntity;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMasterCapability;

public class NecromancerTridentItem extends ArtifactItem implements IHasInventorySprite {
    public NecromancerTridentItem(Properties properties) {
        super(properties);
    }

    public int tridentSummonRange = 5;

    public ActionResult<ItemStack> procArtifact(ArtifactUseContext itemUseContext) {
        World world = itemUseContext.getLevel();
        if (world.isClientSide) {
            return ActionResult.success(itemUseContext.getItemStack());
        } else {
            ItemStack itemUseContextItem = itemUseContext.getItemStack();
            PlayerEntity itemUseContextPlayer = itemUseContext.getPlayer();
            BlockPos itemUseContextPos = itemUseContext.getClickedPos();

            if(itemUseContextPlayer != null){
                for(int i = 0; i < 8; i++){
                    TridentStormEntity tridentStorm = ModEntityTypes.TRIDENT_STORM.get().create(itemUseContextPlayer.level);
                    tridentStorm.owner = itemUseContextPlayer;
                    tridentStorm.moveTo(new BlockPos(itemUseContextPos.getX() - tridentSummonRange + itemUseContextPlayer.getRandom().nextInt(tridentSummonRange * 2), itemUseContextPos.getY(), itemUseContextPos.getZ() - tridentSummonRange + itemUseContextPlayer.getRandom().nextInt(tridentSummonRange * 2)), 0, 0);
                    tridentStorm.yRot = itemUseContextPlayer.getRandom().nextInt(360);
                    itemUseContextPlayer.level.addFreshEntity(tridentStorm);
                    PositionUtils.moveToCorrectHeight(tridentStorm);
                }
                itemUseContextItem.hurtAndBreak(1, itemUseContextPlayer, (entity) -> NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new BreakItemMessage(entity.getId(), itemUseContextItem)));
                ArtifactItem.putArtifactOnCooldown(itemUseContextPlayer, itemUseContextItem.getItem());
            }
            return ActionResult.consume(itemUseContextItem);
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
