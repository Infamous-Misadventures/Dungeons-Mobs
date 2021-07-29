package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.undead.NecromancerModel;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;

public class NecromancerEyesLayer<T extends NecromancerEntity> extends AbstractEyesLayer<T, NecromancerModel<T>> {
  private static final RenderType EYE_LAYER = RenderType.eyes(new ResourceLocation(DungeonsMobs.MODID, "textures/entity/skeleton/necromancer_eyes.png"));

  public NecromancerEyesLayer(IEntityRenderer<T, NecromancerModel<T>> entityRenderer) {
    super(entityRenderer);
  }

  @Override
  public RenderType renderType() {
    return EYE_LAYER;
  }
}