package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsVindicatorEntity;
import com.infamous.dungeons_mobs.entities.illagers.RoyalGuardEntity;
import com.infamous.dungeons_mobs.goals.RangedWebAttackGoal;
import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends AbstractIllagerEntity {

    protected VindicatorEntityMixin(EntityType<? extends AbstractIllagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);

        VindicatorEntityMixin v = this;
        CompoundNBT v1 = new CompoundNBT();
        v1 = v.saveWithoutId(v1);
        BlockPos vvf=v.blockPosition();
        DungeonsVindicatorEntity vv = new DungeonsVindicatorEntity(this.level);
        vv.moveTo(vvf,0F,0F);
        vv.load(v1);
        vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
        v.level.addFreshEntity(vv);

        v.remove();

        return spawnData;
    }
}
