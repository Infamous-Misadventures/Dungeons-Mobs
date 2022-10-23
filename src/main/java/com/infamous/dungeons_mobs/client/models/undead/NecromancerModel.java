package com.infamous.dungeons_mobs.client.models.undead;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.infamous.dungeons_mobs.mod.ModItems;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

@OnlyIn(Dist.CLIENT)
public class NecromancerModel extends AnimatedGeoModel<NecromancerEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(NecromancerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "animations/necromancer.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(NecromancerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "geo/necromancer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(NecromancerEntity entity) {
        return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/skeleton/necromancer.png");
    }

    @Override
    public void setLivingAnimations(NecromancerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("bipedHead");
        IBone cape = this.getAnimationProcessor().getBone("bipedCape");
        
        IBone particles = this.getAnimationProcessor().getBone("staffParticles");
        
        if (entity.tickCount % 1 == 0 && particles instanceof GeoBone && entity.isSpellcasting()) {
        	GeoBone particleBone = ((GeoBone)particles);
        	entity.level.addParticle(ModParticleTypes.NECROMANCY.get(), particleBone.getWorldPosition().x, particleBone.getWorldPosition().y, particleBone.getWorldPosition().z, 0, 0, 0);
        }
        
        if (entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECROMANCER_ROBES.getChest().get()) {
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
