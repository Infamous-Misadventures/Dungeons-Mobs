package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EnchanterEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> ATTACK_TICKS = EntityDataManager.defineId(EnchanterEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> ENCHANT_TICKS = EntityDataManager.defineId(EnchanterEntity.class, DataSerializers.INT);
	
	AnimationFactory factory = new AnimationFactory(this);
	
	private MonsterEntity enchantmentTarget;
	
    public EnchanterEntity(World world){
        super(ModEntityTypes.ENCHANTER.get(), world);
    }

    public EnchanterEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_TICKS, 0);
	    this.entityData.define(ENCHANT_TICKS, 0);
    }
    
	   public int getAttackTicks() {
		      return this.entityData.get(ATTACK_TICKS);
		   }

		   public void setAttackTicks(int p_189794_1_) {	   
		      this.entityData.set(ATTACK_TICKS, p_189794_1_);
		   }
		   
		   public int getEnchantTicks() {
			      return this.entityData.get(ENCHANT_TICKS);
			   }

			   public void setEnchantTicks(int p_189794_1_) {	   
			      this.entityData.set(ENCHANT_TICKS, p_189794_1_);
			   }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EnchanterEntity.AttackingGoal());
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EnchanterEntity.CastingSpellGoal());
        this.goalSelector.addGoal(4, new EnchanterEntity.EnchantSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
    
    private void setEnchantmentTarget(@Nullable MonsterEntity p_190748_1_) {
        this.enchantmentTarget = p_190748_1_;
     }

     @Nullable
     private MonsterEntity getEnchantmentTarget() {
        return this.enchantmentTarget;
     }
    
    public void baseTick() {
    	super.baseTick();
    	if (this.level.getNearestPlayer(this, 2) != null && this.getTarget() != null && this.level.getNearestPlayer(this, 2) == this.getTarget()) {
    		if (this.getAttackTicks() == 0 && this.random.nextInt(10) == 0) {
    			this.setAttackTicks(40);
    			this.playSound(ModSoundEvents.ENCHANTER_PRE_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
    		} else if (this.getAttackTicks() == 12) {
    			this.doHurtTarget(this.level.getNearestPlayer(this, 2));
    			this.playSound(ModSoundEvents.ENCHANTER_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
    		}
    	}
    	
		if (this.getAttackTicks() > 0) {
			this.setAttackTicks(this.getAttackTicks() - 1);
		}
		
		if (this.getEnchantTicks() > 0) {
			this.setEnchantTicks(this.getEnchantTicks() - 1);
		}
    }
    
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getEnchantTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_enchant", true));
			} else if (this.getAttackTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_attack", true));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_walk", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("enchanter_idle", true));
			}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MonsterEntity.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MAX_HEALTH, 14.0D);
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
        return ModSoundEvents.ENCHANTER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ENCHANTER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ENCHANTER_HURT.get();
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return ModSoundEvents.ENCHANTER_BEAM.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.ENCHANTER_IDLE.get();
    }

    class CastingSpellGoal extends SpellcastingIllagerEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
           if (EnchanterEntity.this.getEnchantmentTarget() != null) {
        	   EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getEnchantmentTarget(), (float)EnchanterEntity.this.getMaxHeadYRot(), (float)EnchanterEntity.this.getMaxHeadXRot());
           }

        }
     }

   

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }
    
    public class EnchantSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private final EntityPredicate wololoTargeting = (new EntityPredicate()).range(16.0D).allowInvulnerable();

        public boolean canUse() {
           if (EnchanterEntity.this.getTarget() == null) {
              return false;
           } else if (EnchanterEntity.this.isCastingSpell()) {
              return false;
           } else if (EnchanterEntity.this.tickCount < this.nextAttackTickCount) {
              return false;
           } else {
              List<MonsterEntity> list = EnchanterEntity.this.level.getNearbyEntities(MonsterEntity.class, this.wololoTargeting, EnchanterEntity.this, EnchanterEntity.this.getBoundingBox().inflate(16.0D, 8.0D, 16.0D));
              if (list.isEmpty()) {
                 return false;
              } else {
            	  EnchanterEntity.this.setEnchantmentTarget(list.get(EnchanterEntity.this.random.nextInt(list.size())));
            	  EnchanterEntity.this.setEnchantTicks(45);
                 return true;
              }
           }
        }

        public boolean canContinueToUse() {
           return EnchanterEntity.this.getEnchantmentTarget() != null && this.attackWarmupDelay > 0;
        }

        public void stop() {
           super.stop();
           EnchanterEntity.this.setEnchantmentTarget((MonsterEntity)null);
        }

        protected void performSpellCasting() {
        	MonsterEntity sheepentity = EnchanterEntity.this.getEnchantmentTarget();
           if (sheepentity != null && sheepentity.isAlive()) {

           }

        }

        protected int getCastWarmupTime() {
           return 40;
        }

        protected int getCastingTime() {
           return 45;
        }

        protected int getCastingInterval() {
           return 140;
        }

        protected SoundEvent getSpellPrepareSound() {
           return ModSoundEvents.ENCHANTER_SPELL.get();
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
           return SpellcastingIllagerEntity.SpellType.NONE;
        }
     }
    
    class AttackingGoal extends Goal {
        public AttackingGoal() {
           this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
           return EnchanterEntity.this.getAttackTicks() > 0 && EnchanterEntity.this.getTarget() != null;
        }
        
        public void tick() {
        	EnchanterEntity.this.getNavigation().stop();
        	EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getTarget().getX(), EnchanterEntity.this.getTarget().getEyeY(), EnchanterEntity.this.getTarget().getZ());
        }
     }

}