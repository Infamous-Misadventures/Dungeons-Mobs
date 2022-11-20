package com.infamous.dungeons_mobs.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mixin(HumanoidModel.class)
public class BipedModelMixin {

    @Shadow
    public ModelPart head;
    @Shadow
    public ModelPart hat;
    @Shadow
    public ModelPart body;
    @Shadow
    public ModelPart rightArm;
    @Shadow
    public ModelPart leftArm;
    @Shadow
    public ModelPart rightLeg;
    @Shadow
    public ModelPart leftLeg;

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/renderer/entity/model/BipedModel;headParts()Ljava/lang/Iterable;", cancellable = true)
    private void moveHatToHeadParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {
        Iterable<ModelPart> parts = cir.getReturnValue();
        List<ModelPart> newResult = StreamSupport.stream(parts.spliterator(), false).collect(Collectors.toList());
        if(!newResult.contains(this.hat)){
            newResult.add(this.hat);
            cir.setReturnValue(newResult);
        }
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/renderer/entity/model/BipedModel;bodyParts()Ljava/lang/Iterable;", cancellable = true)
    private void removeHatfromBodyParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {
        Iterable<ModelPart> parts = cir.getReturnValue();
        List<ModelPart> newResult = StreamSupport.stream(parts.spliterator(), false).collect(Collectors.toList());
        if(newResult.contains(this.hat)){
            newResult.remove(this.hat);
            cir.setReturnValue(newResult);
        }
    }
}
