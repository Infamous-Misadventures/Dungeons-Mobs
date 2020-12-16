package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import net.minecraft.block.*;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WraithFireChargeItem extends Item {
    public WraithFireChargeItem(Properties builder) {
        super(builder);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        boolean flag = false;
        if (CampfireBlock.canBeLit(blockstate)) {
            this.playUseSound(world, blockpos);
            world.setBlockState(blockpos, blockstate.with(CampfireBlock.LIT, Boolean.valueOf(true)));
            flag = true;
        } else {
            blockpos = blockpos.offset(context.getFace());
            BlockState fireChargeTargetBlockState = world.getBlockState(blockpos);
            BlockState soulFireBlockState = ModBlocks.WRAITH_FIRE_BLOCK.get().getDefaultState();
            boolean canLightBlock = fireChargeTargetBlockState.isAir() && (soulFireBlockState.isValidPosition(world, blockpos));
            if (canLightBlock) {
                this.playUseSound(world, blockpos);
                world.setBlockState(blockpos, soulFireBlockState);
                flag = true;
            }
        }

        if (flag) {
            context.getItem().shrink(1);
            return ActionResultType.func_233537_a_(world.isRemote);
        } else {
            return ActionResultType.FAIL;
        }
    }

    public static void initSoulFireCharge(){
        DefaultDispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior(){

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().get(DispenserBlock.FACING);
                IPosition iposition = DispenserBlock.getDispensePosition(source);
                double d0 = iposition.getX() + (double)((float)direction.getXOffset() * 0.3F);
                double d1 = iposition.getY() + (double)((float)direction.getYOffset() * 0.3F);
                double d2 = iposition.getZ() + (double)((float)direction.getZOffset() * 0.3F);
                World world = source.getWorld();
                Random random = world.rand;
                double d3 = random.nextGaussian() * 0.05D + (double)direction.getXOffset();
                double d4 = random.nextGaussian() * 0.05D + (double)direction.getYOffset();
                double d5 = random.nextGaussian() * 0.05D + (double)direction.getZOffset();
                world.addEntity(Util.make(new WraithFireballEntity(world, d0, d1, d2, d3, d4, d5), (fireball) -> {
                    fireball.setStack(stack);
                }));
                stack.shrink(1);
                return stack;
            }

            /**
             * Play the dispense sound from the specified block.
             */
            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playEvent(1018, source.getBlockPos(), 0);
            }
        };


        DispenserBlock.registerDispenseBehavior(ModItems.WRAITH_FIRE_CHARGE.get(), dispenseItemBehavior);
    }

    private void playUseSound(World worldIn, BlockPos pos) {
        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }
}
