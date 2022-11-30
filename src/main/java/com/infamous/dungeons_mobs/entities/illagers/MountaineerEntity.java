package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_libraries.utils.GoalUtils;
import com.infamous.dungeons_mobs.entities.AnimatableMeleeAttackMob;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.BasicModdedAttackGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Map;


public class MountaineerEntity extends Vindicator implements SpawnArmoredMob, IAnimatable, AnimatableMeleeAttackMob {
	
	   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(MountaineerEntity.class, EntityDataSerializers.BYTE);
    public int attackAnimationTick;
    public int attackAnimationLength = 7;
    public int attackAnimationActionPoint = 6;
	   
    public MountaineerEntity(Level worldIn){
        super(ModEntityTypes.MOUNTAINEER.get(), worldIn);
    }

    public MountaineerEntity(EntityType<? extends MountaineerEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        GoalUtils.removeGoal(this.goalSelector, MeleeAttackGoal.class);
        this.goalSelector.addGoal(4, new BasicModdedAttackGoal<>(this, null, 20));
        this.goalSelector.addGoal(5, new ApproachTargetGoal(this, 0, 1.0D, true));
    }
    
    protected PathNavigation createNavigation(Level p_175447_1_) {
        return new WallClimberNavigation(this, p_175447_1_);
     }

     protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
     }

     public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
           this.setClimbing(this.horizontalCollision);
        }

     }
     
     public boolean onClimbable() {
         return this.isClimbing();
      }
     
     public boolean isClimbing() {
         return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
      }

      public void setClimbing(boolean p_70839_1_) {
         byte b0 = this.entityData.get(DATA_FLAGS_ID);
         if (p_70839_1_) {
            b0 = (byte)(b0 | 1);
         } else {
            b0 = (byte)(b0 & -2);
         }

         this.entityData.set(DATA_FLAGS_ID, b0);
      }
      
    protected float getSoundVolume() {
    	return 0.5F;
    }
    
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
     }
    
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
     }

     protected SoundEvent getDeathSound() {
        return ModSoundEvents.MOUNTAINEER_DEATH.get();
     }

     protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.MOUNTAINEER_HURT.get();
     }


    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 28.0D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getHead().get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getChest().get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getLegs().get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getFeet().get()));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_213386_1_, DifficultyInstance p_213386_2_,
                                           MobSpawnType p_213386_3_, @Nullable SpawnGroupData p_213386_4_, @Nullable CompoundTag p_213386_5_) {
        SpawnGroupData iLivingEntityData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_,
                p_213386_5_);
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.populateDefaultEquipmentEnchantments(p_213386_2_);
        return iLivingEntityData;
    }

    public void applyRaidBuffs(int waveNumber, boolean bool) {
        ItemStack itemStack = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (raid != null && waveNumber > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = false;
        if (raid != null) {
            flag = this.random.nextFloat() <= raid.getEnchantOdds();
        }
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemStack);
        }

        this.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
    }

    @Override
    public IllagerArmPose getArmPose() {
        IllagerArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == IllagerArmPose.CROSSED){
            return IllagerArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.MOUNTAINEER_ARMOR;
    }

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.attackAnimationTick = attackAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    @Override
    public int getAttackAnimationTick() {
        return attackAnimationTick;
    }

    @Override
    public void setAttackAnimationTick(int attackAnimationTick) {
        this.attackAnimationTick = attackAnimationTick;
    }

    @Override
    public int getAttackAnimationLength() {
        return attackAnimationLength;
    }

    @Override
    public int getAttackAnimationActionPoint() {
        return attackAnimationActionPoint;
    }

    public void tickDownAnimTimers() {
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }
    }
    
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        String animation = "animation.vindicator";
        if (false) {
            animation += "_mcd";
        }
        String handSide = "_right";
        if(this.isLeftHanded()){
            handSide = "_left";
        }
        if(this.getMainHandItem().isEmpty()){
            handSide += "_both";
        }
        String crossed = "";
        if(IllagerArmsUtil.armorHasCrossedArms(this, this.getItemBySlot(EquipmentSlot.CHEST))){
            crossed = "_crossed";
        }
        if (this.attackAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".attack" + handSide, true));
        } else if (this.isAggressive() && !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".run" + handSide, true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".walk" + crossed, true));
        } else {
            if (this.isCelebrating()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".win", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".idle" + crossed, true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
