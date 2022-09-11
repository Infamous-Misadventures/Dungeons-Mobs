package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.items.armor.WindcallerClothesArmorGear;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WindcallerClothesArmorGearModel extends AnimatedGeoModel<WindcallerClothesArmorGear> {
    @Override
    public ResourceLocation getModelLocation(WindcallerClothesArmorGear object) {
        return object.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureLocation(WindcallerClothesArmorGear object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(WindcallerClothesArmorGear object) {
        return object.getAnimationFileLocation();
    }
}
