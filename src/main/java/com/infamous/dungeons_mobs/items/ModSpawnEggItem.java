package com.infamous.dungeons_mobs.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item.Properties;

public class ModSpawnEggItem extends SpawnEggItem {
    protected static final List<ModSpawnEggItem> UNADDED_EGGS = new ArrayList<>();
    private final Lazy<? extends EntityType<?>> entityTypeSupplier;
    private final EntityType<?> typeIn;

    public ModSpawnEggItem(final RegistryObject<? extends EntityType<?>> entityTypeSupplier, int primaryColorIn, int secondaryColorIn, Properties builder) {
        super(null, primaryColorIn, secondaryColorIn, builder);
        this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
        this.typeIn = null;
        UNADDED_EGGS.add(this);
    }


    public ModSpawnEggItem(EntityType<?> entityType, int primaryColorIn, int secondaryColorIn, Properties builder) {
        super(entityType, primaryColorIn, secondaryColorIn, builder);
        this.typeIn = entityType;
        this.entityTypeSupplier = null;
        UNADDED_EGGS.add(this);
    }

    public static void initSpawnEggs(){
        final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b");
        DefaultDispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior(){

            @Override
            protected ItemStack execute(IBlockSource source, ItemStack stack){
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                entityType.spawn(source.getLevel(), stack, null, source.getPos(), SpawnReason.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };

        for(final SpawnEggItem spawnEggItem : UNADDED_EGGS){
            EGGS.put(spawnEggItem.getType(null), spawnEggItem);
            DispenserBlock.registerBehavior(spawnEggItem, dispenseItemBehavior);
        }
        UNADDED_EGGS.clear();
    }

    public EntityType<?> getVanillaType(@Nullable CompoundNBT compoundNBT){
        if (compoundNBT != null && compoundNBT.contains("EntityTag", 10)) {
            CompoundNBT compoundnbt = compoundNBT.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8)) {
                return EntityType.byString(compoundnbt.getString("id")).orElse(this.typeIn);
            }
        }
        return this.typeIn;
    }

    @Override
    public EntityType<?> getType(CompoundNBT nbt){
        if(this.entityTypeSupplier == null){
            return this.getVanillaType(nbt);
        }
        else{
            return this.entityTypeSupplier.get();
        }
    }
}
