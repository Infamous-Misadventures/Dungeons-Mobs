package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.interfaces.IAquaticMob;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class GoToBeachGoal<T extends PathfinderMob & IAquaticMob> extends MoveToBlockGoal {
	private final T aquaticMob;

	public GoToBeachGoal(T p_i48911_1_, double p_i48911_2_) {
		super(p_i48911_1_, p_i48911_2_, 8, 2);
		this.aquaticMob = p_i48911_1_;
	}

	public boolean canUse() {
		return super.canUse() && !this.aquaticMob.level.isDay() && this.aquaticMob.isInWater()
				&& this.aquaticMob.getY() >= (double) (this.aquaticMob.level.getSeaLevel() - 3);
	}

	public boolean canContinueToUse() {
		return super.canContinueToUse();
	}

	protected boolean isValidTarget(LevelReader worldReader, BlockPos blockPos) {
		BlockPos blockpos = blockPos.above();
		return worldReader.isEmptyBlock(blockpos) && worldReader.isEmptyBlock(blockpos.above())
				&& worldReader.getBlockState(blockPos).entityCanStandOn(worldReader, blockPos, this.aquaticMob);
	}

	public void start() {
		this.aquaticMob.setSearchingForLand(false);
		this.aquaticMob.setNavigation(this.aquaticMob.getGroundNavigation());
		super.start();
	}

	public void stop() {
		super.stop();
	}
}
