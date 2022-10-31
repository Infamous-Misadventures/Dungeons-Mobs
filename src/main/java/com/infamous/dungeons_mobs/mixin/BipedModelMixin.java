package com.infamous.dungeons_mobs.mixin;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Mixin(BipedModel.class)
public class BipedModelMixin {

    @Shadow
    public ModelRenderer head;
    @Shadow
    public ModelRenderer hat;
    @Shadow
    public ModelRenderer body;
    @Shadow
    public ModelRenderer rightArm;
    @Shadow
    public ModelRenderer leftArm;
    @Shadow
    public ModelRenderer rightLeg;
    @Shadow
    public ModelRenderer leftLeg;

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/renderer/entity/model/BipedModel;headParts()Ljava/lang/Iterable;", cancellable = true)
    private void moveHatToHeadParts(CallbackInfoReturnable<Iterable<ModelRenderer>> cir) {
        Iterable<ModelRenderer> parts = cir.getReturnValue();
        List<ModelRenderer> newResult = StreamSupport.stream(parts.spliterator(), false).collect(Collectors.toList());
        if(!newResult.contains(this.hat)){
            newResult.add(this.hat);
            cir.setReturnValue(newResult);
        }
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/renderer/entity/model/BipedModel;bodyParts()Ljava/lang/Iterable;", cancellable = true)
    private void removeHatfromBodyParts(CallbackInfoReturnable<Iterable<ModelRenderer>> cir) {
        Iterable<ModelRenderer> parts = cir.getReturnValue();
        List<ModelRenderer> newResult = StreamSupport.stream(parts.spliterator(), false).collect(Collectors.toList());
        if(newResult.contains(this.hat)){
            newResult.remove(this.hat);
            cir.setReturnValue(newResult);
        }
    }
}
