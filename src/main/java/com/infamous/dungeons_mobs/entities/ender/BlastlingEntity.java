package com.infamous.dungeons_mobs.entities.ender;

import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.goals.SimpleRangedAttackGoal;
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

// Ranged Enderman
public class BlastlingEntity extends EndermanEntity {
    public BlastlingEntity(EntityType<? extends BlastlingEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return EndermanEntity.createAttributes().add(Attributes.MAX_HEALTH, 60.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        ((GoalSelectorAccessor)this.goalSelector)
                .getAvailableGoals()
                .removeIf(pg -> pg.getPriority() == 2 && pg.getGoal() instanceof MeleeAttackGoal);
        this.goalSelector.addGoal(2,
                new SimpleRangedAttackGoal<>(
                        this,
                        item -> true,
                        BlastlingEntity::performBlastGooAttack,
                        1.0D,
                        60,
                        20.0F));
    }

    private static void performBlastGooAttack(MobEntity shooter, LivingEntity target){
        double xDifference = target.getX() - shooter.getX();
        double yDifference = target.getY(0.5D) - shooter.getY(0.5D);
        double zDifference = target.getZ() - shooter.getZ();
        float euclidDist = MathHelper.sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference);


        WraithFireballEntity wraithFireball = new WraithFireballEntity(shooter.level,
                shooter,
                0,
                0,
                0);
        wraithFireball.setPos(wraithFireball.getX(),
                shooter.getY(0.5D),
                wraithFireball.getZ());
        wraithFireball.shoot(xDifference, yDifference, zDifference, euclidDist, 0.0F);
        shooter.level.addFreshEntity(wraithFireball);
    }

}
