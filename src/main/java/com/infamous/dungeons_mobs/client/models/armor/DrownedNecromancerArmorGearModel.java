package com.infamous.dungeons_mobs.client.models.armor;

import com.infamous.dungeons_libraries.client.model.ArmorGearModel;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class DrownedNecromancerArmorGearModel<T extends ArmorGear> extends ArmorGearModel<T> {

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
        
    	if (this.getWearer() != null && this.getWearer() instanceof DrownedNecromancerEntity) {
    		cloak.setHidden(true);
    	} else {
    		cloak.setHidden(false);
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
