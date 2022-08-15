package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class Tornado2Entity extends CreatureEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    private int ltick = 0;

    private LivingEntity caster;
    private UUID casterUuid;
    private LivingEntity target;
    private UUID targetUUID;
    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUUID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    public void setTargetr(@Nullable LivingEntity target) {
        this.target = target;
        this.targetUUID = target == null ? null : target.getUUID();
    }

    @Nullable
    public LivingEntity getTargetr() {
        if (this.target == null && this.targetUUID != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.targetUUID);
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity)entity;
            }
        }

        return this.target;
    }
    public Tornado2Entity(World world) {
        super(ModEntityTypes.TORNADO_MELEE.get(), world);
    }

    public Tornado2Entity(EntityType<? extends Tornado2Entity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.ATTACK_DAMAGE, 0.0D);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.ltick ++;
        if (this.ltick == 4) {
            this.playSound(ModSoundEvents.B_WIND.get(), 1.5f,1);
        }
        if (this.ltick <= 3 && this.getTargetr() != null && this.getCaster() != null) {
            WindcallerEntity v = (WindcallerEntity)this.getCaster();
            LivingEntity c = this.getTargetr();
            v.getLookControl().setLookAt(c,5,5);
            this.yBodyRot = -v.yBodyRot;
            this.yBodyRotO = this.yBodyRot;
            this.yRot = -v.yBodyRot;
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
            this.moveTo(
                    v.getX() + Math.max(Math.min(c.getX() - v.getX(), 1.25), -1.25),
                    v.getY(),
                    v.getZ() + Math.max(Math.min(c.getX() - v.getZ(), 1.25), -1.25),
                    0,
                    0
            );
            return;
        }
        if (this.ltick >= 32) {
            this.remove();
        }
        List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(1, 1, 1)));
        for (Entity entity : list) {
            if (entity instanceof LivingEntity && !(entity == this.caster) && !(entity instanceof Tornado2Entity)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                float attackKnockback = 0.45f;
                double ratioX = (double) MathHelper.sin(this.yRot * ((float) Math.PI / 360F));
                double ratioZ = (double) (-MathHelper.cos(this.yRot * ((float) Math.PI / 360F)));
                double knockbackReduction = 0.0D;
                this.forceKnockback(livingEntity, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tornado.blast_attack2", false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
        if (event.isCanceled()) return;
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        strength = (float) ((double) strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
        if (!(strength <= 0.0F)) {
            attackTarget.hasImpulse = true;
            Vector3d vector3d = attackTarget.getDeltaMovement();
            Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double) strength);
            attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }
}