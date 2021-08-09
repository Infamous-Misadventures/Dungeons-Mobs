package com.infamous.dungeons_mobs.entities.piglin;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.infamous.dungeons_mobs.mixin.PiglinAccessor;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.utils.PiglinHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class ArmoredPiglinEntity extends PiglinEntity implements IArmoredMob {
    private static final DataParameter<Boolean> STRONG_ARMOR = EntityDataManager.defineId(ArmoredPiglinEntity.class, DataSerializers.BOOLEAN);

    public ArmoredPiglinEntity(EntityType<? extends ArmoredPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRONG_ARMOR, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return PiglinEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        this.designateStrongArmor(this);

        ILivingEntityData spawnData = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

        this.setBaby(false);

        return spawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if (this.random.nextFloat() < 0.5D) {
            this.setRangedWeapon();
        } else{
            this.setMeleeWeapon();
        }

        if(this.hasStrongArmor()){
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.GOLD_PIGLIN_HELMET.get()));
        } else if(this.isHolding(item -> item instanceof CrossbowItem)){
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.NETHERITE_PIGLIN_HELMET.get()));
        }
    }

    @Override
    protected void finishConversion(ServerWorld serverWorld) {
        PiglinHelper.stopAdmiringItem(this);
        ((PiglinAccessor)this).getInventory().removeAllItems().forEach(this::spawnAtLocation);
        PiglinHelper.zombify(ModEntityTypes.ZOMBIFIED_ARMORED_PIGLIN.get(), this);
    }

    private void setRangedWeapon() {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    private void setMeleeWeapon() {
        if(DungeonsGearCompat.isLoaded()){
            ItemStack goldAxe = new ItemStack(DungeonsGearCompat.getGoldAxe().get());
            ItemStack firebrand = new ItemStack(DungeonsGearCompat.getFirebrand().get());
            ItemStack mainhandWeapon = this.hasStrongArmor() ? firebrand : goldAxe;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
        else{
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        this.writeStrongArmorNBT(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.readStrongArmorNBT(compound);
    }

    @Override
    public boolean hasStrongArmor(){
        return this.entityData.get(STRONG_ARMOR);
    }

    @Override
    public void setStrongArmor(boolean strongArmor){
        this.entityData.set(STRONG_ARMOR, strongArmor);
    }


    @Override
    public String getArmorName() {
        return this.hasStrongArmor() ? "gold" : "netherite";
    }
}
