package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ZombieEntity;
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

public class ArmoredDrownedEntity extends DrownedEntity implements IArmoredMob {
    private static final DataParameter<Boolean> STRONG_ARMOR = EntityDataManager.defineId(ArmoredDrownedEntity.class, DataSerializers.BOOLEAN);

    public ArmoredDrownedEntity(EntityType<? extends ArmoredDrownedEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D) // normal zombies have 20
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if ((double)this.random.nextFloat() > 0.9D) {
            this.setRangedWeapon();
        } else{
            this.setMeleeWeapon();
        }
    }

    private void setRangedWeapon() {
        ItemStack purpleTrident = new ItemStack(ModItems.PURPLE_TRIDENT.get());
        ItemStack yellowTrident = new ItemStack(ModItems.YELLOW_TRIDENT.get());
        ItemStack mainhandWeapon = this.hasStrongArmor() ? yellowTrident : purpleTrident;
        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
    }

    private void setMeleeWeapon() {
        if(DungeonsGearCompat.isLoaded()){
            ItemStack soulKnife = new ItemStack(DungeonsGearCompat.getSoulKnife().get());
            ItemStack eternalKnife = new ItemStack(DungeonsGearCompat.getEternalKnife().get());
            ItemStack mainhandWeapon = this.hasStrongArmor() ? eternalKnife : soulKnife;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
        else{
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
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

        ILivingEntityData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        this.setBaby(false);
        if(this.getVehicle() != null){
            this.stopRiding();
        }
        return spawnData;
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
        return this.hasStrongArmor() ? "seaweed" : "pale";
    }
}
