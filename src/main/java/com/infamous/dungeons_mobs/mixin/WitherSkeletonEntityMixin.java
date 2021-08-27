package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity {
    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
    }

    @Inject(at = @At("RETURN"), method = "getArrow")
    private void getWitherArrow(ItemStack ammoStack, float p_213624_2_, CallbackInfoReturnable<AbstractArrowEntity> cir){
        AbstractArrowEntity arrow = cir.getReturnValue();
        arrow.clearFire();
        if (arrow instanceof ArrowEntity && ((ArrowAccessor)arrow).getEffects().isEmpty()) {
            int difficultyFactor = 0;
            if (this.level.getDifficulty() == Difficulty.NORMAL) {
                difficultyFactor = 5;
            } else if (this.level.getDifficulty() == Difficulty.HARD) {
                difficultyFactor = 10;
            }
            if(difficultyFactor > 0){
                ((ArrowEntity)arrow).addEffect(new EffectInstance(Effects.WITHER, difficultyFactor * 20));
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "populateDefaultEquipmentSlots")
    private void setEquipmentOnInitialSpawn(DifficultyInstance difficultyInstance, CallbackInfo ci){
        this.setItemSlot(EquipmentSlotType.MAINHAND, this.createSpawnWeapon());
    }

    private ItemStack createSpawnWeapon() {
        ItemStack bowStack = new ItemStack(Items.BOW);
        ItemStack swordStack = new ItemStack(Items.STONE_SWORD);
        if(DungeonsGearCompat.isLoaded()){
            bowStack = new ItemStack(DungeonsGearCompat.getRedSnake().get());
            swordStack = new ItemStack(DungeonsGearCompat.getStoneSword().get());
        }
        return (double)this.random.nextFloat() < 0.5D ? bowStack : swordStack;
    }
}
