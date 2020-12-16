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
    private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(CobwebTrapEntity.class, DataSerializers.BLOCK_POS);

    public CobwebTrapEntity(World worldIn) {
        super(ModEntityTypes.COBWEB_TRAP.get(), worldIn);
    }

    public CobwebTrapEntity(EntityType<? extends CobwebTrapEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }
    public CobwebTrapEntity(World worldIn, double x, double y, double z, int lifeTicksIn){
        this(ModEntityTypes.COBWEB_TRAP.get(), worldIn);
        this.lifeTicks = lifeTicksIn;
        this.setPosition(x, y, z);
        this.setOrigin(this.getPosition());
    }

    @Override
    protected void registerData() {
        this.dataManager.register(ORIGIN, BlockPos.ZERO);
    }


    public void setOrigin(BlockPos origin) {
        this.dataManager.set(ORIGIN, origin);
    }

    public BlockPos getOrigin() {
        return this.dataManager.get(ORIGIN);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setLifeTicks(compound.getInt("LifeTicks"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("LifeTicks", this.getLifeTicks());
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
        if(!this.world.isRemote()){
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
        entityIn.setMotionMultiplier(Blocks.COBWEB.getDefaultState(), new Vector3d(0.25D, 0.05D, 0.25D));
    }

    private void checkCollisions() {
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(0.2D, 0.2D, 0.2D));
        for (Entity entity : list) {
            if (!entity.removed && !this.removed) {
                entity.setMotionMultiplier(Blocks.COBWEB.getDefaultState(), new Vector3d(0.25D, 0.05D, 0.25D));
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
