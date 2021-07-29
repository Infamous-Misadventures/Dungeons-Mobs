package com.infamous.dungeons_mobs.entities.undead.horseman;

import com.infamous.dungeons_mobs.entities.undead.ArmoredSkeletonEntity;
import com.infamous.dungeons_mobs.interfaces.IMountUser;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SkeletonHorsemanEntity extends ArmoredSkeletonEntity implements IMountUser {

    private final TriggerHorsemanTrapGoal horsemanTrapGoal = new TriggerHorsemanTrapGoal(this);
    private boolean isHorsemanTrap;
    private int horsemanTrapTime;


    public SkeletonHorsemanEntity(World worldIn){
        super(ModEntityTypes.SKELETON_HORSEMAN.get(), worldIn);
    }

    public SkeletonHorsemanEntity(EntityType<? extends SkeletonHorsemanEntity> entityType, World world) {
        super(entityType, world);
        this.lookControl = new HorsemanLookController(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ArmoredSkeletonEntity.setCustomAttributes();
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(ModList.get().isLoaded("dungeons_gear")){

            Item REINFORCED_MAIL_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "reinforced_mail_helmet"));
            Item LONGBOW = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "longbow"));

            ItemStack reinforcedMailHelmet = new ItemStack(REINFORCED_MAIL_HELMET);
            ItemStack longbow = new ItemStack(LONGBOW);

            this.setItemSlot(EquipmentSlotType.HEAD, reinforcedMailHelmet);
            this.setItemSlot(EquipmentSlotType.MAINHAND, longbow);
        }
        else{
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBT) {
        livingEntityDataIn = super.finalizeSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBT);
        if(spawnReason != SpawnReason.TRIGGERED){
            this.setTrap(true);
        }
        return livingEntityDataIn;
    }

    public void aiStep() {
        super.aiStep();
        if (this.isTrap() && this.horsemanTrapTime++ >= 18000) {
            this.remove();
        }
    }



    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof SkeletonHorsemanEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    private boolean isTrap() {
        return this.isHorsemanTrap;
    }

    void setTrap(boolean trap) {
        if (trap != this.isHorsemanTrap) {
            this.isHorsemanTrap = trap;
            if (trap) {
                this.goalSelector.addGoal(1, this.horsemanTrapGoal);
            } else {
                this.goalSelector.removeGoal(this.horsemanTrapGoal);
            }
        }
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HorsemanTrap", this.isTrap());
        compound.putInt("HorsemanTrapTime", this.horsemanTrapTime);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setTrap(compound.getBoolean("HorsemanTrap"));
        this.horsemanTrapTime = compound.getInt("HorsemanTrapTime");
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        super.performRangedAttack(target, distanceFactor);
        if(this.getVehicle() instanceof AbstractHorseEntity){
            AbstractHorseEntity horseEntity = (AbstractHorseEntity) this.getVehicle();
            horseEntity.makeMad();
        }
    }

    static class HorsemanLookController extends LookController {
        HorsemanLookController(MobEntity mob) {
            super(mob);
        }

        @Override
        public void tick() {
            super.tick();
            if(this.mob.isPassenger() && this.mob.getTarget() != null && isNotGettingAttackedByAWolf()){
                this.setLookAt(this.mob.getTarget(), this.mob.getMaxHeadYRot(), this.mob.getMaxHeadXRot());
            }
        }

        private boolean isNotGettingAttackedByAWolf() {
            return !(this.mob.getKillCredit() instanceof WolfEntity);
        }
    }
}
