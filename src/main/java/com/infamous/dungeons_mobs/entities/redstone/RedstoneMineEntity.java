package com.infamous.dungeons_mobs.entities.redstone;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public class RedstoneMineEntity extends Entity implements IAnimatable {
    public static final DataParameter<Integer> LIFE_TICKS = EntityDataManager.defineId(RedstoneMineEntity.class, DataSerializers.INT);

    private LivingEntity caster;
    private UUID casterUuid;

    //nerf
    private float explosionRadius = 2.0F;
    public static final int LIFE_TIME = 250;

    public RedstoneMineEntity(World worldIn) {
        super(ModEntityTypes.REDSTONE_MINE.get(), worldIn);
    }

    public RedstoneMineEntity(EntityType<? extends RedstoneMineEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public RedstoneMineEntity(World worldIn, double x, double y, double z, int delay, LivingEntity casterIn){
        this(ModEntityTypes.REDSTONE_MINE.get(), worldIn);
        this.setLifeTicks(delay);
        this.setCaster(casterIn);
        this.setPos(x, y, z);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getLifeTicks() <= 6) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_mine.deactivate", true));
        }else if (this.getLifeTicks() >= LIFE_TIME - 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_mine.activate", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.redstone_mine.idle", true));
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
        if (this.caster == null && this.casterUuid != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }



    /**
     * Called to update the entity's position/logic.
     */

    public void tick() {
        super.tick();
        this.setLifeTicks(this.getLifeTicks() - 1);
        if (!this.level.isClientSide) {
            if(this.getLifeTicks() <= 0) {
                this.remove();
            }else if (this.getLifeTicks() < LIFE_TIME - 3 && this.getLifeTicks() > 6){
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3D, 0.3D, 0.3D))) {
                    this.explode(livingentity);
                }
            }
        }

    }


    private void explode(LivingEntity livingentity) {
        LivingEntity Caster = this.getCaster();

            if (livingentity.isAlive() && !livingentity.isInvulnerable()) {
                if (Caster != null) {
                    if (!Caster.isAlliedTo(livingentity) || livingentity != Caster && !(livingentity instanceof AbstractRaiderEntity)) {
                        this.level.explode(Caster, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionRadius, Explosion.Mode.NONE);
                        this.remove();
                    }
                } else {
                    this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionRadius, Explosion.Mode.NONE);
                    this.remove();
                }

        }
    }



    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFE_TICKS, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.setLifeTicks(compound.getInt("LifeTicks"));
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
