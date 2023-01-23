package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.gearconfig.ArmorGearRenderer;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterialBaseType;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.infamous.dungeons_mobs.client.models.armor.WindcallerArmorGearModel;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.infamous.dungeons_mobs.items.armor.WindcallerArmorGear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class WindcallerArmorGearRenderer extends ArmorGearRenderer<WindcallerArmorGear> {

    public WindcallerArmorGearRenderer() {
        super(new WindcallerArmorGearModel<>());
    }

    public void prepMatrixForBone(PoseStack stack, GeoBone bone) {
        RenderUtils.translateMatrixToBone(stack, bone);
        RenderUtils.translateToPivotPoint(stack, bone);
        EntityRenderer<? super LivingEntity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(this.entityLiving);
        if (!(entityRenderer instanceof GeoEntityRenderer) || !bone.getName().contains("armor")) {
            RenderUtils.rotateMatrixAroundBone(stack, bone);
        }

        RenderUtils.scaleMatrixForBone(stack, bone);
        ArmorMaterial material = ((ArmorGear)this.currentArmorItem).getMaterial();
        if (bone.getName().contains("Body") && material instanceof DungeonsArmorMaterial && ((DungeonsArmorMaterial)material).getBaseType() == ArmorMaterialBaseType.CLOTH) {
            stack.scale(1.0F, 1.0F, 0.93F);
        }
        if(entityLiving instanceof WindcallerEntity && bone.getName().contains("Head")){
            stack.scale(0.93F, 0.93F, 0.93F);
            stack.translate(0.0D, 0.116D, 0.0D);
        }
        RenderUtils.translateAwayFromPivotPoint(stack, bone);
    }

    @Override
    public void render(float partialTicks, PoseStack stack, VertexConsumer bufferIn, int packedLightIn) {

        AnimatedGeoModel<WindcallerArmorGear> geoModelProvider = getGeoModelProvider();
        if(geoModelProvider instanceof WindcallerArmorGearModel){
            ((WindcallerArmorGearModel<WindcallerArmorGear>) geoModelProvider).setWearer(this.entityLiving);
        }
        super.render(partialTicks, stack, bufferIn, packedLightIn);
    }
}
