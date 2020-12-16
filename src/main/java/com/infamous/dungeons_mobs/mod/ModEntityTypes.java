package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebTrapEntity;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import com.infamous.dungeons_mobs.entities.projectiles.SlimeballEntity;
import com.infamous.dungeons_mobs.entities.summonables.*;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneCubeEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneMineEntity;
import com.infamous.dungeons_mobs.entities.slime.ConjuredSlimeEntity;
import com.infamous.dungeons_mobs.entities.undead.*;
import com.infamous.dungeons_mobs.entities.undead.horseman.SkeletonHorsemanEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    // ZOMBIES
    public static final RegistryObject<EntityType<ArmoredZombieEntity>> ARMORED_ZOMBIE = ENTITY_TYPES.register("armored_zombie", () ->
            EntityType.Builder.<ArmoredZombieEntity>create(ArmoredZombieEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.95F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredZombieEntity(world))
                    .build(new ResourceLocation(MODID, "armored_zombie").toString())
    );


    public static final RegistryObject<EntityType<JungleZombieEntity>> JUNGLE_ZOMBIE = ENTITY_TYPES.register("jungle_zombie", () ->
            EntityType.Builder.<JungleZombieEntity>create(JungleZombieEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new JungleZombieEntity(world))
                    .build(new ResourceLocation(MODID, "jungle_zombie").toString())
    );


    public static final RegistryObject<EntityType<FrozenZombieEntity>> FROZEN_ZOMBIE = ENTITY_TYPES.register("frozen_zombie", () ->
            EntityType.Builder.<FrozenZombieEntity>create(FrozenZombieEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new FrozenZombieEntity(world))
                    .build(new ResourceLocation(MODID, "frozen_zombie").toString())
    );

    // SKELETONS
    public static final RegistryObject<EntityType<ArmoredSkeletonEntity>> ARMORED_SKELETON = ENTITY_TYPES.register("armored_skeleton", () ->
            EntityType.Builder.<ArmoredSkeletonEntity>create(ArmoredSkeletonEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.99F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredSkeletonEntity(world))
                    .build(new ResourceLocation(MODID, "armored_skeleton").toString())
    );


    public static final RegistryObject<EntityType<MossySkeletonEntity>> MOSSY_SKELETON = ENTITY_TYPES.register("mossy_skeleton", () ->
            EntityType.Builder.<MossySkeletonEntity>create(MossySkeletonEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.99F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new MossySkeletonEntity(world))
                    .build(new ResourceLocation(MODID, "mossy_skeleton").toString())
    );


    public static final RegistryObject<EntityType<SkeletonVanguardEntity>> SKELETON_VANGUARD = ENTITY_TYPES.register("skeleton_vanguard", () ->
            EntityType.Builder.<SkeletonVanguardEntity>create(SkeletonVanguardEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.99F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new SkeletonVanguardEntity(world))
                    .build(new ResourceLocation(MODID, "skeleton_vanguard").toString())
    );


    public static final RegistryObject<EntityType<SkeletonHorsemanEntity>> SKELETON_HORSEMAN = ENTITY_TYPES.register("skeleton_horseman", () ->
            EntityType.Builder.<SkeletonHorsemanEntity>create(SkeletonHorsemanEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.99F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new SkeletonHorsemanEntity(world))
                    .build(new ResourceLocation(MODID, "skeleton_horseman").toString())
    );


    public static final RegistryObject<EntityType<NecromancerEntity>> NECROMANCER = ENTITY_TYPES.register("necromancer", () ->
            EntityType.Builder.<NecromancerEntity>create(NecromancerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.2F, 1.99F * 1.2F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new NecromancerEntity(world))
                    .build(new ResourceLocation(MODID, "necromancer").toString())
    );



    // ILLAGERS
    public static final RegistryObject<EntityType<ArmoredVindicatorEntity>> ARMORED_VINDICATOR = ENTITY_TYPES.register("armored_vindicator", () ->
            EntityType.Builder.<ArmoredVindicatorEntity>create(ArmoredVindicatorEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.95F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredVindicatorEntity(world))
                    .build(new ResourceLocation(MODID, "armored_vindicator").toString())
    );

    public static final RegistryObject<EntityType<ArmoredPillagerEntity>> ARMORED_PILLAGER = ENTITY_TYPES.register("armored_pillager", () ->
            EntityType.Builder.<ArmoredPillagerEntity>create(ArmoredPillagerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.95F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredPillagerEntity(world))
                    .build(new ResourceLocation(MODID, "armored_pillager").toString())
    );

    public static final RegistryObject<EntityType<RoyalGuardEntity>> ROYAL_GUARD = ENTITY_TYPES.register("royal_guard", () ->
            EntityType.Builder.<RoyalGuardEntity>create(RoyalGuardEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.95F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new RoyalGuardEntity(world))
                    .build(new ResourceLocation(MODID, "royal_guard").toString())
    );

    public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = ENTITY_TYPES.register("iceologer", () ->
            EntityType.Builder.<IceologerEntity>create(IceologerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IceologerEntity(world))
                    .build(new ResourceLocation(MODID, "iceologer").toString())
    );

    public static final RegistryObject<EntityType<GeomancerEntity>> GEOMANCER = ENTITY_TYPES.register("geomancer", () ->
            EntityType.Builder.<GeomancerEntity>create(GeomancerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer").toString())
    );

    public static final RegistryObject<EntityType<WindcallerEntity>> WINDCALLER = ENTITY_TYPES.register("windcaller", () ->
            EntityType.Builder.<WindcallerEntity>create(WindcallerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new WindcallerEntity(world))
                    .build(new ResourceLocation(MODID, "windcaller").toString())
    );
    public static final RegistryObject<EntityType<VindicatorChefEntity>> VINDICATOR_CHEF = ENTITY_TYPES.register("vindicator_chef", () ->
            EntityType.Builder.<VindicatorChefEntity>create(VindicatorChefEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new VindicatorChefEntity(world))
                    .build(new ResourceLocation(MODID, "vindicator_chef").toString())
    );

    public static final RegistryObject<EntityType<IllusionerCloneEntity>> ILLUSIONER_CLONE = ENTITY_TYPES.register("illusioner_clone", () ->
            EntityType.Builder.<IllusionerCloneEntity>create(IllusionerCloneEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IllusionerCloneEntity(world))
                    .build(new ResourceLocation(MODID, "illusioner_clone").toString())
    );

    public static final RegistryObject<EntityType<MountaineerEntity>> MOUNTAINEER = ENTITY_TYPES.register("mountaineer", () ->
            EntityType.Builder.<MountaineerEntity>create(MountaineerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new MountaineerEntity(world))
                    .build(new ResourceLocation(MODID, "mountaineer").toString())
    );

    public static final RegistryObject<EntityType<ArmoredMountaineerEntity>> ARMORED_MOUNTAINEER = ENTITY_TYPES.register("armored_mountaineer", () ->
            EntityType.Builder.<ArmoredMountaineerEntity>create(ArmoredMountaineerEntity::new, EntityClassification.MONSTER)
                    .size(0.6F * 1.1F, 1.95F * 1.1F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredMountaineerEntity(world))
                    .build(new ResourceLocation(MODID, "armored_mountaineer").toString())
    );

    // CREEPER

    public static final RegistryObject<EntityType<IcyCreeperEntity>> ICY_CREEPER = ENTITY_TYPES.register("icy_creeper", () ->
            EntityType.Builder.<IcyCreeperEntity>create(IcyCreeperEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.7F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new IcyCreeperEntity(world))
                    .build(new ResourceLocation(MODID, "icy_creeper").toString())
    );

    // WRAITH

    public static final RegistryObject<EntityType<WraithEntity>> WRAITH = ENTITY_TYPES.register("wraith", () ->
            EntityType.Builder.<WraithEntity>create(WraithEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.99F)
                    .func_233606_a_(8)
                    .setCustomClientFactory((spawnEntity,world) -> new WraithEntity(world))
                    .build(new ResourceLocation(MODID, "wraith").toString())
    );

    // SLIME

    public static final RegistryObject<EntityType<ConjuredSlimeEntity>> CONJURED_SLIME = ENTITY_TYPES.register("conjured_slime", () ->
                    EntityType.Builder.<ConjuredSlimeEntity>create(ConjuredSlimeEntity::new, EntityClassification.MONSTER)
                            .size(2.04F, 2.04F)
                            .func_233606_a_(10)
                            .setCustomClientFactory((spawnEntity,world) -> new ConjuredSlimeEntity(world))
                            .build(new ResourceLocation(MODID, "conjured_slime").toString())
    );

    // REDSTONE

    public static final RegistryObject<EntityType<RedstoneCubeEntity>> REDSTONE_CUBE = ENTITY_TYPES.register("redstone_cube", () ->
            EntityType.Builder.<RedstoneCubeEntity>create(RedstoneCubeEntity::new, EntityClassification.MONSTER)
                    .size(1.0F, 1.0F)
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneCubeEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_cube").toString())
    );

    public static final RegistryObject<EntityType<RedstoneGolemEntity>> REDSTONE_GOLEM = ENTITY_TYPES.register("redstone_golem", () ->
            EntityType.Builder.<RedstoneGolemEntity>create(RedstoneGolemEntity::new, EntityClassification.MONSTER)
                    .size(2.66F, 3.83F)
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneGolemEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_golem").toString())
    );

    // JUNGLE

    public static final RegistryObject<EntityType<WhispererEntity>> WHISPERER = ENTITY_TYPES.register("whisperer", () ->
            EntityType.Builder.<WhispererEntity>create(WhispererEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 2.99F)
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new WhispererEntity(world))
                    .build(new ResourceLocation(MODID, "whisperer").toString())
    );

    public static final RegistryObject<EntityType<LeapleafEntity>> LEAPLEAF = ENTITY_TYPES.register("leapleaf", () ->
            EntityType.Builder.<LeapleafEntity>create(LeapleafEntity::new, EntityClassification.MONSTER)
                    .size(2.62F * 1.25F, 1.81F * 1.25F) // 42 px wide, 29px tall
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new LeapleafEntity(world))
                    .build(new ResourceLocation(MODID, "leapleaf").toString())
    );

    public static final RegistryObject<EntityType<QuickGrowingVineEntity>> QUICK_GROWING_VINE = ENTITY_TYPES.register("quick_growing_vine", () ->
            EntityType.Builder.<QuickGrowingVineEntity>create(QuickGrowingVineEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(1.0F, 2.5F)
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new QuickGrowingVineEntity(world))
                    .build(new ResourceLocation(MODID, "quick_growing_vine").toString())
    );

    public static final RegistryObject<EntityType<PoisonQuillVineEntity>> POISON_QUILL_VINE = ENTITY_TYPES.register("poison_quill_vine", () ->
            EntityType.Builder.<PoisonQuillVineEntity>create(PoisonQuillVineEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(1.0F, 2.5F)
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new PoisonQuillVineEntity(world))
                    .build(new ResourceLocation(MODID, "poison_quill_vine").toString())
    );

    // GOLEM

    public static final RegistryObject<EntityType<SquallGolemEntity>> SQUALL_GOLEM = ENTITY_TYPES.register("squall_golem", () ->
            EntityType.Builder.<SquallGolemEntity>create(SquallGolemEntity::new, EntityClassification.MONSTER)
                    .size(2.62F, 2.81F) // 42 px wide, 29px tall + 16px of height
                    .func_233606_a_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new SquallGolemEntity(world))
                    .build(new ResourceLocation(MODID, "squall_golem").toString())
    );

    // MISC

    //   public static final EntityType<SmallFireballEntity> SMALL_FIREBALL = register("small_fireball", EntityType.Builder.<SmallFireballEntity>create(SmallFireballEntity::new, EntityClassification.MISC).size(0.3125F, 0.3125F).func_233606_a_(4).func_233608_b_(10));

    public static final RegistryObject<EntityType<WraithFireballEntity>> WRAITH_FIREBALL = ENTITY_TYPES.register("wraith_fireball", () ->
            EntityType.Builder.<WraithFireballEntity>create(WraithFireballEntity::new, EntityClassification.MISC)
                    .size(0.3125F, 0.3125F)
                    .func_233606_a_(4)
                    .func_233608_b_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new WraithFireballEntity(world))
                    .build(new ResourceLocation(MODID, "wraith_fireball").toString())
    );

    public static final RegistryObject<EntityType<SlimeballEntity>> SLIMEBALL = ENTITY_TYPES.register("slimeball", () ->
            EntityType.Builder.<SlimeballEntity>create(SlimeballEntity::new, EntityClassification.MISC)
                    .size(0.3125F, 0.3125F)
                    .func_233606_a_(4)
                    .func_233608_b_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new SlimeballEntity(world))
                    .build(new ResourceLocation(MODID, "slimeball").toString())
    );

    public static final RegistryObject<EntityType<CobwebProjectileEntity>> COBWEB_PROJECTILE = ENTITY_TYPES.register("cobweb_projectile", () ->
            EntityType.Builder.<CobwebProjectileEntity>create(CobwebProjectileEntity::new, EntityClassification.MISC)
                    .size(0.3125F, 0.3125F)
                    .func_233606_a_(4)
                    .func_233608_b_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new CobwebProjectileEntity(world))
                    .build(new ResourceLocation(MODID, "cobweb_projectile").toString())
    );

    public static final RegistryObject<EntityType<CobwebTrapEntity>> COBWEB_TRAP = ENTITY_TYPES.register("cobweb_trap", () ->
            EntityType.Builder.<CobwebTrapEntity>create(CobwebTrapEntity::new, EntityClassification.MISC)
                    .size(1.0F, 1.0F)
                    .func_233606_a_(4)
                    .func_233608_b_(10)
                    .setCustomClientFactory((spawnEntity,world) -> new CobwebTrapEntity(world))
                    .build(new ResourceLocation(MODID, "cobweb_trap").toString())
    );

    public static final RegistryObject<EntityType<GeomancerWallEntity>> GEOMANCER_WALL = ENTITY_TYPES.register("geomancer_wall", () ->
            EntityType.Builder.<GeomancerWallEntity>create(GeomancerWallEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(1.0F, 2.5F)
                    .func_233606_a_(6)
                    .func_233608_b_(2)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerWallEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer_wall").toString())
    );

    public static final RegistryObject<EntityType<GeomancerBombEntity>> GEOMANCER_BOMB = ENTITY_TYPES.register("geomancer_bomb", () ->
            EntityType.Builder.<GeomancerBombEntity>create(GeomancerBombEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(1.0F, 2.5F)
                    .func_233606_a_(6)
                    .func_233608_b_(2)
                    .setCustomClientFactory((spawnEntity,world) -> new GeomancerBombEntity(world))
                    .build(new ResourceLocation(MODID, "geomancer_bomb").toString())
    );

    public static final RegistryObject<EntityType<RedstoneMineEntity>> REDSTONE_MINE = ENTITY_TYPES.register("redstone_mine", () ->
            EntityType.Builder.<RedstoneMineEntity>create(RedstoneMineEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(1.0F, 0.5F)
                    .func_233606_a_(6)
                    .func_233608_b_(2)
                    .setCustomClientFactory((spawnEntity,world) -> new RedstoneMineEntity(world))
                    .build(new ResourceLocation(MODID, "redstone_mine").toString())
    );

    public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = ENTITY_TYPES.register("ice_cloud", () ->
            EntityType.Builder.<IceCloudEntity>create(IceCloudEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(2.0F, 1.0F)
                    .func_233606_a_(6)
                    .func_233608_b_(2)
                    .setCustomClientFactory((spawnEntity,world) -> new IceCloudEntity(world))
                    .build(new ResourceLocation(MODID, "ice_cloud").toString())
    );

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO = ENTITY_TYPES.register("tornado", () ->
            EntityType.Builder.<TornadoEntity>create(TornadoEntity::new, EntityClassification.MISC)
                    .immuneToFire()
                    .size(6.0F, 0.5F)
                    .func_233606_a_(10)
                    .func_233608_b_(Integer.MAX_VALUE)
                    .setCustomClientFactory((spawnEntity,world) -> new TornadoEntity(world))
                    .build(new ResourceLocation(MODID, "tornado").toString())
    );
}
