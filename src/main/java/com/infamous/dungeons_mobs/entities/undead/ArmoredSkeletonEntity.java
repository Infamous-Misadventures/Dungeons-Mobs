package com.infamous.dungeons_mobs.entities.undead;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.*;
import com.infamous.dungeons_mobs.utils.ModProjectileHelper;
import com.infamous.dungeons_mobs.goals.ModdedRangedBowAttackGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ArmoredSkeletonEntity extends SkeletonEntity {
    private final ModdedRangedBowAttackGoal<AbstractSkeletonEntity> rangedAI = new ModdedRangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeAI = new MeleeAttackGoal(this, 1.2D, false) {
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            super.resetTask();
            ArmoredSkeletonEntity.this.setAggroed(false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();
            ArmoredSkeletonEntity.this.setAggroed(true);
        }
    };

    private final boolean isConstructed;

    public ArmoredSkeletonEntity(World worldIn) {
        super(ModEntityTypes.ARMORED_SKELETON.get(), worldIn);
        this.isConstructed = true;
    }

    public ArmoredSkeletonEntity(EntityType<? extends ArmoredSkeletonEntity> p_i50194_1_, World p_i50194_2_) {
        super(p_i50194_1_, p_i50194_2_);
        this.isConstructed = true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //this.goalSelector.addGoal(4, new ModdedRangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return AbstractSkeletonEntity.registerAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D) // normal skeletons have 20
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                ;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        //super.setEquipmentBasedOnDifficulty(difficultyInstance);
        if(ModList.get().isLoaded("dungeons_gear")){

            Item REINFORCED_MAIL_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_helmet"));
            Item REINFORCED_MAIL_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_chestplate"));
            Item POWER_BOW = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "power_bow"));

            ItemStack reinforcedMailHelmet = new ItemStack(REINFORCED_MAIL_HELMET);
            ItemStack reinforcedMailChestplate = new ItemStack(REINFORCED_MAIL_CHESTPLATE);
            ItemStack powerBow = new ItemStack(POWER_BOW);

            this.setItemStackToSlot(EquipmentSlotType.HEAD, reinforcedMailHelmet);
            this.setItemStackToSlot(EquipmentSlotType.CHEST, reinforcedMailChestplate);
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, powerBow);
        }
        else{
            this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
            this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        // changed the getHandWith to my own
        ItemStack itemstack = this.findAmmo(this.getHeldItem(ModProjectileHelper.getHandWith(this, item -> item instanceof BowItem)));
        AbstractArrowEntity abstractarrowentity = this.fireArrow(itemstack, distanceFactor);
        if (this.getHeldItemMainhand().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getHeldItemMainhand().getItem()).customArrow(abstractarrowentity);

        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(abstractarrowentity);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBT) {
        livingEntityDataIn = super.onInitialSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBT);
        return livingEntityDataIn;
    }

    @Override
    public void setCombatTask() {
        if (this.isConstructed && this.world != null && !this.world.isRemote) {
            this.goalSelector.removeGoal(this.meleeAI);
            this.goalSelector.removeGoal(this.rangedAI);
            ItemStack itemstack = this.getHeldItem(ModProjectileHelper.getHandWith(this, item -> item instanceof BowItem));
            if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                int i = 20;
                if (this.world.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.rangedAI.setAttackCooldown(i);
                this.goalSelector.addGoal(4, this.rangedAI);
            } else {
                this.goalSelector.addGoal(4, this.meleeAI);
            }

        }
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

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }
}
