package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.client.event.RenderGeoEntityEvent;
import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.capabilities.properties.IMobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.IAnimatable;

import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.getEnchantableCapabilityLazy;
import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.BURNING;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.HUGE;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class HugeMobEnchantment extends MobEnchantment {

    public HugeMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onEntityEventSize(EntityEvent.Size event) {
        Entity entity = event.getEntity();

        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if (cap.hasEnchantment(HUGE.get())) {
                float totalWidth = entity.getDimensions(entity.getPose()).width * 2.5F;
                float totalHeight = entity.getDimensions(entity.getPose()).height * 2.5F;
                event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * 2.5F);
                event.setNewSize(EntitySize.fixed(totalWidth, totalHeight));
            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if (cap.hasEnchantment(HUGE.get())) {
                event.getMatrixStack().pushPose();
                event.getMatrixStack().scale(2.5F, 2.5F, 2.5F);

            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if (cap.hasEnchantment(HUGE.get())) {
                event.getMatrixStack().popPose();
            }
        });

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderGeoLivingEventPre(RenderGeoEntityEvent.Pre<? extends LivingEntity> event) {
        final LivingEntity entity = event.getEntity();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if (cap.hasEnchantment(HUGE.get())) {
                event.getMatrixStack().pushPose();
                event.getMatrixStack().scale(2.5F, 2.5F, 2.5F);

            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderGeoLivingEventPost(RenderGeoEntityEvent.Post<? extends LivingEntity> event) {
        final LivingEntity entity = event.getEntity();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if (cap.hasEnchantment(HUGE.get())) {
                event.getMatrixStack().popPose();
            }
        });

    }
}