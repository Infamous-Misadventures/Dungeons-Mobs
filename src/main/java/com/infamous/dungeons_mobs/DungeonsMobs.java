package com.infamous.dungeons_mobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infamous.dungeons_mobs.capabilities.cloneable.Cloneable;
import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableStorage;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.capabilities.convertible.Convertible;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleStorage;
import com.infamous.dungeons_mobs.capabilities.convertible.IConvertible;
import com.infamous.dungeons_mobs.capabilities.teamable.ITeamable;
import com.infamous.dungeons_mobs.capabilities.teamable.Teamable;
import com.infamous.dungeons_mobs.capabilities.teamable.TeamableStorage;
import com.infamous.dungeons_mobs.client.ModItemModelProperties;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.items.GroupDungeonsMobs;
import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModRecipes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.worldgen.BiomeSpawnEntries;
import com.infamous.dungeons_mobs.worldgen.EntitySpawnPlacements;
import com.infamous.dungeons_mobs.worldgen.RaidEntries;
import com.infamous.dungeons_mobs.worldgen.SensorMapModifier;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
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
public class DungeonsMobs
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dungeons_mobs";
    public static final ItemGroup DUNGEONS_MOBS = new GroupDungeonsMobs("dungeonsMobs");

    public DungeonsMobs() {
        // Register the setup method for modloading
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsMobsConfig.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        GeckoLib.initialize();
        
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	ModSoundEvents.SOUNDS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);
        ModParticleTypes.PARTICLES.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(EntitySpawnPlacements::createPlacementTypes);
        event.enqueueWork(EntitySpawnPlacements::initSpawnPlacements);
        event.enqueueWork(RaidEntries::initWaveMemberEntries);
        event.enqueueWork(SensorMapModifier::replaceSensorMaps);
        event.enqueueWork(BiomeSpawnEntries::addCustomTypesToBiomes);
        CapabilityManager.INSTANCE.register(ICloneable.class, new CloneableStorage(), Cloneable::new);
        CapabilityManager.INSTANCE.register(IConvertible.class, new ConvertibleStorage(), Convertible::new);
        CapabilityManager.INSTANCE.register(ITeamable.class, new TeamableStorage(), Teamable::new);
        event.enqueueWork(NetworkHandler::init);
    }



    private void doClientStuff(final FMLClientSetupEvent event) {
        // ITEM MODEL PROPERTIES
        MinecraftForge.EVENT_BUS.register(new ModItemModelProperties());

    }

    private void onLoadComplete(final FMLLoadCompleteEvent event){
        if(DungeonsMobsConfig.COMMON.ENABLE_STRONGER_HUSKS.get()){
            EntityType.HUSK.dimensions = EntitySize.scalable(0.6F * 1.2F, 1.95F * 1.2F);
        }
    }
}
