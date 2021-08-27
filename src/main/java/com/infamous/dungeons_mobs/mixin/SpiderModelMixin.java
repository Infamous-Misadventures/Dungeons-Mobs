package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderModel.class)
public class SpiderModelMixin{

    @Shadow @Final private ModelRenderer body1;
    private float originalBodyRotateAngleX;
    private float originalBodyRotationPointY;
    private float originalBodyRotationPointZ;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo callbackInfo){
        this.originalBodyRotateAngleX = this.body1.xRot;
        this.originalBodyRotationPointY = this.body1.y;
        this.originalBodyRotationPointZ = this.body1.z;
    }


    @Inject(at =@At("RETURN"), method = "setupAnim", cancellable = true)
    private void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo){
        if(entityIn instanceof MobEntity && entityIn instanceof IWebShooter){
            IWebShooter webShooter = (IWebShooter)entityIn;
            if(webShooter.isWebShooting()){
                body1.xRot = ((float)Math.PI / 6F);
                body1.y = originalBodyRotationPointY - 5;
                body1.z = originalBodyRotationPointZ - 2;
            } else{
                body1.xRot = this.originalBodyRotateAngleX;
                body1.y = originalBodyRotationPointY;
                body1.z = originalBodyRotationPointZ;
            }
        }
    }
}
