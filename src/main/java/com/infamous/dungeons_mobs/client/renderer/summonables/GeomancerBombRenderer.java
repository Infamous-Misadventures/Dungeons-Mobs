package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.entities.summonables.GeomancerBombEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class GeomancerBombRenderer extends GeomancerConstructRenderer<GeomancerBombEntity> {
    private static final ResourceLocation EXPLODING_PILLAR_TEXTURE = new ResourceLocation(MODID, "textures/entity/constructs/geomancer_bomb.png");

    public GeomancerBombRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(GeomancerBombEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(GeomancerBombEntity entity) {
        return EXPLODING_PILLAR_TEXTURE;
    }
}
