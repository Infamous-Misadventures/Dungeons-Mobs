package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.allcustomentity.illagers.PowerfulRoyalGuardEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;

public class RampartCaptainEntity extends AbstractIllagerEntity implements IAnimatable {
    private boolean isJohnny;
    private int attackTimer;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> JUST_SPAWN = EntityDataManager.defineId(RampartCaptainEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(RampartCaptainEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(RampartCaptainEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_GOLD = EntityDataManager.defineId(RampartCaptainEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);


    public RampartCaptainEntity(World world){
        super(ModEntityTypes.RAMPART_CAPTAIN.get(), world);
    }

    public RampartCaptainEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
        setPatrolLeader(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(JUST_SPAWN, true);
        this.entityData.define(IS_DIAMOND, false);
        this.entityData.define(IS_GOLD, false);
        this.entityData.define(MELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.21D)
                .add(Attributes.ARMOR, 25.0D)
                .add(Attributes.MAX_HEALTH, 158.0D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.2D)
                .add(Attributes.ATTACK_KNOCKBACK, 5.5D);
    }


    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            this.setWeaponBasedOnMod();
        }
    }

    public boolean isMeleeAttacking() {
        return this.entityData.get(MELEEATTACKING);
    }

    public void setMeleeAttacking(boolean attacking) {
        this.entityData.set(MELEEATTACKING, attacking);
    }


    private void setWeaponBasedOnMod() {
        ItemStack mainhandWeapon = new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get()) ;

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.isDiamond()){
            compound.putBoolean("Diamond", true);
        }
        if (this.isGold()){
            compound.putBoolean("Gold", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Diamond", 99)) {
            this.setDiamond(compound.getBoolean("Diamond"));
        }
        if (compound.contains("Gold", 99)) {
            this.setGold(compound.getBoolean("Gold"));
        }
        if (compound.contains("Johnny", 99)) {
            this.isJohnny = compound.getBoolean("Johnny");
        }
    }

    public boolean isDiamond(){
        return this.entityData.get(IS_DIAMOND);
    }

    public boolean isGold(){
        return this.entityData.get(IS_GOLD);
    }

    public boolean Isjustspawn(){
        return this.entityData.get(JUST_SPAWN);
    }

    public void setSpawned(boolean r){
        this.entityData.set(JUST_SPAWN, r);
    }

    public void setDiamond(boolean isDiamond){
        this.entityData.set(IS_DIAMOND, isDiamond);
    }

    public void setGold(boolean IsGold){
        this.entityData.set(IS_GOLD, IsGold);
    }

    @Override
    public boolean canBeLeader() {
        return true;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        populateDefaultEquipmentSlots(difficultyIn);
        for(int i=0;i<10;++i){
            RampartCaptainEntity v = this;
            BlockPos vvf = v.blockPosition();
            int o =v.getRandom().nextInt(10);
            if (o==4){
                PowerfulRoyalGuardEntity vv = new PowerfulRoyalGuardEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.setCanJoinRaid(true);
                vv.finalizeSpawn(worldIn,difficultyIn,reason,spawnDataIn,dataTag);
                v.level.addFreshEntity(vv);
            }else if (o==0){
                ArmoredMountaineerEntity vv = new ArmoredMountaineerEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.setCanJoinRaid(true);
                vv.populateDefaultEquipmentSlots(difficultyIn);
                v.level.addFreshEntity(vv);
            }else if (o==1){
                RoyalGuardEntity vv = new RoyalGuardEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.populateDefaultEquipmentSlots(difficultyIn);
                vv.setCanJoinRaid(true);
                v.level.addFreshEntity(vv);
            }else if (o==2||o==3){
                PillagerEntity vv = new PillagerEntity(EntityType.PILLAGER,this.level);
                vv.setItemInHand(Hand.MAIN_HAND,new ItemStack(Items.CROSSBOW));
                vv.moveTo(vvf,0F,0F);
                vv.setCanJoinRaid(true);
                v.level.addFreshEntity(vv);
            }else {
                MountaineerEntity vv = new MountaineerEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.populateDefaultEquipmentSlots(difficultyIn);
                vv.setCanJoinRaid(true);
                v.level.addFreshEntity(vv);
            }
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = this.getWeaponBasedOnMod();
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = 5;

        boolean applyEnchant = this.random.nextFloat()-1 <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
            weaponEnchantmentMap.put(Enchantments.SHARPNESS, enchantmentLevel);
            EnchantmentHelper.setEnchantments(weaponEnchantmentMap, mainhandWeapon);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
    }

    private ItemStack getWeaponBasedOnMod() {
        ItemStack mainhandWeapon;
        mainhandWeapon = new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get());
        return mainhandWeapon;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (!this.level.isClientSide && this.attackID == 0) {
            this.attackID = MELEE_ATTACK;
        }
        return true;
    }


    private float getAttackKnockback() {
        return (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
    }

    public boolean checkSpawnObstruction(IWorldReader worldIn) {
        BlockPos golemPos = this.blockPosition();
        BlockPos posBeneathGolem = golemPos.below();
        BlockState blockstateBeneathGolem = worldIn.getBlockState(posBeneathGolem);
        if (!blockstateBeneathGolem.entityCanStandOn(worldIn, posBeneathGolem, this)) {
            return false;
        } else {
            for(int i = 1; i < 4; ++i) {
                BlockPos posAboveGolem = golemPos.above(i);
                BlockState blockstateAboveGolem = worldIn.getBlockState(posAboveGolem);
                if (!WorldEntitySpawner
                        .isValidEmptySpawnBlock(worldIn,
                                posAboveGolem,
                                blockstateAboveGolem,
                                blockstateAboveGolem.getFluidState(),
                                ModEntityTypes.ARMORED_VINDICATOR.get())) {
                    return false;
                }
            }

            return WorldEntitySpawner
                    .isValidEmptySpawnBlock(worldIn,
                            golemPos,
                            worldIn.getBlockState(golemPos),
                            Fluids.EMPTY.defaultFluidState(),
                            ModEntityTypes.ARMORED_VINDICATOR.get())
                    && worldIn.isUnobstructed(this);
        }
    }

    private void setAttackID(int id) {
        this.attackID = id;
        this.attackTimer = 0;
        this.level.broadcastEntityEvent(this, (byte)-id);
    }


    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.attackID = Math.abs(id);
            this.attackTimer = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER
                || entityIn instanceof AbstractRaiderEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (isMeleeAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack", false));
        }
        else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            }
            else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
        }
        return PlayState.CONTINUE;
    }

    class AttackGoal extends MeleeAttackGoal {
        private int maxAttackTimer = 20;
        private final double moveSpeed;
        private int delayCounter;
        private int attackTimer;

        public AttackGoal(CreatureEntity creatureEntity, double moveSpeed) {
            super(creatureEntity, moveSpeed, true);
            this.moveSpeed = moveSpeed;
        }

        @Override
        public boolean canUse() {
            return RampartCaptainEntity.this.getTarget() != null && RampartCaptainEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            RampartCaptainEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = RampartCaptainEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            RampartCaptainEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + RampartCaptainEntity.this.getRandom().nextInt(7);
                RampartCaptainEntity.this.getNavigation().moveTo(livingentity, (double)this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, RampartCaptainEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || RampartCaptainEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                RampartCaptainEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            RampartCaptainEntity.this.getNavigation().stop();
            if (RampartCaptainEntity.this.getTarget() == null) {
                RampartCaptainEntity.this.setAggressive(false);
            }
        }

        public AttackGoal setMaxAttackTick(int max) {
            this.maxAttackTimer = max;
            return this;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VINDICATOR_HURT;
    }

    class MeleeGoal extends Goal {
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return RampartCaptainEntity.this.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return attackTimer < 27;
        }


        @Override
        public void start() {
            setAttackID(MELEE_ATTACK);
            setMeleeAttacking(true);
        }

        @Override
        public void stop() {
            setAttackID(0);
            setMeleeAttacking(false);
            RampartCaptainEntity.this.attackTimer = 0;
        }

        @Override
        public void tick() {
            if(RampartCaptainEntity.this.getTarget() != null && RampartCaptainEntity.this.getTarget().isAlive()) {
                RampartCaptainEntity.this.getLookControl().setLookAt(RampartCaptainEntity.this.getTarget(), 30.0F, 30.0F);
                if (attackTimer == 15) {
                    float attackKnockback = RampartCaptainEntity.this.getAttackKnockback();
                    LivingEntity attackTarget = RampartCaptainEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(RampartCaptainEntity.this.yRot * ((float) Math.PI / 300F));
                    double ratioZ = (double) (-MathHelper.cos(RampartCaptainEntity.this.yRot * ((float) Math.PI / 300F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(RampartCaptainEntity.this), (float) RampartCaptainEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget,attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    RampartCaptainEntity.this.setDeltaMovement(RampartCaptainEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }

        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if(event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }
    public void aiStep() {
        super.aiStep();
        if (this.attackID != 0) {
            ++this.attackTimer;
        }
    }

    @Override
    public void die(DamageSource p_70645_1_) {
        super.die(p_70645_1_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MeleeGoal());
        this.goalSelector.addGoal(6, new AttackGoal(this, 1.35D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public JohnnyAttackGoal(RampartCaptainEntity p_i47345_1_) {
            super(p_i47345_1_, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean canUse() {
            return ((RampartCaptainEntity)this.mob).isJohnny && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }
    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

}
