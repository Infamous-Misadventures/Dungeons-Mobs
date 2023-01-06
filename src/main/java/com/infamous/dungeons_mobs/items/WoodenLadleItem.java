package com.infamous.dungeons_mobs.items;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

public class WoodenLadleItem extends ShovelItem {
    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of();

    public WoodenLadleItem(Tier p_43114_, float p_43115_, float p_43116_, Properties p_43117_) {
        super(p_43114_, p_43115_, p_43116_, p_43117_);
    }

}
