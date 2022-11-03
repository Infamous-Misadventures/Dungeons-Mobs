package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactUseContext;
import com.infamous.dungeons_libraries.network.BreakItemMessage;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;

public class GeomancerStaffItem extends ArtifactItem implements IHasInventorySprite {
    public GeomancerStaffItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> procArtifact(ArtifactUseContext c) {
        PlayerEntity playerIn = c.getPlayer();
        ItemStack itemstack = c.getItemStack();

        if(playerIn.getRandom().nextFloat() < 0.25F){
            GeomancyHelper.summonOffensiveConstruct(playerIn, c.getClickedPos(), ModEntityTypes.GEOMANCER_BOMB.get(), 0, 0, Direction.NORTH);
        }
        else{
            int[] rowToRemove = Util.getRandom(GeomancyHelper.CONFIG_1_ROWS, playerIn.getRandom());
            GeomancyHelper.summonAreaDenialTrap(playerIn, c.getClickedPos(), ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
        }
        itemstack.hurtAndBreak(1, playerIn, (entity) -> NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new BreakItemMessage(entity.getId(), itemstack)));
        ArtifactItem.putArtifactOnCooldown(playerIn, itemstack.getItem());
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public int getCooldownInSeconds() {
        return 20;
    }

    @Override
    public int getDurationInSeconds() {
        return 0;
    }
}
