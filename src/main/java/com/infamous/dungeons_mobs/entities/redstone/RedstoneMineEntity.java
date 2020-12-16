package com.infamous.dungeons_mobs.entities.redstone;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RedstoneMineEntity extends Entity {
    private static final int LIFE_TICKS = 8 * 20;
    private int lifeTicks;
    private LivingEntity caster;
    private UUID casterUuid;
    private float explosionRadius = 3.0F;

    public RedstoneMineEntity(World worldIn) {
        super(ModEntityTypes.REDSTONE_MINE.get(), worldIn);
    }

    public RedstoneMineEntity(EntityType<? extends RedstoneMineEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public RedstoneMineEntity(World worldIn, double x, double y, double z, LivingEntity casterIn){
        this(ModEntityTypes.REDSTONE_MINE.get(), worldIn);
        this.setCaster(casterIn);
        this.setPosition(x, y, z);
        this.lifeTicks = LIFE_TICKS;
    }

    public void setCaster(@Nullable LivingEntity livingEntity) {
        this.caster = livingEntity;
        this.casterUuid = livingEntity == null ? null : livingEntity.getUniqueID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    /**
     * Returns the <b>solid</b> collision bounding box for this entity. Used to make (e.g.) boats solid. Return null if
     * this entity is not solid.
     */
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    /*
    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn) {
        super.onCollideWithPlayer(entityIn);
        this.explode();
    }

     */

    private void explode() {
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), this.explosionRadius, Explosion.Mode.NONE);
        this.remove();
    }

    public void handleExistence(){
        this.func_233566_aG_(); // handles being in water
    }

    public void handleExpiration(){
        this.remove();
    }

    /**
     * Called to update the entity's position/logic.
     */

    @Override
    public void tick() {
        //super.tick();
        if(!this.world.isRemote){
            this.lifeTicks--;
            //this.checkCollisions();
            if(this.lifeTicks <= 0){
                this.handleExpiration();
            }
        }
        else{
            this.handleExistence();
        }
    }

    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn) {
        if(entityIn != this.getCaster()){
            this.explode();
        }
    }

    private void checkCollisions() {
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(0.2D, 0.2D, 0.2D));
        for (Entity entity : list) {
            if (!entity.removed && !this.removed && entity != this.getCaster()) {
                this.explode();
            }
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setLifeTicks(compound.getInt("LifeTicks"));
        if (compound.hasUniqueId("Owner")) {
            this.casterUuid = compound.getUniqueId("Owner");
        }

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("LifeTicks", this.getLifeTicks());
        if (this.casterUuid != null) {
            compound.putUniqueId("Owner", this.casterUuid);
        }
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public void setLifeTicks(int lifeTicksIn){
        this.lifeTicks = lifeTicksIn;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
