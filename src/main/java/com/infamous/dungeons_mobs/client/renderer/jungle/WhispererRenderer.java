package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.WhispererModel2;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class WhispererRenderer<T extends WhispererEntity> extends BipedRenderer<T, WhispererModel2<T>> {
    private static final ResourceLocation WHISPERER_TEXTURE = new ResourceLocation(MODID, "textures/entity/jungle/whisperer.png");

    public WhispererRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new WhispererModel2<>(), 0.5F);
    }

    @Override
    protected void scale(T whispererEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 1.2F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(whispererEntity, matrixStack, v);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(T whispererEntity) {
        return WHISPERER_TEXTURE;
    }
}
