package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class MossySkeletonEntity extends AbstractSkeletonEntity {
    public MossySkeletonEntity(World worldIn) {
        super(ModEntityTypes.MOSSY_SKELETON.get(), worldIn);
    }

    public MossySkeletonEntity(EntityType<? extends MossySkeletonEntity> entityType, World world) {
        super(entityType, world);
    }



    public static boolean canMossySkeletonSpawn(EntityType<MossySkeletonEntity> entityType, IWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return canMonsterSpawnInLight(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return AbstractSkeletonEntity.func_234275_m_();
    }

    public static boolean canSpawn(EntityType<MossySkeletonEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return canMonsterSpawnInLight(entityType, world, spawnReason, blockPos, random) && (spawnReason == SpawnReason.SPAWNER || world.canSeeSky(blockPos));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
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
                    ((LivingEntity)targetEntity).addPotionEffect(new EffectInstance(Effects.POISON, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected AbstractArrowEntity fireArrow(ItemStack stack, float damageMultiplier) {
        AbstractArrowEntity abstractArrowEntity = super.fireArrow(stack, damageMultiplier);
        int i = 0;
        if (this.world.getDifficulty() == Difficulty.NORMAL) {
            i = 4;
        } else if (this.world.getDifficulty() == Difficulty.HARD) {
            i = 8;
        }
        if (abstractArrowEntity instanceof ArrowEntity && i > 0) {
            ((ArrowEntity)abstractArrowEntity).addEffect(new EffectInstance(Effects.POISON, i * 20));
        }

        return abstractArrowEntity;
    }
}
