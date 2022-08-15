package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.capabilities.teamable.TeamableHelper;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import com.infamous.dungeons_mobs.goals.AquaticMoveHelperController;
import com.infamous.dungeons_mobs.goals.GoToBeachGoal;
import com.infamous.dungeons_mobs.goals.GoToWaterGoal;
import com.infamous.dungeons_mobs.goals.SwimUpGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.items.NecromancerStaffItem;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DrownedNecromancerEntity extends AbstractSkeletonEntity implements IMagicUser, IAnimatable , IAquaticMob {

    // Required to make use of IMagicUser
    private boolean searchingForLand;
    protected SwimmerPathNavigator waterNavigation;
    protected GroundPathNavigator groundNavigation;

    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(DrownedNecromancerEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> IS_SUMMONING = EntityDataManager.defineId(DrownedNecromancerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_SUMMONING_LIGHTNING = EntityDataManager.defineId(DrownedNecromancerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ATTACKING = EntityDataManager.defineId(DrownedNecromancerEntity.class, DataSerializers.BOOLEAN);
    public static final Predicate<Item> STAFF_PREDICATE = item -> item instanceof NecromancerStaffItem;
    private int magicUseTicks;
    public int cd;
    public int acd;
    public int lcd;
    private MagicType activeMagic = MagicType.NONE;

    public DrownedNecromancerEntity(World worldIn) {
        super(ModEntityTypes.DROWNED_NECROMANCER.get(), worldIn);
    }

    public DrownedNecromancerEntity(EntityType<? extends DrownedNecromancerEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
        this.moveControl = new AquaticMoveHelperController<>(this);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimmerPathNavigator(this, worldIn);
        this.groundNavigation = new GroundPathNavigator(this, worldIn);
        this.maxUpStep = 1.0F;
        this.moveControl = new AquaticMoveHelperController<>(this);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimmerPathNavigator(this, worldIn);
        this.groundNavigation = new GroundPathNavigator(this, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.MAX_HEALTH, 90.0D);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.cd > 0) {
            this.cd--;
        }
        if (this.acd > 0) {
            this.acd--;
        }
        if (this.lcd > 0) {
            this.lcd--;
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new UsingNecromancy());
        this.goalSelector.addGoal(3, new ShotOrb());
        this.goalSelector.addGoal(1, new GoToWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new GoToBeachGoal<>(this, 1.0D));
        this.goalSelector.addGoal(6, new SwimUpGoal<>(this, 1.0D, this.level.getSeaLevel()));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.UseNecromancy());
        this.goalSelector.addGoal(3, new DrownedNecromancerEntity.UseNeptunesWrath());
        //this.goalSelector.addGoal(5, new SimpleRangedAttackGoal<>(this, STAFF_PREDICATE, DrownedNecromancerEntity::performRangedAttack, 1.25D, 20, 20.0F));
        //this.goalSelector.addGoal(5, new MagicAttackGoal<>(this, 1.0D, 6.0F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));
    }

    @Override
    protected boolean isSunBurnTick() {
        return false; // TODO: Not the best solution to prevent Necromancers burning in daylight, but since this method is only used in AbstractSkeletonEntity#livingTick, it's fine for now
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
        //data.addAnimationController(new AnimationController(this, "orb", 1, this::orb));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().animationSpeed = 1;
        if (this.entityData.get(IS_SUMMONING_LIGHTNING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.drowned_necromancer.lightning summon", false));
        } else if (this.entityData.get(IS_ATTACKING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.shoot", false));
        } else if (this.entityData.get(IS_SUMMONING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.attack", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.drowned_necromancer.walking", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.drowned_necromancer.idle", true));
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState orb(AnimationEvent<P> event) {
        event.getController().animationSpeed = 1;
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.orb", true));
        return PlayState.CONTINUE;
    }

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    class UsingNecromancy extends UsingMagicGoal<DrownedNecromancerEntity> {

        UsingNecromancy() {
            super(DrownedNecromancerEntity.this);
        }
    }
    @Override
    public void setSearchingForLand(boolean searchingForLand) {
        this.searchingForLand = searchingForLand;
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader worldReader) {
        return worldReader.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    @Override
    public void updateSwimming() {
        this.updateNavigation(this);
    }

    @Override
    public void travel(Vector3d travelVec) {
        this.checkAquaticTravel(this, travelVec);
    }

    @Override
    public void normalTravel(Vector3d travelVec) {
        super.travel(travelVec);
    }

    @Override
    public boolean isSearchingForLand() {
        return this.searchingForLand;
    }

    @Override
    public void setNavigation(PathNavigator navigation) {
        this.navigation = navigation;
    }

    @Override
    public GroundPathNavigator getGroundNavigation() {
        return this.groundNavigation;
    }

    @Override
    public SwimmerPathNavigator getWaterNavigation() {
        return this.waterNavigation;
    }

    class UseNecromancy extends Goal {
        private final EntityPredicate entityPredicate = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

        public int timer;

        public DrownedNecromancerEntity v = DrownedNecromancerEntity.this;

        UseNecromancy() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null && v.cd <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 35;
        }

        @Override
        public void start() {
            super.start();
            this.timer = 0;
            v.entityData.set(IS_SUMMONING, true);
        }

        @Override
        public void stop() {
            v.cd = 168;
            v.entityData.set(IS_SUMMONING, false);
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void tick() {
            super.tick();
            this.timer++;
            v.getNavigation().stop();
            if (v.getTarget() != null) {
                v.getLookControl().setLookAt(v.getTarget(), 30, 30);
            }
            if (this.timer == 16) {
                useMagic();
            }
        }

        protected void useMagic() {
            {
                summonUndead();
            }
        }

        private void summonUndead() {

            int difficultyAsInt = DrownedNecromancerEntity.this.level.getDifficulty().getId();
            for (int i = 0; i < difficultyAsInt; ++ i) {
                BlockPos blockpos = DrownedNecromancerEntity.this.blockPosition().offset(-2 + DrownedNecromancerEntity.this.random.nextInt(5), 1, -2 + DrownedNecromancerEntity.this.random.nextInt(5));
                boolean summonedMobFromConfig = summonMobFromConfig(blockpos);
                if (!summonedMobFromConfig) {
                    summonedDrowned(blockpos);
                }
            }
        }

        private boolean summonMobFromConfig(BlockPos blockpos) {
            List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_MOB_SUMMONS.get();
            if (necromancerMobSummons.isEmpty()) return false;
            Collections.shuffle(necromancerMobSummons);

            int randomIndex = DrownedNecromancerEntity.this.getRandom().nextInt(necromancerMobSummons.size());
            String randomMobID = necromancerMobSummons.get(randomIndex);
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
            if (entityType == null) return false;

            Entity entity = entityType.create(DrownedNecromancerEntity.this.level);
            if (!(entity instanceof MobEntity)) return false;

            MobEntity mobEntity = (MobEntity) entity;
            DifficultyInstance difficultyForLocation = DrownedNecromancerEntity.this.level.getCurrentDifficultyAt(blockpos);
            mobEntity.moveTo(blockpos, 0.0F, 0.0F);
            ModifiableAttributeInstance spawnReinforcementsAttribute = mobEntity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
            if (spawnReinforcementsAttribute != null) {
                spawnReinforcementsAttribute.setBaseValue(0);
            }
            mobEntity.finalizeSpawn((IServerWorld) DrownedNecromancerEntity.this.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            TeamableHelper.makeTeammates(mobEntity, DrownedNecromancerEntity.this);
            return DrownedNecromancerEntity.this.level.addFreshEntity(mobEntity);
        }

        private void summonedDrowned(BlockPos blockpos) {
            DrownedEntity drownedEntity = EntityType.DROWNED.create(DrownedNecromancerEntity.this.level);
            if (drownedEntity != null) {
                DifficultyInstance difficultyForLocation = DrownedNecromancerEntity.this.level.getCurrentDifficultyAt(blockpos);
                drownedEntity.moveTo(blockpos, 0.0F, 0.0F);
                ModifiableAttributeInstance spawnReinforcementsAttribute = drownedEntity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
                if (spawnReinforcementsAttribute != null) {
                    spawnReinforcementsAttribute.setBaseValue(0);
                }
                drownedEntity.finalizeSpawn((IServerWorld) DrownedNecromancerEntity.this.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                TeamableHelper.makeTeammates(drownedEntity, DrownedNecromancerEntity.this);
                DrownedNecromancerEntity.this.level.addFreshEntity(drownedEntity);
            }
        }

    }


    // ABSTRACTSKELETONENTIY METHODS

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NECROMANCER_TRIDENT.get()));
    }

    protected void populateDefaultEquipmentEnchantments(DifficultyInstance difficulty) {
        // NO-OP
    }

    private void performRangedAttack(LivingEntity shooter, LivingEntity target) {
    }

    // SOUND METHODS
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DROWNED_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.DROWNED_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.DROWNED_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.DROWNED_STEP;
    }

    // MODIFIED SPELLCASTINGILLAGER METHODS
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGIC, (byte) 0);
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(IS_SUMMONING, false);
        this.entityData.define(IS_SUMMONING_LIGHTNING, false);
    }

    @Override
    public void tick() {
        super.tick();
        IMagicUser.spawnMagicParticles(this);

    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.magicUseTicks > 0) {
            --this.magicUseTicks;
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.magicUseTicks = compound.getInt("MagicUseTicks");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MagicUseTicks", this.magicUseTicks);
    }

    // IMAGICUSER METHODS
    @Override
    public boolean isUsingMagic() {
        if (this.level.isClientSide) {
            return this.entityData.get(MAGIC) > 0;
        } else {
            return this.magicUseTicks > 0;
        }
    }

    @Override
    public int getMagicUseTicks() {
        return this.magicUseTicks;
    }

    @Override
    public void setMagicUseTicks(int magicUseTicksIn) {
        this.magicUseTicks = magicUseTicksIn;
    }

    @Override
    public MagicType getMagicType() {
        return !this.level.isClientSide ? this.activeMagic : MagicType.getFromId(this.entityData.get(MAGIC));
    }

    @Override
    public void setMagicType(MagicType magicType) {
        this.activeMagic = magicType;
        this.entityData.set(MAGIC, (byte) magicType.getId());
    }

    @Override
    public SoundEvent getMagicSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    class ShotOrb extends Goal {

        public int timer;

        public DrownedNecromancerEntity v = DrownedNecromancerEntity.this;

        ShotOrb() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null && v.acd <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 25;
        }

        @Override
        public void start() {
            super.start();
            this.timer = 0;
            v.entityData.set(IS_ATTACKING, true);
        }

        @Override
        public void stop() {
            v.acd = 5;
            v.entityData.set(IS_ATTACKING, false);
        }

        @Override
        public void tick() {
            this.timer++;

            v.getNavigation().stop();
            if (v.getTarget() != null) {
                v.getLookControl().setLookAt(v.getTarget(), 30, 30);
            }
            if (this.timer >= 14 && this.timer <=  18) {
                for (int i = 0; i < 2 + v.getRandom().nextInt(2); i++) {
                    useMagic();
                }
            }
        }

        protected void useMagic() {
            LivingEntity target = v.getTarget();
            if (target != null) {
                {
                    NecromancerOrbEntity vv = new NecromancerOrbEntity(v.level, v, v.getTarget());
                    double d2 = 1.25D;
                    float f = (float) MathHelper.atan2(v.getTarget().getZ() - v.getZ(), v.getTarget().getX() - v.getX());
                    double x = v.getX() + Math.cos(f) * d2;
                    double z = v.getZ() + Math.sin(f) * d2;
                    BlockPos blockpos = new BlockPos(x + ((v.getRandom().nextDouble() / 2) * (v.getRandom().nextBoolean() ? -1 : 1)), v.getY(1.22- ((v.getRandom().nextDouble() / 2) * (v.getRandom().nextBoolean() ? -1 : 1))), z+ ((v.getRandom().nextDouble() / 2) * (v.getRandom().nextBoolean() ? -1 : 1)));
                    vv.setYBodyRot(v.getYHeadRot());
                    vv.moveTo(blockpos, 0, 0);
                    v.level.addFreshEntity(vv);
                }
            }
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }
    }

    class UseNeptunesWrath extends Goal {

        public int timer;

        @Override
        public boolean isInterruptable() {
            return false;
        }

        public DrownedNecromancerEntity v = DrownedNecromancerEntity.this;

        UseNeptunesWrath() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null && v.lcd <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 45;
        }

        @Override
        public void start() {
            super.start();
            this.timer = 0;
            v.entityData.set(IS_SUMMONING_LIGHTNING, true);
        }

        @Override
        public void stop() {
            v.lcd = 325;
            v.entityData.set(IS_SUMMONING_LIGHTNING, false);
        }

        @Override
        public void tick() {
            this.timer++;

            v.getNavigation().stop();
            if (v.getTarget() != null) {
                v.getLookControl().setLookAt(v.getTarget(), 30, 30);
            }
            if (this.timer == 30) {
                useMagic();
            }
        }

        protected void useMagic() {
            LivingEntity targetEntity = DrownedNecromancerEntity.this.getTarget();
            if (targetEntity != null) {
                this.summonLightning(targetEntity);
            }
        }

        private void summonLightning(LivingEntity targetEntity) {
            float atan2 = (float) MathHelper.atan2(targetEntity.getZ() - DrownedNecromancerEntity.this.getZ(), targetEntity.getX() - DrownedNecromancerEntity.this.getX());
            double minY = Math.min(targetEntity.getY(), DrownedNecromancerEntity.this.getY());
            double maxY = Math.max(targetEntity.getY(), DrownedNecromancerEntity.this.getY()) + 1.0D;

            // We summon 6  lightning bolts around the summoner
            for (int boltCounter = 0; boltCounter < 6; ++boltCounter) {
                float randomShift = atan2 + (float) boltCounter * (float) Math.PI * 1.0667F;
                this.createSpellEntity(DrownedNecromancerEntity.this.getX() + (double) MathHelper.cos(randomShift) * 2.5D, DrownedNecromancerEntity.this.getZ() + (double) MathHelper.sin(randomShift) * 2.5D, minY, maxY);
            }
        }

        private void createSpellEntity(double x, double z, double minY, double maxY) {
            BlockPos blockpos = new BlockPos(x, maxY, z);
            boolean doSummon = false;
            double yShift = 0.0D;

            World level = DrownedNecromancerEntity.this.level;
            do {
                BlockPos below = blockpos.below();
                BlockState stateAtBelowPos = level.getBlockState(below);
                if (stateAtBelowPos.isFaceSturdy(level, below, Direction.UP)) {
                    if (!level.isEmptyBlock(blockpos)) {
                        BlockState stateAtPos = level.getBlockState(blockpos);
                        VoxelShape shapeAtPos = stateAtPos.getCollisionShape(level, blockpos);
                        if (!shapeAtPos.isEmpty()) {
                            yShift = shapeAtPos.max(Direction.Axis.Y);
                        }
                    }

                    doSummon = true;
                    break;
                }

                blockpos = blockpos.below();
            } while (blockpos.getY() >= MathHelper.floor(minY) - 1);

            if (doSummon) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(level);
                if (lightningboltentity != null) {
                    Vector3d targetPosVec = new Vector3d(x, (double) blockpos.getY() + yShift, z);
                    lightningboltentity.moveTo(targetPosVec);
                    lightningboltentity.setCause(null); // Not ideal, but the method only takes in a ServerPlayerEntity
                    level.addFreshEntity(lightningboltentity);
                }
            }

        }
    }
}
