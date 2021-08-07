package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.client.models.projectile.OrbModel;
import com.infamous.dungeons_mobs.client.models.summonables.IceCloudModel;
import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.LaserOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.TridentFumeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class OrbRenderer<T extends AbstractOrbEntity> extends EntityRenderer<T> {
    private static final ResourceLocation LASER_ORB_TEXTURE = new ResourceLocation(MODID, "textures/entity/laser_orb.png");
    private static final ResourceLocation TRIDENT_FUME_TEXTURE = new ResourceLocation(MODID, "textures/entity/trident_fume.png");

    protected final OrbModel<T> orbModel = new OrbModel<T>();

    public OrbRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(T orb, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStackIn.translate(0.0D, -1.5D, 0.0D);
        ResourceLocation texture = this.getTextureLocation(orb);
        RenderType renderType = this.orbModel.renderType(texture);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderType);
        this.orbModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(orb, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(T orb) {
        if(orb instanceof LaserOrbEntity){
            return LASER_ORB_TEXTURE;
        } else if(orb instanceof TridentFumeEntity){
            return TRIDENT_FUME_TEXTURE;
        } else{
            return LASER_ORB_TEXTURE;
        }
    }
}
