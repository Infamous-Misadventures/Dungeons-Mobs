package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public interface IArmoredMob {

    Random RANDOM = new Random();
    float STRONG_ARMOR_SPAWN_CHANCE = 0.25F;

    default void writeStrongArmorNBT(CompoundNBT compound) {
        if (this.hasStrongArmor()){
            compound.putBoolean("StrongArmor", true);
        }
    }

    default void readStrongArmorNBT(CompoundNBT compound) {
        if (compound.contains("StrongArmor", 99)) {
            this.setStrongArmor(compound.getBoolean("StrongArmor"));
        }
    }

    boolean hasStrongArmor();

    void setStrongArmor(boolean strongArmor);

    default <T extends LivingEntity & IArmoredMob> void designateStrongArmor(T armoredMob) {
        if(this != armoredMob) throw new IllegalArgumentException("Supplied armored mob does not match this instance!");

        float strongArmorChance = RANDOM.nextFloat();
        if(strongArmorChance < STRONG_ARMOR_SPAWN_CHANCE){
            this.setStrongArmored((MobEntity) armoredMob);
        }
    }

    default void setStrongArmored(MobEntity armoredMob) {
        if(this != armoredMob) throw new IllegalArgumentException("Supplied armored mob does not match this instance!");

        this.setStrongArmor(true);
        this.applyStrongArmorBoosts(armoredMob);
    }

    default void applyStrongArmorBoosts(MobEntity armoredMob){
        if(this != armoredMob) throw new IllegalArgumentException("Supplied armored mob does not match this instance!");

        armoredMob.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Strong armor boost", 10.0D, AttributeModifier.Operation.ADDITION));
        armoredMob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Strong knockback resistance boost", 0.6D, AttributeModifier.Operation.ADDITION));
        armoredMob.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Strong attack boost", 1.0D, AttributeModifier.Operation.ADDITION));

    }

    String getArmorName();
}
