package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import java.util.Map;

public class VindicatorChefEntity extends VindicatorEntity {

    public VindicatorChefEntity(World worldIn){
        super(ModEntityTypes.VINDICATOR_CHEF.get(), worldIn);
    }

    public VindicatorChefEntity(EntityType<? extends VindicatorChefEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.func_234322_eI_();
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if(this.getRaid() == null){
            this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.CHEF_HAT.get()));
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.SPATULA.get()));
        }
    }

    public void applyWaveBonus(int waveNumber, boolean bool) {
        ItemStack helmet = new ItemStack(ModItems.CHEF_HAT.get());
        ItemStack weapon = new ItemStack(ModItems.SPATULA.get());
        Raid raid = this.getRaid();
        int i = 1;
        if (raid != null && waveNumber > raid.getWaves(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = false;
        if (raid != null) {
            flag = this.rand.nextFloat() <= raid.getEnchantOdds();
        }
        if (flag) {
            Map<Enchantment, Integer> helmetMap = Maps.newHashMap();
            Map<Enchantment, Integer> weaponMap = Maps.newHashMap();
            helmetMap.put(Enchantments.PROTECTION, i);
            weaponMap.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(helmetMap, helmet);
            EnchantmentHelper.setEnchantments(weaponMap, weapon);
        }

        this.setItemStackToSlot(EquipmentSlotType.HEAD, helmet);
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, weapon);
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
