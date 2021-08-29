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

public class ArmoredZombieEntity extends ZombieEntity {
    public ArmoredZombieEntity(World worldIn) {
        super(worldIn);
    }

    public ArmoredZombieEntity(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ZombieEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D) // normal zombies have 20
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
        ;
    }

    @Override
    protected void doUnderWaterConversion() {
        this.convertToZombieType(ModEntityTypes.ARMORED_DROWNED.get());
        if (!this.isSilent()) {
            this.level.levelEvent((PlayerEntity)null, 1040, this.blockPosition(), 0);
        }
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
        float f = difficultyInstance.getSpecialMultiplier();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * f);
        if (livingEntityDataIn == null) {
            livingEntityDataIn = new ZombieEntity.GroupData(getSpawnAsBabyOdds(world.getRandom()), true);
        }

        if (livingEntityDataIn instanceof ZombieEntity.GroupData) {
            //ZombieEntity.GroupData zombieentity$groupdata = (ZombieEntity.GroupData)livingEntityData;
            // only used for handling baby zombies and their chance to spawn riding chickens
            this.setCanBreakDoors(this.supportsBreakDoorGoal()
                    //&& this.rand.nextFloat() < f * 0.1F
                    // we want these zombies to always be able to break doors
            );
            this.populateDefaultEquipmentSlots(difficultyInstance);
            this.populateDefaultEquipmentEnchantments(difficultyInstance);
        }

        this.handleAttributes(f);
        return (ILivingEntityData)livingEntityDataIn;
    }
}

