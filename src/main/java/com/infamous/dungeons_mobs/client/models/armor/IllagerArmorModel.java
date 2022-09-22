package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;

// Borrowed from tallestred / seymourimadeit's IllagersWearArmor mod
public class IllagerArmorModel<T extends AbstractIllagerEntity> extends BipedModel<T> {

    public IllagerArmorModel(float modelSize) {
        this(modelSize, 0.0F, 64, 32);
    }

    public IllagerArmorModel(float modelSize, float yOffset, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffset, textureWidthIn, textureHeightIn);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
        this.head.setPos(0.0F, 0.0F, 0.0F);
    }
}