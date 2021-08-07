package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import com.infamous.dungeons_mobs.entities.water.ArmoredDrownedEntity;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

public class ArmoredSkeletonEntity extends SkeletonEntity implements IArmoredMob {
    private static final DataParameter<Boolean> STRONG_ARMOR = EntityDataManager.defineId(ArmoredSkeletonEntity.class, DataSerializers.BOOLEAN);

    private final ModdedRangedBowAttackGoal<AbstractSkeletonEntity> rangedAI = new ModdedRangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeAI = new MeleeAttackGoal(this, 1.2D, false) {
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            ArmoredSkeletonEntity.this.setAggressive(false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            super.start();
            ArmoredSkeletonEntity.this.setAggressive(true);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRONG_ARMOR, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //this.goalSelector.addGoal(4, new ModdedRangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return AbstractSkeletonEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D) // normal skeletons have 20
                .add(Attributes.ATTACK_DAMAGE, 4.0D) // normal skeletons have 2
                ;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        //super.setEquipmentBasedOnDifficulty(difficultyInstance);
        if(DungeonsGearCompat.isLoaded()){
            Item REINFORCED_MAIL_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_helmet"));
            Item REINFORCED_MAIL_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_chestplate"));
            Item POWER_BOW = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "power_bow"));

            ItemStack reinforcedMailHelmet = new ItemStack(REINFORCED_MAIL_HELMET);
            ItemStack reinforcedMailChestplate = new ItemStack(REINFORCED_MAIL_CHESTPLATE);
            ItemStack powerBow = new ItemStack(POWER_BOW);

            this.setItemSlot(EquipmentSlotType.HEAD, reinforcedMailHelmet);
            if(this.hasStrongArmor()){
                this.setItemSlot(EquipmentSlotType.CHEST, reinforcedMailChestplate);
                this.setItemSlot(EquipmentSlotType.MAINHAND, powerBow);
            } else{
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
            }
        }
        else{
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
            if(this.hasStrongArmor()){
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                this.getMainHandItem().enchant(Enchantments.POWER_ARROWS, 1);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        // changed the getHandWith to my own
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ModProjectileHelper.getHandWith(this, item -> item instanceof BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getArrow(itemstack, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);

        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBT) {
        this.designateStrongArmor(this);
        livingEntityDataIn = super.finalizeSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBT);
        return livingEntityDataIn;
    }

    @Override
    public void reassessWeaponGoal() {
        if (this.isConstructed && this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeAI);
            this.goalSelector.removeGoal(this.rangedAI);
            ItemStack itemstack = this.getItemInHand(ModProjectileHelper.getHandWith(this, item -> item instanceof BowItem));
            if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                int i = 20;
                if (this.level.getDifficulty() != Difficulty.HARD) {
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
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        this.readStrongArmorNBT(p_70037_1_);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        this.writeStrongArmorNBT(p_213281_1_);
    }

    @Override
    public boolean hasStrongArmor() {
        return this.entityData.get(STRONG_ARMOR);
    }

    @Override
    public void setStrongArmor(boolean strongArmor) {
        this.entityData.set(STRONG_ARMOR, strongArmor);
    }

    @Override
    public String getArmorName() {
        return "";
    }
}
