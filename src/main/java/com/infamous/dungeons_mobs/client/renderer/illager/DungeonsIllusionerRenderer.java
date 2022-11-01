package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.DungeonsIllusionerModel;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nullable;

public class DungeonsIllusionerRenderer extends DefaultIllagerRenderer<DungeonsIllusionerEntity> {
    public DungeonsIllusionerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DungeonsIllusionerModel());
    }

    @Override
    public void render(DungeonsIllusionerEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entity.appearDelay <= 0) {
            super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        } else {

        }
    }
}