package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
	
	@Inject(at = @At("HEAD"), method = "canHitEntity", cancellable = true)
	private void canHitEntity(Entity entity, CallbackInfoReturnable<Boolean> callback) {
		if (entity instanceof IllusionerCloneEntity) {
			if (this.getOwner() != null && ((IllusionerCloneEntity)entity).isAlliedTo(this.getOwner())) {
				callback.setReturnValue(false);
			}
		}
		
		if (entity instanceof MageCloneEntity) {
			if (this.getOwner() != null && ((MageCloneEntity)entity).isAlliedTo(this.getOwner())) {
				callback.setReturnValue(false);
			}
		}
		
		if (entity instanceof TridentStormEntity) {
				callback.setReturnValue(false);
		}
	}
	
	@Shadow
    public abstract Entity getOwner();
}
