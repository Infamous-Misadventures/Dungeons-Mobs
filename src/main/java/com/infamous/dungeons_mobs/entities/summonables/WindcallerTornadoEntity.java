package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class WindcallerTornadoEntity extends Entity implements IAnimatable {

    private static final EntityDataAccessor<Boolean> BLAST = SynchedEntityData.defineId(WindcallerTornadoEntity.class,
            EntityDataSerializers.BOOLEAN);

    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public int lifeTime;

    public static EntityDimensions blastDimensions = EntityDimensions.scalable(0.75F, 2.0F);

    public WindcallerTornadoEntity(Level worldIn) {
        super(ModEntityTypes.TORNADO.get(), worldIn);
    }

    public WindcallerTornadoEntity(EntityType<? extends WindcallerTornadoEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_213305_1_) {
        return this.isBlast() ? blastDimensions : super.getDimensions(p_213305_1_);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isBlast()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_tornado_blast", LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_tornado_lift", LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.refreshDimensions();

        if (!this.isBlast()) {
            List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), Entity::isAlive);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (this.lifeTime == 15) {
                            livingEntity.push(0, 1.5, 0);
                        }
                    }

                }
            }

            if (this.lifeTime >= 15 && this.lifeTime < 30) {
                if (this.level.isClientSide) {
                    for (int i = 0; i < 3; i++) {
                        this.level.addParticle(ModParticleTypes.WIND.get(), this.getRandomX(0.5D), this.getRandomY() - 2, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 1.0D, 5, (this.random.nextDouble() - 0.5D) * 1.0D);
                    }
                }
            }
        }

        this.lifeTime++;

        int removeTime = this.isBlast() ? 14 : 36;

        if (this.lifeTime >= removeTime && !this.level.isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BLAST, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {

    }

    public boolean isBlast() {
        return this.entityData.get(BLAST);
    }

    public void setBlast(boolean attached) {
        this.entityData.set(BLAST, attached);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
