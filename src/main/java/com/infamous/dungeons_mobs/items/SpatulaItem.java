package com.infamous.dungeons_mobs.items;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;

import java.util.Set;

import net.minecraft.item.Item.Properties;

public class SpatulaItem extends ToolItem {
    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of();

    public SpatulaItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, builderIn);
    }

}
