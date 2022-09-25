package com.infamous.dungeons_mobs.goals;

import com.google.common.collect.ImmutableMap;
import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PatrolStructureGoal<T extends LivingEntity> extends Goal {

    protected final Class<T> targetType;
    public CreatureEntity mob;
    @Nullable
    public LivingEntity target;
    private final double moveSpeed;
    private int delayCounter;
    private boolean canBreakBlock;

    public PatrolStructureGoal(CreatureEntity entity, double moveSpeed, Class<T> moveToEntityType) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.mob = entity;
        this.moveSpeed = moveSpeed;
        this.targetType = moveToEntityType;
    }

    @Override
    public boolean canUse() {
        if (mob instanceof AbstractIllagerEntity) {
            AbstractIllagerEntity v = (AbstractIllagerEntity) mob;
            return mob.getTarget() == null && mob.getRandom().nextInt(80) == 0 && v.getCurrentRaid() ==  null;
        }

        return mob.getTarget() == null && mob.getRandom().nextInt(80) == 0 ;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getTarget() == null;
    }

    @Override
    public void start() {
        super.start();
        this.delayCounter = 0;
        this.canBreakBlock = false;
    }

    public boolean isShieldDisabled(CreatureEntity shieldUser) {
        return shieldUser instanceof IShieldUser && ((IShieldUser) shieldUser).isShieldDisabled();
    }


    @Override
    public void tick() {
        super.tick();

        if (mob instanceof IShieldUser && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob)) {
            if (!this.isShieldDisabled(mob)) {
                mob.startUsingItem(Hand.OFF_HAND);
            }
        }

        if (this.canBreakBlock) {
            this.BreakBlock();
        }

        List<T> liste = mob.level.getEntitiesOfClass(targetType, mob.getBoundingBox().inflate(30, 3, 30));
        liste.removeIf(pg -> pg.hurtTime <= 0);

        if (!liste.isEmpty()) {
            T creature = liste.get(this.mob.level.random.nextInt(liste.size()));
            mob.getNavigation().moveTo(creature, this.moveSpeed * 1.68 + new Random().nextDouble() / 10);
            this.delayCounter = 110;
            this.canBreakBlock = true;
        }


        if (this.delayCounter <= 0) {
            List<T> list = mob.level.getEntitiesOfClass(targetType, mob.getBoundingBox().inflate(100, 3, 100));
            if (!list.isEmpty()) {
                T creature = list.get(this.mob.level.random.nextInt(list.size()));
                if (!creature.getType().equals(mob.getType())) {
                    mob.getNavigation().moveTo(creature, this.moveSpeed + new Random().nextDouble() / 15);
                    this.delayCounter = 30;
                }
            }
        }

        if (mob.getNavigation().isDone()) {
            this.delayCounter--;
            this.canBreakBlock = false;
        }
    }

    private void BreakBlock() {
        //if (mob.isAlive()) {

        //    if (mob.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mob.level, mob)) {
        //        boolean destroyedLeafBlock = false;
        //        AxisAlignedBB axisalignedbb = mob.getBoundingBox().inflate(0.35D);

        //        for(BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
        //            BlockState blockstate = mob.level.getBlockState(blockpos);
        //            if (blockpos.getY() >= mob.getY()){
        //                destroyedLeafBlock = mob.level.destroyBlock(blockpos, true, mob) || destroyedLeafBlock;
        //            }
        //        }
        //    }
        //}
    }
}
