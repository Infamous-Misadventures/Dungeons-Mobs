package com.infamous.dungeons_mobs.entities.projectiles;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.MathHelper;
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

public class NecromancerOrbEntity extends Entity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer_proyectile.idle", true));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public NecromancerOrbEntity(EntityType<? extends NecromancerOrbEntity> entityType, World world) {
        super(entityType, world);
    }

    public NecromancerOrbEntity(World worldIn) {
        super(ModEntityTypes.N_LASER_ORB.get(), worldIn);
    }

    public int fallTime = 12;
    private int floatTicks = 350;
    private boolean i=false;
    private float fallHurtAmount = 3.0F;
    private LivingEntity caster;
    private UUID casterUuid;
    private LivingEntity target;
    private UUID targetUUID;


    public NecromancerOrbEntity(World worldIn, LivingEntity casterIn, LivingEntity targetIn) {
        this(ModEntityTypes.N_LASER_ORB.get(), worldIn);
        this.setCaster(casterIn);
        this.setTarget(targetIn);
        this.setDeltaMovement(
                Math.max(Math.min((targetIn.getX() - casterIn.getX()),1),-1),
                Math.max(Math.min((targetIn.getY(1.0) - casterIn.getY(1.25)),1),-1),
                Math.max(Math.min((targetIn.getZ() - casterIn.getZ()),1),-1)
        );
        this.setFloatTicks(350);
        this.i=false;

    }

    public NecromancerOrbEntity(World worldIn, LivingEntity casterIn, LivingEntity targetIn,double j) {
        this(ModEntityTypes.N_LASER_ORB.get(), worldIn);
        this.setCaster(casterIn);
        this.setTarget(targetIn);
        this.setDeltaMovement(
                Math.max(Math.min((targetIn.getX(j*3) - casterIn.getX()),1),-1),
                Math.max(Math.min((targetIn.getY(1.0) - casterIn.getY(1.25)),1),-1),
                Math.max(Math.min((targetIn.getZ(j*3) - casterIn.getZ()),1),-1)
        );
        this.setFloatTicks(350);
        this.i=false;

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
        if (this.getTarget() != null) {
            this.setDeltaMovement(getDeltaMovement().add(
                    0,
                    1 * Math.max(Math.min((this.getTarget().getY() + this.getTarget().getBbHeight() + 0.5 - this.getY()) / 40, 0.2), -0.1),
                    0
            ));
        }
        this.move(MoverType.SELF, this.getDeltaMovement().multiply(0.25,0.25,0.25));
        List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(1, 0.5, 1)));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    damage(livingEntity);
                }
            }
            if (this.i) {
                this.remove();
            }
        }
        this.setFloatTicks(this.getFloatTicks()-1);
        if (this.getFloatTicks()<=0){
            this.remove();
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    private void damage(LivingEntity targetEntity) {
        LivingEntity caster = this.getCaster();
        DamageSource summonedFallingBlockDamageSource = new IndirectEntityDamageSource("Orb", this, caster);
        float damageAmount = (float) MathHelper.floor((float) 8);
        if (targetEntity.isAlive() && !targetEntity.isInvulnerable() && targetEntity != caster) {
            if (caster == null) {
                targetEntity.hurt(summonedFallingBlockDamageSource, damageAmount);
            } else {
                if (caster.isAlliedTo(targetEntity)) {
                    return;
                }
                targetEntity.setDeltaMovement(0,Math.max(targetEntity.getAttributeValue(Attributes.ATTACK_KNOCKBACK)/4.75, 0.12),0);
                targetEntity.hurt(summonedFallingBlockDamageSource.setMagic(), damageAmount);
                this.i=true;
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
