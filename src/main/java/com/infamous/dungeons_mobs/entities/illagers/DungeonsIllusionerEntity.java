package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.ModProjectileHelper;
import com.infamous.dungeons_mobs.utils.PositionUtils;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DungeonsIllusionerEntity extends AbstractIllagerEntity implements IAnimatable, SpawnArmoredMob {

	public int shootAnimationTick;
	public int shootAnimationLength = 35;
	public int shootAnimationActionPoint = 15;

	public int blindAnimationTick;
	public int blindAnimationLength = 30;
	public int blindAnimationActionPoint = 18;

	public int vanishAnimationTick;
	public int vanishAnimationLength = 20;

	public int appearAnimationTick;
	public int appearAnimationLength = 20;

	public int appearDelay = 0;

	AnimationFactory factory = new AnimationFactory(this);

	public DungeonsIllusionerEntity(World world) {
		super(ModEntityTypes.ILLUSIONER.get(), world);
	}

	public DungeonsIllusionerEntity(EntityType<? extends DungeonsIllusionerEntity> type, World world) {
		super(type, world);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new DungeonsIllusionerEntity.RemainStationaryGoal());
		this.goalSelector.addGoal(1, new DungeonsIllusionerEntity.CreateIllusionsGoal(this));
		this.goalSelector.addGoal(2, new DungeonsIllusionerEntity.BlindAttackGoal(this));
		this.goalSelector.addGoal(3, new DungeonsIllusionerEntity.ShootAttackGoal(this));
		this.goalSelector.addGoal(4, new ApproachTargetGoal(this, 7.5, 1.0D, true));
		this.goalSelector.addGoal(5, new LookAtTargetGoal(this));
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
		this.targetSelector.addGoal(2,
				(new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(600));
		this.targetSelector.addGoal(4, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false))
				.setUnseenMemoryTicks(600));
		this.targetSelector.addGoal(3,
				new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false).setUnseenMemoryTicks(600));
	}

	public boolean shouldBeStationary() {
		return this.appearAnimationTick > 0 || this.appearDelay > 0;
	}

	@Override
	public boolean isLeftHanded() {
		return true;
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.FOLLOW_RANGE, 25.0D).add(Attributes.MAX_HEALTH, 50.0D);
	}

	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.shootAnimationTick = shootAnimationLength;
		} else if (p_28844_ == 11) {
			this.blindAnimationTick = blindAnimationLength;
		} else if (p_28844_ == 9) {
			this.vanishAnimationTick = vanishAnimationLength;
		} else if (p_28844_ == 8) {
			this.appearAnimationTick = appearAnimationLength;
		} else if (p_28844_ == 6) {
			this.appearDelay = 11;
		} else if (p_28844_ == 7) {
			for(int i = 0; i < 20; ++i) {
	            double d0 = this.random.nextGaussian() * 0.02D;
	            double d1 = this.random.nextGaussian() * 0.02D;
	            double d2 = this.random.nextGaussian() * 0.02D;
	            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
	         }
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}

	public void baseTick() {
		super.baseTick();
		this.tickDownAnimTimers();

		if (this.appearDelay > 0) {
			this.appearDelay--;
		}

		if (!this.level.isClientSide && this.appearDelay == 1) {
			this.appearAnimationTick = appearAnimationLength;
			this.level.broadcastEntityEvent(this, (byte) 8);
		}
	}

	public void tickDownAnimTimers() {
		if (this.shootAnimationTick > 0) {
			this.shootAnimationTick--;
		}

		if (this.blindAnimationTick > 0) {
			this.blindAnimationTick--;
		}

		if (this.vanishAnimationTick > 0) {
			this.vanishAnimationTick--;
		}

		if (this.appearAnimationTick > 0) {
			this.appearAnimationTick--;
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		String suffix = "_uncrossed";
		if(IllagerArmsUtil.armorHasCrossedArms(this, this.getItemBySlot(EquipmentSlotType.CHEST))){
			suffix = "";
		}
		if (this.appearAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_appear"+suffix, true));
		} else if (this.vanishAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_vanish"+suffix, true));
		} else if (this.shootAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_shoot"+suffix, true));
		} else if (this.blindAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_blind"+suffix, true));
		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_walk"+suffix, true));
		} else {
			if (this.isCelebrating()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_celebrate", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_idle"+suffix, true));
			}
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
		super.populateDefaultEquipmentSlots(p_180481_1_);
		this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.ILLUSIONER_ARMOR.getHead().get()));
		this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.ILLUSIONER_ARMOR.getChest().get()));
		this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.ILLUSIONER_ARMOR.getLegs().get()));
		this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.ILLUSIONER_ARMOR.getFeet().get()));
		if (ModList.get().isLoaded("dungeons_gear")) {
			Item SHORTBOW = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "shortbow"));

			ItemStack shortbow = new ItemStack(SHORTBOW);
			this.setItemSlot(EquipmentSlotType.MAINHAND, shortbow);
		} else {
			this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
		}
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
			SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		ILivingEntityData iLivingEntityData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_,
				p_213386_5_);
		this.populateDefaultEquipmentSlots(p_213386_2_);
		this.populateDefaultEquipmentEnchantments(p_213386_2_);
		return iLivingEntityData;
	}

	/**
	 * Returns whether this Entity is on the same team as the given Entity.
	 */
	public boolean isAlliedTo(Entity entityIn) {
		if (super.isAlliedTo(entityIn)) {
			return true;
		} else if (entityIn instanceof LivingEntity
				&& ((LivingEntity) entityIn).getMobType() == CreatureAttribute.ILLAGER) {
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
		return SoundEvents.ILLUSIONER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.ILLUSIONER_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ILLUSIONER_HURT;
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SoundEvents.ILLUSIONER_AMBIENT;
	}

	@Override
	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (p_70097_1_.getEntity() != null && this.isAlliedTo(p_70097_1_.getEntity()) && p_70097_1_ != DamageSource.OUT_OF_WORLD) {
			return false;
		} else {
			return super.hurt(p_70097_1_, p_70097_2_);
		}
	}

	   public void shootRocket(LivingEntity target) {
		      {
		    	 int explosionsByDifficulty = this.level.getCurrentDifficultyAt(this.blockPosition()).getDifficulty().getId();
		         ItemStack fireworkRocket = ModProjectileHelper.createRocket(explosionsByDifficulty * 2, DyeColor.PINK, DyeColor.PURPLE);
		         FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(this.level, fireworkRocket, this, this.getX(), this.getEyeY() - (double) 0.15F, this.getZ(), true);
		         double xDifference = target.getX() - this.getX();
		         double yDifference = target.getY(0.3333333333333333D) - fireworkrocketentity.getY();
		         double zDifference = target.getZ() - this.getZ();
		         fireworkrocketentity.shoot(xDifference, yDifference, zDifference, 1.0F, (float) (18 - this.level.getDifficulty().getId() * 7.5));
		         this.playSound(SoundEvents.FIREWORK_ROCKET_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		         this.level.addFreshEntity(fireworkrocketentity);
		      }
		   }

	   private void spawnBlindingCloud(BlockPos pos) {
		         AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, pos.getX(), pos.getY(), pos.getZ());
		         areaeffectcloudentity.setRadius(3.0F);
		         areaeffectcloudentity.setRadiusOnUse(-0.5F);
		         areaeffectcloudentity.setWaitTime(10);
		         areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

		         areaeffectcloudentity.addEffect(new EffectInstance(Effects.BLINDNESS, 100));

		         this.level.addFreshEntity(areaeffectcloudentity);

		   }

	@Override
	public ResourceLocation getArmorSet() {
		return ModItems.ILLUSIONER_ARMOR.getArmorSet();
	}

	class ShootAttackGoal extends Goal {
		public DungeonsIllusionerEntity mob;
		@Nullable
		public LivingEntity target;

		public int cooldown;

		public ShootAttackGoal(DungeonsIllusionerEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			if (this.cooldown > 0) {
				this.cooldown --;
			}

			return target != null && mob.distanceTo(target) <= 12.5 && this.cooldown <= 0 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.shootAnimationTick = mob.shootAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 4);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			mob.getNavigation().stop();
			
			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.shootAnimationTick == mob.shootAnimationActionPoint) {
	            mob.shootRocket(target);
			}
		}

		@Override
			public void stop() {
				super.stop();
				this.cooldown = 60 + mob.random.nextInt(40);
			}

		public boolean animationsUseable() {
			return mob.shootAnimationTick <= 0;
		}

	}

	class BlindAttackGoal extends Goal {
		public DungeonsIllusionerEntity mob;
		@Nullable
		public LivingEntity target;

		public int cooldown;

		public BlindAttackGoal(DungeonsIllusionerEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			if (this.cooldown > 0) {
				this.cooldown --;
			}

			return target != null && mob.random.nextInt(30) == 0 && mob.distanceTo(target) <= 10 && this.cooldown <= 0 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, 1.0F, 1.0F);
			mob.blindAnimationTick = mob.blindAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 11);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			mob.getNavigation().stop();
			
			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.blindAnimationTick == mob.blindAnimationActionPoint) {
				mob.playSound(SoundEvents.ILLUSIONER_CAST_SPELL, 1.0F, 1.0F);
	            mob.spawnBlindingCloud(target.blockPosition());

				if (target instanceof MobEntity) {
					((MobEntity)target).setTarget(null);
					((MobEntity)target).setLastHurtByMob(null);
					if (target instanceof IAngerable) {
						((IAngerable)target).stopBeingAngry();
						((IAngerable)target).setLastHurtByMob(null);
						((IAngerable)target).setTarget(null);
						((IAngerable)target).setPersistentAngerTarget(null);
					}
				}
			}
		}

		@Override
			public void stop() {
				super.stop();
				this.cooldown = 200 + mob.random.nextInt(100);
			}

		public boolean animationsUseable() {
			return mob.blindAnimationTick <= 0;
		}

	}

	class CreateIllusionsGoal extends Goal {
		public DungeonsIllusionerEntity mob;
		@Nullable
		public LivingEntity target;

		public int nextUseTime;
		
		private final Predicate<Entity> ILLUSIONER_CLONE = (p_33346_) -> {
			return p_33346_ instanceof IllusionerCloneEntity && ((IllusionerCloneEntity)p_33346_).getOwner() != null && ((IllusionerCloneEntity)p_33346_).getOwner() == mob;
		};

		public CreateIllusionsGoal(DungeonsIllusionerEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			int nearbyClones = mob.level.getEntities(mob, mob.getBoundingBox().inflate(30.0D), ILLUSIONER_CLONE)
					.size();

			return target != null && mob.tickCount >= this.nextUseTime && mob.random.nextInt(10) == 0 && mob.canSee(target) && nearbyClones <= 0 && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, 1.0F, 1.0F);
			mob.vanishAnimationTick = mob.vanishAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 9);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			mob.getNavigation().stop();
			
			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.vanishAnimationTick == 1) {
				SummonSpotEntity summonSpot = ModEntityTypes.SUMMON_SPOT.get().create(mob.level);
				summonSpot.moveTo(target.blockPosition().offset(-12.5 + mob.random.nextInt(25), 0, -12.5 + mob.random.nextInt(25)), 0.0F, 0.0F);
				((ServerWorld)mob.level).addFreshEntityWithPassengers(summonSpot);
				PositionUtils.moveToCorrectHeight(summonSpot);

				mob.level.broadcastEntityEvent(mob, (byte) 7);
				mob.moveTo(summonSpot.blockPosition(), 0.0F, 0.0F);
				mob.setYBodyRot(mob.random.nextInt(360));
				mob.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(mob.getX(), mob.getEyeY(), mob.getZ()));
				mob.appearDelay = 11;
				mob.level.broadcastEntityEvent(mob, (byte) 6);
				mob.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
				PositionUtils.moveToCorrectHeight(mob);

				if (target instanceof MobEntity) {
					((MobEntity)target).setTarget(null);
					((MobEntity)target).setLastHurtByMob(null);
					if (target instanceof IAngerable) {
						((IAngerable)target).stopBeingAngry();
						((IAngerable)target).setLastHurtByMob(null);
						((IAngerable)target).setTarget(null);
						((IAngerable)target).setPersistentAngerTarget(null);
					}
				}

				int clonesByDifficulty = mob.level.getCurrentDifficultyAt(mob.blockPosition()).getDifficulty().getId();

				for (int i = 0; i < clonesByDifficulty * 5; i ++) {
					SummonSpotEntity cloneSummonSpot = ModEntityTypes.SUMMON_SPOT.get().create(mob.level);
					cloneSummonSpot.moveTo(target.blockPosition().offset(-12.5 + mob.random.nextInt(25), 0, -12.5 + mob.random.nextInt(25)), 0.0F, 0.0F);
					cloneSummonSpot.mobSpawnRotation = mob.random.nextInt(360);
					((ServerWorld)mob.level).addFreshEntityWithPassengers(cloneSummonSpot);
					PositionUtils.moveToCorrectHeight(cloneSummonSpot);

					IllusionerCloneEntity clone = ModEntityTypes.ILLUSIONER_CLONE.get().create(mob.level);
					clone.finalizeSpawn(((ServerWorld)mob.level), mob.level.getCurrentDifficultyAt(cloneSummonSpot.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
					clone.setOwner(mob);
					clone.setHealth(mob.getHealth());
					for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
						ItemStack itemstack = mob.getItemBySlot(equipmentslottype);
						if (!itemstack.isEmpty()) {
							clone.setItemSlot(equipmentslottype, itemstack.copy());
							clone.setDropChance(equipmentslottype, mob.getEquipmentDropChance(equipmentslottype));
						}
					}
					clone.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(mob.getX(), mob.getEyeY(), mob.getZ()));
					clone.setDelayedAppear(true);
					cloneSummonSpot.summonedEntity = clone;
					cloneSummonSpot.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
				}
			}
		}

		@Override
		public void stop() {
			super.stop();
			this.nextUseTime = mob.tickCount + 60;
		}
		
		public boolean animationsUseable() {
			return mob.vanishAnimationTick <= 0;
		}

	}

	class RemainStationaryGoal extends Goal {

		public RemainStationaryGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET, Goal.Flag.JUMP));
		}

		@Override
		public boolean canUse() {
			return DungeonsIllusionerEntity.this.shouldBeStationary();
		}
	}

}
