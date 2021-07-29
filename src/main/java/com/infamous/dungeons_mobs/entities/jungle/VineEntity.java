package com.infamous.dungeons_mobs.entities.jungle;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings({"EntityConstructor", "WeakerAccess"})
public abstract class VineEntity extends MobEntity implements IMob {
    private LivingEntity caster;
    private UUID casterUuid;
    private int lifeTicks;
    private boolean isPerishable;

    protected VineEntity(EntityType<? extends VineEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    protected VineEntity(EntityType<? extends VineEntity> entityTypeIn, World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn){
        this(entityTypeIn, worldIn);
        this.setPos(x, y, z);
        this.setCaster(casterIn);
        this.setLifeTicks(lifeTicksIn);
    }

    protected static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }
        this.setLifeTicks(compound.getInt("LifeTicks"));
        this.setPerishable(compound.getBoolean("IsPerishable"));
    }


    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.casterUuid != null) {
            compound.putUUID("Owner", this.casterUuid);
        }
        compound.putInt("LifeTicks", this.getLifeTicks());
        compound.putBoolean("IsPerishable", this.isPerishable());
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

    public boolean isPerishable(){
        return this.isPerishable;
    }

    public void setPerishable(boolean perishable) {
        this.isPerishable = perishable;
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public void setLifeTicks(int lifeTicksIn){
        this.setPerishable(true);
        this.lifeTicks = lifeTicksIn;
    }

    // used for some kind of collision checking
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public void push(Entity entity) {
        // NO-OP
    }

    @Override
    public void knockback(float strength, double ratioX, double ratioZ) {
        // NO-OP
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isPerishable && !this.level.isClientSide()){
            this.lifeTicks--;
            if(this.lifeTicks <= 0){
                this.remove();
            }
        }
    }

    public static boolean canVineSpawnInLight(EntityType<? extends VineEntity> type, IServerWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL
                && MonsterEntity.isDarkEnoughToSpawn(worldIn, pos, randomIn)
                && checkMobSpawnRules(type, worldIn, reason, pos, randomIn)
                && (reason == SpawnReason.SPAWNER || worldIn.canSeeSky(pos));
    }
}
