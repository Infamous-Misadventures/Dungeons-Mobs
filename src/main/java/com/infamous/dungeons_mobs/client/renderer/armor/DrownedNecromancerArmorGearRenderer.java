package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.gearconfig.ArmorGearRenderer;
import com.infamous.dungeons_mobs.client.models.armor.DrownedNecromancerArmorGearModel;
import com.infamous.dungeons_mobs.items.armor.DrownedNecromancerArmorGear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeoUtils;

public class DrownedNecromancerArmorGearRenderer extends ArmorGearRenderer<DrownedNecromancerArmorGear> {

	public String hoodBone = "armorHood";

    public DrownedNecromancerArmorGearRenderer() {
        super(new DrownedNecromancerArmorGearModel<>());
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
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public GeoArmorRenderer applySlot(EquipmentSlot slot) {
		super.applySlot(slot);
		
		this.getGeoModelProvider().getModel(this.getGeoModelProvider().getModelResource(currentArmorItem));

		IBone hoodBone = this.getAndHideBone(this.hoodBone);

		switch (slot) {
		case HEAD:
			if (hoodBone != null)
				hoodBone.setHidden(false);
			break;
		case CHEST:
			break;
		case LEGS:
			break;
		case FEET:
			break;
		}
		return this;
	}
	
    @Override
    public void render(float partialTicks, PoseStack stack, VertexConsumer bufferIn, int packedLightIn) {

        AnimatedGeoModel<DrownedNecromancerArmorGear> geoModelProvider = getGeoModelProvider();
        if(geoModelProvider instanceof DrownedNecromancerArmorGearModel){
            ((DrownedNecromancerArmorGearModel<DrownedNecromancerArmorGear>) geoModelProvider).setWearer(this.entityLiving);
        }
        super.render(partialTicks, stack, bufferIn, packedLightIn);
    }
}
