package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.goals.RangedWebAttackGoal;
import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin extends MonsterEntity implements IWebShooter {

    private static final DataParameter<Boolean> WEBSHOOTING = EntityDataManager.defineId(SpiderEntity.class, DataSerializers.BOOLEAN);
    private RangedWebAttackGoal<SpiderEntityMixin> rangedWebAttackGoal;
    private LeapAtTargetGoal leapAtTargetGoal;
    private MeleeAttackGoal meleeAttackGoal;

    protected SpiderEntityMixin(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals")
    private void registerGoals(CallbackInfo callbackInfo){
        ((GoalSelectorAccessor) this.goalSelector)
                .getAvailableGoals()
                .stream()
                .filter(pg -> pg.getPriority() == 4 && pg.getGoal() instanceof MeleeAttackGoal)
                .findFirst()
                .ifPresent(pg -> {
                    this.meleeAttackGoal = (MeleeAttackGoal) pg.getGoal();
                    //DungeonsMobs.LOGGER.debug("Found and stored melee attack goal for Spider {}", this);
                });
        ((GoalSelectorAccessor) this.goalSelector)
                .getAvailableGoals()
                .stream()
                .filter(pg -> pg.getPriority() == 3 && pg.getGoal() instanceof LeapAtTargetGoal)
                .findFirst()
                .ifPresent(pg -> {
                    this.leapAtTargetGoal = (LeapAtTargetGoal) pg.getGoal();
                    //DungeonsMobs.LOGGER.debug("Found and stored leap at target goal for Spider {}", this);
                });

        this.rangedWebAttackGoal = new RangedWebAttackGoal<>(this, 1.0D, 60, 20.0F);
    }

    @Inject(at = @At("RETURN"), method = "defineSynchedData")
    private void registerData(CallbackInfo callbackInfo){
        this.entityData.define(WEBSHOOTING, false);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if(DungeonsMobsConfig.COMMON.ENABLE_RANGED_SPIDERS.get()){
            this.reassessAttackGoals();
        }
    }

    /*
    We check for leapAtTargetGoal not being null on a case-by-case basis since
        we want compatibility with Spiders 2.0 which changes the LeapAtTargetGoal to
        a custom Goal that doesn't extend it
     */
    private void reassessAttackGoals() {
        LivingEntity target = this.getTarget();
        if(this.meleeAttackGoal != null
                && this.rangedWebAttackGoal != null
                && target != null){
            if(!this.isTargetTrapped(target)){
                //DungeonsMobs.LOGGER.debug("Changing Spider {} to ranged AI!", this);
                this.goalSelector.removeGoal(this.meleeAttackGoal);
                if(this.leapAtTargetGoal != null){
                    this.goalSelector.removeGoal(this.leapAtTargetGoal);
                }
                this.goalSelector.addGoal(4, this.rangedWebAttackGoal);
            } else{
                //DungeonsMobs.LOGGER.debug("Changing Spider {} to melee AI!", this);
                this.goalSelector.removeGoal(this.rangedWebAttackGoal);
                if(this.leapAtTargetGoal != null){
                    this.goalSelector.addGoal(3, this.leapAtTargetGoal);
                }
                this.goalSelector.addGoal(4, this.meleeAttackGoal);
            }
        }
    }

    @Override
    public void setWebShooting(boolean webShooting) {
        this.entityData.set(WEBSHOOTING, webShooting);
    }

    @Override
    public boolean isWebShooting() {
        return this.entityData.get(WEBSHOOTING);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.shootWeb(this, target);
    }
}
