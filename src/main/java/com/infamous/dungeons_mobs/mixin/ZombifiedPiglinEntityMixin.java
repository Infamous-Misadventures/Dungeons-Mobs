package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import com.infamous.dungeons_mobs.goals.SmartZombieAttackGoal;
import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityMixin extends ZombieEntity implements ISmartCrossbowUser {
    private static final DataParameter<Boolean> DATA_IS_CROSSBOW_USER = EntityDataManager.defineId(ZombifiedPiglinEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_IS_CHARGING_CROSSBOW = EntityDataManager.defineId(ZombifiedPiglinEntity.class, DataSerializers.BOOLEAN);

    public ZombifiedPiglinEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/entity/ai/goal/Goal;)V"), method = "addBehaviourGoals")
    private void addCustomGoal(GoalSelector goalSelector, int priority, Goal originalGoal){
        if(goalSelector == this.goalSelector && priority == 2 && originalGoal instanceof ZombieAttackGoal){
            goalSelector.addGoal(priority, new SmartZombieAttackGoal(this, 1.0D, false));
            goalSelector.addGoal(priority, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
        } else{
            goalSelector.addGoal(priority, originalGoal);
        }
    }

    @Inject(at = @At("TAIL"), method = "populateDefaultEquipmentSlots", cancellable = true)
    private void spawnWeapon(DifficultyInstance p_180481_1_, CallbackInfo ci){
        this.setItemSlot(EquipmentSlotType.MAINHAND, this.createSpawnWeapon());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditional(CompoundNBT compoundNBT, CallbackInfo ci){
        this.readCrossbowUserNBT(compoundNBT);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeAdditional(CompoundNBT compoundNBT, CallbackInfo ci){
        this.writeCrossbowUserNBT(compoundNBT);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        this.setCrossbowUser(this.isHolding(item -> item instanceof CrossbowItem));
        return spawnData;
    }

    private ItemStack createSpawnWeapon() {
        ItemStack meleeWeapon = DungeonsGearCompat.isLoaded() ?
                new ItemStack (DungeonsGearCompat.getGoldAxe().get()) :
                new ItemStack(Items.GOLDEN_SWORD);
        return (double)this.random.nextFloat() < 0.5D ? new ItemStack(Items.CROSSBOW) : meleeWeapon;
    }

    @Override
    public boolean canFireProjectileWeapon(ShootableItem shootableItem) {
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
    public void shootCrossbowProjectile(LivingEntity target, ItemStack weapon, ProjectileEntity projectile, float inaccuracy) {
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
