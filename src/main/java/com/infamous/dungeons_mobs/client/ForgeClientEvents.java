package com.infamous.dungeons_mobs.client;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapabilityLazy;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEvents {


    @SubscribeEvent
    public static void onRenderNamePlateEvent(RenderNameplateEvent event){
        Entity entity = event.getEntity();
        IFormattableTextComponent copy = event.getContent().copy();
        StringBuilder enchantmentString = new StringBuilder();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if(cap.hasEnchantment()){
                enchantmentString.append(" (");
                enchantmentString.append(cap.getEnchantments().stream().map(mobEnchantment -> mobEnchantment.getRegistryName().getPath()).collect(Collectors.joining(", ")));
                enchantmentString.append(")");
                event.setResult(Event.Result.ALLOW);
            }
        });
        copy.append(enchantmentString.toString());
        event.setContent(copy);
    }
}
