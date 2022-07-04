package com.infamous.dungeons_mobs;

import com.infamous.dungeons_mobs.capabilities.ancient.AncientProvider;
import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableProvider;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleHelper;
import com.infamous.dungeons_mobs.capabilities.convertible.ConvertibleProvider;
import com.infamous.dungeons_mobs.capabilities.convertible.IConvertible;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsProvider;
import com.infamous.dungeons_mobs.capabilities.teamable.TeamableHelper;
import com.infamous.dungeons_mobs.capabilities.teamable.TeamableProvider;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.illagers.ArmoredPillagerEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.*;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.goals.SmartTridentAttackGoal;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.utils.ModProjectileHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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

            if (!(raider instanceof SpellcastingIllagerEntity)) {
                raider.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("FR", 24.0D, AttributeModifier.Operation.ADDITION));
            }
            if (!(raider instanceof IllusionerCloneEntity) && !(raider instanceof IllusionerCloneCloneEntity) && !(raider instanceof MageCloneEntity)) {
                raider.addEffect(new EffectInstance(Effects.HEAL, 1, 10, (false), (false)));
                raider.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 20, 5, (false), (false)));
            }
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
        if (livingEntity.invulnerableTime > 15){
            livingEntity.invulnerableTime = 15;
        }
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
    public static void onDamageMob(LivingHurtEvent event){
        if(event.getSource().getDirectEntity() instanceof SnowballEntity){
            if(event.getSource().getEntity() instanceof FrozenZombieEntity){
                if(!(event.getEntityLiving() instanceof PlayerEntity)){
                    event.setAmount(event.getAmount() + 3.0F);
                }
            }
        }
        if(event.getEntityLiving() instanceof DungeonsEvokerEntity || event.getEntityLiving() instanceof DungeonsIllusionerEntity ||event.getEntityLiving() instanceof MinecraftIllusionerEntity ||event.getEntityLiving() instanceof VindicatorRaidCaptainEntity ||event.getEntityLiving() instanceof RampartCaptainEntity){
            if(!(event.getSource().getEntity() instanceof PlayerEntity) && !(event.getSource().getEntity() instanceof ServerPlayerEntity))
                event.setAmount(event.getAmount() / 3.0F / 2.0F);
        }
        if(event.getSource().getDirectEntity() instanceof FireworkRocketEntity){
            if(event.getSource().getEntity() instanceof DungeonsIllusionerEntity)
                event.setAmount((float) (Math.min(event.getEntityLiving().getAttributeValue(Attributes.MAX_HEALTH) * 0.8F,35.75F)));
        }
        if(event.getSource().isProjectile()){
            if(event.getSource().getEntity() instanceof MinecraftIllusionerEntity)
                event.setAmount((float) (event.getAmount() + Math.min(event.getEntityLiving().getAttributeValue(Attributes.MAX_HEALTH) * 0.725F,32.5F)));
        }
        if(event.getSource().getEntity() instanceof DungeonsEvokerEntity)
            if(event.getSource().getDirectEntity() instanceof EvokerFangsEntity) {
                event.getEntityLiving().setDeltaMovement(event.getEntityLiving().getDeltaMovement().add((event.getEntityLiving().getX() - event.getSource().getDirectEntity().getX()) / 10,0.1,(event.getEntityLiving().getZ() - event.getSource().getDirectEntity().getZ()) / 10));
                event.getEntityLiving().invulnerableTime = 0;
            }
        if(event.getSource().isProjectile()){
            if(event.getSource().getEntity() instanceof ArmoredPillagerEntity) {
                event.getEntityLiving().setDeltaMovement(event.getEntityLiving().getDeltaMovement().add((event.getEntityLiving().getX() - event.getSource().getDirectEntity().getX()) / 0.5 * (((ArmoredPillagerEntity) event.getSource().getEntity()).isDiamond() ? 1 : 0) * Math.max(1-event.getEntityLiving().getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0),0.1,(event.getEntityLiving().getZ() - event.getSource().getDirectEntity().getZ()) / 0.5 * (((ArmoredPillagerEntity) event.getSource().getEntity()).isDiamond() ? 1 : 0) * Math.max(1-event.getEntityLiving().getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0)));
                event.setAmount((float) (event.getAmount() + Math.min(event.getEntityLiving().getAttributeValue(Attributes.MAX_HEALTH) * 0.8F, ((ArmoredPillagerEntity) event.getSource().getEntity()).isDiamond() ? 20F : 12F)));
            }
        }
        if(event.getSource().getEntity() instanceof VindicatorRaidCaptainEntity || event.getSource().getEntity() instanceof RampartCaptainEntity) {
            if ((event.getEntityLiving() instanceof PlayerEntity || event.getEntityLiving() instanceof AbstractVillagerEntity)) {
                event.setAmount(18F);
            }
        }
        if(event.getSource().isProjectile()) {
            if (!(event.getEntityLiving() instanceof PlayerEntity)) {
                event.getEntityLiving().invulnerableTime = 0;
            }else {
                event.getEntityLiving().invulnerableTime = 12;
            }
        }
        if(event.getSource().isExplosion()) {
            event.getEntityLiving().invulnerableTime = 0;
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
