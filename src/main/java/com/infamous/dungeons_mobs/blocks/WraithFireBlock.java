package com.infamous.dungeons_mobs.blocks;

import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class WraithFireBlock extends AbstractFireBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_15;
    private final float fireDamage;

    public WraithFireBlock(Properties properties) {
        super(properties, 2.0F);
        this.fireDamage = 2.0F;
        this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
        return this.isValidPosition(stateIn, worldIn, currentPos) ? this.getFireWithAge(worldIn, currentPos, stateIn.get(AGE)) : Blocks.AIR.getDefaultState();
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return worldIn.getBlockState(blockpos).isSolidSide(worldIn, blockpos, Direction.UP);
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, getTickCooldown(worldIn.rand));
        if (worldIn.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            if (!state.isValidPosition(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }

            BlockState blockstate = worldIn.getBlockState(pos.down());

            boolean isOnFireSourceBlock = blockstate.isFireSource(worldIn, pos, Direction.UP)
                    || SoulFireBlock.shouldLightSoulFire(worldIn.getBlockState(pos.down()).getBlock());
            int i = state.get(AGE);
                int j = Math.min(15, i + rand.nextInt(3) / 2);
                if (i != j) {
                    state = state.with(AGE, Integer.valueOf(j));
                    worldIn.setBlockState(pos, state, 4);
                }

                if (!isOnFireSourceBlock) {
                    //if (!this.areNeighborsFlammable(worldIn, pos)) {
                        BlockPos blockpos = pos.down();
                        if (!worldIn.getBlockState(blockpos).isSolidSide(worldIn, blockpos, Direction.UP) || i > 3) {
                            worldIn.removeBlock(pos, false);
                        }

                        return;
                    //}

                    /*
                    if (i == 15 && rand.nextInt(4) == 0 && !this.canCatchFire(worldIn, pos.down(), Direction.UP)) {
                        worldIn.removeBlock(pos, false);
                        return;
                    }

                     */
                }
        }
    }


    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, getTickCooldown(worldIn.rand));
    }

    /**
     * Gets the delay before this block ticks again (without counting random ticks)
     */
    private static int getTickCooldown(Random rand) {
        return 30 + rand.nextInt(10);
    }

    private BlockState getFireWithAge(IWorld world, BlockPos pos, int age) {
        BlockState blockstate = ModBlocks.WRAITH_FIRE_BLOCK.get().getDefaultState();
        return blockstate.isIn(ModBlocks.WRAITH_FIRE_BLOCK.get()) ? blockstate.with(AGE, age) : blockstate;
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() || entityIn.getType() != ModEntityTypes.WRAITH.get()) {
            entityIn.forceFireTicks(entityIn.getFireTimer() + 1);
            if (entityIn.getFireTimer() == 0) {
                entityIn.setFire(8);
            }

            entityIn.attackEntityFrom(DamageSource.IN_FIRE, this.fireDamage);
        }
    }

    protected boolean canBurn(BlockState state) {
        return true;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
