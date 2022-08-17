package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.projectile.GeoNecromancerOrbModel;
import com.infamous.dungeons_mobs.client.renderer.layer.GeoEyeLayer;
import com.infamous.dungeons_mobs.entities.illagers.ArmoredPillagerEntity;
import com.infamous.dungeons_mobs.entities.illagers.bosses.ArchIllagerEntity;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class GeoNecromancerOrbRenderer extends GeoProjectilesRenderer<NecromancerOrbEntity> {

    public IRenderTypeBuffer rtb;
    public NecromancerOrbEntity thisMage;
    public GeoNecromancerOrbRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GeoNecromancerOrbModel());
    }

    @Override
    public void render(NecromancerOrbEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float scaleFactor = 1f;
        thisMage = entityIn;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public RenderType getRenderType(NecromancerOrbEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        this.rtb = renderTypeBuffer;
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(new ResourceLocation(
                                DungeonsMobs.MODID,
                                thisMage.getCaster() instanceof ArchIllagerEntity ?
                                        "textures/geo_entity/arch_illager_proyectile.png" :
                                        "textures/geo_entity/necromancer_proyectile.png"
                        )
                )
        );

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
