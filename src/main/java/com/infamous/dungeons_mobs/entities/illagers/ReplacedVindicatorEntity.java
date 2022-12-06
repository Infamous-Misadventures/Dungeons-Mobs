package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedProps;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedPropsHelper;
import com.infamous.dungeons_mobs.client.renderer.util.IGeoReplacedEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vindicator;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class ReplacedVindicatorEntity implements IAnimatable, IGeoReplacedEntity {

    public Mob entity;

    @Override
    public Mob getMob(){
        return this.entity;
    }

    @Override
    public void setMob(Mob mob){
        this.entity = mob;
    }

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        String animation = "animation.vindicator";
        if (false) {
            animation += "_mcd";
        }
        String handSide = "_right";
        if(this.entity.isLeftHanded()){
            handSide = "_left";
        }
        if(this.entity.getMainHandItem().isEmpty()){
            handSide = "_both";
        }
        String crossed = "";
        if(IllagerArmsUtil.armorHasCrossedArms((Vindicator) this.entity, this.entity.getItemBySlot(EquipmentSlot.CHEST))){
            crossed = "_crossed";
        }
        AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(entity);
        if (cap.attackAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".attack" + handSide, LOOP));
        } else if (this.entity.isAggressive() && !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".run" + handSide, LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".walk" + crossed, LOOP));
        } else {
            if (((Vindicator) this.entity).isCelebrating()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".win", LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".idle" + crossed, LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
