package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_libraries.client.model.ArmorGearModel;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.illagers.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class IllusionerArmorGearModel<T extends ArmorGear> extends ArmorGearModel<T> {

    LivingEntity wearer;

    public LivingEntity getWearer() {
        return wearer;
    }

    public void setWearer(LivingEntity wearer) {
        this.wearer = wearer;
    }

    @Override
    public void setLivingAnimations(T entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone cloak = this.getAnimationProcessor().getBone("armorCloak");
        IBone rightArm = this.getAnimationProcessor().getBone("armorRightArm");
        IBone leftArm = this.getAnimationProcessor().getBone("armorLeftArm");

        if (this.getWearer() != null && (this.getWearer() instanceof DungeonsIllusionerEntity || this.getWearer() instanceof IllusionerCloneEntity)) {
            if(!DungeonsMobsConfig.COMMON.ENABLE_3D_SLEEVES.get()){
                rightArm.setHidden(true);
                leftArm.setHidden(true);
            }
        }
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double currentTick) {
        super.setMolangQueries(animatable, currentTick);

        MolangParser parser = GeckoLibCache.getInstance().parser;
        Vector3d velocity = wearer.getDeltaMovement();
        float groundSpeed = MathHelper.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
        parser.setValue("query.ground_speed", () -> groundSpeed * 13);
    }
}
