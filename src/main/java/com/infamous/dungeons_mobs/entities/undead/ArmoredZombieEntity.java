package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ArmoredZombieEntity extends DungeonsZombieEntity {
    public ArmoredZombieEntity(World worldIn) {
        super(worldIn);
    }

    public ArmoredZombieEntity(EntityType<? extends DungeonsZombieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 48.0D) // normal zombies have 20
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.ARMOR, 8.0D)
        ;
    }

    @Override
    public void setBaby(boolean childZombie) {
        // NO-OP
    }

    @Override
    public boolean isBaby() {
        // technically, NO-OP
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(ModList.get().isLoaded("dungeons_gear")){

            Item REINFORCED_MAIL_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_helmet"));
            Item REINFORCED_MAIL_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_chestplate"));

            ItemStack reinforcedMailHelmet = new ItemStack(REINFORCED_MAIL_HELMET);
            ItemStack reinforcedMailChestplate = new ItemStack(REINFORCED_MAIL_CHESTPLATE);

            this.setItemSlot(EquipmentSlotType.HEAD, reinforcedMailHelmet);
            this.setItemSlot(EquipmentSlotType.CHEST, reinforcedMailChestplate);
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
        else{
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
            this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBTIn) {
        livingEntityDataIn = super.finalizeSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBTIn);

        this.populateDefaultEquipmentSlots(difficultyInstance);
        this.populateDefaultEquipmentEnchantments(difficultyInstance);

        return (ILivingEntityData)livingEntityDataIn;
    }
}

