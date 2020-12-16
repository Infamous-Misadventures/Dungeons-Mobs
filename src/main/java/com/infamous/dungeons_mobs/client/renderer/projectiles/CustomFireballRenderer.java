package com.infamous.dungeons_mobs.client.renderer.projectiles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomFireballRenderer extends SpriteRenderer<AbstractFireballEntity> {

    public CustomFireballRenderer(EntityRendererManager p_i226035_1_) {
        super(p_i226035_1_, Minecraft.getInstance().getItemRenderer(), 0.75F, true);
    }
}
