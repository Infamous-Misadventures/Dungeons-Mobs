package com.infamous.dungeons_mobs.entities.redstone;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class RedstoneMineEntity extends Entity implements IAnimatable {
    public static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(ConstructEntity.class, EntityDataSerializers.INT);

    private LivingEntity caster;
    private UUID casterUuid;

    //nerf
    private final float explosionRadius = 1.0F;
    public static final int LIFE_TIME = 250;

    public RedstoneMineEntity(Level worldIn) {
        super(ModEntityTypes.REDSTONE_MINE.get(), worldIn);
    }

    public RedstoneMineEntity(EntityType<? extends RedstoneMineEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public RedstoneMineEntity(Level worldIn, double x, double y, double z, int delay, LivingEntity casterIn) {
        this(ModEntityTypes.REDSTONE_MINE.get(), worldIn);
        this.setLifeTicks(delay);
        this.setCaster(casterIn);
        this.setPos(x, y, z);
    }

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getLifeTicks() == 6) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_mine.deactive", LOOP));
        }
        if (this.getLifeTicks() == LIFE_TIME) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_mine.activate", LOOP));
        }
        if (this.getLifeTicks() < LIFE_TIME - 3 && this.getLifeTicks() > 4) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_mine.idle", LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public void setCaster(@Nullable LivingEntity livingEntity) {
        this.caster = livingEntity;
        this.casterUuid = livingEntity == null ? null : livingEntity.getUUID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity) entity;
            }
        }

        return this.caster;
    }

    protected float getRandomPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            for (int i = 0; i < 2; i++) {
                this.level.addParticle(ModParticleTypes.REDSTONE_SPARK.get(), this.getRandomX(1.1D), this.getRandomY(), this.getRandomZ(1.1D), -0.05D + this.random.nextDouble() * 0.05D, -0.05D + this.random.nextDouble() * 0.05D, -0.05D + this.random.nextDouble() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */

    public void tick() {
        super.tick();

        if (!this.level.isClientSide && this.random.nextInt(20) == 0) {
            this.playSound(ModSoundEvents.REDSTONE_GOLEM_SPARK.get(), 0.1F, this.getRandomPitch());
            this.level.broadcastEntityEvent(this, (byte) 4);
        }

        this.setLifeTicks(this.getLifeTicks() - 1);
        if (!this.level.isClientSide) {
            if (this.getLifeTicks() <= 0) {
                this.remove(RemovalReason.DISCARDED);
            } else if (this.getLifeTicks() < LIFE_TIME - 3 && this.getLifeTicks() > 6) {
                for (LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3D, 0.3D, 0.3D))) {
                    this.explode(livingentity);
                }
            }
        }

    }


    private void explode(LivingEntity livingentity) {
        LivingEntity Caster = this.getCaster();

        if (livingentity.isAlive() && !livingentity.isInvulnerable()) {
            if (Caster != null) {
                if (!Caster.isAlliedTo(livingentity) || livingentity != Caster) {
                    this.level.explode(Caster, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionRadius, Explosion.BlockInteraction.NONE);
                    this.remove(RemovalReason.DISCARDED);
                }
            } else {
                this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionRadius, Explosion.BlockInteraction.NONE);
                this.remove(RemovalReason.DISCARDED);
            }

        }
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFE_TICKS, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setLifeTicks(compound.getInt("LifeTicks"));
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("LifeTicks", this.getLifeTicks());
        if (this.casterUuid != null) {
            compound.putUUID("Owner", this.casterUuid);
        }
    }

    public int getLifeTicks() {
        return this.entityData.get(LIFE_TICKS);
    }

    public void setLifeTicks(int p_189794_1_) {
        this.entityData.set(LIFE_TICKS, p_189794_1_);
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
