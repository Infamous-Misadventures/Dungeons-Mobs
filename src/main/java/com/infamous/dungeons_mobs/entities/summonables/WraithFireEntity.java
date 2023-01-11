package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Predicate;

public class WraithFireEntity extends Entity implements IAnimatable, IAnimationTickable {
    private static final Predicate<Entity> ALIVE = (p_213685_0_) -> {
        return p_213685_0_.isAlive();
    };

    public int lifeTime;

    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Entity owner;

    public int textureChange = 0;

    public WraithFireEntity(EntityType<? extends WraithFireEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.lifeTime++;

        textureChange++;

        this.setYBodyRot(0);

        if (this.lifeTime == 1) {
            this.playSound(ModSoundEvents.WRAITH_FIRE.get(), 1.25F, this.random.nextFloat() * 0.7F + 0.3F);
        }

        if (this.random.nextInt(24) == 0 && !this.isSilent()) {
            this.level.playLocalSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
        }

        double particleOffsetAmount = 1.25;

        if (this.isBurning()) {
            for (double x = -particleOffsetAmount; x < particleOffsetAmount * 2; x = x + particleOffsetAmount) {
                for (double z = -particleOffsetAmount; z < particleOffsetAmount * 2; z = z + particleOffsetAmount) {
                    if (this.random.nextInt(10) == 0) {
                        this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + x, this.getY(), this.getZ() + z, this.random.nextGaussian() * 0.01, 0.1, this.random.nextGaussian() * 0.01);
                    }

                    if (this.random.nextInt(5) == 0) {
                        this.level.addParticle(ParticleTypes.SMOKE, this.getX() + x, this.getY(), this.getZ() + z, this.random.nextGaussian() * 0.01, 0.15, this.random.nextGaussian() * 0.01);
                    }
                }
            }
        }

        if (!this.level.isClientSide) {

            if (this.isInWaterOrBubble()) {
                this.remove(RemovalReason.DISCARDED);
                this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
            }

            if (this.isInRain()) {
                if (this.random.nextInt(40) == 0) {
                    this.remove(RemovalReason.DISCARDED);
                    this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
                }
            }


            if (this.lifeTime >= 82) {
                this.remove(RemovalReason.DISCARDED);
            }

            if (this.isBurning()) {
                List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), ALIVE);
                if (!list.isEmpty()) {
                    for (Entity entity : list) {
                        if (entity instanceof LivingEntity && this.canHarmEntity(entity)) {
                            entity.hurt(DamageSource.IN_FIRE, 4.0F);
                            entity.setSecondsOnFire(4);
                        }
                    }
                }
            }
        }
    }

    public boolean isInRain() {
        BlockPos blockpos = this.blockPosition();
        return this.level.isRainingAt(blockpos) || this.level.isRainingAt(new BlockPos(blockpos.getX(), this.getBoundingBox().maxY, blockpos.getZ()));
    }

    public boolean isBurning() {
        return this.lifeTime >= 20 && this.lifeTime <= 70;
    }

    @Override
    public int tickTimer() {
        return this.tickCount;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_fire_burn", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean canHarmEntity(Entity target) {
        return this.owner != null && this.owner instanceof Mob ? ((Mob) this.owner).getTarget() == target : !target.fireImmune();
    }
}
