package com.infamous.dungeons_mobs;

import com.infamous.dungeons_mobs.capabilities.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.CloneableProvider;
import com.infamous.dungeons_mobs.capabilities.ICloneable;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.jungle.VineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.goals.AvoidBaseEntityGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class MobEvents {


    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (isCloneableEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(DungeonsMobs.MODID, "cloneable"), new CloneableProvider());
        }
    }

    private static boolean isCloneableEntity(Entity object) {
        if(object instanceof IllusionerEntity){
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        // Making mobs avoid Geomancer Constructs
        if(event.getEntity() instanceof CreatureEntity && !(event.getEntity() instanceof GeomancerEntity)){
            CreatureEntity creatureEntity = (CreatureEntity) event.getEntity();
            creatureEntity.goalSelector.addGoal(3, new AvoidBaseEntityGoal<>(creatureEntity, ConstructEntity.class, 8.0F, 0.6D, 1.0D));
        }
        if(event.getEntity() instanceof CreatureEntity && !(event.getEntity() instanceof WhispererEntity)){
            CreatureEntity creatureEntity = (CreatureEntity) event.getEntity();
            creatureEntity.goalSelector.addGoal(3, new AvoidEntityGoal<>(creatureEntity, VineEntity.class, 8.0F, 0.6D, 1.0D));
        }
        if(event.getEntity() instanceof IllusionerCloneEntity){
            IllusionerCloneEntity illusionerCloneEntity = (IllusionerCloneEntity)event.getEntity();
            if(illusionerCloneEntity.getCaster() != null && !illusionerCloneEntity.world.isRemote){
                LivingEntity caster = illusionerCloneEntity.getCaster();
                if(caster instanceof IllusionerEntity){
                    ICloneable cloneable = CloneableHelper.getCloneableCapability(caster);
                    if(cloneable != null){
                        cloneable.addClone(illusionerCloneEntity.getUniqueID());
                    }
                }
            }
        }
        if(event.getEntity() instanceof IllusionerEntity){
            IllusionerEntity illusionerEntity = (IllusionerEntity)event.getEntity();
            GoalSelector goalSelector = illusionerEntity.goalSelector;

        }
    }

    @SubscribeEvent
    public static void onIllusionerAttacked(LivingAttackEvent event){
        if(event.getEntityLiving() instanceof IllusionerEntity && !(event.getSource().getTrueSource() instanceof IllusionerCloneEntity)){
            IllusionerEntity illusionerEntity = (IllusionerEntity) event.getEntityLiving();
            ICloneable cloneable = CloneableHelper.getCloneableCapability(illusionerEntity);
            if(cloneable != null && illusionerEntity.world instanceof ServerWorld){
                UUID[] clones = cloneable.getClones();
                for(int i = 0; i < clones.length; i++){
                    UUID currentClone = clones[i];
                    if(currentClone != null){
                        ServerWorld serverWorld = (ServerWorld) illusionerEntity.world;
                        Entity entity = serverWorld.getEntityByUuid(currentClone);
                        if(entity != null){
                            entity.remove();
                        }
                        clones[i] = null;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onIllusionerBecomesInvisible(PotionEvent.PotionAddedEvent event){
        if(event.getEntityLiving() instanceof IllusionerEntity){
            IllusionerEntity illusionerEntity = (IllusionerEntity) event.getEntityLiving();
            if(!illusionerEntity.isPotionActive(Effects.INVISIBILITY)
                    && event.getPotionEffect().getPotion() == Effects.INVISIBILITY
                    && !illusionerEntity.world.isRemote
                    && illusionerEntity.isSpellcasting()){
                summonIllusionerClones(illusionerEntity);
            }
        }
    }

    private static void summonIllusionerClones(IllusionerEntity illusionerEntity){
        int difficultyAsInt = illusionerEntity.world.getDifficulty().getId();
        int mobsToSummon = difficultyAsInt * 2 + 1; // 3 on easy, 5 on normal, 7 on hard
        for(int i = 0; i < mobsToSummon; ++i) {
            BlockPos blockpos = illusionerEntity.getPosition().add(-2 + illusionerEntity.getRNG().nextInt(5), 1, -2 + illusionerEntity.getRNG().nextInt(5));
            IllusionerCloneEntity illusionerCloneEntity = new IllusionerCloneEntity(illusionerEntity.world, illusionerEntity, 30 * 20);
            DifficultyInstance difficultyForLocation = illusionerEntity.world.getDifficultyForLocation(blockpos);
            illusionerCloneEntity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            illusionerCloneEntity.onInitialSpawn((IServerWorld) illusionerCloneEntity.world, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
            illusionerEntity.world.addEntity(illusionerCloneEntity);
        }
    }

    @SubscribeEvent
    public static void onSnowballHitPlayer(ProjectileImpactEvent event){
        if(event.getEntity() instanceof SnowballEntity){
            SnowballEntity snowballEntity = (SnowballEntity)event.getEntity();
            Entity shooter = snowballEntity.func_234616_v_();
            if(shooter instanceof FrozenZombieEntity){
                RayTraceResult rayTraceResult = event.getRayTraceResult();
                if(rayTraceResult instanceof EntityRayTraceResult){
                    EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
                    if(entityRayTraceResult.getEntity() instanceof PlayerEntity){
                        PlayerEntity playerEntity = (PlayerEntity) entityRayTraceResult.getEntity();
                        playerEntity.attackEntityFrom(DamageSource.causeThrownDamage(snowballEntity, shooter), 1.0F);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onSnowballDamageMob(LivingHurtEvent event){
        if(event.getSource().getImmediateSource() instanceof SnowballEntity){
            if(event.getSource().getTrueSource() instanceof FrozenZombieEntity){
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
            iceCreeperEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600));
        }
    }


    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event){
        handlePillarProtection(event);
        if(event.getExplosion().getExploder() instanceof IcyCreeperEntity){
            List<Entity> entityList = event.getAffectedEntities();
            for(Entity entity : entityList){
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600));
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
        BlockPos entityPos = entity.getPosition();
        for(ConstructEntity pillarEntity : pillarEntities){
            BlockPos pillarPos = pillarEntity.getPosition();
            double widthAllowance = pillarEntity.getWidth();
            double distanceEntityToPillar = Math.sqrt(entityPos.distanceSq(pillarPos));
            double distanceExplosionToPillar = Math.sqrt(detonationOrigin.distanceSq(pillarPos));
            double distanceExplosionToEntity = Math.sqrt(detonationOrigin.distanceSq(entityPos));
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
