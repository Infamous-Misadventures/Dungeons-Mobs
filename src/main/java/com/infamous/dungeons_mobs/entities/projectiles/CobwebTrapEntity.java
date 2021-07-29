package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class CobwebTrapEntity extends Entity {
    private int lifeTicks;
    private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.defineId(CobwebTrapEntity.class, DataSerializers.BLOCK_POS);

    public CobwebTrapEntity(World worldIn) {
        super(ModEntityTypes.COBWEB_TRAP.get(), worldIn);
    }

    public CobwebTrapEntity(EntityType<? extends CobwebTrapEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }
    public CobwebTrapEntity(World worldIn, double x, double y, double z, int lifeTicksIn){
        this(ModEntityTypes.COBWEB_TRAP.get(), worldIn);
        this.lifeTicks = lifeTicksIn;
        this.setPos(x, y, z);
        this.setOrigin(this.blockPosition());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ORIGIN, BlockPos.ZERO);
    }


    public void setOrigin(BlockPos origin) {
        this.entityData.set(ORIGIN, origin);
    }

    public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.setLifeTicks(compound.getInt("LifeTicks"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("LifeTicks", this.getLifeTicks());
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public void setLifeTicks(int lifeTicksIn){
        this.lifeTicks = lifeTicksIn;
    }

    public void handleExistence(){
        this.updateInWaterStateAndDoFluidPushing(); // handles being in water
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
        if(!this.level.isClientSide()){
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
    public void playerTouch(PlayerEntity entityIn) {
        entityIn.makeStuckInBlock(Blocks.COBWEB.defaultBlockState(), new Vector3d(0.25D, 0.05D, 0.25D));
    }

    private void checkCollisions() {
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(0.2D, 0.2D, 0.2D));
        for (Entity entity : list) {
            if (!entity.removed && !this.removed) {
                entity.makeStuckInBlock(Blocks.COBWEB.defaultBlockState(), new Vector3d(0.25D, 0.05D, 0.25D));
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
