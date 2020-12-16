package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.List;

public class NecromancerEntity extends AbstractSkeletonEntity implements IMagicUser {

    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.createKey(WraithEntity.class, DataSerializers.BYTE);
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    public NecromancerEntity(World worldIn) {
        super(ModEntityTypes.NECROMANCER.get(), worldIn);
    }
    public NecromancerEntity(EntityType<? extends NecromancerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new UsingNecromancy());
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new NecromancerEntity.UseNecromancy());
        this.goalSelector.addGoal(5, new RangedAttackGoal(this, 1.25D, 20, 20.0F));
        //this.goalSelector.addGoal(5, new MagicAttackGoal<>(this, 1.0D, 6.0F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.TARGET_DRY_BABY));
    }

    @Override
    protected boolean isInDaylight() {
        return false; // TODO: Not the best solution to prevent Necromancers burning in daylight, but since this method is only used in AbstractSkeletonEntity#livingTick, it's fine for now
    }

    class UsingNecromancy extends UsingMagicGoal<NecromancerEntity>{

        UsingNecromancy() {
            super(NecromancerEntity.this);
        }
    }

    class UseNecromancy extends UseMagicGoal<NecromancerEntity>{
        private final EntityPredicate entityPredicate = (new EntityPredicate()).setDistance(16.0D).setLineOfSiteRequired().setUseInvisibilityCheck().allowInvulnerable().allowFriendlyFire();


        UseNecromancy() {
            super(NecromancerEntity.this);
        }

        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else {
                int i = NecromancerEntity.this.world.getTargettableEntitiesWithinAABB(ZombieEntity.class, this.entityPredicate, NecromancerEntity.this, NecromancerEntity.this.getBoundingBox().grow(16.0D)).size();
                return NecromancerEntity.this.rand.nextInt(16) + 1 > i;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity targetEntity = NecromancerEntity.this.getAttackTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = NecromancerEntity.this.canEntityBeSeen(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive()){
                return super.shouldContinueExecuting();
            } else {
                return false;
            }
        }

        @Override
        protected void useMagic() {
            LivingEntity targetEntity = NecromancerEntity.this.getAttackTarget();
            if (targetEntity != null) {
                summonUndead();
            }
        }

        private void summonUndead(){

            int difficultyAsInt = NecromancerEntity.this.world.getDifficulty().getId();
            int mobsToSummon = difficultyAsInt * 2;
            for(int i = 0; i < mobsToSummon; ++i) {
                BlockPos blockpos = NecromancerEntity.this.getPosition().add(-2 + NecromancerEntity.this.rand.nextInt(5), 1, -2 + NecromancerEntity.this.rand.nextInt(5));
                boolean summonedMobFromConfig = summonMobFromConfig(blockpos);
                if(!summonedMobFromConfig){
                    summonZombie(blockpos);
                }
            }
        }

        private boolean summonMobFromConfig(BlockPos blockpos) {
            List<String> necromancerMobSummons = (List<String>) DungeonsMobsConfig.COMMON.NECROMANCER_MOB_SUMMONS.get();
            if(necromancerMobSummons.isEmpty()) return false;
            Collections.shuffle(necromancerMobSummons);

            int randomIndex = NecromancerEntity.this.getRNG().nextInt(necromancerMobSummons.size());
            String randomMobID = necromancerMobSummons.get(randomIndex);
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(randomMobID));
            if(entityType == null) return false;

            Entity entity = entityType.create(NecromancerEntity.this.world);
            if(!(entity instanceof MobEntity)) return false;

            MobEntity mobEntity = (MobEntity)entity;
            DifficultyInstance difficultyForLocation = NecromancerEntity.this.world.getDifficultyForLocation(blockpos);
            mobEntity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            ModifiableAttributeInstance spawnReinforcementsAttribute = mobEntity.getAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS);
            if(spawnReinforcementsAttribute != null){
                spawnReinforcementsAttribute.setBaseValue(0);
            }
            mobEntity.onInitialSpawn(NecromancerEntity.this.world, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
            return NecromancerEntity.this.world.addEntity(mobEntity);
        }

        private void summonZombie(BlockPos blockpos){
            ZombieEntity zombieEntity = EntityType.ZOMBIE.create(NecromancerEntity.this.world);
            if (zombieEntity != null) {
                DifficultyInstance difficultyForLocation = NecromancerEntity.this.world.getDifficultyForLocation(blockpos);
                zombieEntity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                ModifiableAttributeInstance spawnReinforcementsAttribute = zombieEntity.getAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS);
                if(spawnReinforcementsAttribute != null){
                    spawnReinforcementsAttribute.setBaseValue(0);
                }
                zombieEntity.onInitialSpawn(NecromancerEntity.this.world, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                NecromancerEntity.this.world.addEntity(zombieEntity);
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
            return SoundEvents.ENTITY_WITHER_SPAWN;
        }

        @Override
        protected MagicType getMagicType() {
            return MagicType.SUMMON_UNDEAD;
        }
    }


    // ABSTRACTSKELETONENTIY METHODS
    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NECROMANCER_STAFF.get()));
    }

    protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {
        // NO-OP
    }

    @Override
    public void setCombatTask() {
        // NO-OP
    }

    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        NecromancerEntity.this.swingArm(Hand.MAIN_HAND);
        double squareDistanceToTarget = NecromancerEntity.this.getDistanceSq(target);
        double xDifference = target.getPosX() - NecromancerEntity.this.getPosX();
        double yDifference = target.getPosYHeight(0.5D) - NecromancerEntity.this.getPosYHeight(0.5D);
        double zDifference = target.getPosZ() - NecromancerEntity.this.getPosZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

        WraithFireballEntity wraithFireballEntity = new WraithFireballEntity(this.world, this, xDifference, yDifference, zDifference);
        wraithFireballEntity.setPosition(wraithFireballEntity.getPosX(), NecromancerEntity.this.getPosYHeight(0.5D) + 0.5D, wraithFireballEntity.getPosZ());
        NecromancerEntity.this.world.addEntity(wraithFireballEntity);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setCanPickUpLoot(false);
        // Clear the pumpkin head given to skeletons on Halloween
        boolean wearingHalloweenPumpkin = this.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == Blocks.JACK_O_LANTERN.asItem() || this.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == Blocks.CARVED_PUMPKIN.asItem();
        if (wearingHalloweenPumpkin) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31) {
                this.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                //this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }

        return spawnDataIn;
    }


    // SOUND METHODS
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    // MODIFIED SPELLCASTINGILLAGER METHODS
    protected void registerData() {
        super.registerData();
        this.dataManager.register(MAGIC, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        IMagicUser.spawnMagicParticles(this);

    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.magicUseTicks > 0) {
            --this.magicUseTicks;
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.magicUseTicks = compound.getInt("MagicUseTicks");
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("MagicUseTicks", this.magicUseTicks);
    }

    // IMAGICUSER METHODS
    @Override
    public boolean isUsingMagic() {
        if (this.world.isRemote) {
            return this.dataManager.get(MAGIC) > 0;
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
        return !this.world.isRemote ? this.activeMagic : MagicType.getFromId(this.dataManager.get(MAGIC));
    }

    @Override
    public void setMagicType(MagicType magicType) {
        this.activeMagic = magicType;
        this.dataManager.set(MAGIC, (byte)magicType.getId());
    }

    @Override
    public SoundEvent getMagicSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }
}
