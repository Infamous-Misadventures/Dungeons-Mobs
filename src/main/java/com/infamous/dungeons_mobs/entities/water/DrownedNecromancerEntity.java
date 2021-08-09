package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.capabilities.teamable.TeamableHelper;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.projectiles.TridentFumeEntity;
import com.infamous.dungeons_mobs.goals.SimpleRangedAttackGoal;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.items.NecromancerTridentItem;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class DrownedNecromancerEntity extends DrownedEntity implements IMagicUser {

    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(DrownedNecromancerEntity.class, DataSerializers.BYTE);
    public static final Predicate<Item> TRIDENT_STAFF_PREDICATE = item -> item instanceof NecromancerTridentItem;
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    public DrownedNecromancerEntity(EntityType<? extends DrownedNecromancerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected boolean isSunBurnTick() {
        return false; // TODO: Not the best solution to prevent Drowned Necromancers burning in daylight, but since this method is only used in AbstractSkeletonEntity#livingTick, it's fine for now
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes().add(Attributes.MAX_HEALTH, 40.0D);
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        ((GoalSelectorAccessor)this.goalSelector)
                .getAvailableGoals()
                .removeIf(
                        pg -> pg.getPriority() == 2
                                && (pg.getGoal() instanceof ZombieAttackGoal || pg.getGoal() instanceof RangedAttackGoal)
                );
        this.goalSelector.addGoal(1, new DrownedNecromancerEntity.UsingOceanMagic());
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.UseHydromancy());
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.UseNeptunesWrath());
        this.goalSelector.addGoal(5, new SimpleRangedAttackGoal<>(this, TRIDENT_STAFF_PREDICATE, DrownedNecromancerEntity::performRangedAttack, 1.25D, 20, 20.0F));

    }

    private static void performRangedAttack(LivingEntity shooter, LivingEntity target) {
        shooter.swing(ProjectileHelper.getWeaponHoldingHand(shooter, TRIDENT_STAFF_PREDICATE));
        double scale = 1.0D;
        Vector3d viewVector = shooter.getViewVector(1.0F);
        double xAccel = target.getX() - (shooter.getX() + viewVector.x * scale);
        double yAccel = target.getY(0.5D) - (0.5D + shooter.getY(0.5D));
        double zAccel = target.getZ() - (shooter.getZ() + viewVector.z * scale);
        float euclidDist = MathHelper.sqrt(xAccel * xAccel + yAccel * yAccel + zAccel * zAccel);

        TridentFumeEntity tridentFumeEntity = new TridentFumeEntity(shooter.level, shooter, xAccel, yAccel, zAccel);
        tridentFumeEntity.setPos(shooter.getX() + viewVector.x * scale, shooter.getY(0.5D) + 0.5D, tridentFumeEntity.getZ() + viewVector.z * scale);
        tridentFumeEntity.shoot(xAccel, yAccel, zAccel, euclidDist, 0.0F);
        shooter.level.addFreshEntity(tridentFumeEntity);
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        // remove the shell that drowned spawn with naturally
        if (this.getItemBySlot(EquipmentSlotType.OFFHAND).getItem() == Items.NAUTILUS_SHELL) {
            this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
        }
        this.setBaby(false);
        if(this.getVehicle() != null){
            this.stopRiding();
        }
        return spawnData;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NECROMANCER_TRIDENT.get()));
    }

    protected void populateDefaultEquipmentEnchantments(DifficultyInstance difficulty) {
        // NO-OP
    }

    // MODIFIED SPELLCASTINGILLAGER METHODS
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGIC, (byte)0);
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
        this.entityData.set(MAGIC, (byte)magicType.getId());
    }

    @Override
    public SoundEvent getMagicSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }



    class UsingOceanMagic extends UsingMagicGoal<DrownedNecromancerEntity> {

        UsingOceanMagic() {
            super(DrownedNecromancerEntity.this);
        }
    }

    class UseHydromancy extends UseMagicGoal<DrownedNecromancerEntity> {
        private final EntityPredicate entityPredicate = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();


        UseHydromancy() {
            super(DrownedNecromancerEntity.this);
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int i = DrownedNecromancerEntity.this.level.getNearbyEntities(DrownedEntity.class, this.entityPredicate, DrownedNecromancerEntity.this, DrownedNecromancerEntity.this.getBoundingBox().inflate(16.0D)).size();
                return DrownedNecromancerEntity.this.random.nextInt(16) + 1 > i;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity targetEntity = DrownedNecromancerEntity.this.getTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = DrownedNecromancerEntity.this.canSee(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive()){
                return super.canContinueToUse();
            } else {
                return false;
            }
        }

        @Override
        protected void useMagic() {
            LivingEntity targetEntity = DrownedNecromancerEntity.this.getTarget();
            if (targetEntity != null) {
                summonUndead();
            }
        }

        private void summonUndead(){

            int difficultyAsInt = DrownedNecromancerEntity.this.level.getDifficulty().getId();
            int mobsToSummon = difficultyAsInt * 2;
            for(int i = 0; i < mobsToSummon; ++i) {
                BlockPos blockpos = DrownedNecromancerEntity.this.blockPosition().offset(-2 + DrownedNecromancerEntity.this.random.nextInt(5), 1, -2 + DrownedNecromancerEntity.this.random.nextInt(5));
                boolean summonedMobFromConfig = summonMobFromConfig(blockpos);
                if(!summonedMobFromConfig){
                    summonedDrowned(blockpos);
                }
            }
        }

        private boolean summonMobFromConfig(BlockPos blockpos) {
            List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_MOB_SUMMONS.get();
            if(necromancerMobSummons.isEmpty()) return false;
            Collections.shuffle(necromancerMobSummons);

            int randomIndex = DrownedNecromancerEntity.this.getRandom().nextInt(necromancerMobSummons.size());
            String randomMobID = necromancerMobSummons.get(randomIndex);
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
            if(entityType == null) return false;

            Entity entity = entityType.create(DrownedNecromancerEntity.this.level);
            if(!(entity instanceof MobEntity)) return false;

            MobEntity mobEntity = (MobEntity)entity;
            DifficultyInstance difficultyForLocation = DrownedNecromancerEntity.this.level.getCurrentDifficultyAt(blockpos);
            mobEntity.moveTo(blockpos, 0.0F, 0.0F);
            ModifiableAttributeInstance spawnReinforcementsAttribute = mobEntity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
            if(spawnReinforcementsAttribute != null){
                spawnReinforcementsAttribute.setBaseValue(0);
            }
            mobEntity.finalizeSpawn((IServerWorld) DrownedNecromancerEntity.this.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
            TeamableHelper.makeTeammates(mobEntity, DrownedNecromancerEntity.this);
            return DrownedNecromancerEntity.this.level.addFreshEntity(mobEntity);
        }

        private void summonedDrowned(BlockPos blockpos){
            DrownedEntity drownedEntity = EntityType.DROWNED.create(DrownedNecromancerEntity.this.level);
            if (drownedEntity != null) {
                DifficultyInstance difficultyForLocation = DrownedNecromancerEntity.this.level.getCurrentDifficultyAt(blockpos);
                drownedEntity.moveTo(blockpos, 0.0F, 0.0F);
                ModifiableAttributeInstance spawnReinforcementsAttribute = drownedEntity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
                if(spawnReinforcementsAttribute != null){
                    spawnReinforcementsAttribute.setBaseValue(0);
                }
                drownedEntity.finalizeSpawn((IServerWorld) DrownedNecromancerEntity.this.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                TeamableHelper.makeTeammates(drownedEntity, DrownedNecromancerEntity.this);
                DrownedNecromancerEntity.this.level.addFreshEntity(drownedEntity);
            }
        }

        @Override
        protected int getMagicUseTime() {
            return 100;
        }

        @Override
        protected int getMagicUseInterval() {
            return 340;
        }

        @Nullable
        @Override
        protected SoundEvent getMagicPrepareSound() {
            return SoundEvents.WITHER_SPAWN;
        }

        @Override
        protected MagicType getMagicType() {
            return MagicType.SUMMON_UNDEAD;
        }
    }

    class UseNeptunesWrath extends UseMagicGoal<DrownedNecromancerEntity> {

        UseNeptunesWrath() {
            super(DrownedNecromancerEntity.this);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = DrownedNecromancerEntity.this.getTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = DrownedNecromancerEntity.this.canSee(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive() && DrownedNecromancerEntity.this.distanceTo(targetEntity) < 16.0D) {
                return super.canUse();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity targetEntity = DrownedNecromancerEntity.this.getTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = DrownedNecromancerEntity.this.canSee(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive()){
                return super.canContinueToUse();
            } else {
                return false;
            }
        }

        @Override
        protected void useMagic() {
            LivingEntity targetEntity = DrownedNecromancerEntity.this.getTarget();
            if (targetEntity != null) {
                this.summonLightning(targetEntity);
            }
        }

        private void summonLightning(LivingEntity targetEntity) {
            float atan2 = (float)MathHelper.atan2(targetEntity.getZ() - DrownedNecromancerEntity.this.getZ(), targetEntity.getX() - DrownedNecromancerEntity.this.getX());
            double minY = Math.min(targetEntity.getY(), DrownedNecromancerEntity.this.getY());
            double maxY = Math.max(targetEntity.getY(), DrownedNecromancerEntity.this.getY()) + 1.0D;

            // We summon 6  lightning bolts around the summoner
            for(int boltCounter = 0; boltCounter < 5; ++boltCounter) {
                float randomShift = atan2 + (float)boltCounter * (float)Math.PI * 0.4F;
                this.createSpellEntity(DrownedNecromancerEntity.this.getX() + (double) MathHelper.cos(randomShift) * 1.5D, DrownedNecromancerEntity.this.getZ() + (double)MathHelper.sin(randomShift) * 1.5D, minY, maxY);
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
            } while(blockpos.getY() >= MathHelper.floor(minY) - 1);

            if (doSummon) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(level);
                if (lightningboltentity != null) {
                    Vector3d targetPosVec = new Vector3d(x, (double)blockpos.getY() + yShift, z);
                    lightningboltentity.moveTo(targetPosVec);
                    lightningboltentity.setCause(null); // Not ideal, but the method only takes in a ServerPlayerEntity
                    level.addFreshEntity(lightningboltentity);
                }
            }

        }

        @Override
        protected int getMagicUseTime() {
            return 100;
        }

        @Override
        protected int getMagicUseInterval() {
            return 340;
        }

        @Nullable
        @Override
        protected SoundEvent getMagicPrepareSound() {
            return SoundEvents.LIGHTNING_BOLT_THUNDER;
        }

        @Override
        protected MagicType getMagicType() {
            return MagicType.SUMMON_LIGHTNING;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if(damageSource == DamageSource.LIGHTNING_BOLT && this.getMagicType() == MagicType.SUMMON_LIGHTNING){
            return false;
        }
        return super.hurt(damageSource, amount);
    }
}
