package com.infamous.dungeons_mobs.client.renderer.undead;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;

public class CustomZombieRenderer extends ZombieRenderer {

	private static final ResourceLocation JUNGLE_ZOMBIE_TEXUTRE = new ResourceLocation(MODID,
			"textures/entity/zombie/jungle_zombie.png");
	private static final ResourceLocation FROZEN_ZOMBIE_TEXTURE = new ResourceLocation(MODID,
			"textures/entity/zombie/frozen_zombie.png");
	private static final ResourceLocation HUSK_ZOMBIE_TEXTURE = new ResourceLocation("textures/entity/zombie/husk.png");

	public CustomZombieRenderer(EntityRendererProvider.Context renderContext) {
		super(renderContext);
	}

	@Override
	protected void scale(Zombie zombieEntity, PoseStack matrixStack, float v) {
		if (zombieEntity instanceof Husk) {
			float scaleFactor = 1.2F;
			matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
		}
		super.scale(zombieEntity, matrixStack, v);
	}

	public ResourceLocation getTextureLocation(Zombie zombieEntity) {
		if (zombieEntity instanceof JungleZombieEntity) {
			return JUNGLE_ZOMBIE_TEXUTRE;
		} else if (zombieEntity instanceof FrozenZombieEntity) {
			return FROZEN_ZOMBIE_TEXTURE;
		} else if (zombieEntity instanceof Husk) {
			return HUSK_ZOMBIE_TEXTURE;
		} else {
			return super.getTextureLocation(zombieEntity);
		}
	}
}
