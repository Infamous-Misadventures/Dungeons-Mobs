package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.goals.MobHurtByTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PoisonQuillVineEntity extends VineEntity implements IRangedAttackMob, IAnimatable {
    public static final DataParameter<Integer> ATTACK_TICKS = EntityDataManager.defineId(PoisonQuillVineEntity.class, DataSerializers.INT);
    private static final int MAX_LIFE_TIME = 300;

    AnimationFactory factory = new AnimationFactory(this);


    public PoisonQuillVineEntity(World world) {
        super(ModEntityTypes.POISON_QUILL_VINE.get(), world);
    }

    public PoisonQuillVineEntity(EntityType<? extends PoisonQuillVineEntity> entityType, World world) {
        super(entityType, world);
    }

    public PoisonQuillVineEntity(EntityType<? extends PoisonQuillVineEntity> entityType, World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(entityType, worldIn, x, y, z, casterIn, lifeTicks);
    }

    public PoisonQuillVineEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(ModEntityTypes.POISON_QUILL_VINE.get(), worldIn, x, y, z, casterIn, lifeTicks);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VineEntity.setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_TICKS, 0);
    }

    public int getAttackTicks() {
        return this.entityData.get(ATTACK_TICKS);
    }

    public void setAttackTicks(int p_189794_1_) {
        this.entityData.set(ATTACK_TICKS, p_189794_1_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 0.0D, 40, 20.0F));

        this.targetSelector.addGoal(0, new MobHurtByTargetGoal(this, VineEntity.class));
        this.targetSelector.addGoal(1, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = this.getPoisonQuill(distanceFactor);
        double xDifference = target.getX() - this.getX();
        double yDifference = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double zDifference = target.getZ() - this.getZ();
        float adjustedHorizontalDistanceSq = MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference) * 0.2F;
        abstractarrowentity.shoot(xDifference, yDifference + (double) adjustedHorizontalDistanceSq, zDifference, 1.5F, 10.0F - 10.0F);
        if (!this.isSilent()) {
            this.level.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }
        setAttackTicks(18);

        this.level.addFreshEntity(abstractarrowentity);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAttackTicks() >= 0) {
            this.setAttackTicks(getAttackTicks() - 1);
        }

    }

    protected AbstractArrowEntity getPoisonQuill(float distanceFactor) {
        ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
        PotionUtils.setPotion(itemstack, Potions.POISON);
        return ProjectileHelper.getMobArrow(this, itemstack, distanceFactor);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.entityData.get(DYING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poison_quill_vine.die", false));
        } else if (this.getLifeTicks() > MAX_LIFE_TIME - 36) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poison_quill_vine.burst", false));
        } else if (this.getAttackTicks() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poison_quill_vine.firing", false));
        } else if (this.getTarget() != null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poison_quill_vine.armed_idle", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poison_quill_vine.grown_idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
