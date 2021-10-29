package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper;
import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_libraries.network.MobEnchantmentMessage;
import com.infamous.dungeons_mobs.capabilities.ancient.properties.AncientHelper;
import com.infamous.dungeons_mobs.capabilities.ancient.properties.IAncient;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mobenchants.MobEnchantmentSelector;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AncientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;
import java.util.UUID;

import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapabilityLazy;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.HEALS_ALLIES;

@Mod.EventBusSubscriber(modid = MODID)
public class MobEnchantmentEvents {

    @SubscribeEvent
    public static void enchantOnEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
        Entity entity = event.getEntity();
        if (!entity.level.isClientSide && EnchantableHelper.isEnchantableEntity(entity) && isSpawnEnchantableEntity(entity) && DungeonsMobsConfig.ENCHANTS.ENABLE_ENCHANTS_ON_SPAWN.get()) {
            getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
                if(!cap.isSpawned()) {
                    addEnchantmentOnSpawn(entity, cap);
//                    addEnchantmentOnSpawnDEVELOPMENT(entity, cap);
                }
            });
        }
    }
    private static boolean isSpawnEnchantableEntity(Entity entity) {
        return !(entity instanceof PlayerEntity) && !(entity instanceof ArmorStandEntity) && !(entity instanceof BoatEntity) && !(entity instanceof MinecartEntity) && !DungeonsMobsConfig.ENCHANTS.ENCHANT_ON_SPAWN_EXCLUSION_MOBS.get().contains(entity.getType().getRegistryName().toString());
    }

    private static void addEnchantmentOnSpawn(Entity entity, IEnchantable cap) {
        int difficultyAsInt = entity.level.getDifficulty().getId();
        Random random = entity.level.getRandom();
        if(random.nextFloat() <= DungeonsMobsConfig.ENCHANTS.ENCHANT_ON_SPAWN_CHANCE.get() * difficultyAsInt) {
            int totalNumberOfEnchants = (int) (random.nextGaussian() * difficultyAsInt+1) + 1;
            if(totalNumberOfEnchants < 4) {
                makeEnchanted(entity, cap, random, totalNumberOfEnchants);
            }else{
                makeAncient(entity, cap, random, totalNumberOfEnchants);
            }
        }
    }

    private static void makeEnchanted(Entity entity, IEnchantable cap, Random random, int totalNumberOfEnchants) {
        for (int i = 0; i < totalNumberOfEnchants; i++) {
            MobEnchantment randomMobEnchantment = MobEnchantmentSelector.getRandomMobEnchantment(entity, random);
            cap.addEnchantment(randomMobEnchantment);
            entity.refreshDimensions();
        }
        cap.setSpawned(true);
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
            createCopy(entity);
            createCopy(entity);
    }

    private static void makeAncient(Entity entity, IEnchantable cap, Random random, int totalNumberOfEnchants) {
        if(entity instanceof LivingEntity) {
            for (int i = 0; i < totalNumberOfEnchants; i++) {
                MobEnchantment randomMobEnchantment = MobEnchantmentSelector.getRandomMobEnchantment(entity, random);
                cap.addEnchantment(randomMobEnchantment);
                entity.refreshDimensions();
            }
            IAncient ancientCapability = AncientHelper.getAncientCapability(entity);
            ancientCapability.setAncient(true);
            cap.setSpawned(true);
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new AncientMessage(entity.getId(), true));
            createMinions();
        }
    }

    private static void createMinions() {
    }

    private static void createCopy(Entity entity) {
        Entity copy = entity.getType().create(entity.level);
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT = entity.saveWithoutId(compoundNBT);
        UUID uuid = copy.getUUID();
        copy.load(compoundNBT);
        copy.setUUID(uuid);
        entity.level.addFreshEntity(copy);
    }

    private static void addEnchantmentOnSpawnDEVELOPMENT(Entity entity, IEnchantable cap) {
        cap.addEnchantment(HEALS_ALLIES.get());
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
        entity.refreshDimensions();
        cap.setSpawned(true);
    }
}
