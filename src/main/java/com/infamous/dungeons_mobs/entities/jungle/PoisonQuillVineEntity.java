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
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PoisonQuillVineEntity extends VineEntity implements IRangedAttackMob {

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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RangedAttackGoal(this,  0.0D, 40, 20.0F));

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
        abstractarrowentity.shoot(xDifference, yDifference + (double)adjustedHorizontalDistanceSq, zDifference, 1.5F, 10.0F - 10.0F);
        if (!this.isSilent()) {
            this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }

        this.level.addFreshEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity getPoisonQuill(float distanceFactor) {
        ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
        PotionUtils.setPotion(itemstack, Potions.POISON);
        return ProjectileHelper.getMobArrow(this, itemstack, distanceFactor);
    }

}
