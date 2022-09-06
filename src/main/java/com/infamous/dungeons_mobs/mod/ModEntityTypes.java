package com.infamous.dungeons_mobs.mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.DUNGEONS_MOBS;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.infamous.dungeons_mobs.entities.blaze.WildfireEntity;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.ender.BlastlingEntity;
import com.infamous.dungeons_mobs.entities.ender.EndersentEntity;
import com.infamous.dungeons_mobs.entities.ender.SnarelingEntity;
import com.infamous.dungeons_mobs.entities.ender.WatchlingEntity;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
import com.infamous.dungeons_mobs.entities.illagers.EnchanterEntity;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.infamous.dungeons_mobs.entities.illagers.IceologerEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageEntity;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;
import com.infamous.dungeons_mobs.entities.illagers.RoyalGuardEntity;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.piglin.ZombifiedFungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.projectiles.BlastlingBulletEntity;
import com.infamous.dungeons_mobs.entities.projectiles.BlueNethershroomEntity;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebTrapEntity;
import com.infamous.dungeons_mobs.entities.projectiles.GeoOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.LaserOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.SlimeballEntity;
import com.infamous.dungeons_mobs.entities.projectiles.SnarelingGlobEntity;
import com.infamous.dungeons_mobs.entities.projectiles.TridentFumeEntity;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneCubeEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneMineEntity;
import com.infamous.dungeons_mobs.entities.slime.ConjuredSlimeEntity;
import com.infamous.dungeons_mobs.entities.summonables.GeomancerBombEntity;
import com.infamous.dungeons_mobs.entities.summonables.GeomancerWallEntity;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.entities.summonables.Tornado2Entity;
import com.infamous.dungeons_mobs.entities.summonables.TornadoEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.infamous.dungeons_mobs.entities.undead.SkeletonVanguardEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import com.infamous.dungeons_mobs.entities.water.PoisonAnemoneEntity;
import com.infamous.dungeons_mobs.entities.water.QuickGrowingAnemoneEntity;
import com.infamous.dungeons_mobs.entities.water.SunkenSkeletonEntity;
import com.infamous.dungeons_mobs.entities.water.WavewhispererEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final List<String> ENTITY_IDS = new ArrayList<>();

    // ZOMBIES
    public static final RegistryObject<EntityType<JungleZombieEntity>> JUNGLE_ZOMBIE = registerEntity("jungle_zombie", () ->
    EntityType.Builder.<JungleZombieEntity>of(com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity::new, EntityClassification.MONSTER)
                        .sized(0.6F, 1.95F)
                        .clientTrackingRange(8)
                        .setCustomClientFactory((spawnEntity, world) -> new JungleZombieEntity(world))
            .build(new ResourceLocation(MODID, "jungle_zombie").toString()),
            0x4f7d33, 0x00afa8);


    public static final RegistryObject<EntityType<FrozenZombieEntity>> FROZEN_ZOMBIE = registerEntity("frozen_zombie", () ->
            EntityType.Builder.<FrozenZombieEntity>of(FrozenZombieEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new FrozenZombieEntity(world))
                    .build(new ResourceLocation(MODID, "frozen_zombie").toString()),
            0x639694, 0xbae1ec
    );

    // SKELETONS
    public static final RegistryObject<EntityType<MossySkeletonEntity>> MOSSY_SKELETON = registerEntity("mossy_skeleton", () ->
            EntityType.Builder.<MossySkeletonEntity>of(MossySkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new MossySkeletonEntity(world))
                    .build(new ResourceLocation(MODID, "mossy_skeleton").toString()),
            0xd6d7c6, 0x4a5d18
    );


    public static final RegistryObject<EntityType<SkeletonVanguardEntity>> SKELETON_VANGUARD = registerEntity("skeleton_vanguard", () ->
            EntityType.Builder.<SkeletonVanguardEntity>of(SkeletonVanguardEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.99F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new SkeletonVanguardEntity(world))
                    .build(new ResourceLocation(MODID, "skeleton_vanguard").toString()),
            0x493615, 0xe8b42f
    );

    public static final RegistryObject<EntityType<NecromancerEntity>> NECROMANCER = registerEntity("necromancer", () ->
            EntityType.Builder.<NecromancerEntity>of(NecromancerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.2F, 1.99F * 1.2F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new NecromancerEntity(world))
                    .build(new ResourceLocation(MODID, "necromancer").toString()),
            0x3f243d, 0x0b9cbb
    );



    // ILLAGERS

    public static final RegistryObject<EntityType<RoyalGuardEntity>> ROYAL_GUARD = registerEntity("royal_guard", () ->
            EntityType.Builder.<RoyalGuardEntity>of(RoyalGuardEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.2F, 1.95F * 1.2F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new RoyalGuardEntity(world))
                    .build(new ResourceLocation(MODID, "royal_guard").toString()),
            0x676767, 0x014675
    );

    public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = registerEntity("iceologer", () ->
            EntityType.Builder.<IceologerEntity>of(IceologerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IceologerEntity(world))
                    .build(new ResourceLocation(MODID, "iceologer").toString()),
            0x173873, 0xb6c6ca
    );
    
    public static final RegistryObject<EntityType<MageEntity>> MAGE = registerEntity("mage", () ->
    EntityType.Builder.<MageEntity>of(MageEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity,world) -> new MageEntity(world))
            .build(new ResourceLocation(MODID, "mage").toString()),
            0x951f75, 0xe3ab58
    );
    
    public static final RegistryObject<EntityType<MageCloneEntity>> MAGE_CLONE = registerEntityWithoutEgg("mage_clone", () ->
    EntityType.Builder.<MageCloneEntity>of(MageCloneEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity,world) -> new MageCloneEntity(world))
            .build(new ResourceLocation(MODID, "mage_clone").toString())
    );

    public static final RegistryObject<EntityType<GeomancerEntity>> GEOMANCER = registerEntity("geomancer", () ->
            EntityType.Builder.<GeomancerEntity>of(GeomancerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer").toString()),
            0x373b3b, 0x8b5ea3
    );

    public static final RegistryObject<EntityType<WindcallerEntity>> WINDCALLER = registerEntity("windcaller", () ->
            EntityType.Builder.<WindcallerEntity>of(WindcallerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new WindcallerEntity(world))
                    .build(new ResourceLocation(MODID, "windcaller").toString()),
            0x348179, 0xdc6c46
    );
    
    public static final RegistryObject<EntityType<EnchanterEntity>> ENCHANTER = registerEntity("enchanter", () ->
    EntityType.Builder.<EnchanterEntity>of(EnchanterEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity,world) -> new EnchanterEntity(world))
            .build(new ResourceLocation(MODID, "enchanter").toString()),
            0x62162c, 0xfbd600
    );
    
    public static final RegistryObject<EntityType<DungeonsIllusionerEntity>> ILLUSIONER = registerEntity("illusioner", () ->
            EntityType.Builder.<DungeonsIllusionerEntity>of(DungeonsIllusionerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "illusioner").toString()),
            0x603e5c, 0x945c45
    );

    public static final RegistryObject<EntityType<IllusionerCloneEntity>> ILLUSIONER_CLONE = registerEntityWithoutEgg("illusioner_clone", () ->
            EntityType.Builder.<IllusionerCloneEntity>of(IllusionerCloneEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IllusionerCloneEntity(world))
                    .build(new ResourceLocation(MODID, "illusioner_clone").toString())
    );

    public static final RegistryObject<EntityType<MountaineerEntity>> MOUNTAINEER = registerEntity("mountaineer", () ->
            EntityType.Builder.<MountaineerEntity>of(MountaineerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new MountaineerEntity(world))
                    .build(new ResourceLocation(MODID, "mountaineer").toString()),
            0x715039, 0xe6e4d4
    );

    // CREEPER

    public static final RegistryObject<EntityType<IcyCreeperEntity>> ICY_CREEPER = registerEntity("icy_creeper", () ->
            EntityType.Builder.<IcyCreeperEntity>of(IcyCreeperEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IcyCreeperEntity(world))
                    .build(new ResourceLocation(MODID, "icy_creeper").toString()),
                    0x5ccea5, 0xd9eef2
    );

    // WRAITH

    public static final RegistryObject<EntityType<WraithEntity>> WRAITH = registerEntity("wraith", () ->
            EntityType.Builder.<WraithEntity>of(WraithEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new WraithEntity(world))
                    .build(new ResourceLocation(MODID, "wraith").toString()),
            0x1a1862, 0x2586d9
    );

    // SLIME

    public static final RegistryObject<EntityType<ConjuredSlimeEntity>> CONJURED_SLIME = registerEntityWithoutEgg("conjured_slime", () ->
                    EntityType.Builder.<ConjuredSlimeEntity>of(ConjuredSlimeEntity::new, EntityClassification.MONSTER)
                            .sized(2.04F, 2.04F)
                            .clientTrackingRange(10)
                            .setCustomClientFactory((spawnEntity,world) -> new ConjuredSlimeEntity(world))
                            .build(new ResourceLocation(MODID, "conjured_slime").toString())
    );

    // REDSTONE

    public static final RegistryObject<EntityType<RedstoneCubeEntity>> REDSTONE_CUBE = registerEntityWithoutEgg("redstone_cube", () ->
            EntityType.Builder.<RedstoneCubeEntity>of(RedstoneCubeEntity::new, EntityClassification.MONSTER)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneCubeEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_cube").toString())
    );

    public static final RegistryObject<EntityType<RedstoneGolemEntity>> REDSTONE_GOLEM = registerEntity("redstone_golem", () ->
            EntityType.Builder.<RedstoneGolemEntity>of(RedstoneGolemEntity::new, EntityClassification.MONSTER)
                    .sized(2.66F, 3.83F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneGolemEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_golem").toString()),
            0xaeaaa6, 0xe3260c
    );

    // JUNGLE

    public static final RegistryObject<EntityType<WhispererEntity>> WHISPERER = registerEntity("whisperer", () ->
            EntityType.Builder.<WhispererEntity>of(WhispererEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.99F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new WhispererEntity(world))
                    .build(new ResourceLocation(MODID, "whisperer").toString()),
            0x80a242, 0xe20703
    );

    public static final RegistryObject<EntityType<LeapleafEntity>> LEAPLEAF = registerEntity("leapleaf", () ->
            EntityType.Builder.<LeapleafEntity>of(LeapleafEntity::new, EntityClassification.MONSTER)
                    .sized(2.02F, 2.81F) 
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new LeapleafEntity(world))
                    .build(new ResourceLocation(MODID, "leapleaf").toString()),
            0x818a1a, 0x8a54ef
    );

    public static final RegistryObject<EntityType<QuickGrowingVineEntity>> QUICK_GROWING_VINE = registerEntity("quick_growing_vine", () ->
            EntityType.Builder.<QuickGrowingVineEntity>of(QuickGrowingVineEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new QuickGrowingVineEntity(world))
                    .build(new ResourceLocation(MODID, "quick_growing_vine").toString()),
            0x90ad49, 0xfbc883
    );

    public static final RegistryObject<EntityType<PoisonQuillVineEntity>> POISON_QUILL_VINE = registerEntity("poison_quill_vine", () ->
            EntityType.Builder.<PoisonQuillVineEntity>of(PoisonQuillVineEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new PoisonQuillVineEntity(world))
                    .build(new ResourceLocation(MODID, "poison_quill_vine").toString()),
            0x90ad49, 0x632cbb
    );

    // GOLEM
    public static final RegistryObject<EntityType<SquallGolemEntity>> SQUALL_GOLEM = registerEntity("squall_golem", () ->
            EntityType.Builder.<SquallGolemEntity>of(SquallGolemEntity::new, EntityClassification.MONSTER)
                    .sized(1.9F, 2.75F) // 42 px wide, 29px tall + 16px of height
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new SquallGolemEntity(world))
                    .build(new ResourceLocation(MODID, "squall_golem").toString()),
            0x828f8f, 0xffd426
    );



    // PIGLIN
    public static final RegistryObject<EntityType<FungusThrowerEntity>> FUNGUS_THROWER = registerEntity("fungus_thrower", () ->
            EntityType.Builder.<FungusThrowerEntity>of(FungusThrowerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "fungus_thrower").toString()),
                    10051392, 0x336baf
    );

    public static final RegistryObject<EntityType<ZombifiedFungusThrowerEntity>> ZOMBIFIED_FUNGUS_THROWER = registerEntity("zombified_fungus_thrower", () ->
            EntityType.Builder.<ZombifiedFungusThrowerEntity>of(ZombifiedFungusThrowerEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "zombified_fungus_thrower").toString()),
            15373203, 0x336baf
    );

    // WATER
    public static final RegistryObject<EntityType<WavewhispererEntity>> WAVEWHISPERER = registerEntity("wavewhisperer", () ->
            EntityType.Builder.<WavewhispererEntity>of(WavewhispererEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.99F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MODID, "wavewhisperer").toString()),
                    0x48a867, 0x69ebff
    );

    public static final RegistryObject<EntityType<QuickGrowingAnemoneEntity>> QUICK_GROWING_ANEMONE = registerEntity("quick_growing_anemone", () ->
            EntityType.Builder.<QuickGrowingAnemoneEntity>of(QuickGrowingAnemoneEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MODID, "quick_growing_anemone").toString()),
                    0x2b9477, 0x0d8f99
    );

    public static final RegistryObject<EntityType<PoisonAnemoneEntity>> POISON_ANEMONE = registerEntity("poison_anemone", () ->
            EntityType.Builder.<PoisonAnemoneEntity>of(PoisonAnemoneEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MODID, "poison_anemone").toString()),
                    0x2b9477, 0xc436cd
    );

    public static final RegistryObject<EntityType<DrownedNecromancerEntity>> DROWNED_NECROMANCER = registerEntity("drowned_necromancer", () ->
            EntityType.Builder.<DrownedNecromancerEntity>of(DrownedNecromancerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.2F, 1.95F * 1.2F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "drowned_necromancer").toString()),
                    9433559, 0x274d72
    );
    
    public static final RegistryObject<EntityType<SunkenSkeletonEntity>> SUNKEN_SKELETON = registerEntity("sunken_skeleton", () ->
    EntityType.Builder.<SunkenSkeletonEntity>of(SunkenSkeletonEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.99F)
            .clientTrackingRange(8)
            .build(new ResourceLocation(MODID, "sunken_skeleton").toString()),
    0x87a964, 0xc06fe5
    	    );

    // ENDER MOBS

    public static final RegistryObject<EntityType<EndersentEntity>> ENDERSENT = registerEntity("endersent", () ->
            EntityType.Builder.<EndersentEntity>of(EndersentEntity::new, EntityClassification.MONSTER)
                    .sized(0.8F, 5.6F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "endersent").toString()),
            1447446, 0
    );

    public static final RegistryObject<EntityType<BlastlingEntity>> BLASTLING = registerEntity("blastling", () ->
            EntityType.Builder.<BlastlingEntity>of(BlastlingEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.4F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "blastling").toString()),
                    0x03030a, 0x8900b0
    );

    public static final RegistryObject<EntityType<WatchlingEntity>> WATCHLING = registerEntity("watchling", () ->
            EntityType.Builder.<WatchlingEntity>of(WatchlingEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.4F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "watchling").toString()),
                    0x110e13, 0xff84f7
    );

    public static final RegistryObject<EntityType<SnarelingEntity>> SNARELING = registerEntity("snareling", () ->
            EntityType.Builder.<SnarelingEntity>of(SnarelingEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.4F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "snareling").toString()),
                    0x161616, 0xdbe64e
    );
    
    // BLAZES
    
    public static final RegistryObject<EntityType<WildfireEntity>> WILDFIRE = registerEntity("wildfire", () ->
	    EntityType.Builder.<WildfireEntity>of(WildfireEntity::new, EntityClassification.MONSTER)
	            .fireImmune()
	            .sized(0.9F, 2.25F)
	            .clientTrackingRange(10)
	            .build(new ResourceLocation(MODID, "wildfire").toString()),
	            0x8b3401, 0xffd528
	);

    // Projectiles
    public static final RegistryObject<EntityType<WraithFireballEntity>> WRAITH_FIREBALL = registerEntityWithoutEgg("wraith_fireball", () ->
            EntityType.Builder.<WraithFireballEntity>of(WraithFireballEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new WraithFireballEntity(world))
                    .build(new ResourceLocation(MODID, "wraith_fireball").toString())
    );

    public static final RegistryObject<EntityType<SlimeballEntity>> SLIMEBALL = registerEntityWithoutEgg("slimeball", () ->
            EntityType.Builder.<SlimeballEntity>of(SlimeballEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new SlimeballEntity(world))
                    .build(new ResourceLocation(MODID, "slimeball").toString())
    );

    public static final RegistryObject<EntityType<CobwebProjectileEntity>> COBWEB_PROJECTILE = registerEntityWithoutEgg("cobweb_projectile", () ->
            EntityType.Builder.<CobwebProjectileEntity>of(CobwebProjectileEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new CobwebProjectileEntity(world))
                    .build(new ResourceLocation(MODID, "cobweb_projectile").toString())
    );

    // Traps
    public static final RegistryObject<EntityType<BlueNethershroomEntity>> BLUE_NETHERSHROOM = registerEntityWithoutEgg("blue_nethershroom", () ->
            EntityType.Builder.<BlueNethershroomEntity>of(BlueNethershroomEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(new ResourceLocation(MODID, "blue_nethershroom").toString())
    );

    public static final RegistryObject<EntityType<CobwebTrapEntity>> COBWEB_TRAP = registerEntityWithoutEgg("cobweb_trap", () ->
            EntityType.Builder.<CobwebTrapEntity>of(CobwebTrapEntity::new, EntityClassification.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new CobwebTrapEntity(world))
                    .build(new ResourceLocation(MODID, "cobweb_trap").toString())
    );

    public static final RegistryObject<EntityType<GeomancerWallEntity>> GEOMANCER_WALL = registerEntityWithoutEgg("geomancer_wall", () ->
            EntityType.Builder.<GeomancerWallEntity>of(GeomancerWallEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerWallEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer_wall").toString())
    );

    public static final RegistryObject<EntityType<GeomancerBombEntity>> GEOMANCER_BOMB = registerEntityWithoutEgg("geomancer_bomb", () ->
            EntityType.Builder.<GeomancerBombEntity>of(GeomancerBombEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerBombEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer_bomb").toString())
    );

    public static final RegistryObject<EntityType<RedstoneMineEntity>> REDSTONE_MINE = registerEntityWithoutEgg("redstone_mine", () ->
            EntityType.Builder.<RedstoneMineEntity>of(RedstoneMineEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 0.5F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneMineEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_mine").toString())
    );

    public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = registerEntityWithoutEgg("ice_cloud", () ->
            EntityType.Builder.<IceCloudEntity>of(IceCloudEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 1.0F)
                    .clientTrackingRange(6)
                    .updateInterval(1)
                    .setCustomClientFactory((spawnEntity,world) -> new IceCloudEntity(world))
                    .build(new ResourceLocation(MODID, "ice_cloud").toString())
    );

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO = registerEntityWithoutEgg("tornado", () ->
            EntityType.Builder.<TornadoEntity>of(TornadoEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .setCustomClientFactory((spawnEntity,world) -> new TornadoEntity(world))
                    .build(new ResourceLocation(MODID, "tornado").toString())
    );
    public static final RegistryObject<EntityType<Tornado2Entity>> TORNADO_MELEE = ENTITY_TYPES.register("melee_tornado", () ->
            EntityType.Builder.<Tornado2Entity>of(Tornado2Entity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(0.5F, 6.0F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .setCustomClientFactory((spawnEntity,world) -> new Tornado2Entity(world))
                    .build(new ResourceLocation(MODID, "melee_tornado").toString())
    );

    public static final RegistryObject<EntityType<LaserOrbEntity>> LASER_ORB = registerEntityWithoutEgg("laser_orb", () ->
            EntityType.Builder.<LaserOrbEntity>of(LaserOrbEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    //.setCustomClientFactory((spawnEntity,world) -> new LaserOrbEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ()))
                    .build(new ResourceLocation(MODID, "laser_orb").toString())
    );

    public static final RegistryObject<EntityType<GeoOrbEntity>> GEO_ORB = ENTITY_TYPES.register("geo_orb", () ->
            EntityType.Builder.<GeoOrbEntity>of(GeoOrbEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(.5F, .5F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new GeoOrbEntity(world))
                    .build(new ResourceLocation(MODID, "geo_orb").toString())
    );
    public static final RegistryObject<EntityType<TridentFumeEntity>> TRIDENT_FUME = registerEntityWithoutEgg("trident_fume", () ->
            EntityType.Builder.<TridentFumeEntity>of(TridentFumeEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    //.setCustomClientFactory((spawnEntity,world) -> new TridentFumeEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ()))
                    .build(new ResourceLocation(MODID, "trident_fume").toString())
    );

    public static final RegistryObject<EntityType<BlastlingBulletEntity>> BLASTLING_BULLET = registerEntityWithoutEgg("blastling_bullet", () ->
    EntityType.Builder.<BlastlingBulletEntity>of(BlastlingBulletEntity::new, EntityClassification.MISC)
            .sized(0.3F, 0.3F)
            .clientTrackingRange(4)
            .updateInterval(2)
            //.setCustomClientFactory((spawnEntity,world) -> new LaserOrbEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ()))
            .build(new ResourceLocation(MODID, "blastling_bullet").toString())
);
    
    public static final RegistryObject<EntityType<SnarelingGlobEntity>> SNARELING_GLOB = registerEntityWithoutEgg("snareling_glob", () ->
    EntityType.Builder.<SnarelingGlobEntity>of(SnarelingGlobEntity::new, EntityClassification.MISC)
            .sized(0.6F, 0.6F)
            .clientTrackingRange(4)
            .updateInterval(2)
            //.setCustomClientFactory((spawnEntity,world) -> new LaserOrbEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ()))
            .build(new ResourceLocation(MODID, "snareling_glob").toString())
);


    private static <T extends MobEntity> RegistryObject<EntityType<T>> registerEntity(String key, Supplier<EntityType<T>> sup, int primaryColor, int secondaryColor) {
        ENTITY_IDS.add(key);
        RegistryObject<EntityType<T>> entityType = ENTITY_TYPES.register(key, sup);

        SPAWN_EGGS.register(key + "_spawn_egg" , () -> new ForgeSpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Properties().tab(DUNGEONS_MOBS)));

        return entityType;
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityWithoutEgg(String key, Supplier<EntityType<T>> sup) {
        ENTITY_IDS.add(key);
        RegistryObject<EntityType<T>> entityType = ENTITY_TYPES.register(key, sup);

        return entityType;
    }

}
