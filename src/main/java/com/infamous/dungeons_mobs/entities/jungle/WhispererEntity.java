package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.goals.AvoidBaseEntityGoal;
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
    private static final DataParameter<Byte> MAGIC = EntityDataManager.createKey(WhispererEntity.class, DataSerializers.BYTE);
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    public WhispererEntity(World worldIn) {
        super(ModEntityTypes.WHISPERER.get(), worldIn);
    }

    public WhispererEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new UsingMagicGoal<>(this));
        this.goalSelector.addGoal(2, new WhispererEntity.SummonVinesGoal());
        this.goalSelector.addGoal(3, new WhispererEntity.AttackGoal());
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VineEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, MobEntity.class, 8.0F));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, WhispererEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D) // same as Evoker
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D) // same as Evoker
                .createMutableAttribute(Attributes.MAX_HEALTH, 24.0D) // same as Evoker
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D); // same as Pillager
    }

    class AttackGoal extends MeleeAttackGoal{

        private AttackGoal() {
            super(WhispererEntity.this, 1.0D, false);
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity targetEntity = WhispererEntity.this.getAttackTarget();
            if (targetEntity != null && WhispererEntity.this.getDistanceSq(targetEntity) < 16.0D) {
                return super.shouldExecute();
            }
            return false;
        }
    }

    class SummonVinesGoal extends UseMagicGoal<WhispererEntity> {
        private SummonVinesGoal() {
            super(WhispererEntity.this);
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity targetEntity = WhispererEntity.this.getAttackTarget();
            if (targetEntity != null && WhispererEntity.this.getDistanceSq(targetEntity) >= 16.0D) {
                return super.shouldExecute();
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
            LivingEntity targetEntity = WhispererEntity.this.getAttackTarget();
            if (targetEntity != null) {
                if(WhispererEntity.this.getRNG().nextFloat() < 0.25F){
                    GeomancyHelper.summonOffensiveVine(WhispererEntity.this, WhispererEntity.this, ModEntityTypes.POISON_QUILL_VINE.get());
                }
                else{
                    int[] rowToRemove = Util.getRandomObject(GeomancyHelper.ROWS, WhispererEntity.this.getRNG());
                    GeomancyHelper.summonAreaDenialVineTrap(targetEntity, targetEntity, ModEntityTypes.QUICK_GROWING_VINE.get(), rowToRemove);
                }
            }
        }


        protected SoundEvent getMagicPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        protected MagicType getMagicType() {
            return MagicType.SUMMON_VINES;
        }
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(MAGIC, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        IMagicUser.spawnMagicParticles(this);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.magicUseTicks > 0) {
            --this.magicUseTicks;
        }
    }


    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.magicUseTicks = compound.getInt("MagicUseTicks");
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("MagicUseTicks", this.magicUseTicks);
    }

    // IMAGICUSER METHODS
    @Override
    public boolean isUsingMagic() {
        if (this.world.isRemote) {
            return this.dataManager.get(MAGIC) > 0;
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
        return !this.world.isRemote ? this.activeMagic : MagicType.getFromId(this.dataManager.get(MAGIC));
    }

    @Override
    public void setMagicType(MagicType magicType) {
        this.activeMagic = magicType;
        this.dataManager.set(MAGIC, (byte)magicType.getId());
    }

    @Override
    public SoundEvent getMagicSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }
}
