package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class IceCloudEntity extends Entity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("m",true));
        return PlayState.CONTINUE;
    }
    private int floatTicks = 180;
    public int fallTime = 0;
    private float fallHurtAmount = 3.0F;
    private LivingEntity caster;
    private UUID casterUuid;
    private LivingEntity target;
    private UUID targetUUID;
    private double heightAboveTarget = 2.0D;
    private double heightAdjustment = (1.0F - this.getBbHeight()) / 2.0F;

    public IceCloudEntity(World worldIn) {
        super(ModEntityTypes.ICE_CLOUD.get(), worldIn);
    }
    public IceCloudEntity(EntityType<? extends IceCloudEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public IceCloudEntity(World worldIn, LivingEntity casterIn, LivingEntity targetIn) {
        this(ModEntityTypes.ICE_CLOUD.get(), worldIn);
        this.setCaster(casterIn);
        this.setTarget(targetIn);
        this.moveTo(targetIn.getX(),
                targetIn.getY() + targetIn.getBbHeight() + 0.5,
                targetIn.getZ());

        this.blocksBuilding = true;
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = targetIn.getX();
        this.yo = targetIn.getY(1.0D) + heightAboveTarget + heightAdjustment;
        this.zo = targetIn.getZ();

    }


    protected void defineSynchedData() {
    }

    private void tryToFloatAboveTarget(LivingEntity targetIn) {
        List<IceCloudEntity> nearbyIceClouds = this.level.getEntities(
                ModEntityTypes.ICE_CLOUD.get(),
                this.getBoundingBox().inflate(1, 0.5, 1),
                (nearbyEntity) -> nearbyEntity != this);

        if(nearbyIceClouds.isEmpty()) {
            //this.moveTo(this.getX() + Math.max(Math.min(targetIn.getX()-this.getX(),0.21),-0.21),this.getY() + Math.max(Math.min(targetIn.getY(1.0D) - this.getY() + (targetIn.getBbHeight() / 2),0.21),-0.21),this.getZ() + Math.max(Math.min(targetIn.getZ()-this.getZ(),0.21),-0.21);
            this.setDeltaMovement(getDeltaMovement().add(
                    1 * Math.max(Math.min((targetIn.getX() - this.getX()) / 40, 0.15), -0.15) / 5,
                    0,
                    1 * Math.max(Math.min((targetIn.getZ() - this.getZ()) / 40, 0.15), -0.15) / 5
            ));
            this.setDeltaMovement(getDeltaMovement().add(
                    0,
                    1 * Math.max(Math.min((targetIn.getY() + targetIn.getBbHeight() + 0.5 - this.getY() ) / 40, 0.1), -0.05),
                    0
            ));
        }
    }



    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUUID();
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

    public void setTarget(@Nullable LivingEntity target) {
        this.target = target;
        this.targetUUID = target == null ? null : target.getUUID();
    }

    @Nullable
    public LivingEntity getTarget() {
        if (this.target == null && this.targetUUID != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.targetUUID);
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    @Override
    public void tick() {
        BlockPos e = this.blockPosition();
        BlockState o = this.level.getBlockState(e);
        if (!o.is(Blocks.MOVING_PISTON) && this.isOnGround() && this.isInWall()) {
            List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(2, 1.25, 2)));
            for(Entity entity : list) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    damage(livingEntity,  12);
                }
            }
            this.spawnIceExplosionCloud();
            this.remove();
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.8D, 0.8D, 0.8D));
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if(this.floatTicks > 0){
            this.floatTicks--;
            this.move(MoverType.SELF, this.getDeltaMovement());
            if(this.target != null && !this.level.isClientSide){
                this.tryToFloatAboveTarget(this.target);
            }
        }
        else{
            if (this.fallTime < 0) {
                if (!this.level.isClientSide) {
                    this.remove();
                    return;
                }
            }
            this.fallTime++;

            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(
                        0.0D,
                        -1.0D,
                        0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            if (!this.level.isClientSide) {
                BlockPos iceCloudPosition = this.blockPosition();

                if (!this.onGround ) {
                    if (!this.level.isClientSide
                            && (this.fallTime > 100
                            && (iceCloudPosition.getY() < 1
                            || iceCloudPosition.getY() > 256)
                            || this.fallTime > 600)) {
                        this.remove();
                    }
                } else{
                    BlockState blockstate = this.level.getBlockState(iceCloudPosition);
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -3.65D, 0.7D));
                    if (!blockstate.is(Blocks.MOVING_PISTON)) {
                        this.spawnIceExplosionCloud();
                        this.remove();
                    }
                    //if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                    //     this.spawnIceExplosionCloud();
                    //    this.remove();
                    //}
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }

    public void spawnIceExplosionCloud(){
        AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
        areaeffectcloudentity.setParticle(ParticleTypes.EXPLOSION);
        areaeffectcloudentity.setRadius(3.0F);
        areaeffectcloudentity.setDuration(0);
        this.level.addFreshEntity(areaeffectcloudentity);
    }


    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        int distanceFallen = MathHelper.ceil(distance - 1.0F);
        if (distanceFallen > 0) {
            List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(2, 0.5, 2)));
            for(Entity entity : list) {
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    damage(livingEntity, distanceFallen * 2);
                }
            }/*
            for(Entity entity : list1) {
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    damage(livingEntity, (int) (distanceFallen * 0.75));
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 5));
                }
            }
            for(Entity entity : list2) {
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    damage(livingEntity, (int) (distanceFallen * 0.5));
                    livingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 200, 5));
                }
            }
            */
        }
        return false;
    }

    private void damage(LivingEntity targetEntity, int distanceFallen) {
        LivingEntity caster = this.getCaster();
        DamageSource summonedFallingBlockDamageSource = new IndirectEntityDamageSource("iceCloud", this, caster);
        float damageAmount = (float)MathHelper.floor((float)distanceFallen * this.fallHurtAmount);
        if (targetEntity.isAlive() && !targetEntity.isInvulnerable() && targetEntity != caster) {
            if (caster == null) {
                targetEntity.hurt(summonedFallingBlockDamageSource, damageAmount);
            } else {
                if (caster.isAlliedTo(targetEntity)) {
                    return;
                }
                targetEntity.hurt(summonedFallingBlockDamageSource, damageAmount);
                targetEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 5));
                targetEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 200, 5));
                targetEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 200, 5));
            }

        }
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.removed;
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.fallTime = compound.getInt("Time");
        this.fallHurtAmount = compound.getFloat("FallHurtAmount");
        this.setFloatTicks(compound.getInt("FloatTicks"));
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }
        if (compound.hasUUID("Target")) {
            this.casterUuid = compound.getUUID("Target");
        }

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Time", this.fallTime);
        compound.putFloat("FallHurtAmount", this.fallHurtAmount);
        compound.putInt("FloatTicks", this.getFloatTicks());
        if (this.casterUuid != null) {
            compound.putUUID("Owner", this.casterUuid);
        }
        if (this.targetUUID != null) {
            compound.putUUID("Target", this.targetUUID);
        }
    }

    public int getFloatTicks() {
        return this.floatTicks;
    }

    public void setFloatTicks(int floatTicksIn){
        this.floatTicks = floatTicksIn;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    /**
     * Return whether this entity should be rendered as on fire.
     */
    @OnlyIn(Dist.CLIENT)
    public boolean displayFireAnimation() {
        return false;
    }
}