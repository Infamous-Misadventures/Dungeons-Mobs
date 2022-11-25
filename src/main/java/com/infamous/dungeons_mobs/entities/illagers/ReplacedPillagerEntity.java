package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.client.renderer.util.IGeoReplacedEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ReplacedPillagerEntity implements IAnimatable, IGeoReplacedEntity {

    public MobEntity entity;

    @Override
    public MobEntity getMobEntity(){
        return this.entity;
    }

    @Override
    public void setMobEntity(MobEntity mob){
        this.entity = mob;
    }

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        String animation = "animation.pillager";
        if (false) {
            animation += "_mcd";
        }
        String handSide = ".right";
        if(this.entity.isLeftHanded()){
            handSide = ".left";
        }
        if(this.entity.getMainHandItem().isEmpty()){
            handSide += ".both";
        }
        if(IllagerArmsUtil.armorHasCrossedArms((PillagerEntity) this.entity, this.entity.getItemBySlot(EquipmentSlotType.CHEST))){
        }
        if (((PillagerEntity) this.entity).isChargingCrossbow()) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".charge" + handSide, true));
        }else if (this.entity.isAggressive() && !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".run" + handSide, true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".walk" + handSide, true));
        } else {
            if (((PillagerEntity) this.entity).isCelebrating()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".win", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".idle" + handSide, true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
