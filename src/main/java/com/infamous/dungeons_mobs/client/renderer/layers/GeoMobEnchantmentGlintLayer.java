package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper;
import com.infamous.dungeons_mobs.capabilities.ancient.AncientHelper;
import com.infamous.dungeons_mobs.capabilities.ancient.IAncient;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class GeoMobEnchantmentGlintLayer<T extends Entity & IAnimatable> extends GeoLayerRenderer<T> {
    private static final ResourceLocation NORMAL_LOCATION = new ResourceLocation(MODID, "textures/misc/mob_enchantment_glint.png");
    private static final ResourceLocation ANCIENT_LOCATION = new ResourceLocation(MODID, "textures/misc/ancient_glint.png");
    private GeoModelProvider<T> modelProvider;

    public GeoMobEnchantmentGlintLayer(IGeoRenderer<T> geoRenderer) {
        super(geoRenderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        EnchantableHelper.getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if (cap.hasEnchantment()) {
                float f = (float) entity.tickCount + partialTicks;
//                GeoModelProvider<T> geomodel = (GeoModelProvider<T>) this.getEntityModel();
//                renderModel(geomodel, this.getTextureLocation(), matrixStack, bufferIn, packedLightIn, entity, partialTicks, 1.0F, 1.0F, 1.0F);
                float intensity = cap.getEnchantments().size() < 3 ? ((float)cap.getEnchantments().size() / 3) : 3;
                RenderType glint =  RenderType.energySwirl(this.getTextureLocationForEnchantment(entity), this.xOffset(f), f * 0.01F);
                this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entity)), entity, partialTicks, glint, matrixStack, bufferIn,
                        bufferIn.getBuffer(glint), packedLightIn, OverlayTexture.NO_OVERLAY, intensity, intensity, intensity, intensity);
            }
        });
    }

    @Override
    public RenderType getRenderType(ResourceLocation textureLocation) {
        return RenderType.entityGlint();
    }
    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }


    protected ResourceLocation getTextureLocationForEnchantment(T entity) {
        LazyOptional<IAncient> ancientCap = AncientHelper.getAncientCapabilityLazy(entity);
        if(ancientCap.isPresent() && AncientHelper.getAncientCapability(entity).isAncient()){
            return ANCIENT_LOCATION;
        }
        return NORMAL_LOCATION;
    }

    protected ResourceLocation getModel(T entity){
        ResourceLocation registryName = entity.getType().getRegistryName();
        return new ResourceLocation(registryName.getNamespace(), "geo/" + registryName.getPath() + ".geo.json");
    }

}
