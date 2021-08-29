package com.infamous.dungeons_mobs.client.renderer.slime;

import com.infamous.dungeons_mobs.entities.slime.ConjuredSlimeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class ConjuredSlimeRenderer extends SlimeRenderer {
   private static final ResourceLocation CONJURED_SLIME_TEXTURE = new ResourceLocation(MODID, "textures/entity/slime/conjured_slime.png");

   public ConjuredSlimeRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn);
   }

   @Override
   public ResourceLocation getTextureLocation(SlimeEntity entity) {
      return CONJURED_SLIME_TEXTURE;
   }
}