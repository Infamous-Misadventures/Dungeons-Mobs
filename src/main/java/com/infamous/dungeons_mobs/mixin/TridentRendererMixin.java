package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IHasItemStackData;
import com.infamous.dungeons_mobs.items.ColoredTridentItem;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TridentRenderer;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentRenderer.class)
public abstract class TridentRendererMixin extends EntityRenderer<TridentEntity> {
    protected TridentRendererMixin(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Inject(at = @At("RETURN"), method = "getTextureLocation", cancellable = true)
    private void checkColoredTexture(TridentEntity tridentEntity, CallbackInfoReturnable<ResourceLocation> cir){
        if(tridentEntity instanceof IHasItemStackData){
            ItemStack dataItem = ((IHasItemStackData) tridentEntity).getDataItem();
            if(dataItem.getItem() instanceof ColoredTridentItem){
                cir.setReturnValue(CustomISTER.getTridentTexture(((ColoredTridentItem) dataItem.getItem()).getTridentColor()));
            }
        }
    }
}
