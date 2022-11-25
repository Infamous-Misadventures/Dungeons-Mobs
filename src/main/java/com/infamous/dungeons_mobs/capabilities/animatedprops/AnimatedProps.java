package com.infamous.dungeons_mobs.capabilities.animatedprops;

import com.infamous.dungeons_mobs.entities.AnimatableMeleeAttackMob;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;

public class AnimatedProps implements AnimatableMeleeAttackMob {
    public int attackAnimationTick = 0;
    public int attackAnimationLength = 7;
    public int attackAnimationActionPoint = 6;


    @Override
    public int getAttackAnimationTick() {
        return attackAnimationTick;
    }

    @Override
    public void setAttackAnimationTick(int attackAnimationTick) {
        this.attackAnimationTick = attackAnimationTick;
    }

    @Override
    public int getAttackAnimationLength() {
        return attackAnimationLength;
    }

    public void setAttackAnimationLength(int attackAnimationLength) {
        this.attackAnimationLength = attackAnimationLength;
    }

    @Override
    public int getAttackAnimationActionPoint() {
        return attackAnimationActionPoint;
    }

    public void setAttackAnimationActionPoint(int attackAnimationActionPoint) {
        this.attackAnimationActionPoint = attackAnimationActionPoint;
    }

    public void tickDownAnimTimers() {
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }
    }

    public INBT save(CompoundNBT tag, Direction side) {
        tag.putInt("AttackAnimationTick", this.attackAnimationTick);
        tag.putInt("AttackAnimationLength", this.attackAnimationLength);
        tag.putInt("AttackAnimationActionPoint", this.attackAnimationActionPoint);
        return tag;
    }

    public void load(INBT nbt, Direction side) {
        CompoundNBT tag = (CompoundNBT) nbt;
        this.attackAnimationTick = tag.getInt("AttackAnimationTick");
        this.attackAnimationLength = tag.getInt("AttackAnimationLength");
        this.attackAnimationActionPoint = tag.getInt("AttackAnimationActionPoint");
    }
}
