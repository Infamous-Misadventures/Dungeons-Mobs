package com.infamous.dungeons_mobs.client.renderer.projectiles;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomFireballRenderer extends ThrownItemRenderer<Fireball> {

	public CustomFireballRenderer(EntityRendererProvider.Context p_i226035_1_) {
		super(p_i226035_1_, 0.75F, true);
	}
}
