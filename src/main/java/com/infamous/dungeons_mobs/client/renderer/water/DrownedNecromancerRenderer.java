package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.models.ocean.DrownedNecromancerModel;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class DrownedNecromancerRenderer extends BipedRenderer<DrownedNecromancerEntity, DrownedNecromancerModel<DrownedNecromancerEntity>> {
    private static final ResourceLocation DROWNED_NECROMANCER_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/drowned_necromancer.png");

    public DrownedNecromancerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DrownedNecromancerModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
    }

    @Override
    public void render(DrownedNecromancerEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    protected void scale(DrownedNecromancerEntity necromancerEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 1.2F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(necromancerEntity, matrixStack, v);
    }

    protected void setupRotations(DrownedNecromancerEntity drownedNecromancer, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupRotations(drownedNecromancer, matrixStack, p_225621_3_, p_225621_4_, p_225621_5_);
        float swimAmount = drownedNecromancer.getSwimAmount(p_225621_5_);
        if (swimAmount > 0.0F) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(swimAmount, drownedNecromancer.xRot, -10.0F - drownedNecromancer.xRot)));
        }

    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(DrownedNecromancerEntity entity) {
        return DROWNED_NECROMANCER_TEXTURE;
    }
}