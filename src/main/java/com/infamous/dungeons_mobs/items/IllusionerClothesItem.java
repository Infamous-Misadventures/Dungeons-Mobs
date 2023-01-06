package com.infamous.dungeons_mobs.items;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class IllusionerClothesItem extends GeoArmorItem implements IAnimatable {

    public IllusionerClothesItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    protected AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
                20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", LOOP));
        return PlayState.CONTINUE;
    }


}
