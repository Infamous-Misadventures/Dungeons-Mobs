package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;

@Mixin(Drowned.class)
public abstract class DrownedEntityMixin extends Zombie {

	public DrownedEntityMixin(EntityType<? extends Zombie> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(at = @At("RETURN"), method = "canReplaceCurrentItem", cancellable = true)
	private void checkForAnyTrident(ItemStack replacement, ItemStack current, CallbackInfoReturnable<Boolean> cir) {
		if (current.getItem() == Items.NAUTILUS_SHELL) {
			cir.setReturnValue(false);
		} else if (current.getItem() instanceof TridentItem) {
			if (replacement.getItem() instanceof TridentItem) {
				cir.setReturnValue(replacement.getDamageValue() < current.getDamageValue());
			} else {
				cir.setReturnValue(false);
			}
		} else {
			cir.setReturnValue(
					replacement.getItem() instanceof TridentItem || super.canReplaceCurrentItem(replacement, current));
		}
	}

	@ModifyVariable(at = @At("STORE"), method = "performRangedAttack")
	private ThrownTrident createTrident(ThrownTrident original) {
		InteractionHand tridentHoldingHand = ProjectileUtil.getWeaponHoldingHand(this,
				item -> item instanceof TridentItem);
		ItemStack tridentStack = this.getItemInHand(tridentHoldingHand);
		return new ThrownTrident(this.level, this, tridentStack);
	}
}
