package com.infamous.dungeons_mobs.entities.illagers.minibosses;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsPillagerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;
import java.util.Map;

public class PillagerRaidCaptainEntity extends DungeonsPillagerEntity {
    public PillagerRaidCaptainEntity(World world){
        super(ModEntityTypes.PILLAGER_RAID_CAPRAIN.get(), world);
    }


    public PillagerRaidCaptainEntity(EntityType<? extends DungeonsPillagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return DungeonsPillagerEntity.setCustomAttributes()
                .add(Attributes.MAX_HEALTH, 135.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.5D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
    }
    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        float diamondChance = random.nextFloat();

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }


    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.DIAMOND_PILLAGER_HELMET.get()));
            ItemStack mainhandWeapon = new ItemStack(Items.CROSSBOW);
            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
    }

    @Override
    public void applyRaidBuffs(int i, boolean b) {
        ItemStack crossbow = new ItemStack(Items.CROSSBOW);
        ItemStack helmet = new ItemStack(ModItems.DIAMOND_PILLAGER_HELMET.get());
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = (int) (2 + (i / 2.5));

        float o = this.getRandom().nextFloat();
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

        boolean applyEnchant = this.random.nextFloat() - 0.5 <= raid.getEnchantOdds();
        if (applyEnchant) {
            float e = this.getRandom().nextFloat();

            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.QUICK_CHARGE, Math.min(enchantmentLevel, 5));
            this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

            if (e <= 0.35) {
                enchantmentIntegerMap.put(Enchantments.MULTISHOT, 1);
            }

            enchantmentIntegerMap.put(Enchantments.VANISHING_CURSE, 1);

            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, crossbow);
        }
        if (i > raid.getNumGroups(Difficulty.EASY) && !(i > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
        }

        if (i > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 12.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.5D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 2.0D, AttributeModifier.Operation.ADDITION));
        }

        this.setItemSlot(EquipmentSlotType.HEAD, helmet);
        this.setItemSlot(EquipmentSlotType.MAINHAND, crossbow);
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }
}