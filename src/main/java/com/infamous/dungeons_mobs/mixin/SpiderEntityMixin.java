package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.goals.RangedWebAttackGoal;
import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin extends MonsterEntity implements IWebShooter {

    private static final DataParameter<Boolean> WEBSHOOTING = EntityDataManager.createKey(SpiderEntity.class, DataSerializers.BOOLEAN);

    protected SpiderEntityMixin(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Inject(at = @At("RETURN"), method = "registerGoals")
    private void registerGoals(CallbackInfo callbackInfo){
        this.goalSelector.addGoal(2, new RangedWebAttackGoal<>(this, 1.0D, 60, 20.0F));
    }

    @Inject(at = @At("RETURN"), method = "registerData")
    private void registerData(CallbackInfo callbackInfo){
        this.dataManager.register(WEBSHOOTING, false);
    }

    @Override
    public boolean shouldShootWeb() {
        return this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 9.0D;
    }

    @Override
    public boolean isTargetSlowedDown() {
        LivingEntity attackTarget = this.getAttackTarget();
        if(attackTarget != null){
            Vector3d motionMultiplier = ObfuscationReflectionHelper.getPrivateValue(Entity.class, attackTarget, "field_213328_B");
            if (motionMultiplier != null) {
                return motionMultiplier.x <= 0.25D && motionMultiplier.y <= 0.05D && motionMultiplier.z <= 0.25D;
            }
            else return false;
        }
        else return false;
    }

    @Override
    public void setWebShooting(boolean webShooting) {
        this.dataManager.set(WEBSHOOTING, webShooting);
    }

    @Override
    public boolean isWebShooting() {
        return this.dataManager.get(WEBSHOOTING);
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.shootWeb(this, target);
    }
}
