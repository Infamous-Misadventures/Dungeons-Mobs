package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.effects.EnsnaredEffect;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
	
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
			DungeonsMobs.MODID);
	
	public static final RegistryObject<Effect> ENSNARED = EFFECTS.register("ensnared", () -> new EnsnaredEffect(EffectType.HARMFUL, 0xdbe64e).addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -5.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
}
