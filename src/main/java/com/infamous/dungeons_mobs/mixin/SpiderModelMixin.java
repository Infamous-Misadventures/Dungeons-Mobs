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

    @Shadow @Final private ModelRenderer spiderBody;
    private float originalBodyRotateAngleX;
    private float originalBodyRotationPointY;
    private float originalBodyRotationPointZ;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo callbackInfo){
        this.originalBodyRotateAngleX = this.spiderBody.rotateAngleX;
        this.originalBodyRotationPointY = this.spiderBody.rotationPointY;
        this.originalBodyRotationPointZ = this.spiderBody.rotationPointZ;
    }


    @Inject(at =@At("RETURN"), method = "setRotationAngles", cancellable = true)
    private void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo){
        if(entityIn instanceof MobEntity && entityIn instanceof IWebShooter){
            IWebShooter webShooter = (IWebShooter)entityIn;
            if(webShooter.isWebShooting()){
                spiderBody.rotateAngleX = ((float)Math.PI / 6F);
                spiderBody.rotationPointY = originalBodyRotationPointY - 5;
                spiderBody.rotationPointZ = originalBodyRotationPointZ - 2;
            } else{
                spiderBody.rotateAngleX = this.originalBodyRotateAngleX;
                spiderBody.rotationPointY = originalBodyRotationPointY;
                spiderBody.rotationPointZ = originalBodyRotationPointZ;
            }
        }
    }
}
