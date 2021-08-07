package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.LaserOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import net.minecraft.item.Item.Properties;
import net.minecraft.util.math.vector.Vector3d;

public class NecromancerStaffItem extends AbstractNecromancerStaffItem{
    public NecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected AbstractOrbEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference) {
        return new LaserOrbEntity(playerIn.level, playerIn, xDifference, yDifference, zDifference);
    }
}
