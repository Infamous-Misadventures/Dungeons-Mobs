package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.entities.projectiles.WindcallerBlastProjectileEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WindcallerBlastProjectileRenderer extends EntityRenderer<WindcallerBlastProjectileEntity> {

	   public WindcallerBlastProjectileRenderer(EntityRendererManager p_i46553_1_) {
	      super(p_i46553_1_);
	   }

	@Override
	public ResourceLocation getTextureLocation(WindcallerBlastProjectileEntity p_110775_1_) {
		return null;
	}
}