package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.items.VanguardArmorItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class VanguardArmorModel extends AnimatedGeoModel<VanguardArmorItem>
{
    @Override
    public ResourceLocation getModelLocation(VanguardArmorItem vanguardArmorItem) {
        return new ResourceLocation(MODID, "geo/armor/vanguard_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VanguardArmorItem vanguardArmorItem) {
        return new ResourceLocation(MODID, "textures/models/armor/vanguard_armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VanguardArmorItem vanguardArmorItem) {
        return new ResourceLocation(MODID, "animations/armor_default.animation.json");
    }
}
