package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public class GeomancerBombEntity extends ConstructEntity implements IAnimatable {
	
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
    private float explosionRadius = 3.0F;

    public GeomancerBombEntity(Level worldIn){
        super(ModEntityTypes.GEOMANCER_BOMB.get(), worldIn);
    }

    public GeomancerBombEntity(Level worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn) {
        super(ModEntityTypes.GEOMANCER_BOMB.get(), worldIn, x, y, z, casterIn, lifeTicksIn);
    }

    public GeomancerBombEntity(EntityType<? extends GeomancerBombEntity> explodingPillarEntityEntityType, Level world) {
        super(explodingPillarEntityEntityType, world);
    }

    @Override
    public void handleExpiration() {
        super.handleExpiration();
        if (!this.level.isClientSide) {
            this.explode();
        }
    }

    private void explode() {
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionRadius, Explosion.BlockInteraction.NONE);
    }
    
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.ATTACK_DAMAGE, 0.0D);
    }
    
    @Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.getLifeTicks() > 75) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_appear", PLAY_ONCE));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_pillar_idle", LOOP));
		}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}


 //  @Override
 //  public IPacket<?> getAddEntityPacket() {
 //       return NetworkHooks.getEntitySpawningPacket(this);
 //   }
}
