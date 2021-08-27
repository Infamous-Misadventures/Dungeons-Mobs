package com.infamous.dungeons_mobs.client.renderer.ender;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.monster.EndermanEntity;

public class WatchlingRenderer extends EndermanRenderer {

    public WatchlingRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    protected void scale(EndermanEntity enderman, MatrixStack matrixStack, float p_225620_3_) {
        float scale = 0.9375F;
        matrixStack.scale(scale, scale, scale);

        super.scale(enderman, matrixStack, p_225620_3_);
    }
}
