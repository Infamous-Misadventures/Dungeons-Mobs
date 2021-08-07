package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.client.models.undead.NecromancerModel;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class NecromancerRenderer extends BipedRenderer<NecromancerEntity, NecromancerModel<NecromancerEntity>> {
    private static final ResourceLocation NECROMANCER_TEXTURE = new ResourceLocation(MODID, "textures/entity/skeleton/necromancer.png");

    public NecromancerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new NecromancerModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        //this.entityModel.bipedHeadwear.showModel = false;
        //this.addLayer(new NecromancerEyesLayer<>(this));
        //this.addLayer(new WraithClothingLayer<>(this));
    }

    @Override
    protected void scale(NecromancerEntity necromancerEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 1.2F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(necromancerEntity, matrixStack, v);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(NecromancerEntity entity) {
        return NECROMANCER_TEXTURE;
    }
}
