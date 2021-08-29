package com.infamous.dungeons_mobs.goals;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GoToWaterGoal extends Goal {
    private final CreatureEntity mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    private final World level;

    public GoToWaterGoal(CreatureEntity p_i48910_1_, double p_i48910_2_) {
        this.mob = p_i48910_1_;
        this.speedModifier = p_i48910_2_;
        this.level = p_i48910_1_.level;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.level.isDay()) {
            return false;
        } else if (this.mob.isInWater()) {
            return false;
        } else {
            Vector3d vector3d = this.getWaterPos();
            if (vector3d == null) {
                return false;
            } else {
                this.wantedX = vector3d.x;
                this.wantedY = vector3d.y;
                this.wantedZ = vector3d.z;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Nullable
    private Vector3d getWaterPos() {
        Random random = this.mob.getRandom();
        BlockPos blockpos = this.mob.blockPosition();

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
            if (this.level.getBlockState(blockpos1).is(Blocks.WATER)) {
                return Vector3d.atBottomCenterOf(blockpos1);
            }
        }

        return null;
    }
}
