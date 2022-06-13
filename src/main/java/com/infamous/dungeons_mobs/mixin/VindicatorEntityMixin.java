package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.entities.illagers.DungeonsVindicatorEntity;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.DungeonsEvokerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

import static net.minecraftforge.common.BiomeManager.BiomeType.ICY;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends AbstractIllagerEntity {

    protected VindicatorEntityMixin(EntityType<? extends AbstractIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    public void s(){
        if (this.getCurrentRaid() != null) {
            VindicatorEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition();
            Biome b = this.level.getBiome(blockPosition());
            ResourceLocation bb = b.getRegistryName();
            DungeonsVindicatorEntity vv = new DungeonsVindicatorEntity(this.level);
            vv.moveTo(vvf, 0F, 0F);
            vv.load(v1);
            vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
            v.level.addFreshEntity(vv);

            v.remove();
        }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.s();
        this.populateDefaultEquipmentSlots(p_213386_2_);
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

}