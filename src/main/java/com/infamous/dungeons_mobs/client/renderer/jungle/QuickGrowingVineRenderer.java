package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class QuickGrowingVineRenderer<T extends QuickGrowingVineEntity> extends GeoEntityRenderer<T> {
   public QuickGrowingVineRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new QuickGrowingVineModel());
   }
}