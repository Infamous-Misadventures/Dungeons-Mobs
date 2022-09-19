package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.ArmorGearRenderer;
import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterialBaseType;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.infamous.dungeons_mobs.items.armor.NecromancerArmorGear;
import com.infamous.dungeons_mobs.items.armor.WindcallerClothesArmorGear;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.GeoUtils;
import software.bernie.geckolib3.util.RenderUtils;

public class NecromancerArmorGearRenderer extends ArmorGearRenderer<NecromancerArmorGear> {

    public String leggingsBodyBone = "armorLeggingsTop";
    public String hoodBodyBone = "armorHood";

//    @Override
//    public void fitToBiped() {
//        super.fitToBiped();
//        if (this.leggingsBodyBone != null) {
//            IBone leggingsBodyBone = this.getGeoModelProvider().getBone(this.leggingsBodyBone);
//            GeoUtils.copyRotations(this.body, leggingsBodyBone);
//            leggingsBodyBone.setPositionX(this.body.x);
//            leggingsBodyBone.setPositionY(-this.body.y);
//            leggingsBodyBone.setPositionZ(this.body.z);
//        }
//        if (this.hoodBodyBone != null) {
//            IBone hoodBodyBone = this.getGeoModelProvider().getBone(this.hoodBodyBone);
//            GeoUtils.copyRotations(this.head, hoodBodyBone);
//            hoodBodyBone.setPositionX(this.head.x);
//            hoodBodyBone.setPositionY(-this.head.y);
//            hoodBodyBone.setPositionZ(this.head.z);
//        }
//    }
//
//
//    @SuppressWarnings("incomplete-switch")
//    @Override
//    public GeoArmorRenderer applySlot(EquipmentSlotType slot) {
//        this.getGeoModelProvider().getModel(this.getGeoModelProvider().getModelLocation(currentArmorItem));
//
//        IBone headBone = this.getAndHideBone(this.headBone);
//        IBone bodyBone = this.getAndHideBone(this.bodyBone);
//        IBone leggingsBodyBone = this.getAndHideBone(this.leggingsBodyBone);
//        IBone hoodBodyBone = this.getAndHideBone(this.hoodBodyBone);
//        IBone rightArmBone = this.getAndHideBone(this.rightArmBone);
//        IBone leftArmBone = this.getAndHideBone(this.leftArmBone);
//        IBone rightLegBone = this.getAndHideBone(this.rightLegBone);
//        IBone leftLegBone = this.getAndHideBone(this.leftLegBone);
//        IBone rightBootBone = this.getAndHideBone(this.rightBootBone);
//        IBone leftBootBone = this.getAndHideBone(this.leftBootBone);
//
//        switch (slot) {
//            case HEAD:
//                if (headBone != null)
//                    headBone.setHidden(false);
//                break;
//            case CHEST:
//                if (bodyBone != null)
//                    bodyBone.setHidden(false);
//                if (rightArmBone != null)
//                    rightArmBone.setHidden(false);
//                if (leftArmBone != null)
//                    leftArmBone.setHidden(false);
//                if (hoodBodyBone != null)
//                    hoodBodyBone.setHidden(false);
//                break;
//            case LEGS:
//                if (rightLegBone != null)
//                    rightLegBone.setHidden(false);
//                if (leftLegBone != null)
//                    leftLegBone.setHidden(false);
//                if (leggingsBodyBone != null)
//                    leggingsBodyBone.setHidden(false);
//                break;
//            case FEET:
//                if (rightBootBone != null)
//                    rightBootBone.setHidden(false);
//                if (leftBootBone != null)
//                    leftBootBone.setHidden(false);
//                break;
//        }
//        return this;
//    }
}
