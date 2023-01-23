package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class WindcallerModel extends HeadTurningAnimatedGeoModel<WindcallerEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(WindcallerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/windcaller.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(WindcallerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/windcaller.geo.json") ;
	}

	@Override
	public ResourceLocation getTextureLocation(WindcallerEntity entity) {
		if(DungeonsMobsConfig.COMMON.ENABLE_3D_SLEEVES.get()){
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/windcaller.png");
		}else{
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/windcaller_sleeved.png");
		}
	}

	@Override
	public void setCustomAnimations(WindcallerEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);

		IBone cape = this.getAnimationProcessor().getBone("bipedCape");
		cape.setHidden(entity.getItemBySlot(EquipmentSlot.CHEST).getItem() != entity.getArmorSet().getChest().get());
	}
}
