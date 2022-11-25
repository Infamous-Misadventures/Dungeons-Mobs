package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.utils.GoalUtils;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedProps;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedPropsHelper;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.ReplacedModdedAttackGoal;
import com.infamous.dungeons_mobs.network.NetworkHandler;
import com.infamous.dungeons_mobs.network.message.AnimatedPropsMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ReplacedIllagerEvents {


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof VindicatorEntity) {
            VindicatorEntity vindicatorEntity = (VindicatorEntity) entity;
            GoalUtils.removeGoal(vindicatorEntity.goalSelector, MeleeAttackGoal.class);
            vindicatorEntity.goalSelector.addGoal(4, new ReplacedModdedAttackGoal<>(vindicatorEntity, null, 20));
            vindicatorEntity.goalSelector.addGoal(5, new ApproachTargetGoal(vindicatorEntity, 0, 1.0D, true));
            AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(vindicatorEntity);
            cap.setAttackAnimationLength(7);
            cap.setAttackAnimationActionPoint(6);
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event){

        LivingEntity livingEntity = event.getEntityLiving();
        if(livingEntity instanceof VindicatorEntity){
            tickDownAnimTimers((MobEntity) livingEntity);
        }
    }

    public static void tickDownAnimTimers(MobEntity mobEntity) {
        AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(mobEntity);
        if (cap.getAttackAnimationTick() > 0) {
            cap.setAttackAnimationTick(cap.getAttackAnimationTick() - 1);
//            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> mobEntity), new AnimatedPropsMessage(mobEntity.getId(), cap));
        }
    }

}
