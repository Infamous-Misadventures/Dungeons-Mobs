package com.infamous.dungeons_mobs.client.renderer.layer;

import com.infamous.dungeons_mobs.client.models.ModdedCapeModel;
import com.infamous.dungeons_mobs.client.models.illager.WindcallerModel;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class WindcallerCapeLayer extends LayerRenderer<WindcallerEntity, WindcallerModel<WindcallerEntity>> {

    private static final ResourceLocation WINDCALLER_CAPE_TEXTURE = new ResourceLocation(MODID,"textures/entity/illager/windcaller_cape.png");

    ModdedCapeModel windcallerCapeModel = new ModdedCapeModel(0.0F);

    public WindcallerCapeLayer(IEntityRenderer<WindcallerEntity, WindcallerModel<WindcallerEntity>> windcallerRendererIn) {
        super(windcallerRendererIn);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, WindcallerEntity illagerEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!illagerEntity.isInvisible()) {
            ItemStack itemstack = illagerEntity.getItemBySlot(EquipmentSlotType.CHEST);
            if (itemstack.getItem() != Items.ELYTRA) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.0D, 0.0D, 0.125D + 0.1D);
                double d0 = MathHelper.lerp((double)partialTicks, illagerEntity.prevChasingPosX, illagerEntity.chasingPosX) - MathHelper.lerp((double)partialTicks, illagerEntity.xo, illagerEntity.getX());
                double d1 = MathHelper.lerp((double)partialTicks, illagerEntity.prevChasingPosY, illagerEntity.chasingPosY) - MathHelper.lerp((double)partialTicks, illagerEntity.yo, illagerEntity.getY());
                double d2 = MathHelper.lerp((double)partialTicks, illagerEntity.prevChasingPosZ, illagerEntity.chasingPosZ) - MathHelper.lerp((double)partialTicks, illagerEntity.zo, illagerEntity.getZ());
                float f = illagerEntity.yBodyRotO + (illagerEntity.yBodyRot - illagerEntity.yBodyRotO);
                double d3 = (double)MathHelper.sin(f * ((float)Math.PI / 180F));
                double d4 = (double)(-MathHelper.cos(f * ((float)Math.PI / 180F)));
                float f1 = (float)d1 * 10.0F;
                f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
                f3 = MathHelper.clamp(f3, -20.0F, 20.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                float f4 = MathHelper.lerp(partialTicks, illagerEntity.prevCameraYaw, illagerEntity.cameraYaw);
                f1 = f1 + MathHelper.sin(MathHelper.lerp(partialTicks, illagerEntity.walkDistO, illagerEntity.walkDist) * 6.0F) * 32.0F * f4;
                if (illagerEntity.isCrouching()) {
                    f1 += 25.0F;
                }

                matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(WINDCALLER_CAPE_TEXTURE));
                this.windcallerCapeModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);
                matrixStackIn.popPose();
            }
        }

    }
}