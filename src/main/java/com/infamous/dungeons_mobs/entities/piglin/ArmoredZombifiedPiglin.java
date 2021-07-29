package com.infamous.dungeons_mobs.entities.piglin;

import com.infamous.dungeons_mobs.DungeonsGearCompat;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ArmoredZombifiedPiglin extends ZombifiedPiglinEntity {
    private static final DataParameter<Boolean> IS_GOLDEN = EntityDataManager.defineId(ArmoredZombifiedPiglin.class, DataSerializers.BOOLEAN);

    public ArmoredZombifiedPiglin(EntityType<? extends ArmoredZombifiedPiglin> entityType, World world) {
        super(entityType, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_GOLDEN, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return ZombifiedPiglinEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.isGolden()){
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
        }

        if (this.random.nextFloat() < 0.5D) {
            this.setRangedWeapon();
        } else{
            this.setMeleeWeapon();
        }
    }

    private void setRangedWeapon() {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    private void setMeleeWeapon() {
        if(DungeonsGearCompat.isLoaded()){
            ItemStack goldAxe = new ItemStack(DungeonsGearCompat.getGoldAxe().get());
            ItemStack firebrand = new ItemStack(DungeonsGearCompat.getFirebrand().get());
            ItemStack mainhandWeapon = this.isGolden() ? firebrand : goldAxe;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
        else{
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.isGolden()){
            compound.putBoolean("Golden", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Golden", 99)) {
            this.setGolden(compound.getBoolean("Golden"));
        }
    }

    public boolean isGolden(){
        return this.entityData.get(IS_GOLDEN);
    }

    public void setGolden(boolean isGolden){
        this.entityData.set(IS_GOLDEN, isGolden);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        float goldenChance = random.nextFloat();
        if(goldenChance < 0.25F){
            this.setGolden(true);
            this.applyGoldenArmorBoosts();
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void applyGoldenArmorBoosts() {
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Golden armor boost", 10.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Golden knockback resistance boost", 0.6D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Golden attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
    }
}
