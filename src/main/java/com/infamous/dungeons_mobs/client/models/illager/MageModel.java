package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class MageModel extends HeadTurningAnimatedGeoModel {

    @Override
    public ResourceLocation getAnimationFileLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/mage.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Object entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/geo_illager.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity) {
        if(DungeonsMobsConfig.COMMON.ENABLE_3D_SLEEVES.get()){
            return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/mage.png");
        }else{
            return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/mage_sleeved.png");
        }
    }

    @Override
    public void setCustomAnimations(IAnimatable entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);

        IBone illagerArms = this.getAnimationProcessor().getBone("illagerArms");
        
        illagerArms.setHidden(true);

        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        if(entity instanceof SpawnArmoredMob && entity instanceof Mob) {
            Mob mobEntity = (Mob) entity;
            cape.setHidden(mobEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() != ((SpawnArmoredMob) entity).getArmorSet().getChest().get());
        }
    }
    
	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick) {
		super.setMolangQueries(animatable, currentTick);
		
		MolangParser parser = GeckoLibCache.getInstance().parser;
		LivingEntity livingEntity = (LivingEntity) animatable;
		Vec3 velocity = livingEntity.getDeltaMovement();
		float groundSpeed = Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		parser.setValue("query.ground_speed", () -> groundSpeed * 15);
	}
}

