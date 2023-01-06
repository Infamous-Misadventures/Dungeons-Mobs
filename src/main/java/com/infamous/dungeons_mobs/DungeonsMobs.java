package com.infamous.dungeons_mobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infamous.dungeons_libraries.client.ClientProxy;
import com.infamous.dungeons_libraries.network.CommonProxy;
import com.infamous.dungeons_mobs.client.ModItemModelProperties;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.compat.EnchantWithMobCompat;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.items.GroupDungeonsMobs;
import com.infamous.dungeons_mobs.items.GroupDungeonsMobsItems;
import com.infamous.dungeons_mobs.mod.ModEffects;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModMobEnchants;
import com.infamous.dungeons_mobs.mod.ModRecipes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.datasync.ModDataSerializers;
import com.infamous.dungeons_mobs.tags.BiomeTags;
import com.infamous.dungeons_mobs.tags.EntityTags;
import com.infamous.dungeons_mobs.worldgen.EntitySpawnPlacements;
import com.infamous.dungeons_mobs.worldgen.RaidEntries;
import com.infamous.dungeons_mobs.worldgen.SensorMapModifier;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dungeons_mobs")
public class DungeonsMobs {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "dungeons_mobs";
	public static final CreativeModeTab DUNGEONS_MOBS = new GroupDungeonsMobs("dungeonsMobs");
	public static final CreativeModeTab DUNGEONS_MOBS_ITEMS = new GroupDungeonsMobsItems("dungeonsMobsItems");

	public static CommonProxy PROXY;

	public DungeonsMobs() {
		// Register the setup method for modloading
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsMobsConfig.COMMON_SPEC,
				"dungeons-mobs-common.toml");
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		GeckoLib.initialize();

		// Registering custom tags
		EntityTags.register();
		BiomeTags.register();

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModSoundEvents.SOUNDS.register(modEventBus);
		ModEffects.EFFECTS.register(modEventBus);
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);
		ModEntityTypes.SPAWN_EGGS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModRecipes.RECIPES.register(modEventBus);
		ModParticleTypes.PARTICLES.register(modEventBus);
		if (EnchantWithMobCompat.isLoaded()) {
			ModMobEnchants.MOB_ENCHANTS_DEFERRED.register(modEventBus);
			EnchantWithMobCompat.initMobEnchants(modEventBus);
		}
		ModDataSerializers.DATA_SERIALIZERS.register(modEventBus);
		PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

		// ANCIENT_DATA.subscribeAsSyncable(CHANNEL, AncientDatas::toPacket);
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(EntitySpawnPlacements::createPlacementTypes);
		event.enqueueWork(EntitySpawnPlacements::initSpawnPlacements);
		event.enqueueWork(RaidEntries::initWaveMemberEntries);
		event.enqueueWork(SensorMapModifier::replaceSensorMaps);
		event.enqueueWork(NetworkHandler::init);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// ITEM MODEL PROPERTIES
		MinecraftForge.EVENT_BUS.register(new ModItemModelProperties());
	}

	private void onLoadComplete(final FMLLoadCompleteEvent event) {
		if (DungeonsMobsConfig.COMMON.ENABLE_STRONGER_HUSKS.get()) {
			EntityType.HUSK.dimensions = EntityDimensions.scalable(0.6F * 1.2F, 1.95F * 1.2F);
		}
	}
}
