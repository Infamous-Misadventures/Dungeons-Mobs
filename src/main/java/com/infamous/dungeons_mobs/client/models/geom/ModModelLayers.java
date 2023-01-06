package com.infamous.dungeons_mobs.client.models.geom;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.client.models.FungusSackModel;
import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.armor.PillagerHelmetModel;
import com.infamous.dungeons_mobs.client.models.armor.VanguardShieldModel;
import com.infamous.dungeons_mobs.client.models.armor.VindicatorHelmetModel;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.client.models.projectile.SnarelingGlobModel;
import com.infamous.dungeons_mobs.client.models.redstone.RedstoneCubeModel;
import com.infamous.dungeons_mobs.client.models.undead.SunkenSkeletonModel;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModModelLayers {
	public static ModelLayerLocation REDSTONE_CUBE = new ModelLayerLocation(
			new ResourceLocation(MODID + "redstone_cube_model"), "redstone_cube_model");
	public static ModelLayerLocation SUNKEN_SKELETON = new ModelLayerLocation(
			new ResourceLocation(MODID + "sunken_skeleton_model"), "sunken_skeleton_model");

	public static ModelLayerLocation PILLAGER_HELMET = new ModelLayerLocation(
			new ResourceLocation(MODID + "pillager_helmet_model"), "pillager_helmet_model");
	public static ModelLayerLocation VINDICATOR_HELMET = new ModelLayerLocation(
			new ResourceLocation(MODID + "vindicator_helmet_model"), "vindicator_helmet_model");
	public static ModelLayerLocation VANGUARD_SHIELD = new ModelLayerLocation(
			new ResourceLocation(MODID + "vanguard_shield_model"), "vanguard_shield_model");
	public static ModelLayerLocation FUNGUS_SACK = new ModelLayerLocation(
			new ResourceLocation(MODID + "fungus_sack_model"), "fungus_sack_model");

	public static ModelLayerLocation SNARELING_GLOB = new ModelLayerLocation(
			new ResourceLocation(MODID + "snareling_glob_model"), "snareling_glob_model");

	public static ModelLayerLocation BIPED_ILLAGER_MODEL = new ModelLayerLocation(
			new ResourceLocation(MODID + "biped_illager_model"), "biped_illager_model");
	public static ModelLayerLocation ILLAGER_ARMOR_MODEL_INNER_LAYER = new ModelLayerLocation(
			new ResourceLocation(MODID + "illager_armor_model_inner"), "illager_armor_model_inner");
	public static ModelLayerLocation ILLAGER_ARMOR_MODEL_OUTER_LAYER = new ModelLayerLocation(
			new ResourceLocation(MODID + "illager_armor_model_outer"), "illager_armor_model_outer");

	@SubscribeEvent
	public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(REDSTONE_CUBE, RedstoneCubeModel::createBodyLayer);
		event.registerLayerDefinition(SUNKEN_SKELETON, SunkenSkeletonModel::createBodyLayer);
		event.registerLayerDefinition(PILLAGER_HELMET, PillagerHelmetModel::createHelmetLayer);
		event.registerLayerDefinition(VINDICATOR_HELMET, VindicatorHelmetModel::createHelmetLayer);
		event.registerLayerDefinition(VANGUARD_SHIELD, VanguardShieldModel::createLayer);
		event.registerLayerDefinition(FUNGUS_SACK, FungusSackModel::createBackpackLayer);
		event.registerLayerDefinition(SNARELING_GLOB, SnarelingGlobModel::createLayer);
		event.registerLayerDefinition(BIPED_ILLAGER_MODEL, IllagerBipedModel::createBodyLayer);
		event.registerLayerDefinition(ILLAGER_ARMOR_MODEL_OUTER_LAYER, IllagerArmorModel::createOuterArmorLayer);
		event.registerLayerDefinition(ILLAGER_ARMOR_MODEL_INNER_LAYER, IllagerArmorModel::createInnerArmorLayer);
	}
}
