package com.infamous.dungeons_mobs.entities.piglin.ai;

import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.projectiles.BlueNethershroomEntity;
import com.infamous.dungeons_mobs.items.BlueNethershroomItem;
import com.infamous.dungeons_mobs.tasks.ThrowAtTargetTask;
import com.infamous.dungeons_mobs.utils.BrainHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.AttackStrafingTask;
import net.minecraft.entity.ai.brain.task.SupplementedTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Predicate;

public class FungusThrowerAi {

    public static final Predicate<Item> FUNGUS_ITEM_PREDICATE = item -> item instanceof BlueNethershroomItem;

    public static <E extends FungusThrowerEntity> void addFungusThrowerTasks(Brain<E> brain) {

        ImmutableList<? extends Task<? super E>> additionalFightTasks = ImmutableList.of(
                new SupplementedTask<>(FungusThrowerAi::hasBlueNethershroom, new AttackStrafingTask<>(6, 0.75F)),
                new ThrowAtTargetTask<>(FUNGUS_ITEM_PREDICATE, FungusThrowerAi::performFungusThrow));

        int priorityStart = 7; // Number of fight tasks piglins start with - would like to find a way to dynamically get this from the brain
        ImmutableList<? extends Pair<Integer, ? extends Task<? super E>>> prioritizedFightTasks =
                BrainHelper.createPriorityPairs(priorityStart, additionalFightTasks);

        BrainHelper.addPrioritizedBehaviors(Activity.FIGHT, prioritizedFightTasks, brain);
    }

    public static void performFungusThrow(LivingEntity fungusThrower, LivingEntity attackTarget){
        Vector3d targetDeltaMove = attackTarget.getDeltaMovement();
        double xDiff = attackTarget.getX() + targetDeltaMove.x - fungusThrower.getX();
        double yDiff = attackTarget.getY() - fungusThrower.getY();
        double zDiff = attackTarget.getZ() + targetDeltaMove.z - fungusThrower.getZ();
        float horizDistSq = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);

        BlueNethershroomEntity blueNethershroom = new BlueNethershroomEntity(fungusThrower.level, fungusThrower);
        ItemStack blueNethershroomStack = blueNethershroom.getItem();
        BlueNethershroomEntity.setLightBluePotionColor(blueNethershroomStack);
        blueNethershroom.setItem(PotionUtils.setPotion(blueNethershroomStack, Potions.POISON));

        blueNethershroom.xRot -= -20.0F;
        blueNethershroom.shoot(xDiff, yDiff + (double)(horizDistSq * 0.2F), zDiff, 0.75F, 8.0F);
        if (!fungusThrower.isSilent()) {
            fungusThrower.level.playSound((PlayerEntity)null, fungusThrower.getX(), fungusThrower.getY(), fungusThrower.getZ(), SoundEvents.FUNGUS_HIT, fungusThrower.getSoundSource(), 1.0F, 0.8F + fungusThrower.getRandom().nextFloat() * 0.4F);
        }

        fungusThrower.level.addFreshEntity(blueNethershroom);
        fungusThrower.swing(ProjectileHelper.getWeaponHoldingHand(fungusThrower, FUNGUS_ITEM_PREDICATE));
    }

    private static boolean hasBlueNethershroom(LivingEntity living) {
        return living.isHolding(FUNGUS_ITEM_PREDICATE);
    }

    public static boolean isBlueNethershroom(ItemStack itemStack) {
        return FUNGUS_ITEM_PREDICATE.test(itemStack.getItem());
    }

}
