package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.compat.DungeonsGearCompat;
import com.infamous.dungeons_mobs.entities.SpawnEquipmentHelper;
import com.infamous.dungeons_mobs.goals.SmartZombieAttackGoal;
import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinEntityMixin extends Zombie implements ISmartCrossbowUser, CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_CROSSBOW_USER = SynchedEntityData.defineId(ZombifiedPiglin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(ZombifiedPiglin.class, EntityDataSerializers.BOOLEAN);

    public ZombifiedPiglinEntityMixin(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"), method = "addBehaviourGoals")
    private void addCustomGoal(GoalSelector goalSelector, int priority, Goal originalGoal) {
        if (goalSelector == this.goalSelector && priority == 2 && originalGoal instanceof ZombieAttackGoal) {
            goalSelector.addGoal(priority, new SmartZombieAttackGoal(this, 1.0D, false));
            goalSelector.addGoal(priority, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
        } else {
            goalSelector.addGoal(priority, originalGoal);
        }
    }

    @Inject(at = @At("TAIL"), method = "populateDefaultEquipmentSlots", cancellable = true)
    private void spawnWeapon(RandomSource random, DifficultyInstance p_180481_1_, CallbackInfo ci) {
        SpawnEquipmentHelper.equipMainhand(this.createSpawnWeapon(), this);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditional(CompoundTag compoundNBT, CallbackInfo ci) {
        this.readCrossbowUserNBT(compoundNBT);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeAdditional(CompoundTag compoundNBT, CallbackInfo ci) {
        this.writeCrossbowUserNBT(compoundNBT);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_213386_1_, DifficultyInstance p_213386_2_, MobSpawnType p_213386_3_, @Nullable SpawnGroupData p_213386_4_, @Nullable CompoundTag p_213386_5_) {
        SpawnGroupData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        this.setCrossbowUser(this.isHolding(itemStack -> itemStack.getItem() instanceof CrossbowItem));
        return spawnData;
    }

    private ItemStack createSpawnWeapon() {
        ItemStack meleeWeapon = DungeonsGearCompat.isLoaded() ?
                new ItemStack(DungeonsGearCompat.getGoldAxe().get()) :
                new ItemStack(Items.GOLDEN_SWORD);
        return (double) this.random.nextFloat() < 0.5D ? new ItemStack(Items.CROSSBOW) : meleeWeapon;
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem shootableItem) {
        return shootableItem instanceof CrossbowItem;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.define(DATA_IS_CROSSBOW_USER, false);
    }

    @Override
    public boolean isCrossbowUser() {
        return this.entityData.get(DATA_IS_CROSSBOW_USER);
    }

    @Override
    public void setCrossbowUser(boolean crossbowUser) {
        this.entityData.set(DATA_IS_CROSSBOW_USER, crossbowUser);
    }

    @Override
    public boolean _isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean chargingCrossbow) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, chargingCrossbow);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity target, ItemStack weapon, Projectile projectile, float inaccuracy) {
        this.shootCrossbowProjectile(this, target, projectile, inaccuracy, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.performCrossbowAttack(this, 1.6F);
    }
}
