package com.infamous.dungeons_mobs.client.renderer.creeper;

import com.infamous.dungeons_mobs.client.renderer.layers.IcyCreeperChargeLayer;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class IcyCreeperRenderer extends MobRenderer<IcyCreeperEntity, CreeperModel<IcyCreeperEntity>> {
    private static final ResourceLocation ICY_CREEPER_TEXTURE = new ResourceLocation(MODID, "textures/entity/creeper/icy_creeper.png");

    public IcyCreeperRenderer(EntityRendererProvider.Context p_i46186_1_) {
        super(p_i46186_1_, new CreeperModel<>(p_i46186_1_.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.addLayer(new IcyCreeperChargeLayer(this, new CreeperModel<>(p_i46186_1_.bakeLayer(ModelLayers.CREEPER_ARMOR))));
    }

    protected void scale(IcyCreeperEntity p_225620_1_, PoseStack p_225620_2_, float p_225620_3_) {
        float f = p_225620_1_.getSwelling(p_225620_3_);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        p_225620_2_.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(IcyCreeperEntity p_225625_1_, float p_225625_2_) {
        float f = p_225625_1_.getSwelling(p_225625_2_);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(IcyCreeperEntity p_110775_1_) {
        return ICY_CREEPER_TEXTURE;
    }
}
