package com.infamous.dungeons_mobs.blocks;

import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
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

import net.minecraft.block.AbstractBlock.Properties;

public class WraithFireBlock extends AbstractFireBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    private final float fireDamage;

    public WraithFireBlock(Properties properties) {
        super(properties, 2.0F);
        this.fireDamage = 2.0F;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
        return this.canSurvive(stateIn, worldIn, currentPos) ? this.getFireWithAge(worldIn, currentPos, stateIn.getValue(AGE)) : Blocks.AIR.defaultBlockState();
    }

    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return worldIn.getBlockState(blockpos).isFaceSturdy(worldIn, blockpos, Direction.UP);
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        worldIn.getBlockTicks().scheduleTick(pos, this, getTickCooldown(worldIn.random));
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            if (!state.canSurvive(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }

            BlockState blockstate = worldIn.getBlockState(pos.below());

            boolean isOnFireSourceBlock = blockstate.isFireSource(worldIn, pos, Direction.UP)
                    || SoulFireBlock.canSurviveOnBlock(worldIn.getBlockState(pos.below()).getBlock());
            int i = state.getValue(AGE);
                int j = Math.min(15, i + rand.nextInt(3) / 2);
                if (i != j) {
                    state = state.setValue(AGE, Integer.valueOf(j));
                    worldIn.setBlock(pos, state, 4);
                }

                if (!isOnFireSourceBlock) {
                    //if (!this.areNeighborsFlammable(worldIn, pos)) {
                        BlockPos blockpos = pos.below();
                        if (!worldIn.getBlockState(blockpos).isFaceSturdy(worldIn, blockpos, Direction.UP) || i > 3) {
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


    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        worldIn.getBlockTicks().scheduleTick(pos, this, getTickCooldown(worldIn.random));
    }

    /**
     * Gets the delay before this block ticks again (without counting random ticks)
     */
    private static int getTickCooldown(Random rand) {
        return 30 + rand.nextInt(10);
    }

    private BlockState getFireWithAge(IWorld world, BlockPos pos, int age) {
        BlockState blockstate = ModBlocks.WRAITH_FIRE_BLOCK.get().defaultBlockState();
        return blockstate.is(ModBlocks.WRAITH_FIRE_BLOCK.get()) ? blockstate.setValue(AGE, age) : blockstate;
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    	System.out.print("\r\n");
        if (!entityIn.fireImmune() && !(entityIn instanceof WraithEntity)) {
            entityIn.setRemainingFireTicks(entityIn.getRemainingFireTicks() + 1);
            if (entityIn.getRemainingFireTicks() == 0) {
                entityIn.setSecondsOnFire(8);
            }

            entityIn.hurt(DamageSource.IN_FIRE, this.fireDamage);
        }
    }

    protected boolean canBurn(BlockState state) {
        return true;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
