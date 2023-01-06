package com.infamous.dungeons_mobs.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EmptyRenderer extends EntityRenderer<Entity> {

	public EmptyRenderer(EntityRendererProvider.Context p_i46553_1_) {
		super(p_i46553_1_);
	}

	@Override
	public ResourceLocation getTextureLocation(Entity p_110775_1_) {
		return null;
	}
}