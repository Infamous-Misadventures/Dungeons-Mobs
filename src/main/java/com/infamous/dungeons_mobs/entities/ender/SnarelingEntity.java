package com.infamous.dungeons_mobs.entities.ender;

import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import com.infamous.dungeons_mobs.goals.SimpleRangedAttackGoal;
import com.infamous.dungeons_mobs.interfaces.ITrapsTarget;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// SPIDER-LIKE
public class SnarelingEntity extends EndermanEntity implements ITrapsTarget {

    private SimpleRangedAttackGoal<SnarelingEntity> rangedAttackGoal;
    private MeleeAttackGoal meleeAttackGoal;

    public SnarelingEntity(EntityType<? extends SnarelingEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return EndermanEntity.createAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        ((GoalSelectorAccessor) this.goalSelector)
                .getAvailableGoals()
                .stream()
                .filter(pg -> pg.getPriority() == 2 && pg.getGoal() instanceof MeleeAttackGoal)
                .findFirst()
                .ifPresent(pg -> {
                    this.meleeAttackGoal = (MeleeAttackGoal) pg.getGoal();
                    //DungeonsMobs.LOGGER.debug("Found and stored melee attack goal for Spider {}", this);
                });

        this.rangedAttackGoal = new SimpleRangedAttackGoal<>(
                this,
                item -> true,
                SnarelingEntity::performSnareGooAttack,
                1.0D,
                60,
                20.0F);

    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.reassessAttackGoals();
    }

    private void reassessAttackGoals() {
        LivingEntity target = this.getTarget();
        if(this.meleeAttackGoal != null
                && this.rangedAttackGoal != null
                && target != null){
            if(!this.isTargetTrapped(target)){
                this.goalSelector.removeGoal(this.meleeAttackGoal);
                this.goalSelector.addGoal(2, this.rangedAttackGoal);
            } else{
                this.goalSelector.removeGoal(this.rangedAttackGoal);
                this.goalSelector.addGoal(2, this.meleeAttackGoal);
            }
        }
    }

    private static void performSnareGooAttack(MobEntity shooter, LivingEntity target){
        double xDifference = target.getX() - shooter.getX();
        double yDifference = target.getY(0.5D) - shooter.getY(0.5D);
        double zDifference = target.getZ() - shooter.getZ();
        float euclidDist = MathHelper.sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference);

        CobwebProjectileEntity cobwebProjectileEntity = new CobwebProjectileEntity(shooter.level,
                shooter,
                0,
                0,
                0);
        cobwebProjectileEntity.setPos(cobwebProjectileEntity.getX(),
                shooter.getY(0.5D),
                cobwebProjectileEntity.getZ());
        cobwebProjectileEntity.shoot(xDifference, yDifference, zDifference, euclidDist, 0.0F);
        shooter.level.addFreshEntity(cobwebProjectileEntity);
    }
}
