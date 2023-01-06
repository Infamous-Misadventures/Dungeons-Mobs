package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.entities.projectiles.SlimeballEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimeballRenderer extends ThrownItemRenderer<SlimeballEntity> {

	public SlimeballRenderer(EntityRendererProvider.Context entityRendererManager) {
		super(entityRendererManager, 0.75F, true);
	}
}
