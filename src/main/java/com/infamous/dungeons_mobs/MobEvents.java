package com.infamous.dungeons_mobs;

import com.infamous.dungeons_mobs.capabilities.ancient.AncientProvider;
import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableProvider;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleHelper;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleProvider;
import com.infamous.dungeons_mobs.capabilities.convertible.IConvertible;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsProvider;
import com.infamous.dungeons_mobs.capabilities.teamable.TeamableHelper;
import com.infamous.dungeons_mobs.capabilities.teamable.TeamableProvider;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.jungle.VineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.infamous.dungeons_mobs.goals.AvoidBaseEntityGoal;
import com.infamous.dungeons_mobs.goals.SmartTridentAttackGoal;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.command.ICommandSource;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.boss.dragon.phase.AttackingSittingPhase;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.lang.annotation.Target;
import java.util.*;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class MobEvents {
    private static ArmorStandEntity DUMMY_TARGET;


    @SubscribeEvent
    public static void onSetAttackTarget(LivingSetAttackTargetEvent event){
        LivingEntity attacker = event.getEntityLiving();
        World level = attacker.level;
        LivingEntity target = event.getTarget();
        if(attacker instanceof MobEntity && target instanceof MobEntity){
            if(TeamableHelper.areTeammates((MobEntity) attacker, (MobEntity)target)){
                createDummyTarget(level);
                if(attacker instanceof IAngerable)
                {
                    ((IAngerable) attacker).setPersistentAngerTarget((UUID)null);
                    ((IAngerable) attacker).setRemainingPersistentAngerTime(0);
                }
                ((MobEntity) attacker).setTarget(DUMMY_TARGET);
                attacker.setLastHurtByMob(DUMMY_TARGET);
            }
        }
    }

    private static void createDummyTarget(World level) {
        if(DUMMY_TARGET == null){
            DUMMY_TARGET = EntityType.ARMOR_STAND.create(level);
            if (DUMMY_TARGET != null) {
                DUMMY_TARGET.remove();
            }
        }
    }


    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (isCloneableEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(DungeonsMobs.MODID, "cloneable"), new CloneableProvider());
        }
        if(event.getObject() instanceof MobEntity && ConvertibleHelper.convertsInWater(((MobEntity) event.getObject()))){
            event.addCapability(new ResourceLocation(DungeonsMobs.MODID, "convertible"), new ConvertibleProvider());
        }
        if(event.getObject() instanceof MobEntity){
            event.addCapability(new ResourceLocation(MODID, "teammable"), new TeamableProvider());
        }
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(DungeonsMobs.MODID, "dungeons_mobs_mob_props"), new MobPropsProvider());
        }
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(DungeonsMobs.MODID, "ancient"), new AncientProvider());
        }
    }

    private static boolean isCloneableEntity(Entity object) {
        if(object instanceof DungeonsIllusionerEntity){
            return true;
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AbstractRaiderEntity){
            AbstractRaiderEntity raider = (AbstractRaiderEntity) event.getEntity();
            raider.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE,20,5, (false), (false)));
        }
        if (event.getEntity() instanceof DrownedEntity) {
            DrownedEntity drownedEntity = (DrownedEntity) event.getEntity();
            ((GoalSelectorAccessor) drownedEntity.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 2 && pg.getGoal() instanceof RangedAttackGoal);
            drownedEntity.goalSelector.addGoal(2, new SmartTridentAttackGoal(drownedEntity, 1.0D, 40, 10.0F));
        }
        //v.getServer().getCommands().performCommand(v.createCommandSourceStack().withSuppressedOutput().withPermission(4), "tp 0 0 0");

    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        if(livingEntity instanceof MobEntity && ConvertibleHelper.convertsInWater((MobEntity)livingEntity)){
            MobEntity mob = (MobEntity) livingEntity;
            if (!mob.level.isClientSide && mob.isAlive() && !mob.isNoAi()) {
                IConvertible convertibleCap = ConvertibleHelper.getConvertibleCapability(mob);
                if(convertibleCap == null) return;

                convertibleCap.setCanConvert(mob.isEyeInFluid(FluidTags.WATER));

                if (convertibleCap.isConverting()) {
                    convertibleCap.tickConversionTime();

                    EntityType<? extends MobEntity> convertToType = ConvertibleHelper.getDrowningConvertTo(mob);

                    if (convertibleCap.getConversionTime() < 0 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(mob, convertToType, convertibleCap::setConversionTime)) {
                        convertibleCap.doConversion(mob, convertToType, ConvertibleHelper::onDrownedAndConvertedTo);
                    }
                } else {
                    if (convertibleCap.canConvert()) {
                        convertibleCap.tickPrepareConversionTime();
                        if (convertibleCap.getPrepareConversionTime() >= 600) {
                            convertibleCap.startConversion(300);
                        }
                    } else {
                        convertibleCap.setPrepareConversionTime(-1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSnowballHitPlayer(ProjectileImpactEvent event){
        if(event.getEntity() instanceof SnowballEntity){
            SnowballEntity snowballEntity = (SnowballEntity)event.getEntity();
            Entity shooter = snowballEntity.getOwner();
            if(shooter instanceof FrozenZombieEntity){
                RayTraceResult rayTraceResult = event.getRayTraceResult();
                if(rayTraceResult instanceof EntityRayTraceResult){
                    EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
                    if(entityRayTraceResult.getEntity() instanceof PlayerEntity){
                        PlayerEntity playerEntity = (PlayerEntity) entityRayTraceResult.getEntity();
                        playerEntity.hurt(DamageSource.thrown(snowballEntity, shooter), 1.0F);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onSnowballDamageMob(LivingHurtEvent event){
        if(event.getSource().getDirectEntity() instanceof SnowballEntity){
            if(event.getSource().getEntity() instanceof FrozenZombieEntity){
                if(!(event.getEntityLiving() instanceof PlayerEntity)){
                    event.setAmount(event.getAmount() + 1.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onIceCreeperExplosion(EntityMobGriefingEvent event){
        if(event.getEntity() instanceof IcyCreeperEntity){
            IcyCreeperEntity iceCreeperEntity = (IcyCreeperEntity)event.getEntity();
            iceCreeperEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 600));
        }
    }

    @SubscribeEvent
    public static void onIceCloudExplosion(EntityMobGriefingEvent event){
        if(event.getEntity() instanceof IceCloudEntity){
            IceCloudEntity iceCreeperEntity = (IceCloudEntity) event.getEntity();
            iceCreeperEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200,5));
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event){
        handlePillarProtection(event);
        if(event.getExplosion().getSourceMob() instanceof IcyCreeperEntity){
            if(!DungeonsMobsConfig.COMMON.ENABLE_ICY_CREEPER_GRIEFING.get()){
                event.getAffectedBlocks().clear();
            }
            List<Entity> entityList = event.getAffectedEntities();
            for(Entity entity : entityList){
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 600));
                }
            }
        }
        if (event.getExplosion().getSourceMob() instanceof IceCloudEntity) {
            IceCloudEntity j = (IceCloudEntity)event.getExplosion().getSourceMob();
            event.getAffectedBlocks().clear();
            List<Entity> eee = event.getAffectedEntities();

            for(Entity entity : eee){
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200,5));
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event){
        RayTraceResult rayTraceResult = event.getRayTraceResult();
        if(rayTraceResult instanceof EntityRayTraceResult){
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
            if(entityRayTraceResult.getEntity() instanceof AbstractPillarEntity){
                event.setCanceled(true);
            }
        }
    }

     */

    private static void handlePillarProtection(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        Entity source = explosion.getExploder();
        BlockPos detonationOrigin = new BlockPos(explosion.getPosition());

        List<Entity> entityList = event.getAffectedEntities();
        List<ConstructEntity> potentialProtectingPillars = new java.util.ArrayList<>(Collections.emptyList());
        for(Entity entity : entityList){
            if (entity instanceof ConstructEntity  && entity != source) {
                potentialProtectingPillars.add((ConstructEntity) entity);
            }
        }
        boolean pillarsArePresent = !potentialProtectingPillars.isEmpty();
        if(!pillarsArePresent) return;

        Iterator<Entity> it = entityList.iterator();
        while (it.hasNext()) {
            Entity currentEntity = it.next();
            boolean notAPillar = !(currentEntity instanceof ConstructEntity);
            if (notAPillar && currentEntity != null) {
                boolean protectedByPillar = entityProtectedByPillar(currentEntity, potentialProtectingPillars, detonationOrigin);
                if(protectedByPillar) {
                    it.remove();
                }
            }
        }
    }

    private static boolean entityProtectedByPillar(Entity entity, List<ConstructEntity> pillarEntities, BlockPos detonationOrigin){
        if(pillarEntities.isEmpty()) return false;
        BlockPos entityPos = entity.blockPosition();
        for(ConstructEntity pillarEntity : pillarEntities){
            BlockPos pillarPos = pillarEntity.blockPosition();
            double widthAllowance = pillarEntity.getBbWidth();
            double distanceEntityToPillar = Math.sqrt(entityPos.distSqr(pillarPos));
            double distanceExplosionToPillar = Math.sqrt(detonationOrigin.distSqr(pillarPos));
            double distanceExplosionToEntity = Math.sqrt(detonationOrigin.distSqr(entityPos));
            boolean canProtect = distanceEntityToPillar + distanceExplosionToPillar <= distanceExplosionToEntity + widthAllowance;
            if(canProtect) return true;
        }
        return false;
    }



    /*
    @SubscribeEvent
    public static void onMobsSpawn(WorldEvent.PotentialSpawns event){
        IWorld world = event.getWorld();
        BlockPos blockPos = event.getPos();
        Chunk chunk = world.getWorld().getChunkAt(blockPos);
        List<Biome.SpawnListEntry> spawnListEntries = event.getList();
        EntityClassification classification = event.getType();
    }

     */
}
