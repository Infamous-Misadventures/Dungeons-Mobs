package com.infamous.dungeons_mobs.mixin;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsVindicatorEntity;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.MinecraftIllusionerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
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
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends AbstractIllagerEntity {

    public SpawnReason spawnReason;

    @Shadow protected abstract void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_);


    protected VindicatorEntityMixin(EntityType<? extends AbstractIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.applyEnchantmentBuffs();
        this.spawnReason =p_213386_3_;
        if (p_213386_3_ != SpawnReason.STRUCTURE && this.getCurrentRaid() == null) {
            VindicatorEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition();
            Biome b = this.level.getBiome(blockPosition());
            ResourceLocation bb = b.getRegistryName();
            DungeonsVindicatorEntity vv = new DungeonsVindicatorEntity(this.level);
            vv.moveTo(vvf, 0F, 0F);
            vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
            vv.setUUID(v.getUUID());
            vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
            v.level.addFreshEntity(vv);
            v.remove();
        }
        this.s();
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }


    public void s(){
        if (this.getCurrentRaid() != null) {

            VindicatorEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition();
            Biome b = this.level.getBiome(blockPosition());
            ResourceLocation bb = b.getRegistryName();
            MinecraftIllusionerEntity vv = new MinecraftIllusionerEntity(this.level);
            vv.moveTo(vvf, 0F, 0F);
            vv.load(v1);
            vv.setCurrentRaid(v.getCurrentRaid());
            vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
            v.level.addFreshEntity(vv);

            v.moveTo(0,0,0);
        }
    }

    public void applyEnchantmentBuffs() {
        if (this.getRandom().nextInt(100) <= 10) {
            ItemStack mainhandWeapon = this.getMainHandItem();
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            float e = this.getRandom().nextFloat();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, Math.min(this.getRandom().nextInt(2)+1, 2));
            if (e <= 0.25) {
                enchantmentIntegerMap.put(Enchantments.KNOCKBACK, 1);
            }
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap,mainhandWeapon);
            this.applyEnchantmentBuffs();
        }
    }


}
