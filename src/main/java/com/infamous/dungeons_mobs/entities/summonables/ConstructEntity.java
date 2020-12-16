package com.infamous.dungeons_mobs.entities.summonables;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("EntityConstructor")
public abstract class ConstructEntity extends Entity {
    private int lifeTicks;
    private LivingEntity caster;
    private UUID casterUuid;

    protected ConstructEntity(EntityType<? extends ConstructEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    protected ConstructEntity(EntityType<? extends ConstructEntity> entityTypeIn, World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn){
        this(entityTypeIn, worldIn);
        this.setLifeTicks(lifeTicksIn);
        this.setCaster(casterIn);
        this.setPosition(x, y, z);
    }

    @Override
    public void registerData(){

    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean func_241845_aY() {
        return this.isAlive();
    }

    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUniqueID();
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

    public void faceDirection(Direction directionToFace){
        float currentRotationYaw = this.rotationYaw;

        Direction currentDirection = this.getAdjustedHorizontalFacing();
        float rotationAmount = 0;
        while(currentDirection != directionToFace){
            currentDirection = currentDirection.rotateY();
            rotationAmount += 90.0F;
        }
        this.rotationYaw = currentRotationYaw + rotationAmount;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */


    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem() {
        return false;
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
        this.lifeTicks--;
        if(!this.world.isRemote() && this.lifeTicks <= 0){
            this.handleExpiration();
        }
        else{
            this.handleExistence();
        }
    }
}
