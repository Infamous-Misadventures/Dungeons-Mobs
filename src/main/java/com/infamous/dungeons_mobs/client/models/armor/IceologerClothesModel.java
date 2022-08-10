package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.items.IceologerClothesItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class IceologerClothesModel extends AnimatedGeoModel<IceologerClothesItem>
{
    @Override
    public ResourceLocation getModelLocation(IceologerClothesItem iceologerClothesItem) {
        return new ResourceLocation(MODID, "geo/armor/iceologer_clothes.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(IceologerClothesItem iceologerClothesItem) {
        return new ResourceLocation(MODID, "textures/models/armor/iceologer_clothes.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(IceologerClothesItem iceologerClothesItem) {
        return new ResourceLocation(MODID, "animations/armor_default.animation.json");
    }
}
