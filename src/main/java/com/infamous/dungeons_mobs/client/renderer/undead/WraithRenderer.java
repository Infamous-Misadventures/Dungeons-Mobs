package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.client.models.undead.WraithBipedModel;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class WraithRenderer extends BipedRenderer<WraithEntity, WraithBipedModel<WraithEntity>> {
    private static final ResourceLocation WRAITH_TEXTURE = new ResourceLocation(MODID, "textures/entity/wraith/wraith.png");

    public WraithRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new WraithBipedModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new WraithBipedModel<>(0.5F, true), new WraithBipedModel(1.0F, true)));
        this.addLayer(new WraithClothingLayer<>(this));
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(WraithEntity entity) {
        return WRAITH_TEXTURE;
    }
}
