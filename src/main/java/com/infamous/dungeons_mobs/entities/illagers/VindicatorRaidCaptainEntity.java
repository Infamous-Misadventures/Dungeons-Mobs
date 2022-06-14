package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
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
import net.minecraft.entity.monster.*;
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
import net.minecraft.util.text.ITextComponent;
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

public class VindicatorRaidCaptainEntity extends AbstractIllagerEntity implements IAnimatable {
    private boolean isJohnny;
    private int attackTimer;
    private int attackID;
    public static final byte MELEE_ATTACK = 1;
    private static final DataParameter<Boolean> JUST_SPAWN = EntityDataManager.defineId(VindicatorRaidCaptainEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(VindicatorRaidCaptainEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(VindicatorRaidCaptainEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_GOLD = EntityDataManager.defineId(VindicatorRaidCaptainEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        if (!this.isJohnny && p_200203_1_ != null && p_200203_1_.getString().equals("Johnny")) {
            this.isJohnny = true;
        }
    }

    public VindicatorRaidCaptainEntity(World world){
        super(ModEntityTypes.VINDICATOR_RAID_CAPTAIN.get(), world);
    }

    public VindicatorRaidCaptainEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
        setPatrolLeader(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(JUST_SPAWN, true);
        this.entityData.define(IS_DIAMOND, true);
        this.entityData.define(IS_GOLD, false);
        this.entityData.define(MELEEATTACKING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.21D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 18.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.2D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.5D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            if(this.isDiamond()){
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get()));
            }
            else{
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.GOLD_VINDICATOR_HELMET.get()));
            }

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
        if(ModList.get().isLoaded("dungeons_gear")){
            Item DOUBLE_AXE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "double_axe"));
            Item WHIRLWIND = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "whirlwind"));

            ItemStack doubleAxe = new ItemStack(DOUBLE_AXE);
            ItemStack whirlwind = new ItemStack(WHIRLWIND);
            ItemStack mainhandWeapon = this.isDiamond() ? whirlwind : doubleAxe;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
        else{
            ItemStack diamondAxe = new ItemStack(Items.DIAMOND_AXE);
            ItemStack ironAxe = new ItemStack(Items.IRON_AXE);
            ItemStack mainhandWeapon = this.isDiamond() ? diamondAxe : ironAxe;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
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
        /*
        for(int i=0;i<10;++i){
            VindicatorRaidCaptainEntity v = this;
            BlockPos vvf = v.blockPosition();
            int o =v.getRandom().nextInt(6);
            if (o==0){
                ArmoredVindicatorEntity vv = new ArmoredVindicatorEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.setCanJoinRaid(true);
                vv.finalizeSpawn(worldIn,difficultyIn,reason,spawnDataIn,dataTag);
                v.level.addFreshEntity(vv);
            }else if (o==1){
                RoyalGuardEntity vv = new RoyalGuardEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.finalizeSpawn(worldIn,difficultyIn,reason,spawnDataIn,dataTag);
                vv.setCanJoinRaid(true);
                v.level.addFreshEntity(vv);
            }else if (o==2||o==3){
                PillagerEntity vv = new PillagerEntity(EntityType.PILLAGER,this.level);
                vv.setItemInHand(Hand.MAIN_HAND,new ItemStack(Items.CROSSBOW));
                vv.moveTo(vvf,0F,0F);
                vv.setCanJoinRaid(true);
                v.level.addFreshEntity(vv);
            }else {
                DungeonsVindicatorEntity vv = new DungeonsVindicatorEntity(this.level);
                vv.moveTo(vvf,0F,0F);
                vv.finalizeSpawn(worldIn,difficultyIn,reason,spawnDataIn,dataTag);
                vv.setCanJoinRaid(true);
                v.level.addFreshEntity(vv);
            }
        }
        */
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = this.getWeaponBasedOnMod();
        ItemStack helmet = this.isDiamond() ? new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get()) : new ItemStack(ModItems.GOLD_VINDICATOR_HELMET.get());
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = 4;
        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL)) {
            enchantmentLevel = 4;
        }

        boolean applyEnchant = this.random.nextFloat()-1 <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
            Map<Enchantment, Integer> armorEnchantmentMap = Maps.newHashMap();
            weaponEnchantmentMap.put(Enchantments.SHARPNESS, enchantmentLevel);
            armorEnchantmentMap.put(Enchantments.ALL_DAMAGE_PROTECTION, enchantmentLevel);
            EnchantmentHelper.setEnchantments(weaponEnchantmentMap, mainhandWeapon);
            EnchantmentHelper.setEnchantments(armorEnchantmentMap, helmet);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        this.setItemSlot(EquipmentSlotType.HEAD, helmet);
    }

    private ItemStack getWeaponBasedOnMod() {
        ItemStack mainhandWeapon;
        if(ModList.get().isLoaded("dungeons_gear")){
            Item DOUBLE_AXE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "double_axe"));
            Item WHIRLWIND = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "whirlwind"));

            ItemStack doubleAxe = new ItemStack(DOUBLE_AXE);
            ItemStack whirlwind = new ItemStack(WHIRLWIND);
            ItemStack ironAxe = new ItemStack(Items.IRON_AXE);

            mainhandWeapon = whirlwind;
        }
        else{
            mainhandWeapon = new ItemStack(Items.DIAMOND_AXE);
        }
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
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack_run", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                event.getController().animationSpeed = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            } else {
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
            event.getController().animationSpeed = 1;
            if (!(this.getYHeadRot() <= 10 && this.getYHeadRot() >= -10) && !this.getLookControl().isHasWanted())
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
            else
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel_look_around", true));
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
            return VindicatorRaidCaptainEntity.this.getTarget() != null && VindicatorRaidCaptainEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            VindicatorRaidCaptainEntity.this.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = VindicatorRaidCaptainEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            VindicatorRaidCaptainEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + VindicatorRaidCaptainEntity.this.getRandom().nextInt(7);
                VindicatorRaidCaptainEntity.this.getNavigation().moveTo(livingentity, (double)this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, VindicatorRaidCaptainEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || VindicatorRaidCaptainEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                VindicatorRaidCaptainEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            VindicatorRaidCaptainEntity.this.getNavigation().stop();
            if (VindicatorRaidCaptainEntity.this.getTarget() == null) {
                VindicatorRaidCaptainEntity.this.setAggressive(false);
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
        public VindicatorRaidCaptainEntity v = VindicatorRaidCaptainEntity.this;
        public MeleeGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return v.getTarget() != null && attackID == MELEE_ATTACK;
        }

        @Override
        public boolean canContinueToUse() {
            //animation tick
            return v.attackTimer < 21 && (v.getTarget() != null && v.getTarget().isAlive());
        }

        @Override
        public void start() {
            v.setAttackID(MELEE_ATTACK);
            v.setMeleeAttacking(true);
        }

        @Override
        public void stop() {
            v.setAttackID(0);
            v.setMeleeAttacking(false);
            v.attackTimer = 0;
            if (v.getTarget() == null) {
                v.setAggressive(false);
            }
        }

        @Override
        public void tick() {
            if (v.getTarget() != null && v.getTarget().isAlive()) {
                v.getNavigation().moveTo(v.getTarget(), 2.3);
                v.getLookControl().setLookAt(v.getTarget(), 30.0F, 30.0F);
                if (v.attackTimer == 15 && v.distanceToSqr(v.getTarget()) <= 6.0D) {
                    float attackKnockback = v.getAttackKnockback();
                    LivingEntity attackTarget = v.getTarget();
                    double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 360F));
                    double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 360F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    v.setDeltaMovement(v.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
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
        this.goalSelector.addGoal(7, new PatrollerEntity.PatrolGoal<>(this,1.42,1.3));
        this.targetSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(6, new AttackGoal(this, 1.45D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.3D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));

        this.targetSelector.addGoal(2, new JohnnyAttackGoal(this));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public JohnnyAttackGoal(VindicatorRaidCaptainEntity p_i47345_1_) {
            super(p_i47345_1_, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean canUse() {
            return ((VindicatorRaidCaptainEntity)this.mob).isJohnny && super.canUse();
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
