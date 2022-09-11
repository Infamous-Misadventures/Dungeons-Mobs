package com.infamous.dungeons_mobs.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EmptyRenderer extends EntityRenderer<Entity> {

	   public EmptyRenderer(EntityRendererManager p_i46553_1_) {
	      super(p_i46553_1_);
	   }

	@Override
	public ResourceLocation getTextureLocation(Entity p_110775_1_) {
		return null;
	}
}