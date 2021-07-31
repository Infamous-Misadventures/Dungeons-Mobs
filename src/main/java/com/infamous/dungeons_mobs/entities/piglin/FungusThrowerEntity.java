package com.infamous.dungeons_mobs.entities.piglin;

import com.infamous.dungeons_mobs.entities.piglin.ai.FungusThrowerAi;
import com.infamous.dungeons_mobs.mixin.PiglinAccessor;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class FungusThrowerEntity extends PiglinEntity {

    public FungusThrowerEntity(EntityType<? extends FungusThrowerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        Brain<?> brain = super.makeBrain(dynamic);
        //noinspection unchecked
        FungusThrowerAi.addFungusThrowerTasks((Brain<FungusThrowerEntity>)brain);
        return brain;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT compoundNBT) {
        ILivingEntityData spawnData = super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, spawnDataIn, compoundNBT);
        if(spawnReason != SpawnReason.STRUCTURE){
            if(this.isAdult()){
                this.setItemSlot(EquipmentSlotType.MAINHAND, ModItems.BLUE_NETHERSHROOM.get().getDefaultInstance());
            }
        }
        return spawnData;
    }

    @Override
    protected void finishConversion(ServerWorld serverWorld) {
        if (this.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM) && !this.getOffhandItem().isEmpty()) {
            this.spawnAtLocation(this.getOffhandItem());
            this.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
        ((PiglinAccessor)this).getInventory().removeAllItems().forEach(this::spawnAtLocation);
        ZombifiedFungusThrowerEntity zombifiedFungusThrower = this.convertTo(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(), true);
        if (zombifiedFungusThrower != null) {
            zombifiedFungusThrower.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, zombifiedFungusThrower);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        // NO-OP
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(DifficultyInstance p_180483_1_) {
        // NO-OP
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack replacement, ItemStack current) {
        boolean canReplaceCurrentItem = super.canReplaceCurrentItem(replacement, current);

        boolean takeReplacement =
                FungusThrowerAi.isBlueNethershroom(replacement);
        boolean keepCurrent =
                FungusThrowerAi.isBlueNethershroom(current);
        if (takeReplacement && !keepCurrent) {
            return true;
        } else if (!takeReplacement && keepCurrent) {
            return canReplaceCurrentItem;
        } else {
            return (!this.isAdult() || !FungusThrowerAi.isBlueNethershroom(replacement) || !(FungusThrowerAi.isBlueNethershroom(current))) && canReplaceCurrentItem;
        }
    }

}
