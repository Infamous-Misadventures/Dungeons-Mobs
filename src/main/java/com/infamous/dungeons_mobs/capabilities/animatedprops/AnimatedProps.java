package com.infamous.dungeons_mobs.capabilities.animatedprops;

import com.infamous.dungeons_mobs.entities.AnimatableMeleeAttackMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.ANIMATED_PROPS_CAPABILITY;

public class AnimatedProps implements INBTSerializable<CompoundTag>, AnimatableMeleeAttackMob {
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

    @Override
    public CompoundTag serializeNBT() {
        if (ANIMATED_PROPS_CAPABILITY == null) {
            return new CompoundTag();
        }
        CompoundTag tag = new CompoundTag();
        tag.putInt("AttackAnimationTick", this.attackAnimationTick);
        tag.putInt("AttackAnimationLength", this.attackAnimationLength);
        tag.putInt("AttackAnimationActionPoint", this.attackAnimationActionPoint);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.attackAnimationTick = tag.getInt("AttackAnimationTick");
        this.attackAnimationLength = tag.getInt("AttackAnimationLength");
        this.attackAnimationActionPoint = tag.getInt("AttackAnimationActionPoint");
    }
}
