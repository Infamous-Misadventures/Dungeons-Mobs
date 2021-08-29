package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class WhispererEntity extends MonsterEntity implements IMagicUser {
    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(WhispererEntity.class, DataSerializers.BYTE);
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    public WhispererEntity(World worldIn) {
        super(ModEntityTypes.WHISPERER.get(), worldIn);
    }

    public WhispererEntity(EntityType<? extends WhispererEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.addMovementBehaviors();
        this.goalSelector.addGoal(1, new UsingMagicGoal<>(this));
        this.goalSelector.addGoal(2, new WhispererEntity.SummonVinesGoal());
        this.goalSelector.addGoal(3, new WhispererEntity.AttackGoal());
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VineEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, MobEntity.class, 8.0F));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, WhispererEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    protected void addMovementBehaviors() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.6D));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5D) // same as Evoker
                .add(Attributes.FOLLOW_RANGE, 12.0D) // same as Evoker
                .add(Attributes.MAX_HEALTH, 24.0D) // same as Evoker
                .add(Attributes.ATTACK_DAMAGE, 5.0D); // same as Pillager
    }

    class AttackGoal extends MeleeAttackGoal{

        private AttackGoal() {
            super(WhispererEntity.this, 1.0D, false);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null && WhispererEntity.this.distanceToSqr(targetEntity) < 16.0D) {
                return super.canUse();
            }
            return false;
        }
    }

    class SummonVinesGoal extends UseMagicGoal<WhispererEntity> {
        private SummonVinesGoal() {
            super(WhispererEntity.this);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null && WhispererEntity.this.distanceToSqr(targetEntity) >= 16.0D) {
                return super.canUse();
            }
            return false;
        }

        protected int getMagicUseTime() {
            return 40;
        }

        protected int getMagicUseInterval() {
            return 140;
        }

        protected void useMagic() {
            LivingEntity targetEntity = WhispererEntity.this.getTarget();
            if (targetEntity != null) {
                if(WhispererEntity.this.getRandom().nextFloat() < 0.25F){
                    GeomancyHelper.summonOffensiveVine(WhispererEntity.this, WhispererEntity.this, WhispererEntity.this.getPoisonVineType());
                }
                else{
                    int[] rowToRemove = Util.getRandom(GeomancyHelper.ROWS, WhispererEntity.this.getRandom());
                    GeomancyHelper.summonAreaDenialVineTrap(targetEntity, targetEntity, WhispererEntity.this.getQuickGrowingVineType(), rowToRemove);
                }
            }
        }


        protected SoundEvent getMagicPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected MagicType getMagicType() {
            return MagicType.SUMMON_VINES;
        }
    }

    protected EntityType<? extends QuickGrowingVineEntity> getQuickGrowingVineType() {
        return ModEntityTypes.QUICK_GROWING_VINE.get();
    }

    protected EntityType<? extends PoisonQuillVineEntity> getPoisonVineType() {
        return ModEntityTypes.POISON_QUILL_VINE.get();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGIC, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        IMagicUser.spawnMagicParticles(this);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.magicUseTicks > 0) {
            --this.magicUseTicks;
        }
    }


    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.magicUseTicks = compound.getInt("MagicUseTicks");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MagicUseTicks", this.magicUseTicks);
    }

    // IMAGICUSER METHODS
    @Override
    public boolean isUsingMagic() {
        if (this.level.isClientSide) {
            return this.entityData.get(MAGIC) > 0;
        } else {
            return this.magicUseTicks > 0;
        }
    }
    @Override
    public int getMagicUseTicks() {
        return this.magicUseTicks;
    }

    @Override
    public void setMagicUseTicks(int magicUseTicksIn) {
        this.magicUseTicks = magicUseTicksIn;
    }

    @Override
    public MagicType getMagicType() {
        return !this.level.isClientSide ? this.activeMagic : MagicType.getFromId(this.entityData.get(MAGIC));
    }

    @Override
    public void setMagicType(MagicType magicType) {
        this.activeMagic = magicType;
        this.entityData.set(MAGIC, (byte)magicType.getId());
    }

    @Override
    public SoundEvent getMagicSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }
}
