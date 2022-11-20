package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderModel.class)
public class SpiderModelMixin{

    @Shadow @Final private ModelPart root;
    private float originalBodyRotateAngleX;
    private float originalBodyRotationPointY;
    private float originalBodyRotationPointZ;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo callbackInfo){
        this.originalBodyRotateAngleX = getBody1().xRot;
        this.originalBodyRotationPointY = getBody1().y;
        this.originalBodyRotationPointZ = getBody1().z;
    }

    private ModelPart getBody1() {
        return root.getChild("body1");
    }


    @Inject(at =@At("RETURN"), method = "setupAnim", cancellable = true)
    private void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo){
        if (entityIn instanceof Mob && entityIn instanceof IWebShooter){
            IWebShooter webShooter = (IWebShooter)entityIn;

            if(webShooter.isWebShooting()){
                getBody1().xRot = ((float)Math.PI / 6F);
                getBody1().y = originalBodyRotationPointY - 5;
                getBody1().z = originalBodyRotationPointZ - 2;
            } else {
                getBody1().xRot = this.originalBodyRotateAngleX;
                getBody1().y = originalBodyRotationPointY;
                getBody1().z = originalBodyRotationPointZ;
            }
        }
    }
}
