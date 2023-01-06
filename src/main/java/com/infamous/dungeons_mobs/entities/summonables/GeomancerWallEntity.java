package com.infamous.dungeons_mobs.entities.summonables;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GeomancerWallEntity extends ConstructEntity implements IAnimatable {

	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
    public GeomancerWallEntity(Level world) {
        super(ModEntityTypes.GEOMANCER_WALL.get(), world);
    }

    public GeomancerWallEntity(EntityType<? extends GeomancerWallEntity> entityType, Level world) {
        super(entityType, world);
    }

    public GeomancerWallEntity(Level worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn) {
        super(ModEntityTypes.GEOMANCER_WALL.get(), worldIn, x, y, z, casterIn, lifeTicksIn);
    }
    
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.ATTACK_DAMAGE, 0.0D);
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
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_disappear", HOLD_ON_LAST_FRAME));
			} else if (this.getLifeTicks() > 75) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_appear", PLAY_ONCE));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_idle", LOOP));
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
