package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_libraries.client.renderer.gearconfig.ArmorGearModel;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.MageEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class MageArmorGearModel<T extends ArmorGear> extends ArmorGearModel<T> {

    LivingEntity wearer;

    public LivingEntity getWearer() {
        return wearer;
    }

    public void setWearer(LivingEntity wearer) {
        this.wearer = wearer;
    }

    @Override
    public void setCustomAnimations(T entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        IBone cloak = this.getAnimationProcessor().getBone("armorCloak");

        cloak.setHidden(cloak != null && this.getWearer() != null && (this.getWearer() instanceof MageEntity || this.getWearer() instanceof MageCloneEntity));

        IBone rightArm = this.getAnimationProcessor().getBone("armorRightArm");
        IBone leftArm = this.getAnimationProcessor().getBone("armorLeftArm");
        if (!DungeonsMobsConfig.COMMON.ENABLE_3D_SLEEVES.get()) {
            rightArm.setHidden(true);
            leftArm.setHidden(true);
        }
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double currentTick) {
        super.setMolangQueries(animatable, currentTick);

        MolangParser parser = GeckoLibCache.getInstance().parser;
        Vec3 velocity = wearer.getDeltaMovement();
        float groundSpeed = Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
        parser.setValue("query.ground_speed", () -> groundSpeed * 13);
    }
}
