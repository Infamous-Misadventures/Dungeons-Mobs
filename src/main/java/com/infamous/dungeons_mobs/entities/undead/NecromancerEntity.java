package com.infamous.dungeons_mobs.entities.undead;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.infamous.dungeons_libraries.attribute.AttributeRegistry;
import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_libraries.summon.SummonHelper;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.infamous.dungeons_mobs.entities.SpawnArmoredHelper.equipArmorSet;

public class NecromancerEntity extends SkeletonEntity implements IAnimatable, SpawnArmoredMob {

    public int shootAnimationTick;
    public int shootAnimationLength = 20;
    public int shootAnimationActionPoint = 7;
    public int summonAnimationTick;
    public int summonAnimationLength = 45;
    public int summonAnimationActionPoint1 = summonAnimationLength - 20;
    public int summonAnimationActionPoint2 = summonAnimationLength - 23;
    public int summonAnimationActionPoint3 = summonAnimationLength - 26;
    public int summonAnimationActionPoint4 = summonAnimationLength - 32;
    public int summonAnimationActionPoint5 = summonAnimationLength - 38;
    public int specialAnimationTick;
    public int specialAnimationLength = 48;
    AnimationFactory factory = new AnimationFactory(this);

    public NecromancerEntity(World worldIn) {
        super(ModEntityTypes.NECROMANCER.get(), worldIn);
    }

    public NecromancerEntity(EntityType<? extends NecromancerEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
        this.xpReward = 20;
        this.maxUpStep = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return SkeletonEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ARMOR, 5.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.4D).add(AttributeRegistry.SUMMON_CAP.get(), 4);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new NecromancerEntity.SummonGoal(this));
        this.goalSelector.addGoal(2, new ApproachTargetGoal(this, 10, 1.2D, true));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 5F, 1.2D, 1.6D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, IronGolemEntity.class, 5F, 1.2D, 1.6D));
        this.goalSelector.addGoal(4, new NecromancerEntity.ShootAttackGoal(this));
        this.goalSelector.addGoal(5, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }
    
    public boolean isSpellcasting() {
    	return this.shootAnimationTick > 0 || this.summonAnimationTick > 0;
    }

    @Override
    public void reassessWeaponGoal() {

    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity
                && ((LivingEntity) entityIn).getMobType() == CreatureAttribute.UNDEAD) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return 2.25F;
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NECROMANCER_STAFF.get()));
        equipArmorSet(ModItems.NECROMANCER_ARMOR, this);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance,
                                           SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn,
                                           @Nullable CompoundNBT compoundNBT) {
        livingEntityDataIn = super.finalizeSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn,
                compoundNBT);

        return livingEntityDataIn;
    }

    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.NECROMANCER_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.NECROMANCER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSoundEvents.NECROMANCER_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSoundEvents.NECROMANCER_STEP.get();
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.specialAnimationTick = specialAnimationLength;
        } else if (p_28844_ == 11) {
            this.shootAnimationTick = shootAnimationLength;
        } else if (p_28844_ == 9) {
            this.summonAnimationTick = summonAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();

        if (!this.level.isClientSide && this.getTarget() == null && this.random.nextInt(300) == 0) {
            this.specialAnimationTick = this.specialAnimationLength;
            this.level.broadcastEntityEvent(this, (byte) 4);
            this.playSound(ModSoundEvents.NECROMANCER_LAUGH.get(), this.getSoundVolume(), this.getVoicePitch());
        }

        if (!this.level.isClientSide && this.getTarget() != null && (this.random.nextInt(100) == 0 || this.getTarget().deathTime > 0)) {
            this.playSound(ModSoundEvents.NECROMANCER_LAUGH.get(), this.getSoundVolume(), this.getVoicePitch());
            this.ambientSoundTime = -this.getAmbientSoundInterval() / 2;
        }
    }

    public void tickDownAnimTimers() {
        if (this.specialAnimationTick > 0) {
            this.specialAnimationTick--;
        }

        if (this.shootAnimationTick > 0) {
            this.shootAnimationTick--;
        }

        if (this.summonAnimationTick > 0) {
            this.summonAnimationTick--;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.summonAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_summon", true));
        } else if (this.shootAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_shoot", true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_walk", true));
        } else {
            if (this.specialAnimationTick > 0) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_rare_idle", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_idle", true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.NECROMANCER_ARMOR;
    }

    class SummonGoal extends Goal {

        public NecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public int nextUseTime;

        public int mobSummonRange = 3;
        public int closeMobSummonRange = 1;

        public SummonGoal(NecromancerEntity mob) {
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

            return target != null && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) > 5 && animationsUseable()
                    && mob.canSee(target);
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.playSound(ModSoundEvents.NECROMANCER_PREPARE_SUMMON.get(), 1.0F, 1.0F);
            mob.summonAnimationTick = mob.summonAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 9);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null &&
                    (mob.summonAnimationTick == mob.summonAnimationActionPoint1 ||
                            mob.summonAnimationTick == mob.summonAnimationActionPoint2 ||
                            mob.summonAnimationTick == mob.summonAnimationActionPoint3 ||
                            (mob.summonAnimationTick == mob.summonAnimationActionPoint4 && mob.random.nextBoolean()) ||
                            (mob.summonAnimationTick == mob.summonAnimationActionPoint5 && mob.random.nextBoolean()))) {
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
                ((ServerWorld) mob.level).addFreshEntityWithPassengers(mobSummonSpot);
                PositionUtils.moveToCorrectHeight(mobSummonSpot);

                EntityType<?> entityType = getEntityType();

                MobEntity summonedMob = null;

                Entity entity = SummonHelper.summonEntity(mob, mobSummonSpot.blockPosition(), entityType);

                if(entity == null){
                    mobSummonSpot.remove();
                    return;
                }

                if (entity instanceof MobEntity) {
                    summonedMob = ((MobEntity) entity);
                }

                summonedMob.setTarget(target);
                summonedMob.finalizeSpawn(((ServerWorld) mob.level), mob.level.getCurrentDifficultyAt(summonPos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
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
            List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.NECROMANCER_MOB_SUMMONS.get();
            if (!necromancerMobSummons.isEmpty()) {
                Collections.shuffle(necromancerMobSummons);

                int randomIndex = mob.getRandom().nextInt(necromancerMobSummons.size());
                String randomMobID = necromancerMobSummons.get(randomIndex);
                entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
            }
            if (entityType == null) {
                entityType = EntityType.ZOMBIE;
            }
            return entityType;
        }

        @Override
        public void stop() {
            this.nextUseTime = mob.tickCount + (200 + mob.random.nextInt(200));
        }

        public boolean animationsUseable() {
            return mob.summonAnimationTick <= 0;
        }

        public boolean canSee(Entity entitySeeing, Entity p_70685_1_) {
            Vector3d vector3d = new Vector3d(entitySeeing.getX(), entitySeeing.getEyeY(), entitySeeing.getZ());
            Vector3d vector3d1 = new Vector3d(p_70685_1_.getX(), p_70685_1_.getEyeY(), p_70685_1_.getZ());
            if (p_70685_1_.level != entitySeeing.level || vector3d1.distanceToSqr(vector3d) > 128.0D * 128.0D)
                return false; //Forge Backport MC-209819
            return entitySeeing.level.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entitySeeing)).getType() == RayTraceResult.Type.MISS;
        }

    }

    class ShootAttackGoal extends Goal {
        public NecromancerEntity mob;
        @Nullable
        public LivingEntity target;

        public ShootAttackGoal(NecromancerEntity mob) {
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
            return target != null && mob.distanceTo(target) <= 12.5 && mob.distanceTo(target) > 5 && mob.canSee(target) && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !animationsUseable();
        }

        @Override
        public void start() {
            mob.shootAnimationTick = mob.shootAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 11);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null && mob.shootAnimationTick == mob.shootAnimationActionPoint) {
                Vector3d pos = PositionUtils.getOffsetPos(mob, 0.3, 1.5, 0.5, mob.yBodyRot);
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
            return mob.shootAnimationTick <= 0;
        }

    }

}
