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
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageEntity;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;
import com.infamous.dungeons_mobs.items.armor.*;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(final ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.YELLOW, false));
        ModelLoader.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.PURPLE, false));
        ModelLoader.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.YELLOW, true));
        ModelLoader.addSpecialModel(CustomISTER.getTridentMRL(DyeColor.PURPLE, true));
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.JUNGLE_ZOMBIE.get(), CustomZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FROZEN_ZOMBIE.get(), CustomZombieRenderer::new);

        // To match Husk proportions found in MCD
        RenderingRegistry.registerEntityRenderingHandler(EntityType.HUSK, CustomZombieRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(EntityType.PILLAGER, CustomPillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityType.VINDICATOR, CustomVindicatorRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(EntityType.DROWNED, CustomDrownedRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MOSSY_SKELETON.get(), CustomSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SKELETON_VANGUARD.get(), SkeletonVanguardRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.NECROMANCER.get(), NecromancerRenderer::new);



        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ROYAL_GUARD.get(), manager -> new DefaultIllagerRenderer<>(manager, new RoyalGuardModel(), 0.9375F*1.2F));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MOUNTAINEER.get(), manager -> new DefaultIllagerRenderer<>(manager, new MountaineerModel()));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ICEOLOGER.get(), manager -> new DefaultIllagerRenderer<>(manager, new IceologerModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GEOMANCER.get(), GeomancerRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MAGE.get(), manager -> new DefaultIllagerRenderer<MageEntity>(manager, new MageModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MAGE_CLONE.get(), manager -> new DefaultIllagerRenderer<MageCloneEntity>(manager, new MageModel()));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ILLUSIONER.get(), DungeonsIllusionerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ILLUSIONER_CLONE.get(), manager -> new DefaultIllagerRenderer<IllusionerCloneEntity>(manager, new DungeonsIllusionerModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WINDCALLER.get(), WindcallerRenderer::new);

//        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ENCHANTER.get(), EnchanterRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ICY_CREEPER.get(), IcyCreeperRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WRAITH.get(), WraithRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CONJURED_SLIME.get(), ConjuredSlimeRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.REDSTONE_CUBE.get(), RedstoneCubeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.REDSTONE_GOLEM.get(), RedstoneGolemRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WHISPERER.get(), WhispererRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.LEAPLEAF.get(), LeapleafRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POISON_QUILL_VINE.get(), PoisonQuillVineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.QUICK_GROWING_VINE.get(), QuickGrowingVineRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POISON_QUILL.get(), PoisonQuillRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MAGE_MISSILE.get(), MageMissileRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SQUALL_GOLEM.get(), SquallGolemRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityType.PIGLIN, manager -> new CustomPiglinRenderer(manager, false, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FUNGUS_THROWER.get(), manager -> new CustomPiglinRenderer(manager, false, true));
        RenderingRegistry.registerEntityRenderingHandler(EntityType.ZOMBIFIED_PIGLIN, manager -> new CustomPiglinRenderer(manager, true, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(), manager -> new CustomPiglinRenderer(manager, true, true));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SLIMEBALL.get(), SlimeballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COBWEB_PROJECTILE.get(), CobwebProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BLUE_NETHERSHROOM.get(), BlueNethershroomRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GEOMANCER_WALL.get(), GeomancerWallRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GEOMANCER_BOMB.get(), GeomancerBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.REDSTONE_MINE.get(), RedstoneMineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ICE_CLOUD.get(), IceCloudRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TORNADO.get(), WindcallerTornadoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), EmptyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.AREA_DAMAGE.get(), EmptyRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SUMMON_SPOT.get(), SummonSpotRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SIMPLE_TRAP.get(), SimpleTrapRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.KELP_TRAP.get(), KelpTrapRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WRAITH_FIRE.get(), WraithFireRenderer::new);


        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WAVEWHISPERER.get(), WavewhispererRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POISON_ANEMONE.get(), PoisonAnemoneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.QUICK_GROWING_KELP.get(), QuickGrowingKelpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DROWNED_NECROMANCER.get(), DrownedNecromancerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SUNKEN_SKELETON.get(), SunkenSkeletonRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.NECROMANCER_ORB.get(), NecromancerOrbRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DROWNED_NECROMANCER_ORB.get(), DrownedNecromancerOrbRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TRIDENT_STORM.get(), TridentStormRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ENDERSENT.get(), EndersentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BLASTLING.get(), BlastlingRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WATCHLING.get(), WatchlingRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNARELING.get(), SnarelingRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BLASTLING_BULLET.get(), BlastlingBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNARELING_GLOB.get(), SnarelingGlobRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);


        GeoArmorRenderer.registerArmorRenderer(WindcallerArmorGear.class, WindcallerArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(MageArmorGear.class, MageArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(IceologerArmorGear.class, IceologerArmorGearRenderer::new);
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
