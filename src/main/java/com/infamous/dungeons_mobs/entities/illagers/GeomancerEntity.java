package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.goals.AvoidBaseEntityGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class GeomancerEntity extends SpellcastingIllagerEntity {

    public GeomancerEntity(World world){
        super(ModEntityTypes.GEOMANCER.get(), world);
    }

    public GeomancerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new SpellcastingIllagerEntity.CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        // Custom goal to avoid Geomancer constructs
        this.goalSelector.addGoal(3, new AvoidBaseEntityGoal<>(this, ConstructEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new GeomancerEntity.SummonPillarsGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.GEOMANCER_STAFF.get()));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    class SummonPillarsGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private SummonPillarsGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = GeomancerEntity.this.getTarget();
            if (targetEntity != null && GeomancerEntity.this.distanceTo(targetEntity) > 4.0D) {
                return super.canUse();
            }
            return false;
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 140;
        }

        protected void performSpellCasting() {
            LivingEntity targetEntity = GeomancerEntity.this.getTarget();
            if (targetEntity != null) {
                if(GeomancerEntity.this.getRandom().nextFloat() < 0.25F){
                    GeomancyHelper.summonOffensiveConstruct(GeomancerEntity.this, targetEntity, ModEntityTypes.GEOMANCER_BOMB.get());
                }
                else{
                    int[] rowToRemove = Util.getRandom(GeomancyHelper.ROWS, GeomancerEntity.this.getRandom());
                    GeomancyHelper.summonAreaDenialTrap(targetEntity, targetEntity, ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
                }
            }
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
            return SpellcastingIllagerEntity.SpellType.SUMMON_VEX;
        }
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }
}
