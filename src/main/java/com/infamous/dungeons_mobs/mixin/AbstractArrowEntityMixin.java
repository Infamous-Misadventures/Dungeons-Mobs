package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(AbstractArrowEntity.class)
public abstract class AbstractArrowEntityMixin extends Entity {
    private Method GET_OWNER_METHOD;
    public AbstractArrowEntityMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(at = @At("RETURN"), method = "getWaterInertia", cancellable = true)
    private void checkShotByAquaticMob(CallbackInfoReturnable<Float> cir){
        Entity owner = this.getOwnerByReflection();
        if(owner instanceof IAquaticMob){
            cir.setReturnValue(0.99F);
        }
    }

    @Nullable
    private Entity getOwnerByReflection() {
        if(GET_OWNER_METHOD == null){
            GET_OWNER_METHOD = ObfuscationReflectionHelper.findMethod(ProjectileEntity.class, "func_234616_v_");
        }
        Entity owner = null;
        try {
            owner = (Entity) GET_OWNER_METHOD.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return owner;
    }
}
