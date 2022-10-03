package com.infamous.dungeons_mobs.entities.undead;

import java.util.Random;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class MossySkeletonEntity extends AbstractSkeletonEntity {
    public MossySkeletonEntity(World worldIn) {
        super(ModEntityTypes.MOSSY_SKELETON.get(), worldIn);
    }

    public MossySkeletonEntity(EntityType<? extends MossySkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean canMossySkeletonSpawn(EntityType<MossySkeletonEntity> entityType, IServerWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return AbstractSkeletonEntity.createAttributes();
    }
    
    @Override
    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
           this.playSound(soundevent, 0.25F, this.getVoicePitch());
        }
    }

    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.MOSSY_SKELETON_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.MOSSY_SKELETON_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSoundEvents.MOSSY_SKELETON_DEATH.get();
    }

    @Override
    protected SoundEvent getStepSound() {
        return ModSoundEvents.MOSSY_SKELETON_STEP.get();
    }
    
    @Override
    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getArrow(itemstack, p_82196_2_);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
           abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = p_82196_1_.getX() - this.getX();
        double d1 = p_82196_1_.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = p_82196_1_.getZ() - this.getZ();
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(ModSoundEvents.MOSSY_SKELETON_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
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
                    ((LivingEntity)targetEntity).addEffect(new EffectInstance(Effects.POISON, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected AbstractArrowEntity getArrow(ItemStack stack, float damageMultiplier) {
        AbstractArrowEntity abstractArrowEntity = super.getArrow(stack, damageMultiplier);
        int i = 0;
        if (this.level.getDifficulty() == Difficulty.NORMAL) {
            i = 4;
        } else if (this.level.getDifficulty() == Difficulty.HARD) {
            i = 8;
        }
        if (abstractArrowEntity instanceof ArrowEntity && i > 0) {
            ((ArrowEntity)abstractArrowEntity).addEffect(new EffectInstance(Effects.POISON, i * 20));
        }

        return abstractArrowEntity;
    }
}
