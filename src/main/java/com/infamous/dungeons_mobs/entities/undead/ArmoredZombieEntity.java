package com.infamous.dungeons_mobs.entities.undead;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ArmoredZombieEntity extends ZombieEntity {
    public ArmoredZombieEntity(World worldIn) {
        super(worldIn);
    }

    public ArmoredZombieEntity(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.func_234342_eQ_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D) // normal zombies have 20
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
        ;
    }

    @Override
    public void setChild(boolean childZombie) {
        // NO-OP
    }

    @Override
    public boolean isChild() {
        // technically, NO-OP
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        if(ModList.get().isLoaded("dungeons_gear")){

            Item REINFORCED_MAIL_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_helmet"));
            Item REINFORCED_MAIL_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_chestplate"));

            ItemStack reinforcedMailHelmet = new ItemStack(REINFORCED_MAIL_HELMET);
            ItemStack reinforcedMailChestplate = new ItemStack(REINFORCED_MAIL_CHESTPLATE);

            this.setItemStackToSlot(EquipmentSlotType.HEAD, reinforcedMailHelmet);
            this.setItemStackToSlot(EquipmentSlotType.CHEST, reinforcedMailChestplate);
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
        else{
            this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
            this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
    }

    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBTIn) {
        livingEntityDataIn = super.onInitialSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBTIn);
        float f = difficultyInstance.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
        if (livingEntityDataIn == null) {
            livingEntityDataIn = new ZombieEntity.GroupData(func_241399_a_(world.getRandom()), true);
        }

        if (livingEntityDataIn instanceof ZombieEntity.GroupData) {
            //ZombieEntity.GroupData zombieentity$groupdata = (ZombieEntity.GroupData)livingEntityData;
            // only used for handling baby zombies and their chance to spawn riding chickens
            this.setBreakDoorsAItask(this.canBreakDoors()
                    //&& this.rand.nextFloat() < f * 0.1F
                    // we want these zombies to always be able to break doors
            );
            this.setEquipmentBasedOnDifficulty(difficultyInstance);
            this.setEnchantmentBasedOnDifficulty(difficultyInstance);
        }

        this.applyAttributeBonuses(f);
        return (ILivingEntityData)livingEntityDataIn;
    }
}

