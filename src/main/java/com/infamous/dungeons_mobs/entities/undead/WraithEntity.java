package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.goals.magic.MagicAttackGoal;
import com.infamous.dungeons_mobs.mod.ModBlocks;
import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.goals.magic.UsingMagicGoal;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import com.infamous.dungeons_mobs.goals.magic.UseMagicGoal;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WraithEntity extends MonsterEntity implements IMagicUser {

    // Required to make use of IMagicUser
    private static final DataParameter<Byte> MAGIC = EntityDataManager.createKey(WraithEntity.class, DataSerializers.BYTE);
    private int magicUseTicks;
    private MagicType activeMagic = MagicType.NONE;

    public WraithEntity(World worldIn) {
        super(ModEntityTypes.WRAITH.get(), worldIn);
    }

    public WraithEntity(EntityType<? extends WraithEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 64.0D) // Enderman follow range
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.3F) // Enderman movement speed
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
    public void livingTick() {
        if (this.isAlive()) {
            boolean canBurnFromSunlight = this.shouldBurnInDay() && this.isInDaylight();
            if (canBurnFromSunlight) {
                canBurnFromSunlight = checkSunlightBurnProtection(canBurnFromSunlight);
                if (canBurnFromSunlight) {
                    this.setFire(8);
                }
            }
        }
        /*
        if (this.world.isRemote) {
            this.world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getPosXRandom(0.5D), this.getPosYRandom() - 0.25D, this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }

         */
        super.livingTick();
    }

    private boolean checkSunlightBurnProtection(boolean canBurnFromSunlight) {
        ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (!itemstack.isEmpty()) {
            if (itemstack.isDamageable()) {
                itemstack.setDamage(itemstack.getDamage() + this.rand.nextInt(2));
                if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
                    this.sendBreakAnimation(EquipmentSlotType.HEAD);
                    this.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
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
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    protected SoundEvent getStepSound() {
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        //this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    // TELEPORT METHODS - FROM ENDERMANENTITY
    /**
     * Try to teleport the mob to a random nearby position - keeps trying until Integer.MAX_VALUE is reached
     */
    private boolean teleportRandomly() {
        if (!this.world.isRemote() && this.isAlive()) {
            boolean hasSuccessfullyTeleported = false;
            int teleportAttemptCounter = 0;
            while(!hasSuccessfullyTeleported && teleportAttemptCounter < Integer.MAX_VALUE){
                double d0 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 64.0D;
                double d1 = this.getPosY() + (double)(this.rand.nextInt(64) - 32);
                double d2 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 64.0D;
                hasSuccessfullyTeleported = this.teleportTo(d0, d1, d2);
                teleportAttemptCounter++;
            }
        }
        return false;
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while(blockpos$mutable.getY() > 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.world.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMovement();
        boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag2 = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.world.playSound((PlayerEntity)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }



    private boolean shouldWraithTeleportAwayFromTarget(double distanceThreshold) {
        LivingEntity targetEntity = this.getAttackTarget();
        boolean wraithTooCloseToTarget = false;
        if (targetEntity != null) {
            wraithTooCloseToTarget = this.getDistance(targetEntity) < distanceThreshold;
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
        public boolean shouldExecute() {
            LivingEntity targetEntity = WraithEntity.this.getAttackTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = WraithEntity.this.canEntityBeSeen(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive() && WraithEntity.this.getDistance(targetEntity) < 16.0D) {
                return super.shouldExecute();
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity targetEntity = WraithEntity.this.getAttackTarget();
            if(targetEntity == null) return false;
            boolean canTargetBeSeen = WraithEntity.this.canEntityBeSeen(targetEntity);
            if (canTargetBeSeen && targetEntity.isAlive()){
                return super.shouldContinueExecuting();
            } else {
                return false;
            }
        }

        @Override
        protected void useMagic() {
            LivingEntity targetEntity = WraithEntity.this.getAttackTarget();
            if (targetEntity != null) {

                if(DungeonsMobsConfig.COMMON.ENABLE_WRAITH_FIRE_SUMMON.get()){
                    summonWraithFire(targetEntity);
                }
                else{
                    fireWraithFireballs(targetEntity);
                }
            }
        }

        private void summonWraithFire(LivingEntity targetEntity) {
            BlockPos originalWraithPosition = new BlockPos(
                    WraithEntity.this.getPosX() + 0.5D,
                    WraithEntity.this.getPosY() + 0.5D,
                    WraithEntity.this.getPosZ() + 0.5D );

            BlockPos originalTargetPosition = new BlockPos(
                    targetEntity.getPosX() + 0.5D,
                    targetEntity.getPosY() + 0.5D,
                    targetEntity.getPosZ() + 0.5D );

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
                    World world = targetEntity.getEntityWorld();
                    BlockPos targetBlockPos = pickBlockPosForAttack(originalTargetPosition, originalWraithPosition, xshift, zshift , wraithTooCloseToPlayer);

                    BlockState blockstate = world.getBlockState(targetBlockPos);
                    BlockState soulFireBlockState = ModBlocks.WRAITH_FIRE_BLOCK.get().getDefaultState();
                    boolean canLightBlock = blockstate.isAir();
                    if (canLightBlock) {
                        world.setBlockState(targetBlockPos, soulFireBlockState, 11);
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
            double squareDistanceToTarget = WraithEntity.this.getDistanceSq(targetEntity);
            double xDifference = targetEntity.getPosX() - WraithEntity.this.getPosX();
            double yDifference = targetEntity.getPosYHeight(0.5D) - WraithEntity.this.getPosYHeight(0.5D);
            double zDifference = targetEntity.getPosZ() - WraithEntity.this.getPosZ();
            float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

            for(int i = 0; i < 3; ++i) {
                WraithFireballEntity wraithFireballEntity = new WraithFireballEntity(WraithEntity.this.world, WraithEntity.this, xDifference + WraithEntity.this.getRNG().nextGaussian() * (double)f, yDifference, zDifference + WraithEntity.this.getRNG().nextGaussian() * (double)f);
                wraithFireballEntity.setPosition(wraithFireballEntity.getPosX(), WraithEntity.this.getPosYHeight(0.5D) + 0.5D, wraithFireballEntity.getPosZ());
                WraithEntity.this.world.addEntity(wraithFireballEntity);
            }
            shouldWraithTeleportAwayFromTarget(3.0D);
        }

        @Override
        protected int getMagicUseTime() {
            return 40;
        }

        @Override
        protected int getMagicUseInterval() {
            return 100;
        }

        @Nullable
        @Override
        protected SoundEvent getMagicPrepareSound() {
            return SoundEvents.ENTITY_BLAZE_SHOOT;
        }

        @Override
        protected MagicType getMagicType() {
            return MagicType.WRAITH_FIRE;
        }
    }

    // MODIFIED SPELLCASTINGILLAGER METHODS
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

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
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
