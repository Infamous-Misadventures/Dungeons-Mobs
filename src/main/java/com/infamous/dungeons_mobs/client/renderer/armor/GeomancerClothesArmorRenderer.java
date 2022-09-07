package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_mobs.client.models.armor.GeomancerClothesModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.LivingEntity;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class GeomancerClothesArmorRenderer extends BaseDungeonsGeoArmorRenderer {
    public GeomancerClothesArmorRenderer()
    {
        super(new GeomancerClothesModel());
    }

    @Override
    protected boolean isClothes() {
        return true;
    }

}
