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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.raid.Raid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MountaineerEntity extends VindicatorEntity {

    public MountaineerEntity(World worldIn){
        super(ModEntityTypes.MOUNTAINEER.get(), worldIn);
    }

    public MountaineerEntity(EntityType<? extends MountaineerEntity> entityType, World world) {
        super(entityType, world);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.func_234322_eI_();
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if(this.getRaid() == null){
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
        }
    }

    public void applyWaveBonus(int waveNumber, boolean bool) {
        ItemStack itemStack = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
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
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemStack);
        }

        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemStack);
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
