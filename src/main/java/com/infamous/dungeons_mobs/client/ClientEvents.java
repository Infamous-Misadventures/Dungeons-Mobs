package com.infamous.dungeons_mobs.client;

import com.infamous.dungeons_mobs.client.models.illager.*;
import com.infamous.dungeons_mobs.client.particle.*;
import com.infamous.dungeons_mobs.client.renderer.EmptyRenderer;
import com.infamous.dungeons_mobs.client.renderer.armor.*;
import com.infamous.dungeons_mobs.client.renderer.blaze.WildfireRenderer;
import com.infamous.dungeons_mobs.client.renderer.creeper.IcyCreeperRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.BlastlingRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.EndersentRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.SnarelingRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.WatchlingRenderer;
import com.infamous.dungeons_mobs.client.renderer.golem.SquallGolemRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.*;
import com.infamous.dungeons_mobs.client.renderer.jungle.LeapleafRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.PoisonQuillVineRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.QuickGrowingVineRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.WhispererRenderer;
import com.infamous.dungeons_mobs.client.renderer.piglin.CustomPiglinRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.*;
import com.infamous.dungeons_mobs.client.renderer.redstone.RedstoneCubeRenderer;
import com.infamous.dungeons_mobs.client.renderer.redstone.RedstoneGolemRenderer;
import com.infamous.dungeons_mobs.client.renderer.redstone.RedstoneMineRenderer;
import com.infamous.dungeons_mobs.client.renderer.slime.ConjuredSlimeRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.*;
import com.infamous.dungeons_mobs.client.renderer.undead.*;
import com.infamous.dungeons_mobs.client.renderer.water.*;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageEntity;
import com.infamous.dungeons_mobs.items.armor.*;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(final ModelRegistryEvent event) {
        ForgeModelBakery.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.YELLOW, false));
        ForgeModelBakery.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.PURPLE, false));
        ForgeModelBakery.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.YELLOW, true));
        ForgeModelBakery.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.PURPLE, true));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.JUNGLE_ZOMBIE.get(), CustomZombieRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FROZEN_ZOMBIE.get(), CustomZombieRenderer::new);

        // To match Husk proportions found in MCD
        event.registerEntityRenderer(EntityType.HUSK, CustomZombieRenderer::new);

//        if(DungeonsMobsConfig.COMMON.ENABLE_PILLAGERS_WEARING_ARMOR.get()){
//            event.registerEntityRenderer(EntityType.PILLAGER, ReplacedPillagerRenderer::new);
//        }
//        if(DungeonsMobsConfig.COMMON.ENABLE_VINDICATORS_WEARING_ARMOR.get()){
//            event.registerEntityRenderer(EntityType.VINDICATOR, ReplacedVindicatorRenderer::new);
//        }
//        if(DungeonsMobsConfig.COMMON.ENABLE_EVOKERS_WEARING_ARMOR.get()){
//            event.registerEntityRenderer(EntityType.EVOKER, ReplacedEvokerRenderer::new);
//        }
        
        event.registerEntityRenderer(EntityType.DROWNED, CustomDrownedRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.MOSSY_SKELETON.get(), CustomSkeletonRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SKELETON_VANGUARD.get(), SkeletonVanguardRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.NECROMANCER.get(), NecromancerRenderer::new);



        event.registerEntityRenderer(ModEntityTypes.ROYAL_GUARD.get(), manager -> new DefaultIllagerRenderer<>(manager, new RoyalGuardModel(), 0.9375F*1.2F));
        event.registerEntityRenderer(ModEntityTypes.MOUNTAINEER.get(), manager -> new DefaultIllagerRenderer<>(manager, new MountaineerModel()));

        event.registerEntityRenderer(ModEntityTypes.ICEOLOGER.get(), manager -> new DefaultIllagerRenderer<>(manager, new IceologerModel()));
        event.registerEntityRenderer(ModEntityTypes.GEOMANCER.get(), manager -> new DefaultIllagerRenderer<>(manager, new GeomancerModel()));

        event.registerEntityRenderer(ModEntityTypes.MAGE.get(), manager -> new DefaultIllagerRenderer<MageEntity>(manager, new MageModel()));
        event.registerEntityRenderer(ModEntityTypes.MAGE_CLONE.get(), manager -> new DefaultIllagerRenderer<MageCloneEntity>(manager, new MageModel()));

        event.registerEntityRenderer(ModEntityTypes.ILLUSIONER.get(), DungeonsIllusionerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ILLUSIONER_CLONE.get(), manager -> new DefaultIllagerRenderer<IllusionerCloneEntity>(manager, new DungeonsIllusionerModel()));
        event.registerEntityRenderer(ModEntityTypes.WINDCALLER.get(), manager -> new DefaultIllagerRenderer<>(manager, new WindcallerModel()));

//        event.registerEntityRenderer(ModEntityTypes.ENCHANTER.get(), EnchanterRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.ICY_CREEPER.get(), IcyCreeperRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.WRAITH.get(), WraithRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.CONJURED_SLIME.get(), ConjuredSlimeRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.REDSTONE_CUBE.get(), RedstoneCubeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.REDSTONE_GOLEM.get(), RedstoneGolemRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.WHISPERER.get(), WhispererRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.LEAPLEAF.get(), LeapleafRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.POISON_QUILL_VINE.get(), PoisonQuillVineRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.QUICK_GROWING_VINE.get(), QuickGrowingVineRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.POISON_QUILL.get(), PoisonQuillRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.MAGE_MISSILE.get(), MageMissileRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.SQUALL_GOLEM.get(), SquallGolemRenderer::new);

        event.registerEntityRenderer(EntityType.PIGLIN, manager -> new CustomPiglinRenderer(manager, ModelLayers.PIGLIN, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false, false));
        event.registerEntityRenderer(ModEntityTypes.FUNGUS_THROWER.get(), manager -> new CustomPiglinRenderer(manager, ModelLayers.PIGLIN, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false, true));
        event.registerEntityRenderer(EntityType.ZOMBIFIED_PIGLIN, manager -> new CustomPiglinRenderer(manager, ModelLayers.ZOMBIFIED_PIGLIN, ModelLayers.ZOMBIFIED_PIGLIN_INNER_ARMOR, ModelLayers.ZOMBIFIED_PIGLIN_OUTER_ARMOR, true, false));
        event.registerEntityRenderer(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(), manager -> new CustomPiglinRenderer(manager, ModelLayers.ZOMBIFIED_PIGLIN, ModelLayers.ZOMBIFIED_PIGLIN_INNER_ARMOR, ModelLayers.ZOMBIFIED_PIGLIN_OUTER_ARMOR, true, true));

        event.registerEntityRenderer(ModEntityTypes.SLIMEBALL.get(), SlimeballRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COBWEB_PROJECTILE.get(), CobwebProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BLUE_NETHERSHROOM.get(), BlueNethershroomRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.GEOMANCER_WALL.get(), GeomancerWallRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.GEOMANCER_BOMB.get(), GeomancerBombRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.REDSTONE_MINE.get(), RedstoneMineRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ICE_CLOUD.get(), IceCloudRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.TORNADO.get(), WindcallerTornadoRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.AREA_DAMAGE.get(), EmptyRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.SUMMON_SPOT.get(), SummonSpotRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.SIMPLE_TRAP.get(), SimpleTrapRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.KELP_TRAP.get(), KelpTrapRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.WRAITH_FIRE.get(), WraithFireRenderer::new);


        event.registerEntityRenderer(ModEntityTypes.WAVEWHISPERER.get(), WavewhispererRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.POISON_ANEMONE.get(), PoisonAnemoneRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.QUICK_GROWING_KELP.get(), QuickGrowingKelpRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DROWNED_NECROMANCER.get(), DrownedNecromancerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SUNKEN_SKELETON.get(), SunkenSkeletonRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.NECROMANCER_ORB.get(), NecromancerOrbRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DROWNED_NECROMANCER_ORB.get(), DrownedNecromancerOrbRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.TRIDENT_STORM.get(), TridentStormRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.ENDERSENT.get(), EndersentRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BLASTLING.get(), BlastlingRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WATCHLING.get(), WatchlingRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SNARELING.get(), SnarelingRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.BLASTLING_BULLET.get(), NecromancerOrbRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SNARELING_GLOB.get(), SnarelingGlobRenderer::new);
        
        event.registerEntityRenderer(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);


        GeoArmorRenderer.registerArmorRenderer(WindcallerArmorGear.class, WindcallerArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(MageArmorGear.class, MageArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(IceologerArmorGear.class, IceologerArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(IllusionerArmorGear.class, IllusionerArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(NecromancerArmorGear.class, NecromancerArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(DrownedNecromancerArmorGear.class, DrownedNecromancerArmorGearRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.SNOWFLAKE.get(), SnowflakeParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.REDSTONE_SPARK.get(), RedstoneSparkParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.WIND.get(), WindParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.DUST.get(), DustParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.NECROMANCY.get(), NecromancyParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.CORRUPTED_MAGIC.get(), CorruptedMagicParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.CORRUPTED_DUST.get(), CorruptedDustParticle.Factory::new);
    }
}
