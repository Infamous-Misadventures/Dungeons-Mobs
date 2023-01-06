package com.infamous.dungeons_mobs.client.renderer.layers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class EyesRenderState extends RenderStateShard{

    public EyesRenderState(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    public static RenderType eyes(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard renderstate$texturestate = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return RenderType.create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(renderstate$texturestate).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    }
}
