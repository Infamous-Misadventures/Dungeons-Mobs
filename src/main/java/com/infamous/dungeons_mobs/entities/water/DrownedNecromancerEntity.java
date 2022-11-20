package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_libraries.attribute.AttributeRegistry;
import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_libraries.summon.SummonHelper;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.projectiles.DrownedNecromancerOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static com.infamous.dungeons_mobs.entities.SpawnArmoredHelper.equipArmorSet;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class DrownedNecromancerEntity extends Drowned implements IAnimatable, SpawnArmoredMob {

    public int landShootAnimationTick;
    public int landShootAnimationLength = 20;
    public int landShootAnimationActionPoint = 7;
    public int landSummonAnimationTick;
    public int landSummonAnimationLength = 45;
    public int landSummonAnimationActionPoint1 = landSummonAnimationLength - 20;
    public int landSummonAnimationActionPoint2 = landSummonAnimationLength - 23;
    public int landSummonAnimationActionPoint3 = landSummonAnimationLength - 26;
    public int landSummonAnimationActionPoint4 = landSummonAnimationLength - 32;
    public int landSummonAnimationActionPoint5 = landSummonAnimationLength - 38;
    public int rainTridentStormAnimationTick;
    public int rainTridentStormAnimationLength = 45;
    public int rainShootAnimationTick;
    public int rainShootAnimationLength = 40;
    public int rainShootAnimationActionPoint = 23;
    public int summonAnimationTick;
    public int summonAnimationLength = 45;
    public int summonAnimationActionPoint = 23;
    public int shootAnimationTick;
    public int shootAnimationLength = 40;
    public int shootAnimationActionPoint = 23;
    public int tridentStormAnimationTick;
    public int tridentStormAnimationLength = 45;
    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public DrownedNecromancerEntity(EntityType<? extends DrownedNecromancerEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Drowned.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2D).add(ForgeMod.SWIM_SPEED.get(), 2.5D).add(Attributes.FOLLOW_RANGE, 30.0D).add(Attributes.MAX_HEALTH, 75.0D).add(Attributes.ARMOR, 12.5D).add(Attributes.KNOCKBACK_RESISTANCE, 0.6D).add(AttributeRegistry.SUMMON_CAP.get(), 4);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DrownedNecromancerEntity.GoToWaterGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new DrownedNecromancerEntity.LandSummonGoal(this));
        this.goalSelector.addGoal(2, new DrownedNecromancerEntity.SummonGoal(this));
        this.goalSelector.addGoal(3, new DrownedNecromancerEntity.TridentStormGoal(this));
        this.goalSelector.addGoal(3, new DrownedNecromancerEntity.RainTridentStormGoal(this));
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.ShootAttackGoal(this));
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.RainShootAttackGoal(this));
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.LandShootAttackGoal(this));
        this.goalSelector.addGoal(5, new ApproachTargetGoal(this, 7.5, 1.1D, true));
        this.goalSelector.addGoal(6, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(7, new DrownedNecromancerEntity.GoToBeachGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new DrownedNecromancerEntity.SwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
    }
    
    public boolean isSpellcasting() {
    	return this.shootAnimationTick > 0 || this.rainShootAnimationTick > 0 || this.landShootAnimationTick > 0 || this.tridentStormAnimationTick > 0 || this.rainTridentStormAnimationTick > 0 || this.summonAnimationTick > 0 || this.landSummonAnimationTick > 0;
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity
                && ((LivingEntity) entityIn).getMobType() == MobType.UNDEAD) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
        return 2.4F;
    }

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();
    }

    public void tickDownAnimTimers() {
        if (this.landShootAnimationTick > 0) {
            this.landShootAnimationTick--;
        }

        if (this.landSummonAnimationTick > 0) {
            this.landSummonAnimationTick--;
        }

        if (this.rainTridentStormAnimationTick > 0) {
            this.rainTridentStormAnimationTick--;
        }

        if (this.rainShootAnimationTick > 0) {
            this.rainShootAnimationTick--;
        }

        if (this.summonAnimationTick > 0) {
            this.summonAnimationTick--;
        }

        if (this.shootAnimationTick > 0) {
            this.shootAnimationTick--;
        }

        if (this.tridentStormAnimationTick > 0) {
            this.tridentStormAnimationTick--;
        }
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }
    
    @Override
    public boolean isBaby() {
        return false;
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 11) {
            this.landShootAnimationTick = landShootAnimationLength;
        } else if (p_28844_ == 9) {
            this.landSummonAnimationTick = landSummonAnimationLength;
        } else if (p_28844_ == 4) {
            this.rainTridentStormAnimationTick = rainTridentStormAnimationLength;
        } else if (p_28844_ == 8) {
            this.rainShootAnimationTick = rainShootAnimationLength;
        } else if (p_28844_ == 7) {
            this.summonAnimationTick = summonAnimationLength;
        } else if (p_28844_ == 6) {
            this.shootAnimationTick = shootAnimationLength;
        } else if (p_28844_ == 5) {
            this.tridentStormAnimationTick = tridentStormAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public float getVoicePitch() {
        return !this.isInWater() ? super.getVoicePitch() / 1.5F : super.getVoicePitch();
    }

    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? ModSoundEvents.DROWNED_NECROMANCER_IDLE.get() : SoundEvents.DROWNED_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isInWater() ? ModSoundEvents.DROWNED_NECROMANCER_HURT.get() : SoundEvents.DROWNED_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWater() ? ModSoundEvents.DROWNED_NECROMANCER_DEATH.get() : SoundEvents.DROWNED_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.DROWNED_STEP;
    }

    protected SoundEvent getSwimSound() {
        return ModSoundEvents.DROWNED_NECROMANCER_SWIM.get();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.NECROMANCER_TRIDENT.get()));
        equipArmorSet(ModItems.DROWNED_NECROMANCER_ARMOR, this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.tridentStormAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_trident_storm", LOOP));
        } else if (this.rainTridentStormAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_trident_storm_land", LOOP));
        } else if (this.summonAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_summon", LOOP));
        } else if (this.landSummonAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_summon", LOOP));
        } else if (this.shootAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_shoot", LOOP));
        } else if (this.rainShootAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_shoot_land", LOOP));
        } else if (this.landShootAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_shoot", LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (this.isInWater()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_swim", LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_walk", LOOP));
            }
        } else {
            if (this.isInWater()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("drowned_necromancer_idle", LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_idle", LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private boolean isInRain() {
        BlockPos blockpos = this.blockPosition();
        return this.level.isRainingAt(blockpos) || this.level.isRainingAt(new BlockPos((double) blockpos.getX(), this.getBoundingBox().maxY, (double) blockpos.getZ()));
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.DROWNED_NECROMANCER_ARMOR;
    }

    static class SwimUpGoal extends Goal {
        private final DrownedNecromancerEntity drowned;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;

        public SwimUpGoal(DrownedNecromancerEntity p_i48908_1_, double p_i48908_2_, int p_i48908_4_) {
            this.drowned = p_i48908_1_;
            this.speedModifier = p_i48908_2_;
            this.seaLevel = p_i48908_4_;
        }

        public boolean canUse() {
            return !this.drowned.level.isDay() && this.drowned.isInWater() && this.drowned.getY() < (double) (this.seaLevel - 2);
        }

        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }

        public void tick() {
            if (this.drowned.getY() < (double) (this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
                Vec3 vector3d = DefaultRandomPos.getPosTowards(this.drowned, 4, 8, new Vec3(this.drowned.getX(), (double) (this.seaLevel - 1), this.drowned.getZ()), (double)((float)Math.PI / 2F));
                if (vector3d == null) {
                    this.stuck = true;
                    return;
                }

                this.drowned.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, this.speedModifier);
            }

        }

        public void start() {
            this.drowned.setSearchingForLand(true);
            this.stuck = false;
        }

        public void stop() {
            this.drowned.setSearchingForLand(false);
        }
    }

    static class GoToWaterGoal extends Goal {
        private final PathfinderMob mob;
        private final double speedModifier;
        private final Level level;
        private double wantedX;
        private double wantedY;
        private double wantedZ;

        public GoToWaterGoal(PathfinderMob p_i48910_1_, double p_i48910_2_) {
            this.mob = p_i48910_1_;
            this.speedModifier = p_i48910_2_;
            this.level = p_i48910_1_.level;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (!this.level.isDay()) {
                return false;
            } else if (this.mob.isInWaterOrRain()) {
                return false;
            } else {
                Vec3 vector3d = this.getWaterPos();
                if (vector3d == null) {
                    return false;
                } else {
                    this.wantedX = vector3d.x;
                    this.wantedY = vector3d.y;
                    this.wantedZ = vector3d.z;
                    return true;
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone();
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @Nullable
        private Vec3 getWaterPos() {
            Random random = this.mob.getRandom();
            BlockPos blockpos = this.mob.blockPosition();

            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (this.level.getBlockState(blockpos1).is(Blocks.WATER)) {
                    return Vec3.atBottomCenterOf(blockpos1);
                }
            }

            return null;
        }
    }

    static class GoToBeachGoal extends MoveToBlockGoal {
        private final DrownedNecromancerEntity drowned;

        public GoToBeachGoal(DrownedNecromancerEntity p_i48911_1_, double p_i48911_2_) {
            super(p_i48911_1_, p_i48911_2_, 8, 2);
            this.drowned = p_i48911_1_;
        }

        public boolean canUse() {
            return super.canUse() && !this.drowned.level.isDay() && this.drowned.level.isRaining() && this.drowned.isInWater() && this.drowned.getY() >= (double) (this.drowned.level.getSeaLevel() - 7);
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected boolean isValidTarget(LevelReader p_179488_1_, BlockPos p_179488_2_) {
            BlockPos blockpos = p_179488_2_.above();
            return p_179488_1_.isEmptyBlock(blockpos) && p_179488_1_.isEmptyBlock(blockpos.above()) ? p_179488_1_.getBlockState(p_179488_2_).entityCanStandOn(p_179488_1_, p_179488_2_, this.drowned) : false;
        }

        public void start() {
            this.drowned.setSearchingForLand(false);
            this.drowned.navigation = this.drowned.groundNavigation;
            super.start();
        }

        public void stop() {
            super.stop();
        }
    }

    class LandSummonGoal extends Goal {

        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        public int mobSummonRange = 5;
        public int closeMobSummonRange = 2;

        public LandSummonGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && mob.tickCount >= this.nextUseTime && animationsUseable()
                    && mob.hasLineOfSight(target) && !mob.isInWaterOrBubble();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(ModSoundEvents.NECROMANCER_PREPARE_SUMMON.get(), 1.0F, 1.0F);
            mob.landSummonAnimationTick = mob.landSummonAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 9);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null &&
                    (mob.landSummonAnimationTick == mob.landSummonAnimationActionPoint1 ||
                            mob.landSummonAnimationTick == mob.landSummonAnimationActionPoint2 ||
                            mob.landSummonAnimationTick == mob.landSummonAnimationActionPoint3 ||
                            (mob.landSummonAnimationTick == mob.landSummonAnimationActionPoint4 && mob.random.nextBoolean()) ||
                            (mob.landSummonAnimationTick == mob.landSummonAnimationActionPoint5 && mob.random.nextBoolean()))) {
                SummonSpotEntity mobSummonSpot = ModEntityTypes.SUMMON_SPOT.get().create(mob.level);
                mobSummonSpot.mobSpawnRotation = mob.random.nextInt(360);
                mobSummonSpot.setSummonType(2);
                BlockPos summonPos = mob.blockPosition().offset(-mobSummonRange + mob.random.nextInt((mobSummonRange * 2) + 1), 0, -mobSummonRange + mob.random.nextInt((mobSummonRange * 2) + 1));
                mobSummonSpot.moveTo(summonPos, 0.0F, 0.0F);

                // RELOCATES SUMMONED MOB CLOSER TO NECROMANCER IF SPAWNED IN A POSITION THAT MAY HINDER ITS ABILITY TO JOIN IN THE BATTLE
                if (mobSummonSpot.isInWall() || !canSee(mobSummonSpot, target)) {
                    summonPos = mob.blockPosition().offset(-closeMobSummonRange + mob.random.nextInt((closeMobSummonRange * 2) + 1), 0, -closeMobSummonRange + mob.random.nextInt((closeMobSummonRange * 2) + 1));
                }

                // RELOCATES SUMMONED MOB TO NECROMANCER'S POSITION IF STILL IN A POSITION THAT MAY HINDER ITS ABILITY TO JOIN IN THE BATTLE
                if (mobSummonSpot.isInWall() || !canSee(mobSummonSpot, target)) {
                    summonPos = mob.blockPosition();
                }
                ((ServerLevel) mob.level).addFreshEntityWithPassengers(mobSummonSpot);
                PositionUtils.moveToCorrectHeight(mobSummonSpot);

                EntityType<?> entityType = getEntityType();

                Mob summonedMob = null;

                Entity entity = SummonHelper.summonEntity(mob, mobSummonSpot.blockPosition(), entityType);

                if (entity == null) {
                    mobSummonSpot.remove(RemovalReason.DISCARDED);
                    return;
                }

                if (entity instanceof Mob) {
                    summonedMob = ((Mob) entity);
                }

                summonedMob.setTarget(target);
                summonedMob.finalizeSpawn(((ServerLevel) mob.level), mob.level.getCurrentDifficultyAt(summonPos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                mobSummonSpot.playSound(ModSoundEvents.NECROMANCER_SUMMON.get(), 1.0F, 1.0F);
                if (mob.getTeam() != null) {
                    Scoreboard scoreboard = mob.level.getScoreboard();
                    scoreboard.addPlayerToTeam(summonedMob.getScoreboardName(), scoreboard.getPlayerTeam(mob.getTeam().getName()));
                }
                mobSummonSpot.summonedEntity = summonedMob;
            }
        }

        private EntityType<?> getEntityType() {
            EntityType<?> entityType = null;
            List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_MOB_SUMMONS.get();
            if (!necromancerMobSummons.isEmpty()) {
                Collections.shuffle(necromancerMobSummons);

                int randomIndex = mob.getRandom().nextInt(necromancerMobSummons.size());
                String randomMobID = necromancerMobSummons.get(randomIndex);
                entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
            }
            if (entityType == null) {
                entityType = EntityType.DROWNED;
            }
            return entityType;
        }

        @Override
        public void stop() {
            this.nextUseTime = mob.tickCount + (200 + mob.random.nextInt(200));
        }

        public boolean animationsUseable() {
            return mob.landSummonAnimationTick <= 0;
        }

        public boolean canSee(Entity entitySeeing, Entity p_70685_1_) {
            Vec3 vector3d = new Vec3(entitySeeing.getX(), entitySeeing.getEyeY(), entitySeeing.getZ());
            Vec3 vector3d1 = new Vec3(p_70685_1_.getX(), p_70685_1_.getEyeY(), p_70685_1_.getZ());
            if (p_70685_1_.level != entitySeeing.level || vector3d1.distanceToSqr(vector3d) > 128.0D * 128.0D)
                return false; //Forge Backport MC-209819
            return entitySeeing.level.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entitySeeing)).getType() == HitResult.Type.MISS;
        }

    }

    class LandShootAttackGoal extends Goal {
        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public LandShootAttackGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();
            return target != null && mob.distanceTo(target) <= 12.5 && mob.hasLineOfSight(target) && animationsUseable() && !mob.isInWaterRainOrBubble();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.landShootAnimationTick = mob.landShootAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 11);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null && mob.landShootAnimationTick == mob.landShootAnimationActionPoint) {
                Vec3 pos = PositionUtils.getOffsetPos(mob, 0.3, 1.5, 0.5, mob.getYRot());
                double d1 = target.getX() - pos.x;
                double d2 = target.getY(0.6D) - pos.y;
                double d3 = target.getZ() - pos.z;
                NecromancerOrbEntity necromancerOrb = new NecromancerOrbEntity(mob.level, mob, d1, d2, d3);
                necromancerOrb.setDelayedForm(true);
                necromancerOrb.rotateToMatchMovement();
                necromancerOrb.moveTo(pos.x, pos.y, pos.z);
                mob.level.addFreshEntity(necromancerOrb);
                mob.playSound(ModSoundEvents.NECROMANCER_SHOOT.get(), 1.0F, 1.0F);
            }
        }

        public boolean animationsUseable() {
            return mob.landShootAnimationTick <= 0;
        }

    }

    class RainTridentStormGoal extends Goal {

        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        public int tridentSummonRange = 15;

        public RainTridentStormGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) <= 13 && animationsUseable()
                    && mob.hasLineOfSight(target) && !mob.isInWaterOrBubble() && mob.isInRain();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.rainTridentStormAnimationTick = mob.rainTridentStormAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 4);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null && mob.rainTridentStormAnimationTick == 30) {
                mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_PREPARE_TRIDENT_STORM.get(), 3.0F, 1.0F);
            }

            if (target != null && mob.rainTridentStormAnimationTick <= 30 && mob.rainTridentStormAnimationTick >= 10 && mob.random.nextBoolean()) {
                TridentStormEntity tridentStorm = ModEntityTypes.TRIDENT_STORM.get().create(mob.level);
                tridentStorm.owner = mob;
                tridentStorm.moveTo(new BlockPos(mob.getX() - tridentSummonRange + mob.random.nextInt(tridentSummonRange * 2), mob.getY(), mob.getZ() - tridentSummonRange + mob.random.nextInt(tridentSummonRange * 2)), 0, 0);
                tridentStorm.setYRot(mob.random.nextInt(360));
                mob.level.addFreshEntity(tridentStorm);
                PositionUtils.moveToCorrectHeight(tridentStorm);
            }
        }

        @Override
        public void stop() {
            this.nextUseTime = mob.tickCount + (100 + mob.random.nextInt(300));
        }

        public boolean animationsUseable() {
            return mob.rainTridentStormAnimationTick <= 0;
        }

    }

    class RainShootAttackGoal extends Goal {
        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public RainShootAttackGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();
            return target != null && mob.distanceTo(target) <= 12.5 && mob.hasLineOfSight(target) && animationsUseable() && !mob.isInWaterOrBubble() && mob.isInRain();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.rainShootAnimationTick = mob.rainShootAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 8);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (mob.rainShootAnimationTick == mob.rainShootAnimationActionPoint) {
                mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_SHOOT.get(), 1.25F, 1.0F);
            }

            if (target != null && (mob.rainShootAnimationTick == mob.rainShootAnimationActionPoint || mob.rainShootAnimationTick == mob.rainShootAnimationActionPoint - 3 || mob.rainShootAnimationTick == mob.rainShootAnimationActionPoint - 6 || mob.rainShootAnimationTick == mob.rainShootAnimationActionPoint - 9)) {
                Vec3 pos = PositionUtils.getOffsetPos(mob, 0.3, 1.5, 0.5, getYRot());
                double d1 = target.getX() - pos.x;
                double d2 = target.getY(0.6D) - pos.y;
                double d3 = target.getZ() - pos.z;
                DrownedNecromancerOrbEntity necromancerOrb = new DrownedNecromancerOrbEntity(mob.level, mob, d1 + (mob.random.nextGaussian() * 1.0), d2 + (mob.random.nextGaussian() * 0.25), d3 + (mob.random.nextGaussian() * 1.0));
                necromancerOrb.rotateToMatchMovement();
                necromancerOrb.moveTo(pos.x, pos.y, pos.z);
                mob.level.addFreshEntity(necromancerOrb);
            }
        }

        public boolean animationsUseable() {
            return mob.rainShootAnimationTick <= 0;
        }

    }

    class SummonGoal extends Goal {

        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        public int mobSummonRange = 6;
        public int closeMobSummonRange = 3;

        public SummonGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && mob.tickCount >= this.nextUseTime && animationsUseable()
                    && mob.hasLineOfSight(target) && mob.isInWaterOrBubble();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_STRONG_ATTACK.get(), 1.0F, 1.0F);
            mob.summonAnimationTick = mob.summonAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 7);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null &&
                    mob.summonAnimationTick == mob.summonAnimationActionPoint) {
                for (int i = 0; i < 6; i++) {
                    SummonSpotEntity mobSummonSpot = ModEntityTypes.SUMMON_SPOT.get().create(mob.level);
                    mobSummonSpot.mobSpawnRotation = mob.random.nextInt(360);
                    mobSummonSpot.setSummonType(2);
                    BlockPos summonPos = mob.blockPosition().offset(-mobSummonRange + mob.random.nextInt((mobSummonRange * 2) + 1), 0, -mobSummonRange + mob.random.nextInt((mobSummonRange * 2) + 1));
                    mobSummonSpot.moveTo(summonPos, 0.0F, 0.0F);

                    // RELOCATES SUMMONED MOB CLOSER TO NECROMANCER IF SPAWNED IN A POSITION THAT MAY HINDER ITS ABILITY TO JOIN IN THE BATTLE
                    if (mobSummonSpot.isInWall() || !canSee(mobSummonSpot, target)) {
                        summonPos = mob.blockPosition().offset(-closeMobSummonRange + mob.random.nextInt((closeMobSummonRange * 2) + 1), 0, -closeMobSummonRange + mob.random.nextInt((closeMobSummonRange * 2) + 1));
                    }

                    // RELOCATES SUMMONED MOB TO NECROMANCER'S POSITION IF STILL IN A POSITION THAT MAY HINDER ITS ABILITY TO JOIN IN THE BATTLE
                    if (mobSummonSpot.isInWall() || !canSee(mobSummonSpot, target)) {
                        summonPos = mob.blockPosition();
                    }
                    ((ServerLevel) mob.level).addFreshEntityWithPassengers(mobSummonSpot);

                    EntityType<?> entityType = getEntityType();

                    Mob summonedMob = null;

                    Entity entity = SummonHelper.summonEntity(mob, mobSummonSpot.blockPosition(), entityType);

                    if (entity == null) {
                        mobSummonSpot.remove(RemovalReason.DISCARDED);
                        return;
                    }

                    if (entity instanceof Mob) {
                        summonedMob = ((Mob) entity);
                    }

                    summonedMob.setTarget(target);
                    summonedMob.finalizeSpawn(((ServerLevel) mob.level), mob.level.getCurrentDifficultyAt(summonPos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    mobSummonSpot.playSound(ModSoundEvents.DROWNED_NECROMANCER_SUMMON.get(), 1.0F, 1.0F);
                    if (mob.getTeam() != null) {
                        Scoreboard scoreboard = mob.level.getScoreboard();
                        scoreboard.addPlayerToTeam(summonedMob.getScoreboardName(), scoreboard.getPlayerTeam(mob.getTeam().getName()));
                    }
                    mobSummonSpot.summonedEntity = summonedMob;
                }
            }
        }

        private EntityType<?> getEntityType() {
            EntityType<?> entityType = null;
            List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_MOB_SUMMONS.get();
            if (!necromancerMobSummons.isEmpty()) {
                Collections.shuffle(necromancerMobSummons);

                int randomIndex = mob.getRandom().nextInt(necromancerMobSummons.size());
                String randomMobID = necromancerMobSummons.get(randomIndex);
                entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
            }
            if (entityType == null) {
                entityType = EntityType.DROWNED;
            }
            return entityType;
        }

        @Override
        public void stop() {
            this.nextUseTime = mob.tickCount + (200 + mob.random.nextInt(250));
        }

        public boolean animationsUseable() {
            return mob.summonAnimationTick <= 0;
        }

        public boolean canSee(Entity entitySeeing, Entity p_70685_1_) {
            Vec3 vector3d = new Vec3(entitySeeing.getX(), entitySeeing.getEyeY(), entitySeeing.getZ());
            Vec3 vector3d1 = new Vec3(p_70685_1_.getX(), p_70685_1_.getEyeY(), p_70685_1_.getZ());
            if (p_70685_1_.level != entitySeeing.level || vector3d1.distanceToSqr(vector3d) > 128.0D * 128.0D)
                return false; //Forge Backport MC-209819
            return entitySeeing.level.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entitySeeing)).getType() == HitResult.Type.MISS;
        }

    }

    class ShootAttackGoal extends Goal {
        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        public ShootAttackGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();
            return target != null && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) <= 14 && mob.hasLineOfSight(target) && animationsUseable() && mob.isInWaterOrBubble();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.shootAnimationTick = mob.shootAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 6);
            mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_STEAM_MISSILE.get(), 1.5F, 1.0F);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (mob.shootAnimationTick == mob.shootAnimationActionPoint) {
                mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_SHOOT.get(), 1.0F, 1.0F);
                mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_ATTACK.get(), 1.5F, 1.0F);
            }

            if (target != null && (mob.shootAnimationTick == mob.shootAnimationActionPoint || mob.shootAnimationTick == mob.shootAnimationActionPoint - 2 || mob.shootAnimationTick == mob.shootAnimationActionPoint - 4 || mob.shootAnimationTick == mob.shootAnimationActionPoint - 6 || mob.shootAnimationTick == mob.shootAnimationActionPoint - 8 || mob.shootAnimationTick == mob.shootAnimationActionPoint - 10)) {
                Vec3 pos = PositionUtils.getOffsetPos(mob, 0.3, 1.5, 0.5, getYRot());
                double d1 = target.getX() - pos.x;
                double d2 = target.getY(0.6D) - pos.y;
                double d3 = target.getZ() - pos.z;
                DrownedNecromancerOrbEntity necromancerOrb = new DrownedNecromancerOrbEntity(mob.level, mob, d1 + (mob.random.nextGaussian() * 1.25), d2 + (mob.random.nextGaussian() * 0.5), d3 + (mob.random.nextGaussian() * 1.25));
                necromancerOrb.rotateToMatchMovement();
                necromancerOrb.moveTo(pos.x, pos.y, pos.z);
                mob.level.addFreshEntity(necromancerOrb);
            }
        }

        @Override
        public void stop() {
            this.nextUseTime = mob.tickCount + 20;
        }

        public boolean animationsUseable() {
            return mob.shootAnimationTick <= 0;
        }

    }

    class TridentStormGoal extends Goal {

        public DrownedNecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        public int tridentSummonRange = 20;

        public TridentStormGoal(DrownedNecromancerEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();

            return target != null && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) <= 15 && animationsUseable()
                    && mob.hasLineOfSight(target) && mob.isInWaterOrBubble();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.tridentStormAnimationTick = mob.tridentStormAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 5);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null && mob.tridentStormAnimationTick == 30) {
                mob.playSound(ModSoundEvents.DROWNED_NECROMANCER_PREPARE_TRIDENT_STORM.get(), 3.0F, 1.0F);
            }

            if (target != null && mob.tridentStormAnimationTick <= 30 && mob.tridentStormAnimationTick >= 10) {
                TridentStormEntity tridentStorm = ModEntityTypes.TRIDENT_STORM.get().create(mob.level);
                tridentStorm.owner = mob;
                tridentStorm.moveTo(new BlockPos(mob.getX() - tridentSummonRange + mob.random.nextInt(tridentSummonRange * 2), mob.getY(), mob.getZ() - tridentSummonRange + mob.random.nextInt(tridentSummonRange * 2)), 0, 0);
                tridentStorm.setYRot(mob.random.nextInt(360));
                mob.level.addFreshEntity(tridentStorm);
                PositionUtils.moveToCorrectHeight(tridentStorm);
            }
        }

        @Override
        public void stop() {
            this.nextUseTime = mob.tickCount + (100 + mob.random.nextInt(300));
        }

        public boolean animationsUseable() {
            return mob.tridentStormAnimationTick <= 0;
        }

    }

}
