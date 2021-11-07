package com.infamous.dungeons_mobs.client.renderer.layers;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class EyesRenderState extends RenderState{

    public EyesRenderState(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    public static RenderType eyes(ResourceLocation p_228652_0_) {
        RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(p_228652_0_, false, false);
        return RenderType.create("eyes", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true, RenderType.State.builder().setTextureState(renderstate$texturestate).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setFogState(BLACK_FOG).createCompositeState(false));
    }
}
