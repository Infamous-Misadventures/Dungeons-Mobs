package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.entities.summonables.GeomancerWallEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class GeomancerWallRenderer extends GeomancerConstructRenderer<GeomancerWallEntity> {
    private static final ResourceLocation BLOCKING_PILLAR_TEXTURE = new ResourceLocation(MODID, "textures/entity/constructs/geomancer_wall.png");

    public GeomancerWallRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(GeomancerWallEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(GeomancerWallEntity entity) {
        return BLOCKING_PILLAR_TEXTURE;
    }
}
