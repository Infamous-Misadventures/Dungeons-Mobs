package com.infamous.dungeons_mobs.capabilities.convertible;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.water.SunkenSkeletonEntity;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.tags.CustomTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConvertibleHelper {

    @Nullable
    public static IConvertible getConvertibleCapability(Entity entity)
    {
        LazyOptional<IConvertible> lazyCap = entity.getCapability(ConvertibleProvider.CONVERTIBLE_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the convertible capability from the Entity!"));
        }
        return null;
    }

    public static void onDrownedAndConvertedTo(MobEntity original, MobEntity convertedTo){
        if(original instanceof AbstractSkeletonEntity && convertedTo instanceof SunkenSkeletonEntity){
            if (!original.isSilent()) {
                original.level.levelEvent((PlayerEntity)null, 1040, original.blockPosition(), 0);
            }
            DungeonsMobs.LOGGER.info("Converted {} to {}", original, convertedTo);
        }
        if(original instanceof IArmoredMob && convertedTo instanceof IArmoredMob){
            if(((IArmoredMob) original).hasStrongArmor()){
                ((IArmoredMob) convertedTo).setStrongArmored(convertedTo);
            }
        }

        if(original instanceof ZombieEntity && convertedTo instanceof ZombieEntity){
            ZombieEntity originalZombie = (ZombieEntity) original;
            ZombieEntity convertedToZombie = (ZombieEntity) convertedTo;
            handleZombieAttributes(convertedToZombie);
            setZombieCanBreakDoors(originalZombie, convertedToZombie);
        }
        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(original, convertedTo);
    }

    private static void setZombieCanBreakDoors(ZombieEntity originalZombie, ZombieEntity convertedToZombie) {
        Method supportsBreakDoorGoalMethod = ObfuscationReflectionHelper.findMethod(ZombieEntity.class, "func_204900_dz");
        try {
            convertedToZombie.setCanBreakDoors((Boolean)supportsBreakDoorGoalMethod.invoke(convertedToZombie) && originalZombie.canBreakDoors());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void handleZombieAttributes(ZombieEntity convertedToZombie) {
        Method handleAttributesMethod = ObfuscationReflectionHelper.findMethod(ZombieEntity.class, "func_207304_a", Float.class);
        try {
            handleAttributesMethod.invoke(convertedToZombie, convertedToZombie.level.getCurrentDifficultyAt(convertedToZombie.blockPosition()).getSpecialMultiplier());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static EntityType<? extends MobEntity> getDrowningConvertTo(MobEntity mob) {
        // default, we will keep converting the mob to itself if it doesn't have an appropriate conversion
        EntityType<? extends MobEntity> convertToType = (EntityType<? extends MobEntity>) mob.getType();
        if(mob instanceof AbstractSkeletonEntity){
            convertToType = mob instanceof IArmoredMob ? ModEntityTypes.ARMORED_SUNKEN_SKELETON.get() : ModEntityTypes.SUNKEN_SKELETON.get();
        }
        else if(mob instanceof ZombieEntity){
            convertToType = mob instanceof IArmoredMob ? ModEntityTypes.ARMORED_DROWNED.get() : EntityType.DROWNED;
        }
        return convertToType;
    }

    public static boolean convertsInWater(MobEntity mobEntity) {
        return mobEntity.getType().is(CustomTags.CONVERTS_IN_WATER);
    }
}
