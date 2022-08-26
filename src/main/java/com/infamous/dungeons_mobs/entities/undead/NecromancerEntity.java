package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.projectiles.GeoOrbEntity;
import com.infamous.dungeons_mobs.goals.magic.SummonMobsGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.items.NecromancerStaffItem;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.function.Predicate;

public class NecromancerEntity extends AbstractSkeletonEntity implements IMagicUser, IAnimatable {

    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(NecromancerEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> IS_SUMMONING = EntityDataManager.defineId(NecromancerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ATTACKING = EntityDataManager.defineId(NecromancerEntity.class, DataSerializers.BOOLEAN);
    public static final Predicate<Item> STAFF_PREDICATE = item -> item instanceof NecromancerStaffItem;
    private int magicUseTicks;
    public int cd;
    public int acd;
    private MagicType activeMagic = MagicType.NONE;

    public NecromancerEntity(World worldIn) {
        super(ModEntityTypes.NECROMANCER.get(), worldIn);
    }
    public NecromancerEntity(EntityType<? extends NecromancerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.MAX_HEALTH, 90.0D);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.cd > 0) {
            this.cd --;
        }
        if (this.acd > 0) {
            this.acd --;
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new UsingNecromancy());
        this.goalSelector.addGoal(3, new ShotOrb());
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new SummonMobsGoal(this));
        //this.goalSelector.addGoal(5, new SimpleRangedAttackGoal<>(this, STAFF_PREDICATE, NecromancerEntity::performRangedAttack, 1.25D, 20, 20.0F));
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
        data.addAnimationController(new AnimationController(this, "orb", 1, this::orb));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event){
        event.getController().animationSpeed = 1;
        if (this.entityData.get(IS_ATTACKING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.shoot", false));
        } else if (this.entityData.get(IS_SUMMONING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.attack", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.walk", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.idle", true));
        }
        return PlayState.CONTINUE;
    }
    private <P extends IAnimatable> PlayState orb(AnimationEvent<P> event){
        event.getController().animationSpeed = 1;
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer.orb", true));
        return PlayState.CONTINUE;
    }

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    class UsingNecromancy extends UsingMagicGoal<NecromancerEntity>{

        UsingNecromancy() {
            super(NecromancerEntity.this);
        }
    }


    // ABSTRACTSKELETONENTIY METHODS
    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NECROMANCER_STAFF.get()));
    }

    protected void populateDefaultEquipmentEnchantments(DifficultyInstance difficulty) {
        // NO-OP
    }

    @Override
    public void reassessWeaponGoal() {
        // NO-OP
    }

    private void performRangedAttack(LivingEntity shooter, LivingEntity target) {
        shooter.swing(ProjectileHelper.getWeaponHoldingHand(shooter, STAFF_PREDICATE));
        GeoOrbEntity laserOrb = new GeoOrbEntity(shooter.level, shooter, 0, 0, 0, 8f);
        float f = (float) MathHelper.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
        Vector3d viewVector = shooter.getViewVector(1.0F);
        //float euclidDist = MathHelper.sqrt(xAccel * xAccel + yAccel * yAccel + zAccel * zAccel) * 0.1f;

        double x = this.getX() + Math.cos(f) * 1.2;
        double z = this.getZ() + Math.sin(f) * 1.2;
        laserOrb.setPos(x, shooter.getY(0.5D) + 0.5D, z);
        double xAccel = target.getX() - laserOrb.getX();
        double yAccel = target.getY() - shooter.getY();
        double zAccel = target.getZ() - laserOrb.getZ();
        laserOrb.shoot(xAccel, yAccel, zAccel, 0.2f, 0.0F);
        shooter.level.addFreshEntity(laserOrb);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setCanPickUpLoot(false);
        // Clear the pumpkin head given to skeletons on Halloween
        boolean wearingHalloweenPumpkin = this.getItemBySlot(EquipmentSlotType.HEAD).getItem() == Blocks.JACK_O_LANTERN.asItem() || this.getItemBySlot(EquipmentSlotType.HEAD).getItem() == Blocks.CARVED_PUMPKIN.asItem();
        if (wearingHalloweenPumpkin) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31) {
                this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                //this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }

        return spawnDataIn;
    }


    // SOUND METHODS
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    // MODIFIED SPELLCASTINGILLAGER METHODS
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGIC, (byte)0);
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(IS_SUMMONING, false);
    }

    @Override
    public void tick() {
        super.tick();
        IMagicUser.spawnMagicParticles(this);
    }

    public void setIsSummoning(boolean isSummoning) {
        this.entityData.set(IS_SUMMONING, isSummoning);
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

    class ShotOrb extends Goal{

        public int timer;

        public NecromancerEntity v = NecromancerEntity.this;

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
                v.getLookControl().setLookAt(v.getTarget(),30,30);
            }
            if (this.timer == 14) {
                useMagic();
            }
        }

        protected void useMagic() {
            LivingEntity target = v.getTarget();
            if (target != null) {
                v.performRangedAttack(v ,target);
            }
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }
    }
}
