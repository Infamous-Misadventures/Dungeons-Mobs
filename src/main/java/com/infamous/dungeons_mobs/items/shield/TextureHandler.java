package com.infamous.dungeons_mobs.items.shield;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TextureHandler {

	@SubscribeEvent
	public static void onStitch(TextureStitchEvent.Pre event) {
		if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
			event.addSprite(ShieldTextures.LOCATION_ROYAL_GUARD_SHIELD_BASE.texture());
			event.addSprite(ShieldTextures.LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN.texture());
			event.addSprite(ShieldTextures.LOCATION_SKELETON_VANGUARD_SHIELD.texture());
		}
	}

}