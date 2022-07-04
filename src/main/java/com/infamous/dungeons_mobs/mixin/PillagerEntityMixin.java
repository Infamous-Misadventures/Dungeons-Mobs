package com.infamous.dungeons_mobs.mixin;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.illagers.*;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.DungeonsEvokerEntity;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.DungeonsIllusionerEntity;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.MinecraftIllusionerEntity;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.VindicatorRaidCaptainEntity;
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

    public int p;

    @Shadow protected abstract void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_);

    @Shadow protected abstract void enchantSpawnedWeapon(float p_241844_1_);

    protected PillagerEntityMixin(EntityType<? extends AbstractIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {

        this.p = this.getRandom().nextInt(3);

        if (this.isPatrolLeader() && !this.isPatrolling()) {
            PillagerEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition();
            VindicatorRaidCaptainEntity vv = new VindicatorRaidCaptainEntity(this.level);
            vv.moveTo(vvf, 0F, 0F);
            vv.load(v1);
            vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
            v.level.addFreshEntity(vv);
        }else if (!this.isPatrolLeader() && this.getCurrentRaid() == null && this.p < 1 && !this.isPatrolLeader() && p_213386_3_ != SpawnReason.SPAWN_EGG&& p_213386_3_ != SpawnReason.SPAWNER) {
            PillagerEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition().offset(4,0,0);
            BlockPos vvvvf = v.blockPosition();
            this.p = this.getRandom().nextInt(50);
            int r = this.getRandom().nextInt(250);
            if (!(r ==0)) {
                if (this.p <= 30) {
                    DungeonsVindicatorEntity vv = new DungeonsVindicatorEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p <= 36) {
                    ArmoredVindicatorEntity vv = new ArmoredVindicatorEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p <= 43) {
                    ArmoredPillagerEntity vv = new ArmoredPillagerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p <= 44) {
                    SquallGolemEntity vv = new SquallGolemEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p == 45) {
                    IceologerEntity vv = new IceologerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p == 46) {
                    GeomancerEntity vv = new GeomancerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p == 47) {
                    WindcallerEntity vv = new WindcallerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (this.p == 48) {
                    RoyalGuardEntity vv = new RoyalGuardEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else {
                    MageEntity vv = new MageEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                }
            }else {
                r = this.getRandom().nextInt(3);
                if (r==1){
                    MinecraftIllusionerEntity vv = new MinecraftIllusionerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                } else if (r==2) {
                    DungeonsIllusionerEntity vv = new DungeonsIllusionerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                }else {
                    DungeonsEvokerEntity vv = new DungeonsEvokerEntity(this.level);
                    vv.moveTo(vvf, 0F, 0F);
                    vv.load(v1);
                    vv.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
                    vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
                    v.level.addFreshEntity(vv);
                }
            }
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
