package com.infamous.dungeons_mobs.entities.projectiles;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GeoOrbEntity extends DamagingProjectileEntity implements IAnimatable {
//Shot Code
        //GeoOrbEntity laserOrb = new GeoOrbEntity(shooter.level, shooter, 0, 0, 0, 8f);
        //float f = (float) MathHelper.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
        //
        //double x = this.getX() + Math.cos(f) * 1.2;
        //double z = this.getZ() + Math.sin(f) * 1.2;
        //laserOrb.setPos(x, shooter.getY(0.5D) + 0.5D, z);
        //double xAccel = target.getX() - laserOrb.getX();
        //double yAccel = target.getY() - shooter.getY();
        //double zAccel = target.getZ() - laserOrb.getZ();
        //laserOrb.shoot(xAccel, yAccel, zAccel, 0.145f, 0.0F);
        //shooter.level.addFreshEntity(laserOrb);
    
    private int timer;
    private float damage;

    AnimationFactory factory = new AnimationFactory(this);

    public GeoOrbEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, float f) {
        super(ModEntityTypes.GEO_ORB.get(), shooter, accelX, accelY, accelZ, worldIn);
        this.setTimer(0);
        this.damage = f;
    }

    public GeoOrbEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public GeoOrbEntity(World p_i50173_2_) {
        super(ModEntityTypes.GEO_ORB.get(), p_i50173_2_);
    }

    @Override
    public float getBrightness() {
        return 1.5f;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.necromancer_proyectile.idle", true));

        return PlayState.CONTINUE;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level.isClientSide || (entity == null || !entity.removed) && this.level.hasChunkAt(this.blockPosition())) {
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, d0 - vector3d.x * 0.25D, d1 - vector3d.y * 0.25D, d2 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
                }

            }

            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower));
            //this.level.addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.remove();
        }

        this.setTimer(this.getTimer() + 1);
        if (!this.level.isClientSide()) {
            if (this.getTimer() >= 200) {
                this.remove();
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHit(RayTraceResult p_70227_1_) {
        super.onHit(p_70227_1_);
        RayTraceResult.Type raytraceresult$type = p_70227_1_.getType();
        if (raytraceresult$type != RayTraceResult.Type.ENTITY) {
            if (!this.level.isClientSide()) {
                if (!(this.getOwner() instanceof NamelessOneEntity)) {
                    this.remove();
                }
            }
        }
    }

    protected void onHitEntity(EntityRayTraceResult entityRTR) {
        super.onHitEntity(entityRTR);
        if (!this.level.isClientSide) {
            Entity target = entityRTR.getEntity();
            Entity owner = this.getOwner();
            boolean didHurt = target.hurt(DamageSource.indirectMagic(this, owner), this.damage); // twice as much damage as normal fire
            if (didHurt && owner instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)owner, target);
                {
                    this.remove();
                }
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
