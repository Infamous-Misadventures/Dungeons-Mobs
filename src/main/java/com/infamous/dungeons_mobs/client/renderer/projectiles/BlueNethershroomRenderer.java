package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.entities.projectiles.BlueNethershroomEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlueNethershroomRenderer extends ThrownItemRenderer<BlueNethershroomEntity> {

	public BlueNethershroomRenderer(EntityRendererProvider.Context entityRendererManager) {
		super(entityRendererManager, 0.75F, true);
	}
}
