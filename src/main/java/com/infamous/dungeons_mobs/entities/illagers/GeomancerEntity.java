package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.goals.AvoidBaseEntityGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class GeomancerEntity extends SpellcastingIllagerEntity implements IAnimatable {
	
	public static final DataParameter<Integer> CAST_TICKS = EntityDataManager.defineId(GeomancerEntity.class, DataSerializers.INT);

	AnimationFactory factory = new AnimationFactory(this);
	
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
        this.goalSelector.addGoal(7, new PatrollerEntity.PatrolGoal<>(this,1.42,1.3));
        this.goalSelector.addGoal(6, new AbstractRaiderEntity.FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new SpellcastingIllagerEntity.CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.0D, 1.0D));
        // Custom goal to avoid Geomancer constructs
        this.goalSelector.addGoal(3, new AvoidBaseEntityGoal<>(this, ConstructEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new GeomancerEntity.SummonPillarsGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10F));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
    
    public void baseTick() {
    	super.baseTick();
    	
		if (this.getCastTicks() > 0) {
			this.setCastTicks(this.getCastTicks() - 1);
		}
    }
    
	   public int getCastTicks() {
		      return this.entityData.get(CAST_TICKS);
		   }

		   public void setCastTicks(int p_189794_1_) {	   
		      this.entityData.set(CAST_TICKS, p_189794_1_);
		   }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
	    this.entityData.define(CAST_TICKS, 0);
    }
    
    protected float getSoundVolume() {
    	return 2.0F;
    }
    
    @Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getCastTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_cast", true));				
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_walk", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_idle", true));
			}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.GEOMANCER_STAFF.get()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.GEOMANCER_BEADS.get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.GEOMANCER_ROBES.get()));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    class SummonPillarsGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private SummonPillarsGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = GeomancerEntity.this.getTarget();
            if (targetEntity != null && GeomancerEntity.this.distanceTo(targetEntity) > 4.0D) {
            	if (super.canUse()) {
            		GeomancerEntity.this.setCastTicks(25);
            	}
                return super.canUse();
            }
            return false;
        }

        protected int getCastingTime() {
            return 26;
        }

        protected int getCastingInterval() {
            return 88;
        }

        protected void performSpellCasting() {
            LivingEntity targetEntity = GeomancerEntity.this.getTarget();
            if (targetEntity != null) {
                if(GeomancerEntity.this.getRandom().nextFloat() < 0.25F){
                	if (GeomancerEntity.this.getRandom().nextBoolean()) {
                	GeomancyHelper.summonQuadOffensiveTrap(targetEntity, targetEntity, ModEntityTypes.GEOMANCER_BOMB.get());	
                	} else {
                    GeomancyHelper.summonOffensiveConstruct(GeomancerEntity.this, targetEntity, ModEntityTypes.GEOMANCER_BOMB.get(), 0, 0, Direction.NORTH);
                	}
                }
                else{
                	int randomInt = GeomancerEntity.this.random.nextInt(3);
                	
                	if (randomInt == 0) {
                    int[] rowToRemove = Util.getRandom(GeomancyHelper.CONFIG_1_ROWS, GeomancerEntity.this.getRandom());
                    GeomancyHelper.summonAreaDenialTrap(targetEntity, targetEntity, ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
                	} else if (randomInt == 1) {
                		GeomancyHelper.summonWallTrap(targetEntity, targetEntity, ModEntityTypes.GEOMANCER_WALL.get());
                	} else {
                		GeomancyHelper.summonRandomPillarsTrap(targetEntity, targetEntity, ModEntityTypes.GEOMANCER_WALL.get());
                	}
                }
            }
        }


        protected SoundEvent getSpellPrepareSound() {
            return ModSoundEvents.GEOMANCER_PRE_ATTACK.get();
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
            return SpellcastingIllagerEntity.SpellType.NONE;
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
        return ModSoundEvents.GEOMANCER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.GEOMANCER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.GEOMANCER_HURT.get();
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return ModSoundEvents.GEOMANCER_ATTACK.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.GEOMANCER_IDLE.get();
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
