package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.WhispererModel;
import com.infamous.dungeons_mobs.client.models.undead.NecromancerModel;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class WhispererRenderer extends BipedRenderer<WhispererEntity, WhispererModel<WhispererEntity>> {
    private static final ResourceLocation NECROMANCER_TEXTURE = new ResourceLocation(MODID, "textures/entity/jungle/whisperer.png");

    public WhispererRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new WhispererModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new WhispererModel<>(0.5F), new WhispererModel(1.0F)));
    }

    @Override
    protected void scale(WhispererEntity whispererEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 1.2F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(whispererEntity, matrixStack, v);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(WhispererEntity whispererEntity) {
        return NECROMANCER_TEXTURE;
    }
}
