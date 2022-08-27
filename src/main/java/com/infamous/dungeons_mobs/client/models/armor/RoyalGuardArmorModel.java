package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.items.RoyalGuardArmorItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class RoyalGuardArmorModel extends AnimatedGeoModel<RoyalGuardArmorItem>
{
    @Override
    public ResourceLocation getModelLocation(RoyalGuardArmorItem iceologerClothesItem) {
        return new ResourceLocation(MODID, "geo/armor/royal_guard_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RoyalGuardArmorItem iceologerClothesItem) {
        return new ResourceLocation(MODID, "textures/models/armor/royal_guard_armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RoyalGuardArmorItem iceologerClothesItem) {
        return new ResourceLocation(MODID, "animations/armor_default.animation.json");
    }
}
