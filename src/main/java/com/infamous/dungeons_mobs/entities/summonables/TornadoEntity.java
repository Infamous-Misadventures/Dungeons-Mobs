package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.entities.illagers.MageEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
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

public class TornadoEntity extends Entity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().animationSpeed = 0.5;
        if (this.getFloatTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tornado.ready", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tornado.casting", false));
        }
        return PlayState.CONTINUE;
    }
    public int fallTime = 12;
    private int floatTicks = 12;
    private float fallHurtAmount = 3.0F;
    private LivingEntity caster;
    private UUID casterUuid;
    private LivingEntity target;
    private UUID targetUUID;
    private double heightAboveTarget = 2.0D;
    private double heightAdjustment = (1.0F - this.getBbHeight()) / 2.0F;

    public TornadoEntity(World worldIn) {
        super(ModEntityTypes.TORNADO.get(), worldIn);
    }
    public TornadoEntity(EntityType<? extends TornadoEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public TornadoEntity(World worldIn, LivingEntity casterIn, LivingEntity targetIn, int Timer) {
        this(ModEntityTypes.TORNADO.get(), worldIn);
        this.setCaster(casterIn);
        this.setTarget(targetIn);
        this.moveTo(targetIn.getX(),
                targetIn.getY(),
                targetIn.getZ());

        this.blocksBuilding = true;
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = targetIn.getX();
        this.yo = targetIn.getY(1.0D) + heightAboveTarget + heightAdjustment;
        this.zo = targetIn.getZ();
        //this.setFloatTicks(Timer);
        this.fallTime = 0;

    }

    protected void defineSynchedData() {
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
        if(this.floatTicks > 0){
            this.floatTicks--;
        }
        else{
            if(this.fallTime >= 5 && this.fallTime <= 20){
                List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(2, 0.5, 2)));
                for(Entity entity : list) {
                    if(entity instanceof LivingEntity){
                        LivingEntity livingEntity = (LivingEntity)entity;
                        livingEntity.push(0, 0.75, 0);
                        //damage(livingEntity);
                    }
                }
            }
            if (this.fallTime == 4) {
                this.playSound(ModSoundEvents.WINDCALLER_LIFT_WIND.get(), 1.5f,1);
            }

            if (this.fallTime >= 40) {
                this.remove();
            }

            this.fallTime++;
        }
    }

    private void damage(LivingEntity targetEntity) {
        LivingEntity caster = this.getCaster();
        DamageSource summonedFallingBlockDamageSource = new IndirectEntityDamageSource("Tornado", this, caster);
        float damageAmount = (float)MathHelper.floor((float) 8);
        if (targetEntity.isAlive() && !targetEntity.isInvulnerable() && targetEntity != caster) {
            if (caster == null) {
                targetEntity.hurt(summonedFallingBlockDamageSource, damageAmount);
            } else {
                if (caster.isAlliedTo(targetEntity)) {
                    return;
                }
                targetEntity.setDeltaMovement(targetEntity.getDeltaMovement().add(0,Math.max(targetEntity.getAttributeValue(Attributes.ATTACK_KNOCKBACK)/4.75, 0.12),0));
                targetEntity.hurt(summonedFallingBlockDamageSource, damageAmount);
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