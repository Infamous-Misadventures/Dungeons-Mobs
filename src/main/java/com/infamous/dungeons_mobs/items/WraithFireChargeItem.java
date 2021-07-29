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

import net.minecraft.item.Item.Properties;

public class WraithFireChargeItem extends Item {
    public WraithFireChargeItem(Properties builder) {
        super(builder);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        boolean flag = false;
        if (CampfireBlock.canLight(blockstate)) {
            this.playUseSound(world, blockpos);
            world.setBlockAndUpdate(blockpos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(true)));
            flag = true;
        } else {
            blockpos = blockpos.relative(context.getClickedFace());
            BlockState fireChargeTargetBlockState = world.getBlockState(blockpos);
            BlockState soulFireBlockState = ModBlocks.WRAITH_FIRE_BLOCK.get().defaultBlockState();
            boolean canLightBlock = fireChargeTargetBlockState.isAir() && (soulFireBlockState.canSurvive(world, blockpos));
            if (canLightBlock) {
                this.playUseSound(world, blockpos);
                world.setBlockAndUpdate(blockpos, soulFireBlockState);
                flag = true;
            }
        }

        if (flag) {
            context.getItemInHand().shrink(1);
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.FAIL;
        }
    }

    public static void initSoulFireCharge(){
        DefaultDispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior(){

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack execute(IBlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                IPosition iposition = DispenserBlock.getDispensePosition(source);
                double d0 = iposition.x() + (double)((float)direction.getStepX() * 0.3F);
                double d1 = iposition.y() + (double)((float)direction.getStepY() * 0.3F);
                double d2 = iposition.z() + (double)((float)direction.getStepZ() * 0.3F);
                World world = source.getLevel();
                Random random = world.random;
                double d3 = random.nextGaussian() * 0.05D + (double)direction.getStepX();
                double d4 = random.nextGaussian() * 0.05D + (double)direction.getStepY();
                double d5 = random.nextGaussian() * 0.05D + (double)direction.getStepZ();
                world.addFreshEntity(Util.make(new WraithFireballEntity(world, d0, d1, d2, d3, d4, d5), (fireball) -> {
                    fireball.setItem(stack);
                }));
                stack.shrink(1);
                return stack;
            }

            /**
             * Play the dispense sound from the specified block.
             */
            protected void playSound(IBlockSource source) {
                source.getLevel().levelEvent(1018, source.getPos(), 0);
            }
        };


        DispenserBlock.registerBehavior(ModItems.WRAITH_FIRE_CHARGE.get(), dispenseItemBehavior);
    }

    private void playUseSound(World worldIn, BlockPos pos) {
        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }
}
