package com.infamous.dungeons_mobs.entities.illagers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class IllagerArmsUtil {
    public static boolean armorHasCrossedArms(AbstractIllagerEntity p_241739_3_, ItemStack itemstack) {
        return !(itemstack.getItem() instanceof ArmorItem) || resourceExists(getArmorResourceStatic(p_241739_3_, itemstack, EquipmentSlotType.CHEST));
    }

    private static ResourceLocation getArmorResourceStatic(Entity entity, ItemStack stack, EquipmentSlotType slot) {
        ArmorItem item = (ArmorItem)stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String defaultString = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, 1, "crossed" == null ? "" : String.format("_%s", "crossed"));

        String s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, defaultString, slot, "crossed");
        if(!s1.endsWith("_crossed.png")){
            s1 = s1.replace(".png", "_crossed.png");
        }

        return new ResourceLocation(s1);
    }

    public static boolean resourceExists(ResourceLocation resourceLocation) {
    	if (resourceLocation != null) {
	        try {
	            Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
	            return true;
	        } catch (IOException e) {
	            return false;
	        }
    	} else {
    		return false;
    	}
    }
}
