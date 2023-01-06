package com.infamous.dungeons_mobs.entities.piglin.ai;

import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.projectiles.BlueNethershroomEntity;
import com.infamous.dungeons_mobs.items.BlueNethershroomItem;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.tasks.ThrowAtTargetTask;
import com.infamous.dungeons_mobs.utils.BrainHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class FungusThrowerAi {

    public static final Predicate<Item> FUNGUS_ITEM_PREDICATE = item -> item instanceof BlueNethershroomItem;
    public static final Predicate<ItemStack> FUNGUS_ITEM_STACK_PREDICATE = itemStack -> itemStack.getItem() instanceof BlueNethershroomItem;

    public static <E extends FungusThrowerEntity> void addFungusThrowerTasks(Brain<E> brain) {

        ImmutableList<? extends Behavior<? super E>> additionalFightTasks = ImmutableList.of(
                new RunIf<>(FungusThrowerAi::hasBlueNethershroom, new BackUpIfTooClose<>(6, 0.75F)),
                new ThrowAtTargetTask<>(FUNGUS_ITEM_STACK_PREDICATE, FungusThrowerAi::performFungusThrow));

        int priorityStart = 7; // Number of fight tasks piglins start with - would like to find a way to dynamically get this from the brain
        ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> prioritizedFightTasks =
                BrainHelper.createPriorityPairs(priorityStart, additionalFightTasks);

        BrainHelper.addPrioritizedBehaviors(Activity.FIGHT, prioritizedFightTasks, brain);
    }

    public static void performFungusThrow(LivingEntity fungusThrower, LivingEntity attackTarget) {
        Vec3 targetDeltaMove = attackTarget.getDeltaMovement();
        double xDiff = attackTarget.getX() + targetDeltaMove.x - fungusThrower.getX();
        double yDiff = attackTarget.getY() - fungusThrower.getY();
        double zDiff = attackTarget.getZ() + targetDeltaMove.z - fungusThrower.getZ();
        float horizDistSq = Mth.sqrt((float) (xDiff * xDiff + zDiff * zDiff));

        BlueNethershroomEntity blueNethershroom = new BlueNethershroomEntity(fungusThrower.level, fungusThrower);
        ItemStack blueNethershroomStack = blueNethershroom.getItem();
        BlueNethershroomEntity.setLightBluePotionColor(blueNethershroomStack);
        blueNethershroom.setItem(PotionUtils.setPotion(blueNethershroomStack, Potions.POISON));

        blueNethershroom.setXRot(blueNethershroom.getXRot() - -20.0F);
        blueNethershroom.shoot(xDiff, yDiff + (double) (horizDistSq * 0.2F), zDiff, 0.75F, 8.0F);
        if (!fungusThrower.isSilent()) {
            fungusThrower.level.playSound(null, fungusThrower.getX(), fungusThrower.getY(), fungusThrower.getZ(), ModSoundEvents.FUNGUS_THROWER_THROW.get(), fungusThrower.getSoundSource(), 1.0F, (fungusThrower.getRandom().nextFloat() - fungusThrower.getRandom().nextFloat()) * 0.2F + 1.0F);
        }

        fungusThrower.level.addFreshEntity(blueNethershroom);
        fungusThrower.swing(ProjectileUtil.getWeaponHoldingHand(fungusThrower, FUNGUS_ITEM_PREDICATE));
    }

    private static boolean hasBlueNethershroom(LivingEntity living) {
        return living.isHolding(FUNGUS_ITEM_STACK_PREDICATE);
    }

    public static boolean isBlueNethershroom(ItemStack itemStack) {
        return FUNGUS_ITEM_PREDICATE.test(itemStack.getItem());
    }

}
