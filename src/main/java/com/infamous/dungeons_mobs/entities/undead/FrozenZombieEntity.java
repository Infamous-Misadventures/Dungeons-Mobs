package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.goals.switchcombat.ThrowAndMeleeAttackGoal;
import com.infamous.dungeons_mobs.goals.switchcombat.SwitchCombatItemGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;

import java.util.Random;

public class FrozenZombieEntity extends ZombieEntity implements IRangedAttackMob {
    public FrozenZombieEntity(World worldIn) {
        super(worldIn);
    }

    public FrozenZombieEntity(EntityType<? extends FrozenZombieEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    public static boolean canFrozenZombieSpawn(EntityType<FrozenZombieEntity> entityType, IServerWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new FrozenZombieAttackGoal(this, 1.0D, 20, 15.0F, false));
        this.goalSelector.addGoal(3, new SwitchCombatItemGoal(this, 6.0D, 6.0D));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglinEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SNOWBALL));
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.level.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }
        super.aiStep();
    }

    @Override
    public boolean doHurtTarget(Entity targetEntity) {
        if (super.doHurtTarget(targetEntity)) {
            if (targetEntity instanceof LivingEntity) {
                int i = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    i = 4;
                } else if (this.level.getDifficulty() == Difficulty.HARD) {
                    i = 8;
                }

                if (i > 0) {
                    ((LivingEntity)targetEntity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        SnowballEntity snowballentity = new SnowballEntity(this.level, this);
        double adjustedEyeY = livingEntity.getEyeY() - 1.100000023841858D;
        double xDifference = livingEntity.getX() - this.getX();
        double yDifference = adjustedEyeY - snowballentity.getY();
        double zDifference = livingEntity.getZ() - this.getZ();
        float adjustedHorizontalDistance = MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference) * 0.2F;
        snowballentity.shoot(xDifference, yDifference + (double)adjustedHorizontalDistance, zDifference, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(snowballentity);
    }

    static class FrozenZombieAttackGoal extends ThrowAndMeleeAttackGoal {
        private final FrozenZombieEntity zombie;
        private int raiseArmTicks;

        FrozenZombieAttackGoal(FrozenZombieEntity zombieEntity, double speedAmplifier, int attackInterval, float maxDistance, boolean useLongMemory) {
            super(zombieEntity, speedAmplifier, attackInterval, maxDistance, useLongMemory);
            this.zombie = zombieEntity;
        }

        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }

        public void stop() {
            super.stop();
            this.zombie.setAggressive(false);
        }

        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
                this.zombie.setAggressive(true);
            } else {
                this.zombie.setAggressive(false);
            }

        }
    }

}
