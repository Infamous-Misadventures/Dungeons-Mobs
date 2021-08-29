package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.renderer.jungle.WhispererRenderer;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.water.WavewhispererEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class WavewhispererRenderer<T extends WavewhispererEntity> extends WhispererRenderer<T> {
    private static final ResourceLocation WAVEWHISPERER_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/wavewhisperer.png");

    public WavewhispererRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(T whispererEntity) {
        return WAVEWHISPERER_TEXTURE;
    }

    @Override
    protected void setupRotations(T skeleton, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupRotations(skeleton, matrixStack, p_225621_3_, p_225621_4_, p_225621_5_);
        float swimAmount = skeleton.getSwimAmount(p_225621_5_);
        if (swimAmount > 0.0F) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(swimAmount, skeleton.xRot, -10.0F - skeleton.xRot)));
        }
    }
}
