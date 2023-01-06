package com.infamous.dungeons_mobs.capabilities;

import com.infamous.dungeons_mobs.capabilities.ancient.Ancient;
import com.infamous.dungeons_mobs.capabilities.ancient.AttacherAncient;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedProps;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AttacherAnimatedProps;
import com.infamous.dungeons_mobs.capabilities.convertible.AttacherConvertible;
import com.infamous.dungeons_mobs.capabilities.convertible.Convertible;
import com.infamous.dungeons_mobs.capabilities.properties.AttacherMobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobProps;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    public static final Capability<Ancient> ANCIENT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<AnimatedProps> ANIMATED_PROPS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<Convertible> CONVERTIBLE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<MobProps> MOB_PROPS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });


    public static void setupCapabilities() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, AttacherAncient::attach);
        forgeBus.addGenericListener(Entity.class, AttacherAnimatedProps::attach);
        forgeBus.addGenericListener(Entity.class, AttacherConvertible::attach);
        forgeBus.addGenericListener(Entity.class, AttacherMobProps::attach);
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(Ancient.class);
        event.register(AnimatedProps.class);
        event.register(Convertible.class);
        event.register(MobProps.class);
    }
}
