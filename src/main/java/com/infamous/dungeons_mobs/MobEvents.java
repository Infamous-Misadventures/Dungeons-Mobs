package com.infamous.dungeons_mobs;

import com.infamous.dungeons_libraries.utils.AbilityHelper;
import com.infamous.dungeons_mobs.capabilities.convertible.Convertible;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleHelper;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.goals.SmartTridentAttackGoal;
import com.infamous.dungeons_mobs.interfaces.IHasItemStackData;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.mixin.TridentEntityAccessor;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class MobEvents {
    private static ArmorStand DUMMY_TARGET;

    public static Random random = new Random();
    
    @SubscribeEvent
    public static void onSetAttackTarget(LivingChangeTargetEvent event){
        LivingEntity attacker = event.getEntityLiving();
        Level level = attacker.level;
        LivingEntity target = event.getNewTarget();
        if(attacker instanceof Mob && target instanceof Mob){
            if(AbilityHelper.isAlly(attacker, target)){
                createDummyTarget(level);
                if(attacker instanceof NeutralMob)
                {
                    ((NeutralMob) attacker).setPersistentAngerTarget((UUID)null);
                    ((NeutralMob) attacker).setRemainingPersistentAngerTime(0);
                }
                ((Mob) attacker).setTarget(DUMMY_TARGET);
                attacker.setLastHurtByMob(DUMMY_TARGET);
            }
        }
    }

    private static void createDummyTarget(Level level) {
        if(DUMMY_TARGET == null){
            DUMMY_TARGET = EntityType.ARMOR_STAND.create(level);
            if (DUMMY_TARGET != null) {
                DUMMY_TARGET.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        // Making mobs avoid Geomancer Constructs
//        if(event.getEntity() instanceof CreatureEntity && !(event.getEntity() instanceof GeomancerEntity)){
//            CreatureEntity creatureEntity = (CreatureEntity) event.getEntity();
//            creatureEntity.goalSelector.addGoal(3, new AvoidBaseEntityGoal<>(creatureEntity, ConstructEntity.class, 8.0F, 0.6D, 1.0D));
//        }
//        if(event.getEntity() instanceof CreatureEntity && !(event.getEntity() instanceof WhispererEntity)){
//            CreatureEntity creatureEntity = (CreatureEntity) event.getEntity();
//            creatureEntity.goalSelector.addGoal(3, new AvoidEntityGoal<>(creatureEntity, VineEntity.class, 8.0F, 0.6D, 1.0D));
//        }
        if(event.getEntity() instanceof Drowned){
            Drowned drownedEntity = (Drowned) event.getEntity();
            ((GoalSelectorAccessor)drownedEntity.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 2 && pg.getGoal() instanceof RangedAttackGoal);
            drownedEntity.goalSelector.addGoal(2, new SmartTridentAttackGoal(drownedEntity, 1.0D, 40, 10.0F));
        }
        if(event.getEntity() instanceof ThrownTrident){
            ((IHasItemStackData)event.getEntity()).setDataItem(((TridentEntityAccessor) event.getEntity()).getTridentItem());
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        if(livingEntity instanceof Mob && ConvertibleHelper.convertsInWater((Mob)livingEntity)){
            Mob mob = (Mob) livingEntity;
            if (!mob.level.isClientSide && mob.isAlive() && !mob.isNoAi()) {
                Convertible convertibleCap = ConvertibleHelper.getConvertibleCapability(mob);
                if(convertibleCap == null) return;

                convertibleCap.setCanConvert(mob.isEyeInFluid(FluidTags.WATER));

                if (convertibleCap.isConverting()) {
                    convertibleCap.tickConversionTime();

                    EntityType<? extends Mob> convertToType = ConvertibleHelper.getDrowningConvertTo(mob);

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
        if(event.getEntity() instanceof Snowball){
            Snowball snowballEntity = (Snowball)event.getEntity();
            Entity shooter = snowballEntity.getOwner();
            if(shooter instanceof FrozenZombieEntity){
            	snowballEntity.playSound(ModSoundEvents.FROZEN_ZOMBIE_SNOWBALL_LAND.get(), 1.0F, 1.0F);
                HitResult rayTraceResult = event.getRayTraceResult();
                if(rayTraceResult instanceof EntityHitResult){
                    EntityHitResult entityRayTraceResult = (EntityHitResult)rayTraceResult;
                    if(entityRayTraceResult.getEntity() instanceof Player){
                        Player playerEntity = (Player) entityRayTraceResult.getEntity();
                        playerEntity.hurt(DamageSource.thrown(snowballEntity, shooter), 2.0F);
                            int i = 0;
                            if (event.getEntity().level.getDifficulty() == Difficulty.NORMAL) {
                                i = 3;
                            } else if (event.getEntity().level.getDifficulty() == Difficulty.HARD) {
                                i = 6;
                            }

                            if (i > 0) {
                                ((LivingEntity)playerEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, i * 20, 1));
                            }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onSnowballDamageMob(LivingHurtEvent event){
        if(event.getSource().getDirectEntity() instanceof Snowball){
            if(event.getSource().getEntity() instanceof FrozenZombieEntity){
                if(!(event.getEntityLiving() instanceof Player)){
                    event.setAmount(event.getAmount() + 2.0F);
                        int i = 0;
                        if (event.getEntityLiving().level.getDifficulty() == Difficulty.NORMAL) {
                            i = 3;
                        } else if (event.getEntityLiving().level.getDifficulty() == Difficulty.HARD) {
                            i = 6;
                        }

                        if (i > 0) {
                            event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, i * 20, 1));
                        }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onIllusionerCloneArrowHit(ProjectileImpactEvent event){
        if(event.getEntity() instanceof AbstractArrow){
        	AbstractArrow arrowEntity = (AbstractArrow)event.getEntity();
            Entity shooter = arrowEntity.getOwner();
            if(shooter instanceof IllusionerCloneEntity){
            	arrowEntity.playSound(ModSoundEvents.ILLUSIONER_CLONE_ARROW_HIT.get(), 1.0F, 1.0F);   
            	if (!arrowEntity.level.isClientSide) {
            		arrowEntity.remove(Entity.RemovalReason.DISCARDED);
            	} else {
            		for(int i = 0; i < 2; ++i) {
        	            double d0 = random.nextGaussian() * 0.02D;
        	            double d1 = random.nextGaussian() * 0.02D;
        	            double d2 = random.nextGaussian() * 0.02D;
        	            arrowEntity.level.addParticle(ParticleTypes.POOF, arrowEntity.getRandomX(1.0D), arrowEntity.getRandomY(), arrowEntity.getRandomZ(1.0D), d0, d1, d2);
        	         }
            	}
            }
        }
    }

    @SubscribeEvent
    public static void onIceCreeperExplosion(EntityMobGriefingEvent event){
        if(event.getEntity() instanceof IcyCreeperEntity){
            IcyCreeperEntity iceCreeperEntity = (IcyCreeperEntity)event.getEntity();
            iceCreeperEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
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
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
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
            if (entity instanceof ConstructEntity && entity != source) {
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
