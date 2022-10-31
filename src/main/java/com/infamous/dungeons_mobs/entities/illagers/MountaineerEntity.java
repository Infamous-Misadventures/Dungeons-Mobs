package com.infamous.dungeons_mobs.entities.illagers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

public class MountaineerEntity extends VindicatorEntity {
	
	   private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(MountaineerEntity.class, DataSerializers.BYTE);
	   
    public MountaineerEntity(World worldIn){
        super(ModEntityTypes.MOUNTAINEER.get(), worldIn);
    }

    public MountaineerEntity(EntityType<? extends MountaineerEntity> entityType, World world) {
        super(entityType, world);
    }
    
    protected PathNavigator createNavigation(World p_175447_1_) {
        return new ClimberPathNavigator(this, p_175447_1_);
     }

     protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
     }

     public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
           this.setClimbing(this.horizontalCollision);
        }

     }
     
     public boolean onClimbable() {
         return this.isClimbing();
      }
     
     public boolean isClimbing() {
         return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
      }

      public void setClimbing(boolean p_70839_1_) {
         byte b0 = this.entityData.get(DATA_FLAGS_ID);
         if (p_70839_1_) {
            b0 = (byte)(b0 | 1);
         } else {
            b0 = (byte)(b0 & -2);
         }

         this.entityData.set(DATA_FLAGS_ID, b0);
      }
      
    protected float getSoundVolume() {
    	return 0.5F;
    }
    
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
     }
    
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
     }

     protected SoundEvent getDeathSound() {
        return ModSoundEvents.MOUNTAINEER_DEATH.get();
     }

     protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.MOUNTAINEER_HURT.get();
     }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 28.0D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getHead().get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getChest().get()));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getLegs().get()));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getFeet().get()));
    }

    public void applyRaidBuffs(int waveNumber, boolean bool) {
        ItemStack itemStack = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (raid != null && waveNumber > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = false;
        if (raid != null) {
            flag = this.random.nextFloat() <= raid.getEnchantOdds();
        }
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemStack);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, itemStack);
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }
}
