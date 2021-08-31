package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GeomancerWallEntity extends ConstructEntity implements IAnimatable {

	AnimationFactory factory = new AnimationFactory(this);
	
    public GeomancerWallEntity(World world) {
        super(ModEntityTypes.GEOMANCER_WALL.get(), world);
    }

    public GeomancerWallEntity(EntityType<? extends GeomancerWallEntity> entityType, World world) {
        super(entityType, world);
    }

    public GeomancerWallEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn) {
        super(ModEntityTypes.GEOMANCER_WALL.get(), worldIn, x, y, z, casterIn, lifeTicksIn);
    }
    
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.ATTACK_DAMAGE, 0.0D);
    }
    
    @Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}
    
    public void baseTick() {
    	super.baseTick();
    	
    	if (this.getLifeTicks() == 100) {
            this.playSound(ModSoundEvents.GEOMANCER_WALL_SPAWN.get(), 1.0F, 1.0F);
    	}
    	
    	if (this.getLifeTicks() == 40) {
            this.playSound(ModSoundEvents.GEOMANCER_WALL_DESPAWN.get(), 1.0F, 1.0F);
    	}
    }
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.getLifeTicks() <= 100) {
			if (this.getLifeTicks() < 40) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_disappear", true));				
			} else if (this.getLifeTicks() > 75) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_appear", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_idle", true));
			}
		}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

  //  @Override
  //  public IPacket<?> getAddEntityPacket() {
  //      return NetworkHooks.getEntitySpawningPacket(this);
  //  }
}
