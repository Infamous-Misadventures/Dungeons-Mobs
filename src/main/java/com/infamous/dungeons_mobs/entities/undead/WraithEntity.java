package com.infamous.dungeons_mobs.entities.undead;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import com.infamous.dungeons_mobs.goals.magic.MagicAttackGoal;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FleeSunGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RestrictSunGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WraithEntity extends MonsterEntity implements IMagicUser, IAnimatable {

	public static final DataParameter<Integer> ATTACK_TICKS = EntityDataManager.defineId(WraithEntity.class, DataSerializers.INT);
	
    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.defineId(WraithEntity.class, DataSerializers.BYTE);
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;
    
	AnimationFactory factory = new AnimationFactory(this);

    public WraithEntity(World worldIn) {
        super(ModEntityTypes.WRAITH.get(), worldIn);
    }

    public WraithEntity(EntityType<? extends WraithEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D) // Enderman follow range
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F) // Enderman movement speed
                //.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
        ;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new UsingMagicGoal<>(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new UseWraithFireMagic());
        this.goalSelector.addGoal(5, new MagicAttackGoal<>(this, 1.0D, 6.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
    
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getAttackTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_attack", true));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_fly", true));
			} else {
				    event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_idle", true));
			}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

    @Override
    public void baseTick() {
    	
        LivingEntity targetEntity = WraithEntity.this.getTarget();
        if (targetEntity != null && this.getAttackTicks() == 5) {

            if(DungeonsMobsConfig.COMMON.ENABLE_WRAITH_FIRE_SUMMON.get()){
                summonWraithFire(targetEntity);
            }
            else{
                fireWraithFireballs(targetEntity);
            }
        }
    	
		if (this.getAttackTicks() > 0) {
			this.setAttackTicks(this.getAttackTicks() - 1);
		}
		
        if (this.isAlive()) {
            boolean canBurnFromSunlight = this.shouldBurnInDay() && this.isSunBurnTick();
            if (canBurnFromSunlight) {
                canBurnFromSunlight = checkSunlightBurnProtection(canBurnFromSunlight);
                if (canBurnFromSunlight) {
                    this.setSecondsOnFire(8);
                }
            }
        }
        /*
        if (this.world.isRemote) {
            this.world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getPosXRandom(0.5D), this.getPosYRandom() - 0.25D, this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }

         */
        super.baseTick();
    }

     public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
     }
     
    protected int decreaseAirSupply(int p_70682_1_) {
    	return p_70682_1_;
    }
    
	   public int getAttackTicks() {
		      return this.entityData.get(ATTACK_TICKS);
		   }

		   public void setAttackTicks(int p_189794_1_) {	   
		      this.entityData.set(ATTACK_TICKS, p_189794_1_);
		   }

    private boolean checkSunlightBurnProtection(boolean canBurnFromSunlight) {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
        if (!itemstack.isEmpty()) {
            if (itemstack.isDamageableItem()) {
                itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                    this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                    this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                }
            }

            canBurnFromSunlight = false;
        }
        return canBurnFromSunlight;
    }

    private boolean shouldBurnInDay() {
        return true;
    }


    // BASIC MOB METHODS

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.WRAITH_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.WRAITH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.WRAITH_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSoundEvents.WRAITH_FLY.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    // TELEPORT METHODS - FROM ENDERMANENTITY
    /**
     * Try to teleport the mob to a random nearby position - keeps trying until Integer.MAX_VALUE is reached
     */
    private boolean teleportRandomly() {
        if (!this.level.isClientSide() && this.isAlive()) {
            boolean hasSuccessfullyTeleported = false;
            int teleportAttemptCounter = 0;
            while(!hasSuccessfullyTeleported && teleportAttemptCounter < Integer.MAX_VALUE){
                double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
                double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
                double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
                hasSuccessfullyTeleported = this.teleport(d0, d1, d2);
                teleportAttemptCounter++;
            }
        }
        return false;
    }

    private boolean teleport(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_70825_1_, p_70825_3_, p_70825_5_);

        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
           blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
           net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, p_70825_1_, p_70825_3_, p_70825_5_);
           if (event.isCanceled()) return false;
           boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
           if (flag2 && !this.isSilent()) {
              this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, ModSoundEvents.WRAITH_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
              this.playSound(ModSoundEvents.WRAITH_TELEPORT.get(), 1.0F, 1.0F);
           }

           return flag2;
        } else {
           return false;
        }
     }
    
    private void summonWraithFire(LivingEntity targetEntity) {
        BlockPos originalWraithPosition = new BlockPos(
                WraithEntity.this.getX() + 0.5D,
                WraithEntity.this.getY() + 0.5D,
                WraithEntity.this.getZ() + 0.5D );

        BlockPos originalTargetPosition = new BlockPos(
                targetEntity.getX() + 0.5D,
                targetEntity.getY() + 0.5D,
                targetEntity.getZ() + 0.5D );

        boolean wraithTooCloseToPlayer = shouldWraithTeleportAwayFromTarget(3.0D);

            for(int i = 0; i < 9; i++){
                double xshift = 0;
                double zshift = 0;

                // positive x shift
                if(i == 1 || i == 2 || i == 8){
                    xshift = 2.0D;
                }
                // negative x shift
                if(i == 4 || i == 5 || i == 6){
                    xshift = -2.0D;
                }
                // positive z shift
                if(i == 2 || i == 3 || i == 4){
                    zshift = 2.0D;
                }
                // negative z shift
                if(i == 6 || i == 7 || i == 8){
                    zshift = -2.0D;
                }
                World world = targetEntity.level;
                BlockPos targetBlockPos = pickBlockPosForAttack(originalTargetPosition, originalWraithPosition, xshift, zshift , wraithTooCloseToPlayer);

                BlockState blockstate = world.getBlockState(targetBlockPos);
                BlockState soulFireBlockState = ModBlocks.WRAITH_FIRE_BLOCK.get().defaultBlockState();
        		this.level.playSound(null, originalTargetPosition, ModSoundEvents.WRAITH_FIRE.get(), this.getSoundSource(), WraithEntity.this.getSoundVolume(), WraithEntity.this.getVoicePitch());
                boolean canLightBlock = blockstate.isAir();
                if (canLightBlock) {
                    world.setBlock(targetBlockPos, soulFireBlockState, 11);
                }
            }
    }

    private BlockPos pickBlockPosForAttack(BlockPos originalTargetPosition, BlockPos originalWraithPosition, double xshift, double zshift, boolean wraithTooCloseToPlayer) {
        BlockPos targetBlockPos = new BlockPos(
                originalTargetPosition.getX() + xshift,
                originalTargetPosition.getY(),
                originalTargetPosition.getZ() + zshift);

        if(wraithTooCloseToPlayer){
            targetBlockPos = new BlockPos(
                    originalWraithPosition.getX() + xshift,
                    originalWraithPosition.getY(),
                    originalWraithPosition.getZ() + zshift);
        }
        return targetBlockPos;
    }

    private void fireWraithFireballs(LivingEntity targetEntity){
        double squareDistanceToTarget = WraithEntity.this.distanceToSqr(targetEntity);
        double xDifference = targetEntity.getX() - WraithEntity.this.getX();
        double yDifference = targetEntity.getY(0.5D) - WraithEntity.this.getY(0.5D);
        double zDifference = targetEntity.getZ() - WraithEntity.this.getZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;
        this.playSound(ModSoundEvents.WRAITH_FIRE.get(), 1.0F, 1.0F);

        for(int i = 0; i < 3; ++i) {
            WraithFireballEntity wraithFireballEntity = new WraithFireballEntity(WraithEntity.this.level, WraithEntity.this, xDifference + WraithEntity.this.getRandom().nextGaussian() * (double)f, yDifference, zDifference + WraithEntity.this.getRandom().nextGaussian() * (double)f);
            wraithFireballEntity.setPos(wraithFireballEntity.getX(), WraithEntity.this.getY(0.5D) + 0.5D, wraithFireballEntity.getZ());
            WraithEntity.this.level.addFreshEntity(wraithFireballEntity);
        }
        shouldWraithTeleportAwayFromTarget(3.0D);
    }




    private boolean shouldWraithTeleportAwayFromTarget(double distanceThreshold) {
        LivingEntity targetEntity = this.getTarget();
        boolean wraithTooCloseToTarget = false;
        if (targetEntity != null) {
            wraithTooCloseToTarget = this.distanceTo(targetEntity) < distanceThreshold;
        }

        if(wraithTooCloseToTarget){
            WraithEntity.this.teleportRandomly();
        }
        return wraithTooCloseToTarget;
    }

    class UseWraithFireMagic extends UseMagicGoal<WraithEntity>{

        UseWraithFireMagic() {
            super(WraithEntity.this);
        }

        @Override
        public boolean canUse() {
            LivingEntity targetEntity = WraithEntity.this.getTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = WraithEntity.this.canSee(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive() && WraithEntity.this.distanceTo(targetEntity) < 16.0D) {
            	if (super.canUse()) {
            		WraithEntity.this.setAttackTicks(35);
            	}
                return super.canUse();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity targetEntity = WraithEntity.this.getTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = WraithEntity.this.canSee(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive()){
                return super.canContinueToUse();
            } else {
                return false;
            }
        }

        @Override
        protected void useMagic() {

        }

        
        @Override
        protected int getMagicUseTime() {
            return 35;
        }

        @Override
        protected int getMagicUseInterval() {
            return 100;
        }

        @Nullable
        @Override
        protected SoundEvent getMagicPrepareSound() {
            return ModSoundEvents.WRAITH_ATTACK.get();
        }

        @Override
        protected MagicType getMagicType() {
            return MagicType.WRAITH_FIRE;
        }
    }

    // MODIFIED SPELLCASTINGILLAGER METHODS
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAGIC, (byte)0);
	    this.entityData.define(ATTACK_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        //IMagicUser.spawnMagicParticles(this);

    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.magicUseTicks > 0) {
            --this.magicUseTicks;
        }
        
        Vector3d vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.8D, 1.0D));
         }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
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
        return null;
    }

}
