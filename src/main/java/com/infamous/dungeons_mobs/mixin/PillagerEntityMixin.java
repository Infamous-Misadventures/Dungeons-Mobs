package com.infamous.dungeons_mobs.mixin;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.*;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends AbstractIllagerEntity {

    // let dungeons mob spawn in outpost

    public int p;

    @Shadow protected abstract void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_);

    @Shadow protected abstract void enchantSpawnedWeapon(float p_241844_1_);

    protected PillagerEntityMixin(EntityType<? extends AbstractIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.PILLAGER_DEATH.get();
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {

        if (this.getCurrentRaid() == null && !this.isPatrolLeader() && p_213386_3_ != SpawnReason.SPAWN_EGG && p_213386_3_ != SpawnReason.SPAWNER) {
            PillagerEntityMixin v = this;
            BlockPos vvf = v.blockPosition();
            int e = v.getRandom().nextInt(5);
            for (int t = 0; t < e + 3 ; t ++) {
                int o = 0;
                int r = v.getRandom().nextInt(258);
                this.p = this.getRandom().nextInt(60);
                vvf = vvf.offset(-2.5 + this.getRandom().nextInt(5) + this.getRandom().nextDouble(), 0, -2.5 +this.getRandom().nextInt(5) + this.getRandom().nextDouble());
                do {
                    if (v.level.isEmptyBlock(vvf) && !v.level.isEmptyBlock(vvf.offset(0,-1,0))) {
                        break;
                    }else if (!v.level.isEmptyBlock(vvf)) {
                        o++;
                        vvf = vvf.offset(0, 1, 0);
                    }else {
                        o++;
                        vvf = vvf.offset(0, -1, 0);
                    }
                }while (o < 20);
                if (!(r == 0)) {
                    if (this.p <= 35) {
                        DungeonsPillagerEntity vv = new DungeonsPillagerEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else if (this.p <= 50) {
                        DungeonsVindicatorEntity vv = new DungeonsVindicatorEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else if (this.p <= 54) {
                        ArmoredVindicatorEntity vv = new ArmoredVindicatorEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else if (this.p <= 57) {
                        ArmoredPillagerEntity vv = new ArmoredPillagerEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else if (this.p <= 58) {
                        PillagerRaidCaptainEntity vv = new PillagerRaidCaptainEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else {
                        VindicatorRaidCaptainEntity vv = new VindicatorRaidCaptainEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    }
                } else {
                    r = this.getRandom().nextInt(5);
                    if (r == 1) {
                        MinecraftIllusionerEntity vv = new MinecraftIllusionerEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else if (r == 2) {
                        DungeonsIllusionerEntity vv = new DungeonsIllusionerEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    } else {
                        DungeonsEvokerEntity vv = new DungeonsEvokerEntity(this.level);
                        vv.moveTo(vvf, 0F, 0F);
                        vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                        v.level.addFreshEntity(vv);
                    }
                }
            }
            v.remove();
        }
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.applyEnchantmentBuffs();
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    public void applyEnchantmentBuffs() {
        if (this.getRandom().nextInt(100) <= 10 || (this.level.getDifficulty().getId() == 2 && this.getRandom().nextInt(100) <= 40) || (this.level.getDifficulty().getId() == 3 && this.getRandom().nextInt(100) <= 75)) {
            ItemStack mainhandWeapon = this.getMainHandItem();
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            float e = this.getRandom().nextFloat();
            enchantmentIntegerMap.put(Enchantments.QUICK_CHARGE, Math.min(this.getRandom().nextInt(2) + 1, 2));
            if (e <= 0.25) {
                enchantmentIntegerMap.put(Enchantments.MULTISHOT, 1);
            }
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
            this.applyEnchantmentBuffs();
        }
    }

    @Override
    public void applyRaidBuffs(int i, boolean b) {
        ItemStack mainhandWeapon = new ItemStack(Items.CROSSBOW);
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = (int) (2 + (i / 2.5));

        float o = this.getRandom().nextFloat();
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
        boolean applyEnchant = this.random.nextFloat() - 0.1 <= raid.getEnchantOdds();
        if (applyEnchant) {
            float e = this.getRandom().nextFloat();

            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.QUICK_CHARGE, Math.min(enchantmentLevel, 3));
            this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

            if (e <= 0.35) {
                enchantmentIntegerMap.put(Enchantments.MULTISHOT, 1);
            }

            enchantmentIntegerMap.put(Enchantments.VANISHING_CURSE, 1);

            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
        }

        if (i > raid.getNumGroups(Difficulty.EASY) && !(i > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 10.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
        }

        if (i > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 18.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.5D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("attack boost", 2.0D, AttributeModifier.Operation.ADDITION));
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
    }
}
