package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.ArmorGearRenderer;
import com.infamous.dungeons_mobs.client.models.armor.NecromancerArmorGearModel;
import com.infamous.dungeons_mobs.items.armor.NecromancerArmorGear;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NecromancerArmorGearRenderer extends ArmorGearRenderer<NecromancerArmorGear> {

    public NecromancerArmorGearRenderer() {
        super(new NecromancerArmorGearModel<>());
    }

    @Override
    public void render(float partialTicks, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn) {

        AnimatedGeoModel<NecromancerArmorGear> geoModelProvider = getGeoModelProvider();
        if(geoModelProvider instanceof NecromancerArmorGearModel){
            ((NecromancerArmorGearModel<NecromancerArmorGear>) geoModelProvider).setWearer(this.entityLiving);
        }
        super.render(partialTicks, stack, bufferIn, packedLightIn);
    }
}
