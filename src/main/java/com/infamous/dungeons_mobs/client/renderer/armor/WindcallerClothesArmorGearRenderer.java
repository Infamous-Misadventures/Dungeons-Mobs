package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterialBaseType;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.infamous.dungeons_mobs.client.models.armor.WindcallerClothesArmorGearModel;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.infamous.dungeons_mobs.items.armor.WindcallerClothesArmorGear;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IArmorMaterial;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import static com.infamous.dungeons_mobs.mod.ModEntityTypes.WINDCALLER;

public class WindcallerClothesArmorGearRenderer extends GeoArmorRenderer<WindcallerClothesArmorGear> {


    public WindcallerClothesArmorGearRenderer() {
        super(new WindcallerClothesArmorGearModel());
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
        IArmorMaterial material = this.currentArmorItem.getMaterial();
        if(bone.getName().contains("Body") && material instanceof DungeonsArmorMaterial && ((DungeonsArmorMaterial) material).getBaseType() == ArmorMaterialBaseType.CLOTH){
            stack.scale(1.0F, 1.0F, 0.85F);
        }
        if(entityLiving instanceof WindcallerEntity && bone.getName().contains("Head")){
            stack.scale(0.93F, 0.93F, 0.93F);
        }
        RenderUtils.moveBackFromPivot(bone, stack);

        if (!bone.isHidden()) {
            for (GeoCube cube : bone.childCubes) {
                stack.pushPose();
                if (!bone.cubesAreHidden()) {
                    int overlay = entityLiving.getType() == WINDCALLER.get() ? LivingRenderer.getOverlayCoords(entityLiving, 0.0F) : packedOverlayIn;
                    renderCube(cube, stack, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
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
