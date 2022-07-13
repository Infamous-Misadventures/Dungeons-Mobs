package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.PoisonQuillVineModel;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class PoisonQuillVineRenderer<T extends PoisonQuillVineEntity> extends GeoEntityRenderer<T> {
   public PoisonQuillVineRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new PoisonQuillVineModel());
   }
}