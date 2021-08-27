package com.infamous.dungeons_mobs.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity {

    public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "canReplaceCurrentItem", cancellable = true)
    private void checkForAnyTrident(ItemStack replacement, ItemStack current, CallbackInfoReturnable<Boolean> cir){
        if (current.getItem() == Items.NAUTILUS_SHELL) {
            cir.setReturnValue(false);
        } else if (current.getItem() instanceof TridentItem) {
            if (replacement.getItem() instanceof TridentItem) {
                cir.setReturnValue(replacement.getDamageValue() < current.getDamageValue());
            } else {
                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(replacement.getItem() instanceof TridentItem || super.canReplaceCurrentItem(replacement, current));
        }
    }

    @ModifyVariable(at = @At("STORE"), method = "performRangedAttack")
    private TridentEntity createTrident(TridentEntity original){
        Hand tridentHoldingHand = ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof TridentItem);
        ItemStack tridentStack = this.getItemInHand(tridentHoldingHand);
        return new TridentEntity(this.level, this, tridentStack);
    }
}
