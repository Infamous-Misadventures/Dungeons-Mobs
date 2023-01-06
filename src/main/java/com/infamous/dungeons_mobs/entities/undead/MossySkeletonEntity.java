package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class MossySkeletonEntity extends AbstractSkeleton {
    public MossySkeletonEntity(Level worldIn) {
        super(ModEntityTypes.MOSSY_SKELETON.get(), worldIn);
    }

    public MossySkeletonEntity(EntityType<? extends MossySkeletonEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static boolean canMossySkeletonSpawn(EntityType<MossySkeletonEntity> entityType, ServerLevelAccessor iWorld, MobSpawnType spawnReason, BlockPos blockPos, RandomSource rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == MobSpawnType.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return AbstractSkeleton.createAttributes();
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
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
        AbstractArrow abstractarrowentity = this.getArrow(itemstack, p_82196_2_);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
            abstractarrowentity = ((net.minecraft.world.item.BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = p_82196_1_.getX() - this.getX();
        double d1 = p_82196_1_.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = p_82196_1_.getZ() - this.getZ();
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        abstractarrowentity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
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
                    ((LivingEntity) targetEntity).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected AbstractArrow getArrow(ItemStack stack, float damageMultiplier) {
        AbstractArrow abstractArrowEntity = super.getArrow(stack, damageMultiplier);
        int i = 0;
        if (this.level.getDifficulty() == Difficulty.NORMAL) {
            i = 4;
        } else if (this.level.getDifficulty() == Difficulty.HARD) {
            i = 8;
        }
        if (abstractArrowEntity instanceof Arrow && i > 0) {
            ((Arrow) abstractArrowEntity).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20));
        }

        return abstractArrowEntity;
    }
}
