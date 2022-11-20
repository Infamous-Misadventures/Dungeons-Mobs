package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.ArmorGearRenderer;
import com.infamous.dungeons_mobs.client.models.armor.NecromancerArmorGearModel;
import com.infamous.dungeons_mobs.items.armor.NecromancerArmorGear;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.inventory.EquipmentSlotType;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeoUtils;

public class NecromancerArmorGearRenderer extends ArmorGearRenderer<NecromancerArmorGear> {

	public String hoodBone = "armorHood";
	public String leggingsBodyBone = "armorLeggingsTop";
	
    public NecromancerArmorGearRenderer() {
        super(new NecromancerArmorGearModel<>());
    }

	@Override
	public void fitToBiped() {
		super.fitToBiped();
		if (this.hoodBone != null) {
			IBone hoodBone = this.getGeoModelProvider().getBone(this.hoodBone);
			GeoUtils.copyRotations(this.head, hoodBone);
			hoodBone.setPositionX(this.head.x);
			hoodBone.setPositionY(-this.head.y);
			hoodBone.setPositionZ(this.head.z);
		}
		
		if (this.leggingsBodyBone != null) {
			IBone leggingsBodyBone = this.getGeoModelProvider().getBone(this.leggingsBodyBone);
			GeoUtils.copyRotations(this.body, leggingsBodyBone);
			leggingsBodyBone.setPositionX(this.body.x);
			leggingsBodyBone.setPositionY(-this.body.y);
			leggingsBodyBone.setPositionZ(this.body.z);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public GeoArmorRenderer applySlot(EquipmentSlotType slot) {
		super.applySlot(slot);
		
		this.getGeoModelProvider().getModel(this.getGeoModelProvider().getModelLocation(currentArmorItem));

		IBone hoodBone = this.getAndHideBone(this.hoodBone);
		IBone leggingsBodyBone = this.getAndHideBone(this.leggingsBodyBone);

		switch (slot) {
		case HEAD:
			break;
		case CHEST:
			if (hoodBone != null)
				hoodBone.setHidden(false);
			break;
		case LEGS:
			if (leggingsBodyBone != null)
				leggingsBodyBone.setHidden(false);
			break;
		case FEET:
			break;
		}
		return this;
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
