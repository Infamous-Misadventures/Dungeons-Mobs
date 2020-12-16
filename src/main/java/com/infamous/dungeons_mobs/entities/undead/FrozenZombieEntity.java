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
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class FrozenZombieEntity extends ZombieEntity implements IRangedAttackMob {
    public FrozenZombieEntity(World worldIn) {
        super(worldIn);
    }

    public FrozenZombieEntity(EntityType<? extends FrozenZombieEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    public static boolean canFrozenZombieSpawn(EntityType<FrozenZombieEntity> entityType, IWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return canMonsterSpawnInLight(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    @Override
    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new FrozenZombieAttackGoal(this, 1.0D, 20, 15.0F, false));
        this.goalSelector.addGoal(3, new SwitchCombatItemGoal(this, 6.0D, 6.0D));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::isBreakDoorsTaskSet));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(ZombifiedPiglinEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.TARGET_DRY_BABY));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.func_234342_eQ_();
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        super.setEquipmentBasedOnDifficulty(difficultyInstance);
        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SNOWBALL));
    }

    @Override
    public void livingTick() {
        if (this.world.isRemote) {
            this.world.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getPosXRandom(0.5D), this.getPosYRandom() - 0.25D, this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }
        super.livingTick();
    }

    @Override
    public boolean attackEntityAsMob(Entity targetEntity) {
        if (super.attackEntityAsMob(targetEntity)) {
            if (targetEntity instanceof LivingEntity) {
                int i = 0;
                if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    i = 4;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    i = 8;
                }

                if (i > 0) {
                    ((LivingEntity)targetEntity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity livingEntity, float v) {
        SnowballEntity snowballentity = new SnowballEntity(this.world, this);
        double adjustedEyeY = livingEntity.getPosYEye() - 1.100000023841858D;
        double xDifference = livingEntity.getPosX() - this.getPosX();
        double yDifference = adjustedEyeY - snowballentity.getPosY();
        double zDifference = livingEntity.getPosZ() - this.getPosZ();
        float adjustedHorizontalDistance = MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference) * 0.2F;
        snowballentity.shoot(xDifference, yDifference + (double)adjustedHorizontalDistance, zDifference, 1.6F, 12.0F);
        this.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(snowballentity);
    }

    static class FrozenZombieAttackGoal extends ThrowAndMeleeAttackGoal {
        private final FrozenZombieEntity zombie;
        private int raiseArmTicks;

        FrozenZombieAttackGoal(FrozenZombieEntity zombieEntity, double speedAmplifier, int attackInterval, float maxDistance, boolean useLongMemory) {
            super(zombieEntity, speedAmplifier, attackInterval, maxDistance, useLongMemory);
            this.zombie = zombieEntity;
        }

        public void startExecuting() {
            super.startExecuting();
            this.raiseArmTicks = 0;
        }

        public void resetTask() {
            super.resetTask();
            this.zombie.setAggroed(false);
        }

        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            if (this.raiseArmTicks >= 5 && this.func_234041_j_() < this.func_234042_k_() / 2) {
                this.zombie.setAggroed(true);
            } else {
                this.zombie.setAggroed(false);
            }

        }
    }

}
