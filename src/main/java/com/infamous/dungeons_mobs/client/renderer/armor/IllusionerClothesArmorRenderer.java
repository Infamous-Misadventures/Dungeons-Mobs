package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_mobs.client.models.armor.IllusionerClothesModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.LivingEntity;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class IllusionerClothesArmorRenderer extends BaseDungeonsGeoArmorRenderer {
    public IllusionerClothesArmorRenderer()
    {
        super(new IllusionerClothesModel());
    }


    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn,
                                  int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.pushPose();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        EntityRenderer<? super LivingEntity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entityLiving);
        if(!(entityRenderer instanceof GeoEntityRenderer) || !bone.getName().contains("armor")) {
            RenderUtils.rotate(bone, stack);
        }
        RenderUtils.scale(bone, stack);
        if(bone.getName().contains("Body")){
            stack.scale(1.0F, 1.0F, 0.85F);
        }
        RenderUtils.moveBackFromPivot(bone, stack);

        if (!bone.isHidden()) {
            for (GeoCube cube : bone.childCubes) {
                stack.pushPose();
                if (!bone.cubesAreHidden()) {
                    renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }
                stack.popPose();
            }
        }
        if (!bone.childBonesAreHiddenToo()) {
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(childBone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }

        stack.popPose();
    }
}