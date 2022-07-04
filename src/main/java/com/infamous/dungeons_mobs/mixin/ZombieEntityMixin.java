package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.entities.undead.DungeonsZombieEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MonsterEntity {

    protected ZombieEntityMixin(EntityType<? extends MonsterEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
    }


    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        if (p_213386_3_ != SpawnReason.SPAWN_EGG && p_213386_3_ != SpawnReason.STRUCTURE) {
            ZombieEntityMixin v = this;
            CompoundNBT v1 = new CompoundNBT();
            v1 = v.saveWithoutId(v1);
            BlockPos vvf = v.blockPosition();
            DungeonsZombieEntity vv = new DungeonsZombieEntity(this.level);
            vv.moveTo(vvf, 0F, 0F);
            vv.load(v1);
            vv.setUUID(v.getUUID());
            vv.addEffect(new EffectInstance(Effects.HARM, 10, 6, (false), (false)));
            v.level.addFreshEntity(vv);
        }
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

}
