package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.BlueNethershroomEntity;
import com.infamous.dungeons_mobs.mod.ModEffects;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public class BlueNethershroomItem extends ProjectileWeaponItem {
    public BlueNethershroomItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return itemStack -> itemStack.getItem() instanceof BlueNethershroomItem;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            BlueNethershroomEntity blueNethershroom = createBlueNethershroom(world, player, itemStack.copy());
            blueNethershroom.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.FUNGUS_THROWER_THROW.get(), player.getSoundSource(), 1.0F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F);
            world.addFreshEntity(blueNethershroom);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    public static BlueNethershroomEntity createBlueNethershroom(Level world, LivingEntity thrower, ItemStack itemstack) {
        BlueNethershroomEntity blueNethershroom = new BlueNethershroomEntity(world, thrower);
        blueNethershroom.setItem(PotionUtils.setCustomEffects(itemstack, List.of(new MobEffectInstance(ModEffects.WARPED.get(), 200))));
        return blueNethershroom;
    }


    public int getDefaultProjectileRange() {
        return 8;
    }
}