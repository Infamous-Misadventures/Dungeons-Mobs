package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

// Model and animation received from CQR and DerToaster
public class MountaineerModel extends AnimatedGeoModel<MountaineerEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(MountaineerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/modelbipedgecko.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(MountaineerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/geo_illager.geo.json") ;
    }

    @Override
    public ResourceLocation getTextureLocation(MountaineerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/mountaineer.png");
    }

    @Override
    public void setLivingAnimations(MountaineerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("bipedHead");
        IBone illagerArms = this.getAnimationProcessor().getBone("illagerArms");
        
        illagerArms.setHidden(true);

        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        if (entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == entity.getArmorSet().getChest().get()) {
            cape.setHidden(false);
        } else {
            cape.setHidden(true);
        }

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }
    
	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick) {
		super.setMolangQueries(animatable, currentTick);
		
		MolangParser parser = GeckoLibCache.getInstance().parser;
		LivingEntity livingEntity = (LivingEntity) animatable;
		Vector3d velocity = livingEntity.getDeltaMovement();
		float groundSpeed = MathHelper.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		parser.setValue("query.ground_speed", groundSpeed * 20);
	}
}
