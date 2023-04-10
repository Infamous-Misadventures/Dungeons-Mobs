package com.infamous.dungeons_mobs.client.models.projectile;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OrbProjectileModel extends AnimatedGeoModel<NecromancerOrbEntity> {

    private final boolean renderTrail;

    public OrbProjectileModel(boolean renderTrail) {
        this.renderTrail = renderTrail;
    }

    @Override
    public ResourceLocation getAnimationResource(NecromancerOrbEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/necromancer_orb.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(NecromancerOrbEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/necromancer_orb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NecromancerOrbEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/projectile/orb_projectile_" + entity.textureChange % 3 + ".png");
    }

    @Override
    public void setCustomAnimations(NecromancerOrbEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone everything = this.getAnimationProcessor().getBone("everything");
        if(!renderTrail) {
            IBone trail = this.getAnimationProcessor().getBone("trail1");
            trail.setHidden(true);
        }

        everything.setRotationY(-1.5708F);
    }
}