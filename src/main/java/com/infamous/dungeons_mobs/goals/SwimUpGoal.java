package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

public class SwimUpGoal<T extends CreatureEntity & IAquaticMob> extends Goal {
    private final T aquaticMob;
    private final double speedModifier;
    private final int seaLevel;
    private boolean stuck;

    public SwimUpGoal(T aquaticMob, double p_i48908_2_, int p_i48908_4_) {
        this.aquaticMob = aquaticMob;
        this.speedModifier = p_i48908_2_;
        this.seaLevel = p_i48908_4_;
    }

    public boolean canUse() {
        return !this.aquaticMob.level.isDay() && this.aquaticMob.isInWater() && this.aquaticMob.getY() < (double) (this.seaLevel - 2);
    }

    public boolean canContinueToUse() {
        return this.canUse() && !this.stuck;
    }

    public void tick() {
        if (this.aquaticMob.getY() < (double) (this.seaLevel - 1) && (this.aquaticMob.getNavigation().isDone() || this.aquaticMob.closeToNextPos(this.aquaticMob))) {
            Vector3d vector3d = RandomPositionGenerator.getPosTowards(this.aquaticMob, 4, 8, new Vector3d(this.aquaticMob.getX(), (double) (this.seaLevel - 1), this.aquaticMob.getZ()));
            if (vector3d == null) {
                this.stuck = true;
                return;
            }

            this.aquaticMob.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, this.speedModifier);
        }

    }

    public void start() {
        this.aquaticMob.setSearchingForLand(true);
        this.stuck = false;
    }

    public void stop() {
        this.aquaticMob.setSearchingForLand(false);
    }
}
