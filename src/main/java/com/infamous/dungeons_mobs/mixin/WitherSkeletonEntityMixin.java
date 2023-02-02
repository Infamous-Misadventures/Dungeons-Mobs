package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.compat.DungeonsGearCompat;
import com.infamous.dungeons_mobs.entities.SpawnEquipmentHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherSkeleton.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeleton {
    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeleton> p_i48555_1_, Level p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
    }

    @Inject(at = @At("RETURN"), method = "getArrow")
    private void getWitherArrow(ItemStack ammoStack, float p_213624_2_, CallbackInfoReturnable<AbstractArrow> cir) {
        AbstractArrow arrow = cir.getReturnValue();
        arrow.clearFire();
        if (arrow instanceof Arrow && ((ArrowAccessor) arrow).getEffects().isEmpty()) {
            int difficultyFactor = 0;
            if (this.level.getDifficulty() == Difficulty.NORMAL) {
                difficultyFactor = 5;
            } else if (this.level.getDifficulty() == Difficulty.HARD) {
                difficultyFactor = 10;
            }
            if (difficultyFactor > 0) {
                ((Arrow) arrow).addEffect(new MobEffectInstance(MobEffects.WITHER, difficultyFactor * 20));
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "populateDefaultEquipmentSlots")
    private void setEquipmentOnInitialSpawn(RandomSource randomSource, DifficultyInstance difficultyInstance, CallbackInfo ci) {
        SpawnEquipmentHelper.equipMainhand(this.createSpawnWeapon(), this);
    }

    private ItemStack createSpawnWeapon() {
        ItemStack bowStack = new ItemStack(Items.BOW);
        ItemStack swordStack = new ItemStack(Items.STONE_SWORD);
        if (DungeonsGearCompat.isLoaded()) {
            bowStack = new ItemStack(DungeonsGearCompat.getRedSnake().get());
            swordStack = new ItemStack(DungeonsGearCompat.getStoneSword().get());
        }
        return (double) this.random.nextFloat() < 0.5D ? bowStack : swordStack;
    }
}
