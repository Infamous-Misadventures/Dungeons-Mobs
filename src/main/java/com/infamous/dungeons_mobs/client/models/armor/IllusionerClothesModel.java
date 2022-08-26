package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.items.IllusionerClothesItem;
import com.infamous.dungeons_mobs.items.IllusionerClothesItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class IllusionerClothesModel extends AnimatedGeoModel<IllusionerClothesItem>
{
    @Override
    public ResourceLocation getModelLocation(IllusionerClothesItem t) {
        return new ResourceLocation(MODID, "geo/armor/illusioner_clothes.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(IllusionerClothesItem t) {
        return new ResourceLocation(MODID, "textures/models/armor/illusioner_clothes.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(IllusionerClothesItem t) {
        return new ResourceLocation(MODID, "animations/armor_default.animation.json");
    }
}
