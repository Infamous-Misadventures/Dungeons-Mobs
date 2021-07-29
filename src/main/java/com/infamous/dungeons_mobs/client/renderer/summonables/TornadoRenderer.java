package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.entities.summonables.TornadoEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TornadoRenderer extends EntityRenderer<TornadoEntity> {
   public TornadoRenderer(EntityRendererManager manager) {
      super(manager);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(TornadoEntity entity) {
      return AtlasTexture.LOCATION_BLOCKS;
   }
}