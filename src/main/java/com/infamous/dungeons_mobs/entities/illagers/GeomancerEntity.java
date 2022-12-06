package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.AvoidBaseEntityGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.infamous.dungeons_mobs.entities.SpawnArmoredHelper.equipArmorSet;

import net.minecraft.world.entity.monster.AbstractIllager.IllagerArmPose;

public class GeomancerEntity extends SpellcasterIllager implements IAnimatable, SpawnArmoredMob {

	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
	public int summonBombsAttackAnimationTick;
	public int summonBombsAttackAnimationLength = 35;
	public int summonBombsAttackAnimationActionPoint = 15;
	
	public int summonWallsAnimationTick;
	public int summonWallsAnimationLength = 35;
	public int summonWallsAnimationActionPoint = 20;

    public GeomancerEntity(EntityType<? extends SpellcasterIllager> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder setCustomAttributes(){
        return Evoker.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new GeomancerEntity.SummonPillarsGoal(this));
        // Custom goal to avoid Geomancer constructs
        this.goalSelector.addGoal(1, new AvoidBaseEntityGoal<>(this, ConstructEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, AbstractVillager.class, 3.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 3.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolem.class, 3.0F, 1.3D, 1.15D));
        this.goalSelector.addGoal(3, new ApproachTargetGoal(this, 15, 1.0D, true));
        this.goalSelector.addGoal(4, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false).setUnseenMemoryTicks(600));
    }
    
    @Override
    public boolean isLeftHanded() {
    	return false;
    }
    
	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.summonWallsAnimationTick = summonWallsAnimationLength;
		} else if (p_28844_ == 11) {
			this.summonBombsAttackAnimationTick = summonBombsAttackAnimationLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}
    
    public void baseTick() {
    	super.baseTick();

    	this.tickDownAnimTimers();
    }

    public void tickDownAnimTimers() {
    	if (this.summonWallsAnimationTick >= 0) {
    		this.summonWallsAnimationTick --;
    	}
    	
    	if (this.summonBombsAttackAnimationTick >= 0) {
    		this.summonBombsAttackAnimationTick --;
    	}
    }
    
    @Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.summonBombsAttackAnimationTick > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_attack", EDefaultLoopTypes.LOOP));	
			} else if (this.summonWallsAnimationTick > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_summon", EDefaultLoopTypes.LOOP));				
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_walk", EDefaultLoopTypes.LOOP));
			} else {
				if (this.isCelebrating()) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_celebrate", EDefaultLoopTypes.LOOP));
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("geomancer_idle", EDefaultLoopTypes.LOOP));
				}
			}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GEOMANCER_STAFF.get()));
		equipArmorSet(ModItems.GEOMANCER_ARMOR, this);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.populateDefaultEquipmentSlots(this.getRandom(), difficultyIn);
        this.populateDefaultEquipmentEnchantments(this.getRandom(), difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.GEOMANCER_ARMOR;
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == MobType.ILLAGER) {
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
    public IllagerArmPose getArmPose() {
        IllagerArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == IllagerArmPose.CROSSED){
            return IllagerArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }
    
    class SummonPillarsGoal extends Goal {
		public GeomancerEntity mob;
		@Nullable
		public LivingEntity target;
		
		public int nextUseTime = 0;
		
		public SummonPillarsGoal(GeomancerEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();
			
			return target != null && mob.tickCount >= this.nextUseTime && mob.distanceTo(target) <= 20 && mob.hasLineOfSight(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && animationsNotUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.GEOMANCER_PRE_ATTACK.get(), 1.0F, mob.getVoicePitch());
			if (mob.random.nextBoolean()) {
				mob.summonWallsAnimationTick = mob.summonWallsAnimationLength;
				mob.level.broadcastEntityEvent(mob, (byte) 4);
			} else {
				mob.summonBombsAttackAnimationTick = mob.summonBombsAttackAnimationLength;
				mob.level.broadcastEntityEvent(mob, (byte) 11);
			}
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			mob.getNavigation().stop();
			
			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.summonWallsAnimationTick == mob.summonWallsAnimationActionPoint) {
				mob.playSound(ModSoundEvents.GEOMANCER_ATTACK.get(), 1.0F, mob.getVoicePitch());
            	int randomInt = mob.random.nextInt(3);
            	
            	if (randomInt == 0) {
                int[] rowToRemove = Util.getRandom(GeomancyHelper.CONFIG_1_ROWS, mob.getRandom());
                GeomancyHelper.summonAreaDenialTrap(mob, target, ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
            	} else if (randomInt == 1) {
            		GeomancyHelper.summonWallTrap(mob, target, ModEntityTypes.GEOMANCER_WALL.get());
            	} else {
            		GeomancyHelper.summonRandomPillarsTrap(mob, target, ModEntityTypes.GEOMANCER_WALL.get());
            	}
			}
			
			if (target != null && mob.summonBombsAttackAnimationTick == mob.summonBombsAttackAnimationActionPoint) {
				mob.playSound(ModSoundEvents.GEOMANCER_ATTACK.get(), 1.0F, mob.getVoicePitch());
            	if (mob.getRandom().nextBoolean()) {
            	GeomancyHelper.summonQuadOffensiveTrap(mob, target, ModEntityTypes.GEOMANCER_BOMB.get());	
            	} else {
            		boolean movingOnX = mob.random.nextBoolean();
            		GeomancyHelper.summonOffensiveConstruct(mob, target, ModEntityTypes.GEOMANCER_BOMB.get(), movingOnX ? (mob.random.nextBoolean() ? 2 : -2) : 0, !movingOnX ? (mob.random.nextBoolean() ? 2 : -2) : 0, Direction.NORTH);
            	}
			}
		}
		
		@Override
		public void stop() {
			super.stop();
			this.nextUseTime = mob.tickCount + 100 + mob.random.nextInt(40);
		}

		public boolean animationsUseable() {
			return mob.summonWallsAnimationTick <= 0 || mob.summonBombsAttackAnimationTick <= 0;
		}
		
		public boolean animationsNotUseable() {
			return mob.summonWallsAnimationTick > 0 || mob.summonBombsAttackAnimationTick > 0;
		}
	}
   
}
