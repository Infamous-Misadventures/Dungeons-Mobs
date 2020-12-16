package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CobwebProjectileRenderer extends SpriteRenderer<CobwebProjectileEntity> {

    public CobwebProjectileRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, Minecraft.getInstance().getItemRenderer(), 0.75F, true);
    }
}
