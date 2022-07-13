package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class QuickGrowingVineEntity extends VineEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);
    public QuickGrowingVineEntity(World world) {
        super(ModEntityTypes.QUICK_GROWING_VINE.get(), world);
    }

    public QuickGrowingVineEntity(EntityType<? extends QuickGrowingVineEntity> entityType, World world) {
        super(entityType, world);
    }

    public QuickGrowingVineEntity(EntityType<? extends QuickGrowingVineEntity> entityType, World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(entityType, worldIn, x, y, z, casterIn, lifeTicks);
    }

    public QuickGrowingVineEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(ModEntityTypes.QUICK_GROWING_VINE.get(), worldIn, x, y, z, casterIn, lifeTicks);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VineEntity.setCustomAttributes();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                !this.entityData.get(DI) ? 0 : 3
                , this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {

        if (this.entityData.get(DI)) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.quick_growing_vine.death", false));
            return PlayState.CONTINUE;

        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.quick_growing_vine.burst", true));
            return PlayState.CONTINUE;
        }
    }
}
