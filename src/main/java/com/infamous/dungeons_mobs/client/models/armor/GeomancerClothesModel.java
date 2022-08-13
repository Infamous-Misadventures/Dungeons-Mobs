package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.items.GeomancerClothesItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class GeomancerClothesModel extends AnimatedGeoModel<GeomancerClothesItem>
{
    @Override
    public ResourceLocation getModelLocation(GeomancerClothesItem geomancerClothesItem) {
        return new ResourceLocation(MODID, "geo/armor/geomancer_clothes.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GeomancerClothesItem geomancerClothesItem) {
        return new ResourceLocation(MODID, "textures/models/armor/geomancer_clothes.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GeomancerClothesItem geomancerClothesItem) {
        return new ResourceLocation(MODID, "animations/armor_default.animation.json");
    }
}
