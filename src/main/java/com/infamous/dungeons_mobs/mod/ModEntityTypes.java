package com.infamous.dungeons_mobs.mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.infamous.dungeons_mobs.entities.allcustomentity.illagers.PowerfulRoyalGuardEntity;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.ender.BlastlingEntity;
import com.infamous.dungeons_mobs.entities.ender.EndersentEntity;
import com.infamous.dungeons_mobs.entities.ender.SnarelingEntity;
import com.infamous.dungeons_mobs.entities.ender.WatchlingEntity;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.piglin.ArmoredPiglinEntity;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.piglin.ZombifiedArmoredPiglinEntity;
import com.infamous.dungeons_mobs.entities.piglin.ZombifiedFungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.projectiles.BlueNethershroomEntity;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebTrapEntity;
import com.infamous.dungeons_mobs.entities.projectiles.LaserOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.SlimeballEntity;
import com.infamous.dungeons_mobs.entities.projectiles.TridentFumeEntity;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneCubeEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneMineEntity;
import com.infamous.dungeons_mobs.entities.slime.ConjuredSlimeEntity;
import com.infamous.dungeons_mobs.entities.summonables.*;
import com.infamous.dungeons_mobs.entities.undead.ArmoredSkeletonEntity;
import com.infamous.dungeons_mobs.entities.undead.ArmoredZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.infamous.dungeons_mobs.entities.undead.SkeletonVanguardEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.entities.undead.horseman.SkeletonHorsemanEntity;
import com.infamous.dungeons_mobs.entities.water.ArmoredDrownedEntity;
import com.infamous.dungeons_mobs.entities.water.ArmoredSunkenSkeletonEntity;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import com.infamous.dungeons_mobs.entities.water.PoisonAnemoneEntity;
import com.infamous.dungeons_mobs.entities.water.QuickGrowingAnemoneEntity;
import com.infamous.dungeons_mobs.entities.water.SunkenSkeletonEntity;
import com.infamous.dungeons_mobs.entities.water.WavewhispererEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    // ZOMBIES
    public static final RegistryObject<EntityType<ArmoredZombieEntity>> ARMORED_ZOMBIE = ENTITY_TYPES.register("armored_zombie", () ->
            EntityType.Builder.<ArmoredZombieEntity>of(ArmoredZombieEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredZombieEntity(world))
                    .build(new ResourceLocation(MODID, "armored_zombie").toString())
    );


    public static final RegistryObject<EntityType<JungleZombieEntity>> JUNGLE_ZOMBIE = ENTITY_TYPES.register("jungle_zombie", () ->
            EntityType.Builder.<JungleZombieEntity>of(JungleZombieEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new JungleZombieEntity(world))
                    .build(new ResourceLocation(MODID, "jungle_zombie").toString())
    );


    public static final RegistryObject<EntityType<FrozenZombieEntity>> FROZEN_ZOMBIE = ENTITY_TYPES.register("frozen_zombie", () ->
            EntityType.Builder.<FrozenZombieEntity>of(FrozenZombieEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new FrozenZombieEntity(world))
                    .build(new ResourceLocation(MODID, "frozen_zombie").toString())
    );

    // SKELETONS
    public static final RegistryObject<EntityType<ArmoredSkeletonEntity>> ARMORED_SKELETON = ENTITY_TYPES.register("armored_skeleton", () ->
            EntityType.Builder.<ArmoredSkeletonEntity>of(ArmoredSkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.99F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredSkeletonEntity(world))
                    .build(new ResourceLocation(MODID, "armored_skeleton").toString())
    );


    public static final RegistryObject<EntityType<MossySkeletonEntity>> MOSSY_SKELETON = ENTITY_TYPES.register("mossy_skeleton", () ->
            EntityType.Builder.<MossySkeletonEntity>of(MossySkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new MossySkeletonEntity(world))
                    .build(new ResourceLocation(MODID, "mossy_skeleton").toString())
    );


    public static final RegistryObject<EntityType<SkeletonVanguardEntity>> SKELETON_VANGUARD = ENTITY_TYPES.register("skeleton_vanguard", () ->
            EntityType.Builder.<SkeletonVanguardEntity>of(SkeletonVanguardEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.99F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new SkeletonVanguardEntity(world))
                    .build(new ResourceLocation(MODID, "skeleton_vanguard").toString())
    );


    public static final RegistryObject<EntityType<SkeletonHorsemanEntity>> SKELETON_HORSEMAN = ENTITY_TYPES.register("skeleton_horseman", () ->
            EntityType.Builder.<SkeletonHorsemanEntity>of(SkeletonHorsemanEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.99F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new SkeletonHorsemanEntity(world))
                    .build(new ResourceLocation(MODID, "skeleton_horseman").toString())
    );


    public static final RegistryObject<EntityType<NecromancerEntity>> NECROMANCER = ENTITY_TYPES.register("necromancer", () ->
            EntityType.Builder.<NecromancerEntity>of(NecromancerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.2F, 1.99F * 1.2F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new NecromancerEntity(world))
                    .build(new ResourceLocation(MODID, "necromancer").toString())
    );



    // ILLAGERS
    public static final RegistryObject<EntityType<ArmoredVindicatorEntity>> ARMORED_VINDICATOR = ENTITY_TYPES.register("armored_vindicator", () ->
            EntityType.Builder.<ArmoredVindicatorEntity>of(ArmoredVindicatorEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredVindicatorEntity(world))
                    .build(new ResourceLocation(MODID, "armored_vindicator").toString())
    );

    public static final RegistryObject<EntityType<VindicatorRaidCaptainEntity>> VINDICATOR_RAID_CAPTAIN = ENTITY_TYPES.register("vindicator_raid_captain", () ->
            EntityType.Builder.<VindicatorRaidCaptainEntity>of(VindicatorRaidCaptainEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new VindicatorRaidCaptainEntity(world))
                    .build(new ResourceLocation(MODID, "vindicator_raid_captain").toString())
    );

    public static final RegistryObject<EntityType<ArmoredPillagerEntity>> ARMORED_PILLAGER = ENTITY_TYPES.register("armored_pillager", () ->
            EntityType.Builder.<ArmoredPillagerEntity>of(ArmoredPillagerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredPillagerEntity(world))
                    .build(new ResourceLocation(MODID, "armored_pillager").toString())
    );

    public static final RegistryObject<EntityType<RoyalGuardEntity>> ROYAL_GUARD = ENTITY_TYPES.register("royal_guard", () ->
            EntityType.Builder.<RoyalGuardEntity>of(RoyalGuardEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new RoyalGuardEntity(world))
                    .build(new ResourceLocation(MODID, "royal_guard").toString())
    );

    public static final RegistryObject<EntityType<PowerfulRoyalGuardEntity>> POWERFUL_ROYAL_GUARD = ENTITY_TYPES.register("powerful_royal_guard", () ->
            EntityType.Builder.<PowerfulRoyalGuardEntity>of(PowerfulRoyalGuardEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new PowerfulRoyalGuardEntity(world))
                    .build(new ResourceLocation(MODID, "powerful_royal_guard").toString())
    );

    public static final RegistryObject<EntityType<TowerGuardEntity>> TOWER_GUARD = ENTITY_TYPES.register("tower_guard", () ->
            EntityType.Builder.<TowerGuardEntity>of(TowerGuardEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new TowerGuardEntity(world))
                    .build(new ResourceLocation(MODID, "tower_guard").toString())
    );

    public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = ENTITY_TYPES.register("iceologer", () ->
            EntityType.Builder.<IceologerEntity>of(IceologerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IceologerEntity(world))
                    .build(new ResourceLocation(MODID, "iceologer").toString())
    );
    
    public static final RegistryObject<EntityType<MageEntity>> MAGE = ENTITY_TYPES.register("mage", () ->
    EntityType.Builder.<MageEntity>of(MageEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity,world) -> new MageEntity(world))
            .build(new ResourceLocation(MODID, "mage").toString())
    );
    
    public static final RegistryObject<EntityType<MageCloneEntity>> MAGE_CLONE = ENTITY_TYPES.register("mage_clone", () ->
    EntityType.Builder.<MageCloneEntity>of(MageCloneEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity,world) -> new MageCloneEntity(world))
            .build(new ResourceLocation(MODID, "mage_clone").toString())
    );

    public static final RegistryObject<EntityType<GeomancerEntity>> GEOMANCER = ENTITY_TYPES.register("geomancer", () ->
            EntityType.Builder.<GeomancerEntity>of(GeomancerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer").toString())
    );

    public static final RegistryObject<EntityType<WindcallerEntity>> WINDCALLER = ENTITY_TYPES.register("windcaller", () ->
            EntityType.Builder.<WindcallerEntity>of(WindcallerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new WindcallerEntity(world))
                    .build(new ResourceLocation(MODID, "windcaller").toString())
    );
    
    public static final RegistryObject<EntityType<EnchanterEntity>> ENCHANTER = ENTITY_TYPES.register("enchanter", () ->
    EntityType.Builder.<EnchanterEntity>of(EnchanterEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity,world) -> new EnchanterEntity(world))
            .build(new ResourceLocation(MODID, "enchanter").toString())
    );

    
    public static final RegistryObject<EntityType<VindicatorChefEntity>> VINDICATOR_CHEF = ENTITY_TYPES.register("vindicator_chef", () ->
            EntityType.Builder.<VindicatorChefEntity>of(VindicatorChefEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new VindicatorChefEntity(world))
                    .build(new ResourceLocation(MODID, "vindicator_chef").toString())
    );

    public static final RegistryObject<EntityType<DungeonsEvokerEntity>> DUNGEONS_EVOKER = ENTITY_TYPES.register("evoker", () ->
            EntityType.Builder.<DungeonsEvokerEntity>of(DungeonsEvokerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new DungeonsEvokerEntity(world))
                    .build(new ResourceLocation(MODID, "evoker").toString())
    );

    public static final RegistryObject<EntityType<IllagerWarriorEntity>> ILLAGER_WARRIOR = ENTITY_TYPES.register("illager_warrior", () ->
            EntityType.Builder.<IllagerWarriorEntity>of(IllagerWarriorEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IllagerWarriorEntity(world))
                    .build(new ResourceLocation(MODID, "illager_warrior").toString())
    );

    public static final RegistryObject<EntityType<DungeonsIllusionerEntity>> ILLUSIONER = ENTITY_TYPES.register("illusioner", () ->
            EntityType.Builder.<DungeonsIllusionerEntity>of(DungeonsIllusionerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "illusioner").toString())
    );

    public static final RegistryObject<EntityType<IllusionerCloneEntity>> ILLUSIONER_CLONE = ENTITY_TYPES.register("illusioner_clone", () ->
            EntityType.Builder.<IllusionerCloneEntity>of(IllusionerCloneEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IllusionerCloneEntity(world))
                    .build(new ResourceLocation(MODID, "illusioner_clone").toString())
    );

    public static final RegistryObject<EntityType<MountaineerEntity>> MOUNTAINEER = ENTITY_TYPES.register("mountaineer", () ->
            EntityType.Builder.<MountaineerEntity>of(MountaineerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new MountaineerEntity(world))
                    .build(new ResourceLocation(MODID, "mountaineer").toString())
    );

    public static final RegistryObject<EntityType<DungeonsVindicatorEntity>> VINDICATOR = ENTITY_TYPES.register("vindicator", () ->
            EntityType.Builder.<DungeonsVindicatorEntity>of(DungeonsVindicatorEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new DungeonsVindicatorEntity(world))
                    .build(new ResourceLocation(MODID, "vindicator").toString())
    );

    public static final RegistryObject<EntityType<ArmoredMountaineerEntity>> ARMORED_MOUNTAINEER = ENTITY_TYPES.register("armored_mountaineer", () ->
            EntityType.Builder.<ArmoredMountaineerEntity>of(ArmoredMountaineerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredMountaineerEntity(world))
                    .build(new ResourceLocation(MODID, "armored_mountaineer").toString())
    );

    public static final RegistryObject<EntityType<RampartCaptainEntity>> RAMPART_CAPTAIN = ENTITY_TYPES.register("rampart_captain", () ->
            EntityType.Builder.<RampartCaptainEntity>of(RampartCaptainEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new RampartCaptainEntity(world))
                    .build(new ResourceLocation(MODID, "rampart_captain").toString())
    );
    // CREEPER

    public static final RegistryObject<EntityType<IcyCreeperEntity>> ICY_CREEPER = ENTITY_TYPES.register("icy_creeper", () ->
            EntityType.Builder.<IcyCreeperEntity>of(IcyCreeperEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IcyCreeperEntity(world))
                    .build(new ResourceLocation(MODID, "icy_creeper").toString())
    );

    // WRAITH

    public static final RegistryObject<EntityType<WraithEntity>> WRAITH = ENTITY_TYPES.register("wraith", () ->
            EntityType.Builder.<WraithEntity>of(WraithEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new WraithEntity(world))
                    .build(new ResourceLocation(MODID, "wraith").toString())
    );

    // SLIME

    public static final RegistryObject<EntityType<ConjuredSlimeEntity>> CONJURED_SLIME = ENTITY_TYPES.register("conjured_slime", () ->
                    EntityType.Builder.<ConjuredSlimeEntity>of(ConjuredSlimeEntity::new, EntityClassification.MONSTER)
                            .sized(2.04F, 2.04F)
                            .clientTrackingRange(10)
                            .setCustomClientFactory((spawnEntity,world) -> new ConjuredSlimeEntity(world))
                            .build(new ResourceLocation(MODID, "conjured_slime").toString())
    );

    // REDSTONE

    public static final RegistryObject<EntityType<RedstoneCubeEntity>> REDSTONE_CUBE = ENTITY_TYPES.register("redstone_cube", () ->
            EntityType.Builder.<RedstoneCubeEntity>of(RedstoneCubeEntity::new, EntityClassification.MONSTER)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneCubeEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_cube").toString())
    );

    public static final RegistryObject<EntityType<RedstoneGolemEntity>> REDSTONE_GOLEM = ENTITY_TYPES.register("redstone_golem", () ->
            EntityType.Builder.<RedstoneGolemEntity>of(RedstoneGolemEntity::new, EntityClassification.MONSTER)
                    .sized(2.66F, 3.83F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneGolemEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_golem").toString())
    );

    // JUNGLE

    public static final RegistryObject<EntityType<WhispererEntity>> WHISPERER = ENTITY_TYPES.register("whisperer", () ->
            EntityType.Builder.<WhispererEntity>of(WhispererEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.99F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new WhispererEntity(world))
                    .build(new ResourceLocation(MODID, "whisperer").toString())
    );

    public static final RegistryObject<EntityType<LeapleafEntity>> LEAPLEAF = ENTITY_TYPES.register("leapleaf", () ->
            EntityType.Builder.<LeapleafEntity>of(LeapleafEntity::new, EntityClassification.MONSTER)
                    .sized(2.62F * 1.25F, 1.81F * 1.25F) // 42 px wide, 29px tall
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new LeapleafEntity(world))
                    .build(new ResourceLocation(MODID, "leapleaf").toString())
    );

    public static final RegistryObject<EntityType<QuickGrowingVineEntity>> QUICK_GROWING_VINE = ENTITY_TYPES.register("quick_growing_vine", () ->
            EntityType.Builder.<QuickGrowingVineEntity>of(QuickGrowingVineEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new QuickGrowingVineEntity(world))
                    .build(new ResourceLocation(MODID, "quick_growing_vine").toString())
    );

    public static final RegistryObject<EntityType<PoisonQuillVineEntity>> POISON_QUILL_VINE = ENTITY_TYPES.register("poison_quill_vine", () ->
            EntityType.Builder.<PoisonQuillVineEntity>of(PoisonQuillVineEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new PoisonQuillVineEntity(world))
                    .build(new ResourceLocation(MODID, "poison_quill_vine").toString())
    );

    // GOLEM

    public static final RegistryObject<EntityType<SquallGolemEntity>> SQUALL_GOLEM = ENTITY_TYPES.register("squall_golem", () ->
            EntityType.Builder.<SquallGolemEntity>of(SquallGolemEntity::new, EntityClassification.MONSTER)
                    .sized(2.62F, 2.81F) // 42 px wide, 29px tall + 16px of height
                    .clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity,world) -> new SquallGolemEntity(world))
                    .build(new ResourceLocation(MODID, "squall_golem").toString())
    );

    // MISC

    //   public static final EntityType<SmallFireballEntity> SMALL_FIREBALL = register("small_fireball", EntityType.Builder.<SmallFireballEntity>create(SmallFireballEntity::new, EntityClassification.MISC).size(0.3125F, 0.3125F).trackingRange(4).updateInterval(10));

    public static final RegistryObject<EntityType<WraithFireballEntity>> WRAITH_FIREBALL = ENTITY_TYPES.register("wraith_fireball", () ->
            EntityType.Builder.<WraithFireballEntity>of(WraithFireballEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new WraithFireballEntity(world))
                    .build(new ResourceLocation(MODID, "wraith_fireball").toString())
    );

    public static final RegistryObject<EntityType<SlimeballEntity>> SLIMEBALL = ENTITY_TYPES.register("slimeball", () ->
            EntityType.Builder.<SlimeballEntity>of(SlimeballEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new SlimeballEntity(world))
                    .build(new ResourceLocation(MODID, "slimeball").toString())
    );

    public static final RegistryObject<EntityType<CobwebProjectileEntity>> COBWEB_PROJECTILE = ENTITY_TYPES.register("cobweb_projectile", () ->
            EntityType.Builder.<CobwebProjectileEntity>of(CobwebProjectileEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new CobwebProjectileEntity(world))
                    .build(new ResourceLocation(MODID, "cobweb_projectile").toString())
    );

    public static final RegistryObject<EntityType<BlueNethershroomEntity>> BLUE_NETHERSHROOM = ENTITY_TYPES.register("blue_nethershroom", () ->
            EntityType.Builder.<BlueNethershroomEntity>of(BlueNethershroomEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(new ResourceLocation(MODID, "blue_nethershroom").toString())
    );

    public static final RegistryObject<EntityType<CobwebTrapEntity>> COBWEB_TRAP = ENTITY_TYPES.register("cobweb_trap", () ->
            EntityType.Builder.<CobwebTrapEntity>of(CobwebTrapEntity::new, EntityClassification.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setCustomClientFactory((spawnEntity,world) -> new CobwebTrapEntity(world))
                    .build(new ResourceLocation(MODID, "cobweb_trap").toString())
    );

    public static final RegistryObject<EntityType<GeomancerWallEntity>> GEOMANCER_WALL = ENTITY_TYPES.register("geomancer_wall", () ->
            EntityType.Builder.<GeomancerWallEntity>of(GeomancerWallEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerWallEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer_wall").toString())
    );

    public static final RegistryObject<EntityType<GeomancerBombEntity>> GEOMANCER_BOMB = ENTITY_TYPES.register("geomancer_bomb", () ->
            EntityType.Builder.<GeomancerBombEntity>of(GeomancerBombEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerBombEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer_bomb").toString())
    );

    public static final RegistryObject<EntityType<RedstoneMineEntity>> REDSTONE_MINE = ENTITY_TYPES.register("redstone_mine", () ->
            EntityType.Builder.<RedstoneMineEntity>of(RedstoneMineEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 0.5F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneMineEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_mine").toString())
    );

    public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = ENTITY_TYPES.register("ice_cloud", () ->
            EntityType.Builder.<IceCloudEntity>of(IceCloudEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 1.0F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .setCustomClientFactory((spawnEntity,world) -> new IceCloudEntity(world))
                    .build(new ResourceLocation(MODID, "ice_cloud").toString())
    );

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO = ENTITY_TYPES.register("tornado", () ->
            EntityType.Builder.<TornadoEntity>of(TornadoEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .setCustomClientFactory((spawnEntity,world) -> new TornadoEntity(world))
                    .build(new ResourceLocation(MODID, "tornado").toString())
    );

    // PIGLIN

    public static final RegistryObject<EntityType<ArmoredPiglinEntity>> ARMORED_PIGLIN = ENTITY_TYPES.register("armored_piglin", () ->
            EntityType.Builder.<ArmoredPiglinEntity>of(ArmoredPiglinEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "armored_piglin").toString())
    );

    public static final RegistryObject<EntityType<ZombifiedArmoredPiglinEntity>> ZOMBIFIED_ARMORED_PIGLIN = ENTITY_TYPES.register("zombified_armored_piglin", () ->
            EntityType.Builder.<ZombifiedArmoredPiglinEntity>of(ZombifiedArmoredPiglinEntity::new, EntityClassification.MONSTER)
                   .fireImmune()
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "zombified_armored_piglin").toString())
    );

    public static final RegistryObject<EntityType<FungusThrowerEntity>> FUNGUS_THROWER = ENTITY_TYPES.register("fungus_thrower", () ->
            EntityType.Builder.<FungusThrowerEntity>of(FungusThrowerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "fungus_thrower").toString())
    );

    public static final RegistryObject<EntityType<ZombifiedFungusThrowerEntity>> ZOMBIFIED_FUNGUS_THROWER = ENTITY_TYPES.register("zombified_fungus_thrower", () ->
            EntityType.Builder.<ZombifiedFungusThrowerEntity>of(ZombifiedFungusThrowerEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "zombified_fungus_thrower").toString())
    );

    // WATER

    public static final RegistryObject<EntityType<WavewhispererEntity>> WAVEWHISPERER = ENTITY_TYPES.register("wavewhisperer", () ->
            EntityType.Builder.<WavewhispererEntity>of(WavewhispererEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.99F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MODID, "wavewhisperer").toString())
    );

    public static final RegistryObject<EntityType<QuickGrowingAnemoneEntity>> QUICK_GROWING_ANEMONE = ENTITY_TYPES.register("quick_growing_anemone", () ->
            EntityType.Builder.<QuickGrowingAnemoneEntity>of(QuickGrowingAnemoneEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MODID, "quick_growing_anemone").toString())
    );

    public static final RegistryObject<EntityType<PoisonAnemoneEntity>> POISON_ANEMONE = ENTITY_TYPES.register("poison_anemone", () ->
            EntityType.Builder.<PoisonAnemoneEntity>of(PoisonAnemoneEntity::new, EntityClassification.MONSTER)
                    //.fireImmune()
                    .sized(1.0F, 2.5F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MODID, "poison_anemone").toString())
    );


    public static final RegistryObject<EntityType<ArmoredDrownedEntity>> ARMORED_DROWNED = ENTITY_TYPES.register("armored_drowned", () ->
            EntityType.Builder.<ArmoredDrownedEntity>of(ArmoredDrownedEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.95F * 1.1F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "armored_drowned").toString())
    );


    public static final RegistryObject<EntityType<DrownedNecromancerEntity>> DROWNED_NECROMANCER = ENTITY_TYPES.register("drowned_necromancer", () ->
            EntityType.Builder.<DrownedNecromancerEntity>of(DrownedNecromancerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.2F, 1.95F * 1.2F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "drowned_necromancer").toString())
    );

    public static final RegistryObject<EntityType<ArmoredSunkenSkeletonEntity>> ARMORED_SUNKEN_SKELETON = ENTITY_TYPES.register("armored_sunken_skeleton", () ->
            EntityType.Builder.<ArmoredSunkenSkeletonEntity>of(ArmoredSunkenSkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.1F, 1.99F * 1.1F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "armored_sunken_skeleton").toString())
    );


    public static final RegistryObject<EntityType<SunkenSkeletonEntity>> SUNKEN_SKELETON = ENTITY_TYPES.register("sunken_skeleton", () ->
            EntityType.Builder.<SunkenSkeletonEntity>of(SunkenSkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "sunken_skeleton").toString())
    );

    public static final RegistryObject<EntityType<LaserOrbEntity>> LASER_ORB = ENTITY_TYPES.register("laser_orb", () ->
            EntityType.Builder.<LaserOrbEntity>of(LaserOrbEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    //.setCustomClientFactory((spawnEntity,world) -> new LaserOrbEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ()))
                    .build(new ResourceLocation(MODID, "laser_orb").toString())
    );

    public static final RegistryObject<EntityType<TridentFumeEntity>> TRIDENT_FUME = ENTITY_TYPES.register("trident_fume", () ->
            EntityType.Builder.<TridentFumeEntity>of(TridentFumeEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    //.setCustomClientFactory((spawnEntity,world) -> new TridentFumeEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getVelX(), spawnEntity.getVelY(), spawnEntity.getVelZ()))
                    .build(new ResourceLocation(MODID, "trident_fume").toString())
    );

    // ENDER MOBS

    public static final RegistryObject<EntityType<EndersentEntity>> ENDERSENT = ENTITY_TYPES.register("endersent", () ->
            EntityType.Builder.<EndersentEntity>of(EndersentEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 1.5F, 2.9F * 1.5F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "endersent").toString())
    );

    public static final RegistryObject<EntityType<BlastlingEntity>> BLASTLING = ENTITY_TYPES.register("blastling", () ->
            EntityType.Builder.<BlastlingEntity>of(BlastlingEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.9F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "blastling").toString())
    );

    public static final RegistryObject<EntityType<WatchlingEntity>> WATCHLING = ENTITY_TYPES.register("watchling", () ->
            EntityType.Builder.<WatchlingEntity>of(WatchlingEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F * 0.9375F, 2.9F * 0.9375F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "watchling").toString())
    );

    public static final RegistryObject<EntityType<SnarelingEntity>> SNARELING = ENTITY_TYPES.register("snareling", () ->
            EntityType.Builder.<SnarelingEntity>of(SnarelingEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 2.9F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "snareling").toString())
    );

    public static final RegistryObject<EntityType<IllagerWarriorCloneEntity>> ILLAGER_WARRIOR_CLONE = ENTITY_TYPES.register("illager_warrior_clone", () ->
            EntityType.Builder.<IllagerWarriorCloneEntity>of(IllagerWarriorCloneEntity::new, EntityClassification.MISC)
                    .sized(0.6F, 1.9F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(MODID, "illager_warrior_clone").toString())
    );
}
