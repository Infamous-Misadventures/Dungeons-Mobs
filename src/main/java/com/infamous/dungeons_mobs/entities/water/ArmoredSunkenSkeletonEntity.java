package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ArmoredSunkenSkeletonEntity extends SunkenSkeletonEntity implements IArmoredMob {
    private static final DataParameter<Boolean> STRONG_ARMOR = EntityDataManager.defineId(ArmoredSunkenSkeletonEntity.class, DataSerializers.BOOLEAN);

    public ArmoredSunkenSkeletonEntity(EntityType<? extends ArmoredSunkenSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return SunkenSkeletonEntity.setCustomAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D) // normal skeletons have 20
                .add(Attributes.ATTACK_DAMAGE, 4.0D) // normal skeletons have 2
        ;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        ItemStack crossbowStack = new ItemStack(Items.CROSSBOW);
        ItemStack bowStack = new ItemStack(Items.BOW);
        if(DungeonsGearCompat.isLoaded()){
            crossbowStack = new ItemStack(DungeonsGearCompat.getHeavyCrossbow().get());
            bowStack = new ItemStack(DungeonsGearCompat.getElitePowerBow().get());
        }
        if(this.hasStrongArmor()){
            this.setItemSlot(EquipmentSlotType.MAINHAND, bowStack);
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, crossbowStack);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRONG_ARMOR, false);
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
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.designateStrongArmor(this);

        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
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
        return this.hasStrongArmor() ? "yellow_coral" : "red_coral";
    }
}
