package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.goals.SimpleRangedAttackGoal;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.items.NecromancerStaffItem;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.mod.ModItems;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class DrownedNecromancerEntity extends DrownedEntity implements IMagicUser {

    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(DrownedNecromancerEntity.class, DataSerializers.BYTE);
    public static final Predicate<Item> STAFF_PREDICATE = item -> item instanceof NecromancerStaffItem;
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    public DrownedNecromancerEntity(EntityType<? extends DrownedNecromancerEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes();
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
        this.goalSelector.addGoal(1, new DrownedNecromancerEntity.UsingNecromancy());
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.UseHydromancy());
        this.goalSelector.addGoal(4, new DrownedNecromancerEntity.UseNeptunesWrath());
        this.goalSelector.addGoal(5, new SimpleRangedAttackGoal<>(this, STAFF_PREDICATE, DrownedNecromancerEntity::performRangedAttack, 1.25D, 20, 20.0F));

    }

    private static void performRangedAttack(LivingEntity shooter, LivingEntity target) {
        shooter.swing(ProjectileHelper.getWeaponHoldingHand(shooter, item -> item instanceof NecromancerStaffItem));
        double xDifference = target.getX() - shooter.getX();
        double yDifference = target.getY(0.5D) - shooter.getY(0.5D);
        double zDifference = target.getZ() - shooter.getZ();

        WraithFireballEntity wraithFireballEntity = new WraithFireballEntity(shooter.level, shooter, xDifference, yDifference, zDifference);
        wraithFireballEntity.setPos(wraithFireballEntity.getX(), shooter.getY(0.5D) + 0.5D, wraithFireballEntity.getZ());
        shooter.level.addFreshEntity(wraithFireballEntity);
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        // remove the shell that drowned spawn with naturally
        if (this.getItemBySlot(EquipmentSlotType.OFFHAND).getItem() == Items.NAUTILUS_SHELL) {
            this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
        }
        return spawnData;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NECROMANCER_STAFF.get()));
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



    class UsingNecromancy extends UsingMagicGoal<DrownedNecromancerEntity> {

        UsingNecromancy() {
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
            BlockPos originalNecromancerPos = new BlockPos(
                    DrownedNecromancerEntity.this.getX() + 0.5D,
                    DrownedNecromancerEntity.this.getY() + 0.5D,
                    DrownedNecromancerEntity.this.getZ() + 0.5D );

            BlockPos originalTargetPosition = new BlockPos(
                    targetEntity.getX() + 0.5D,
                    targetEntity.getY() + 0.5D,
                    targetEntity.getZ() + 0.5D );

            List<BlockPos> possibleTargetPositions = new ArrayList<>();
            for(int i = 0; i < 9; i++){
                double xshift = 0;
                double zshift = 0;

                // positive x shift
                if(i == 1 || i == 2 || i == 8){
                    xshift = 2.0D;
                }
                // negative x shift
                if(i == 4 || i == 5 || i == 6){
                    xshift = -2.0D;
                }
                // positive z shift
                if(i == 2 || i == 3 || i == 4){
                    zshift = 2.0D;
                }
                // negative z shift
                if(i == 6 || i == 7 || i == 8){
                    zshift = -2.0D;
                }
                BlockPos targetBlockPos = pickBlockPosForAttack(originalTargetPosition, xshift, zshift);
                possibleTargetPositions.add(targetBlockPos);
            }

            World world = targetEntity.getCommandSenderWorld();
            BlockPos targetBlockPos = possibleTargetPositions.get(world.random.nextInt(possibleTargetPositions.size()));
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
            if (lightningboltentity != null) {
                lightningboltentity.moveTo(Vector3d.atBottomCenterOf(targetBlockPos));
                lightningboltentity.setCause(null); // Not ideal, but the method only takes in a ServerPlayerEntity
                world.addFreshEntity(lightningboltentity);
            }
        }

        private BlockPos pickBlockPosForAttack(BlockPos originalTargetPosition, double xshift, double zshift) {
            return new BlockPos(
                    originalTargetPosition.getX() + xshift,
                    originalTargetPosition.getY(),
                    originalTargetPosition.getZ() + zshift);
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
}