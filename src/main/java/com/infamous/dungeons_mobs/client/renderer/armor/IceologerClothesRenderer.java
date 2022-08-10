package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_mobs.client.models.armor.IceologerClothesModel;
import com.infamous.dungeons_mobs.items.IceologerClothesItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class IceologerClothesRenderer extends GeoArmorRenderer<IceologerClothesItem>
{
    public IceologerClothesRenderer()
    {
        super(new IceologerClothesModel());
    }
}