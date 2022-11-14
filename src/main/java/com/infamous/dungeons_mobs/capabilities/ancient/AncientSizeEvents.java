package com.infamous.dungeons_mobs.capabilities.ancient;


import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class AncientSizeEvents {
    @SubscribeEvent
    public static void onEntityEventSize(EntityEvent.Size event) {
        Entity entity = event.getEntity();

        IAncient cap = AncientHelper.getAncientCapability(entity);
        if (cap.isAncient()) {
            float totalWidth = event.getNewSize().width * 1.2F;
            float totalHeight = event.getNewSize().height * 1.2F;
            event.setNewEyeHeight(event.getNewEyeHeight() * 1.2F);
            event.setNewSize(EntitySize.fixed(totalWidth, totalHeight));
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        IAncient cap = AncientHelper.getAncientCapability(entity);
        if (cap.isAncient()) {
            event.getMatrixStack().pushPose();
                event.getMatrixStack().scale(1.1F, 1.1F, 1.1F);

        }

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        IAncient cap = AncientHelper.getAncientCapability(entity);
        if (cap.isAncient()) {
            event.getMatrixStack().popPose();
        }

    }
}