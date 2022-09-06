package com.infamous.dungeons_mobs.mobenchantments;

import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_libraries.network.MobEnchantmentMessage;
import com.infamous.dungeons_mobs.capabilities.ancient.AncientHelper;
import com.infamous.dungeons_mobs.capabilities.ancient.IAncient;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.data.AncientDataHelper;
import com.infamous.dungeons_mobs.data.MobAncientData;
import com.infamous.dungeons_mobs.data.UniqueAncientData;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AncientMessage;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapabilityLazy;
import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentsRegistry.MOB_ENCHANTMENTS;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class MobEnchantmentEvents {

    private static List<SpawnReason> blockedSpawnReasons = Arrays.asList(SpawnReason.MOB_SUMMONED, SpawnReason.CONVERSION);

//    @SubscribeEvent
//    public static void enchantOnEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
//        Entity entity = event.getEntity();
//        if (!entity.level.isClientSide &&
//                EnchantableHelper.isEnchantableEntity(entity) &&
//                isSpawnEnchantableEntity(entity) &&
//                DungeonsMobsConfig.ENCHANTS.ENABLE_ENCHANTS_ON_SPAWN.get() && !blockedSpawnReasons.contains(event.getSpawnReason())) {
//            getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
//                if(!cap.isSpawned()) {
//                    addEnchantmentOnSpawn(entity, cap);
////                    addEnchantmentOnSpawnDEVELOPMENT(entity, cap);
//                }
//            });
//        }
//    }
    private static boolean isSpawnEnchantableEntity(Entity entity) {
        return !(entity instanceof PlayerEntity) &&
                !(entity instanceof ArmorStandEntity) &&
                !(entity instanceof BoatEntity) &&
                !(entity instanceof MinecartEntity) &&
                !DungeonsMobsConfig.ENCHANTS.ENCHANT_ON_SPAWN_EXCLUSION_MOBS.get().contains(entity.getType().getRegistryName().toString()) &&
                (DungeonsMobsConfig.ENCHANTS.ENABLE_ENCHANTS_ON_PASSIVES.get() || (!(entity instanceof CreatureEntity) && !(entity instanceof AmbientEntity)));
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
            MobAncientData mobAncientData = AncientDataHelper.getMobAncientData(entity.getType().getRegistryName());
            if(mobAncientData.getUniques().size() > 0){
                generateUniqueAncient(entity, cap, random, totalNumberOfEnchants, mobAncientData);
            }else {
                generateRandomAncient(entity, cap, random, totalNumberOfEnchants, mobAncientData);
            }
        }
    }

    private static void generateUniqueAncient(Entity entity, IEnchantable cap, Random random, int totalNumberOfEnchants, MobAncientData mobAncientData) {
        UniqueAncientData uniqueAncientData = mobAncientData.getUniques().get(random.nextInt(mobAncientData.getUniques().size()));
        IAncient ancientCapability = AncientHelper.getAncientCapability(entity);
        ancientCapability.setAncient(true);
        cap.setSpawned(true);
        uniqueAncientData.getMobEnchantments().forEach(resourceLocation -> {
            MobEnchantment mobEnchantment = MOB_ENCHANTMENTS.getValue(resourceLocation);
            cap.addEnchantment(mobEnchantment);
        });
        entity.refreshDimensions();
        StringTextComponent displayName = new StringTextComponent(uniqueAncientData.getName());
        entity.setCustomName(displayName);
        entity.setCustomNameVisible(true);
        ancientCapability.initiateBossBar(displayName);
        EntityType<?> minionEntityType = ForgeRegistries.ENTITIES.getValue(uniqueAncientData.getMinion());
        for (int i = 0; i < uniqueAncientData.getMinionCount(); i++){
            Entity minion = minionEntityType.create(entity.level);
            minion.setPos(entity.position().x, entity.position().y, entity.position().z);
            getEnchantableCapabilityLazy(minion).ifPresent(minionCap -> {
                uniqueAncientData.getMinionMobEnchantments().forEach(resourceLocation -> {
                    MobEnchantment mobEnchantment = MOB_ENCHANTMENTS.getValue(resourceLocation);
                    minionCap.addEnchantment(mobEnchantment);
                });
                minionCap.setSpawned(true);
                minion.refreshDimensions();
                NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> minion), new MobEnchantmentMessage(minion.getId(), minionCap.getEnchantments()));
            });
            entity.level.addFreshEntity(minion);
        }
    }

    private static void generateRandomAncient(Entity entity, IEnchantable cap, Random random, int totalNumberOfEnchants, MobAncientData mobAncientData) {
        for (int i = 0; i < totalNumberOfEnchants; i++) {
            MobEnchantment randomMobEnchantment = MobEnchantmentSelector.getRandomMobEnchantment(entity, random);
            cap.addEnchantment(randomMobEnchantment);
            entity.refreshDimensions();
        }
        IAncient ancientCapability = AncientHelper.getAncientCapability(entity);
        ancientCapability.setAncient(true);
        cap.setSpawned(true);
        StringTextComponent displayName = new StringTextComponent(AncientDataHelper.getAncientName((LivingEntity) entity));
        entity.setCustomName(displayName);
        entity.setCustomNameVisible(true);
        ancientCapability.initiateBossBar(displayName);
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new AncientMessage(entity.getId(), true));
        createMinions((LivingEntity) entity);
    }

    private static void createMinions(LivingEntity entity) {
        List<ResourceLocation> minions = AncientDataHelper.getMobAncientData(entity.getType().getRegistryName()).getMinions();
        List<? extends EntityType<?>> entityTypes = minions.stream().map(ForgeRegistries.ENTITIES::getValue).collect(Collectors.toList());
        EntityType<?> entityType = entityTypes.get(entity.getRandom().nextInt(entityTypes.size()));
        MobEnchantment minionMobEnchantment = MobEnchantmentSelector.getRandomMobEnchantment(entityType.create(entity.level), entity.getRandom());
        for(int i = 0; i < entity.getRandom().nextInt(4) + 3; i++) {
            Entity minion = entityType.create(entity.level);
            minion.setPos(entity.position().x, entity.position().y, entity.position().z);
            getEnchantableCapabilityLazy(minion).ifPresent(cap -> {
                cap.addEnchantment(minionMobEnchantment);
                cap.setSpawned(true);
                minion.refreshDimensions();
                NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> minion), new MobEnchantmentMessage(minion.getId(), cap.getEnchantments()));
            });
            entity.level.addFreshEntity(minion);
        }
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
        if(entity instanceof ZombieEntity){
            makeAncient(entity, cap, ((ZombieEntity) entity).getRandom(), 4);
        }else {
            /*cap.addEnchantment(HEALS_ALLIES.get());
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
            entity.refreshDimensions();
            cap.setSpawned(true);*/
        }
    }
}
