package com.infamous.dungeons_mobs;

import com.infamous.dungeons_libraries.client.ClientProxy;
import com.infamous.dungeons_libraries.network.CommonProxy;
import com.infamous.dungeons_mobs.capabilities.ancient.Ancient;
import com.infamous.dungeons_mobs.capabilities.ancient.AncientStorage;
import com.infamous.dungeons_mobs.capabilities.ancient.IAncient;
import com.infamous.dungeons_mobs.capabilities.cloneable.Cloneable;
import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableStorage;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.capabilities.convertible.Convertible;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleStorage;
import com.infamous.dungeons_mobs.capabilities.convertible.IConvertible;
import com.infamous.dungeons_mobs.capabilities.properties.MobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsStorage;
import com.infamous.dungeons_mobs.capabilities.properties.IMobProps;
import com.infamous.dungeons_mobs.capabilities.teamable.ITeamable;
import com.infamous.dungeons_mobs.capabilities.teamable.Teamable;
import com.infamous.dungeons_mobs.capabilities.teamable.TeamableStorage;
import com.infamous.dungeons_mobs.client.ModItemModelProperties;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.client.renderer.armor.GeomancerClothesArmorRenderer;
import com.infamous.dungeons_mobs.client.renderer.armor.IceologerClothesArmorRenderer;
import com.infamous.dungeons_mobs.client.renderer.armor.RoyalGuardArmorRenderer;
import com.infamous.dungeons_mobs.client.renderer.armor.VanguardArmorRenderer;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.items.*;
import com.infamous.dungeons_mobs.mod.*;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.datasync.ModDataSerializers;
import com.infamous.dungeons_mobs.tags.CustomTags;
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
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dungeons_mobs")
public class DungeonsMobs
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dungeons_mobs";
    public static final ItemGroup DUNGEONS_MOBS = new GroupDungeonsMobs("dungeonsMobs");

    public static CommonProxy PROXY;

    public DungeonsMobs() {
        // Register the setup method for modloading
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsMobsConfig.COMMON_SPEC, "dungeons-mobs-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsMobsConfig.ENCHANTS_SPEC, "dungeons-mobs-mob-enchantments-common.toml");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        GeckoLib.initialize();

        // Registering custom tags to ensure compat with Morph.
        CustomTags.register();


        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	ModSoundEvents.SOUNDS.register(modEventBus);
    	ModEffects.EFFECTS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModEntityTypes.SPAWN_EGGS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);
        ModParticleTypes.PARTICLES.register(modEventBus);
        ModMobEnchantments.MOB_ENCHANTMENTS_DEFERRED.register(modEventBus);
        ModDataSerializers.DATA_SERIALIZERS.register(modEventBus);
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        //ANCIENT_DATA.subscribeAsSyncable(CHANNEL, AncientDatas::toPacket);
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
        CapabilityManager.INSTANCE.register(IMobProps.class, new MobPropsStorage(), MobProps::new);
        CapabilityManager.INSTANCE.register(IAncient.class, new AncientStorage(), Ancient::new);
        event.enqueueWork(NetworkHandler::init);
    }



    private void doClientStuff(final FMLClientSetupEvent event) {
        // ITEM MODEL PROPERTIES
        MinecraftForge.EVENT_BUS.register(new ModItemModelProperties());
        GeoArmorRenderer.registerArmorRenderer(IceologerClothesItem.class, IceologerClothesArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(VanguardArmorItem.class, VanguardArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(GeomancerClothesItem.class, GeomancerClothesArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(RoyalGuardArmorItem.class, RoyalGuardArmorRenderer::new);

    }

    private void onLoadComplete(final FMLLoadCompleteEvent event){
        if(DungeonsMobsConfig.COMMON.ENABLE_STRONGER_HUSKS.get()){
            EntityType.HUSK.dimensions = EntitySize.scalable(0.6F * 1.2F, 1.95F * 1.2F);
        }
    }
}
