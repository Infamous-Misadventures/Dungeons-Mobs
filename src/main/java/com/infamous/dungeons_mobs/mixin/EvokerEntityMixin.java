package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.entities.illagers.minibosses.DungeonsEvokerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(EvokerEntity.class)
public abstract class EvokerEntityMixin extends SpellcastingIllagerEntity {

    protected EvokerEntityMixin(EntityType<? extends SpellcastingIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    public void s(){
        if (this.getCurrentRaid() != null) {

                EvokerEntityMixin v = this;
                CompoundNBT v1 = new CompoundNBT();
                v1 = v.saveWithoutId(v1);
                BlockPos vvf = v.blockPosition();
                Biome b = this.level.getBiome(blockPosition());
                ResourceLocation bb = b.getRegistryName();
                DungeonsEvokerEntity vv = new DungeonsEvokerEntity(this.level);
                vv.moveTo(vvf, 0F, 0F);
                vv.load(v1);
                vv.setCurrentRaid(v.getCurrentRaid());
                vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                v.level.addFreshEntity(vv);

            if(!ModList.get().isLoaded("illagersweararmor")) {
                vv.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                vv.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                vv.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                vv.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }

                v.moveTo(0,0,0);
            }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.s();
        if (p_213386_3_ != SpawnReason.STRUCTURE && this.getCurrentRaid() == null) {
            EvokerEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition();
            Biome b = this.level.getBiome(blockPosition());
            ResourceLocation bb = b.getRegistryName();
            DungeonsEvokerEntity vv = new DungeonsEvokerEntity(this.level);
            vv.moveTo(vvf, 0F, 0F);
            vv.load(v1);
            vv.setUUID(v.getUUID());
            vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
            v.level.addFreshEntity(vv);
            v.remove();
        }
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

}
