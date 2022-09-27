package com.infamous.dungeons_mobs.client;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.client.particle.DustParticle;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.client.particle.NecromancyParticle;
import com.infamous.dungeons_mobs.client.particle.RedstoneSparkParticle;
import com.infamous.dungeons_mobs.client.particle.SnowflakeParticle;
import com.infamous.dungeons_mobs.client.particle.WindParticle;
import com.infamous.dungeons_mobs.client.renderer.EmptyRenderer;
import com.infamous.dungeons_mobs.client.renderer.armor.NecromancerArmorGearRenderer;
import com.infamous.dungeons_mobs.client.renderer.armor.WindcallerClothesArmorGearRenderer;
import com.infamous.dungeons_mobs.client.renderer.blaze.WildfireRenderer;
import com.infamous.dungeons_mobs.client.renderer.creeper.IcyCreeperRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.BlastlingRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.EndersentRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.SnarelingRenderer;
import com.infamous.dungeons_mobs.client.renderer.ender.WatchlingRenderer;
import com.infamous.dungeons_mobs.client.renderer.golem.SquallGolemRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.CustomPillagerRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.CustomVindicatorRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.DungeonsIllusionerRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.EnchanterRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.GeomancerRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.IceologerRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.IllusionerCloneRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.MageCloneRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.MageRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.RoyalGuardRenderer;
import com.infamous.dungeons_mobs.client.renderer.illager.WindcallerRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.LeapleafRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.PoisonQuillVineRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.QuickGrowingVineRenderer;
import com.infamous.dungeons_mobs.client.renderer.jungle.WhispererRenderer;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoMobEnchantmentGlintLayer;
import com.infamous.dungeons_mobs.client.renderer.layers.MobEnchantmentGlintLayer;
import com.infamous.dungeons_mobs.client.renderer.piglin.CustomPiglinRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.BlastlingBulletRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.BlueNethershroomRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.CobwebProjectileRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.CustomFireballRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.DrownedNecromancerOrbRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.GeoOrbRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.NecromancerOrbRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.OrbRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.PoisonQuillRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.SlimeballRenderer;
import com.infamous.dungeons_mobs.client.renderer.projectiles.SnarelingGlobRenderer;
import com.infamous.dungeons_mobs.client.renderer.redstone.RedstoneCubeRenderer;
import com.infamous.dungeons_mobs.client.renderer.redstone.RedstoneGolemRenderer;
import com.infamous.dungeons_mobs.client.renderer.redstone.RedstoneMineRenderer;
import com.infamous.dungeons_mobs.client.renderer.slime.ConjuredSlimeRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.GeomancerBombRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.GeomancerWallRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.IceCloudRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.KelpTrapRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.SimpleTrapRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.SummonSpotRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.TridentStormRenderer;
import com.infamous.dungeons_mobs.client.renderer.summonables.WindcallerTornadoRenderer;
import com.infamous.dungeons_mobs.client.renderer.undead.CustomSkeletonRenderer;
import com.infamous.dungeons_mobs.client.renderer.undead.CustomZombieRenderer;
import com.infamous.dungeons_mobs.client.renderer.undead.NecromancerRenderer;
import com.infamous.dungeons_mobs.client.renderer.undead.SkeletonVanguardRenderer;
import com.infamous.dungeons_mobs.client.renderer.undead.WraithRenderer;
import com.infamous.dungeons_mobs.client.renderer.water.CustomDrownedRenderer;
import com.infamous.dungeons_mobs.client.renderer.water.DrownedNecromancerRenderer;
import com.infamous.dungeons_mobs.client.renderer.water.PoisonAnemoneRenderer;
import com.infamous.dungeons_mobs.client.renderer.water.QuickGrowingKelpRenderer;
import com.infamous.dungeons_mobs.client.renderer.water.SunkenSkeletonRenderer;
import com.infamous.dungeons_mobs.client.renderer.water.WavewhispererRenderer;
import com.infamous.dungeons_mobs.items.WraithFireChargeItem;
import com.infamous.dungeons_mobs.items.armor.NecromancerArmorGear;
import com.infamous.dungeons_mobs.items.armor.WindcallerClothesArmorGear;
import com.infamous.dungeons_mobs.items.shield.CustomISTER;
import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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



        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ROYAL_GUARD.get(), RoyalGuardRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MOUNTAINEER.get(), CustomVindicatorRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ICEOLOGER.get(), IceologerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GEOMANCER.get(), GeomancerRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MAGE.get(), MageRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MAGE_CLONE.get(), MageCloneRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ILLUSIONER.get(), DungeonsIllusionerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ILLUSIONER_CLONE.get(), IllusionerCloneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WINDCALLER.get(), WindcallerRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ENCHANTER.get(), EnchanterRenderer::new);

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

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SQUALL_GOLEM.get(), SquallGolemRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityType.PIGLIN, manager -> new CustomPiglinRenderer(manager, false, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FUNGUS_THROWER.get(), manager -> new CustomPiglinRenderer(manager, false, true));
        RenderingRegistry.registerEntityRenderingHandler(EntityType.ZOMBIFIED_PIGLIN, manager -> new CustomPiglinRenderer(manager, true, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(), manager -> new CustomPiglinRenderer(manager, true, true));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WRAITH_FIREBALL.get(), CustomFireballRenderer::new);
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

        RenderTypeLookup.setRenderLayer(ModBlocks.WRAITH_FIRE_BLOCK.get(), RenderType.cutout());

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WAVEWHISPERER.get(), WavewhispererRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POISON_ANEMONE.get(), PoisonAnemoneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.QUICK_GROWING_KELP.get(), QuickGrowingKelpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DROWNED_NECROMANCER.get(), DrownedNecromancerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SUNKEN_SKELETON.get(), SunkenSkeletonRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.LASER_ORB.get(), OrbRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GEO_ORB.get(),
                manager -> new GeoOrbRenderer(manager));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.NECROMANCER_ORB.get(), NecromancerOrbRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DROWNED_NECROMANCER_ORB.get(), DrownedNecromancerOrbRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TRIDENT_FUME.get(), OrbRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TRIDENT_STORM.get(), TridentStormRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ENDERSENT.get(), EndersentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BLASTLING.get(), BlastlingRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WATCHLING.get(), WatchlingRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNARELING.get(), SnarelingRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BLASTLING_BULLET.get(), BlastlingBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNARELING_GLOB.get(), SnarelingGlobRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);


        GeoArmorRenderer.registerArmorRenderer(WindcallerClothesArmorGear.class, WindcallerClothesArmorGearRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(NecromancerArmorGear.class, NecromancerArmorGearRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetupComplete(final FMLLoadCompleteEvent event) {
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(r -> {
            if (r instanceof LivingRenderer) {
                ((LivingRenderer) r).addLayer(new MobEnchantmentGlintLayer((LivingRenderer) r));
            } else if (r instanceof GeoEntityRenderer) {
                ((GeoEntityRenderer) r).addLayer(new GeoMobEnchantmentGlintLayer((GeoEntityRenderer) r));
            }
        });

        Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach((r) -> {
            if (r instanceof LivingRenderer) {
                r.addLayer(new MobEnchantmentGlintLayer(r));
            }

        });
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        WraithFireChargeItem.initSoulFireCharge();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.SNOWFLAKE.get(), SnowflakeParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.REDSTONE_SPARK.get(), RedstoneSparkParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.WIND.get(), WindParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.DUST.get(), DustParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.NECROMANCY.get(), NecromancyParticle.Factory::new);
    }

    /*@SubscribeEvent
    public static void onRenderNamePlateEvent(RenderNameplateEvent event){
        Entity entity = event.getEntity();
        IFormattableTextComponent copy = event.getContent().copy();
        StringBuilder enchantmentString = new StringBuilder();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if(cap.hasEnchantment()){
                enchantmentString.append(" (");
                enchantmentString.append(cap.getEnchantments().stream().map(mobEnchantment -> mobEnchantment.getRegistryName().getPath()).collect(Collectors.joining(", ")));
                enchantmentString.append(")");
                event.setResult(Event.Result.ALLOW);
            }
        });
        copy.append(enchantmentString.toString());
        event.setContent(copy);
    }*/
}
