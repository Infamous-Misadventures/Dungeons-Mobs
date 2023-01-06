package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.gearconfig.ArmorGearRenderer;
import com.infamous.dungeons_mobs.client.models.armor.IllusionerArmorGearModel;
import com.infamous.dungeons_mobs.items.armor.IllusionerArmorGear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import software.bernie.geckolib3.model.AnimatedGeoModel;

public class IllusionerArmorGearRenderer extends ArmorGearRenderer<IllusionerArmorGear> {

	public IllusionerArmorGearRenderer() {
		super(new IllusionerArmorGearModel<>());
	}

	@Override
	public void render(float partialTicks, PoseStack stack, VertexConsumer bufferIn, int packedLightIn) {

		AnimatedGeoModel<IllusionerArmorGear> geoModelProvider = getGeoModelProvider();
		if (geoModelProvider instanceof IllusionerArmorGearModel) {
			((IllusionerArmorGearModel<IllusionerArmorGear>) geoModelProvider).setWearer(this.entityLiving);
		}
		super.render(partialTicks, stack, bufferIn, packedLightIn);
	}
}
