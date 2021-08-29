package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.material.PushReaction;
import net.minecraft.command.arguments.ParticleArgument;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TornadoEntity extends Entity {
    private static final Logger PRIVATE_LOGGER = LogManager.getLogger();
    private static final DataParameter<Float> RADIUS = EntityDataManager.defineId(TornadoEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(TornadoEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.defineId(TornadoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<IParticleData> PARTICLE = EntityDataManager.defineId(TornadoEntity.class, DataSerializers.PARTICLE);
    private Potion potion = Potions.EMPTY;
    private final List<EffectInstance> effects = Lists.newArrayList();
    private final Map<Entity, Integer> reapplicationDelayMap = Maps.newHashMap();
    private int duration = 600;
    private int waitTime = 20;
    private int reapplicationDelay = 20;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private LivingEntity owner;
    private UUID ownerUniqueId;
    private Entity target;
    private UUID targetUniqueId;

    public TornadoEntity(World world){
        super(ModEntityTypes.TORNADO.get(), world);
    }

    public TornadoEntity(EntityType<? extends TornadoEntity> cloud, World world) {
        super(cloud, world);
        this.noPhysics = true;
        this.setRadius(3.0F);
    }

    public TornadoEntity(World worldIn, LivingEntity caster, double x, double y, double z) {
        this(ModEntityTypes.TORNADO.get(), worldIn);
        this.setOwner(caster);
        this.setPos(x, y, z);
    }

    public TornadoEntity(World world, LivingEntity caster, Entity target) {
        this(world, caster, target.getX(), target.getY(), target.getZ());
        this.setTarget(target);
    }

    protected void defineSynchedData() {
        this.getEntityData().define(COLOR, 0);
        this.getEntityData().define(RADIUS, 0.5F);
        this.getEntityData().define(IGNORE_RADIUS, false);
        this.getEntityData().define(PARTICLE, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
    }

    public void setRadius(float radiusIn) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(RADIUS, radiusIn);
        }

    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public float getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setPotion(Potion potionIn) {
        this.potion = potionIn;
        if (!this.colorSet) {
            this.updateFixedColor();
        }

    }

    private void updateFixedColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getEntityData().set(COLOR, 0);
        } else {
            this.getEntityData().set(COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }

    }

    public void addEffect(EffectInstance effect) {
        this.effects.add(effect);
        if (!this.colorSet) {
            this.updateFixedColor();
        }

    }

    public int getColor() {
        return this.getEntityData().get(COLOR);
    }

    public void setColor(int colorIn) {
        this.colorSet = true;
        this.getEntityData().set(COLOR, colorIn);
    }

    public IParticleData getParticleData() {
        return this.getEntityData().get(PARTICLE);
    }

    public void setParticleData(IParticleData particleData) {
        this.getEntityData().set(PARTICLE, particleData);
    }

    /**
     * Sets if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    protected void setIgnoreRadius(boolean ignoreRadius) {
        this.getEntityData().set(IGNORE_RADIUS, ignoreRadius);
    }

    /**
     * Returns true if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    public boolean shouldIgnoreRadius() {
        return this.getEntityData().get(IGNORE_RADIUS);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        boolean shouldIgnoreRadius = this.shouldIgnoreRadius();
        float radius = this.getRadius();
        if (this.level.isClientSide) {
            IParticleData iparticledata = this.getParticleData();
            if (shouldIgnoreRadius) {
                if (this.random.nextBoolean()) {
                    for(int i = 0; i < 2; ++i) {
                        float randWithin2Pi = this.random.nextFloat() * ((float)Math.PI * 2F);
                        float adjustedSqrtFloat = MathHelper.sqrt(this.random.nextFloat()) * 0.2F;
                        float adjustedCosine = MathHelper.cos(randWithin2Pi) * adjustedSqrtFloat;
                        float adjustedSine = MathHelper.sin(randWithin2Pi) * adjustedSqrtFloat;
                        this.level.addAlwaysVisibleParticle(iparticledata, true,
                                this.getX() + (double)adjustedCosine,
                                this.getY(),
                                this.getZ() + (double)adjustedSine,
                                0.0D,
                                0.07D,
                                0.0D);
                        //this.world.addOptionalParticle(iparticledata, this.getPosX() + (double)adjustedCosine, this.getPosY(), this.getPosZ() + (double)adjustedSine, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                float areaOfEffect = (float)Math.PI * radius * radius;

                for(int k1 = 0; (float)k1 < areaOfEffect; ++k1) {
                    float randWithin2Pi = this.random.nextFloat() * ((float)Math.PI * 2F);
                    float adjustedSqrtFloat = MathHelper.sqrt(this.random.nextFloat()) * radius;
                    float adjustedCosine = MathHelper.cos(randWithin2Pi) * adjustedSqrtFloat;
                    float adjustedSine = MathHelper.sin(randWithin2Pi) * adjustedSqrtFloat;
                    this.level.addAlwaysVisibleParticle(iparticledata,
                            this.getX() + (double)adjustedCosine,
                            this.getY(),
                            this.getZ() + (double)adjustedSine,
                            0.0D,
                            0.07D,
                            0.0D);
                    //this.world.addOptionalParticle(iparticledata, this.getPosX() + (double)adjustedCosine, this.getPosY(), this.getPosZ() + (double)adjustedSine, (0.5D - this.rand.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.rand.nextDouble()) * 0.15D);
                }
            }
        } else {
            if(this.target != null){
                this.setPos(target.getX(), this.getY(), target.getZ());
            }
            if (this.tickCount >= this.waitTime + this.duration) {
                this.remove();
                return;
            }

            boolean flag1 = this.tickCount < this.waitTime;
            if (shouldIgnoreRadius != flag1) {
                this.setIgnoreRadius(flag1);
            }

            if (flag1) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                radius += this.radiusPerTick;
                if (radius < 0.5F) {
                    this.remove();
                    return;
                }

                this.setRadius(radius);
            }

            if (this.tickCount % 5 == 0) {

                this.reapplicationDelayMap.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());

                List<EffectInstance> list = Lists.newArrayList();

                for(EffectInstance effectinstance1 : this.potion.getEffects()) {
                    list.add(new EffectInstance(effectinstance1.getEffect(), effectinstance1.getDuration() / 4, effectinstance1.getAmplifier(), effectinstance1.isAmbient(), effectinstance1.isVisible()));
                }

                list.addAll(this.effects);
                if (list.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                } else {
                    List<LivingEntity> list1 = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                    if (!list1.isEmpty()) {
                        for(LivingEntity livingentity : list1) {
                            if (!this.reapplicationDelayMap.containsKey(livingentity) && livingentity.isAffectedByPotions()) {
                                double xDifference = livingentity.getX() - this.getX();
                                double zDifference = livingentity.getZ() - this.getZ();
                                double horizontalDistanceSquared = xDifference * xDifference + zDifference * zDifference;
                                if (horizontalDistanceSquared <= (double)(radius * radius)) {
                                    this.reapplicationDelayMap.put(livingentity, this.tickCount + this.reapplicationDelay);

                                    for(EffectInstance effectinstance : list) {
                                        if (effectinstance.getEffect().isInstantenous()) {
                                            effectinstance.getEffect().applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), 0.5D);
                                        } else {
                                            livingentity.addEffect(new EffectInstance(effectinstance));
                                        }
                                    }

                                    if (this.radiusOnUse != 0.0F) {
                                        radius += this.radiusOnUse;
                                        if (radius < 0.5F) {
                                            this.remove();
                                            return;
                                        }

                                        this.setRadius(radius);
                                    }

                                    if (this.durationOnUse != 0) {
                                        this.duration += this.durationOnUse;
                                        if (this.duration <= 0) {
                                            this.remove();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void setRadiusOnUse(float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    public void setTarget(@Nullable Entity targetIn) {
        this.target = targetIn;
        this.targetUniqueId = targetIn == null ? null : targetIn.getUUID();
    }
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }
    @Nullable
    public Entity getTarget() {
        if (this.target == null && this.targetUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.targetUniqueId);
            this.target = entity;
        }

        return this.target;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.tickCount = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.reapplicationDelay = compound.getInt("ReapplicationDelay");
        this.durationOnUse = compound.getInt("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        if (compound.hasUUID("Owner")) {
            this.ownerUniqueId = compound.getUUID("Owner");
        }
        if (compound.hasUUID("Target")) {
            this.targetUniqueId = compound.getUUID("Target");
        }

        if (compound.contains("Particle", 8)) {
            try {
                this.setParticleData(ParticleArgument.readParticle(new StringReader(compound.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                PRIVATE_LOGGER.warn("Couldn't load custom particle {}", compound.getString("Particle"), commandsyntaxexception);
            }
        }

        if (compound.contains("Color", 99)) {
            this.setColor(compound.getInt("Color"));
        }

        if (compound.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(compound));
        }

        if (compound.contains("Effects", 9)) {
            ListNBT listnbt = compound.getList("Effects", 10);
            this.effects.clear();

            for(int i = 0; i < listnbt.size(); ++i) {
                EffectInstance effectinstance = EffectInstance.load(listnbt.getCompound(i));
                if (effectinstance != null) {
                    this.addEffect(effectinstance);
                }
            }
        }

    }

    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("Duration", this.duration);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("ReapplicationDelay", this.reapplicationDelay);
        compound.putInt("DurationOnUse", this.durationOnUse);
        compound.putFloat("RadiusOnUse", this.radiusOnUse);
        compound.putFloat("RadiusPerTick", this.radiusPerTick);
        compound.putFloat("Radius", this.getRadius());
        compound.putString("Particle", this.getParticleData().writeToString());
        if (this.ownerUniqueId != null) {
            compound.putUUID("Owner", this.ownerUniqueId);
        }
        if (this.targetUniqueId != null) {
            compound.putUUID("Target", this.targetUniqueId);
        }

        if (this.colorSet) {
            compound.putInt("Color", this.getColor());
        }

        if (this.potion != Potions.EMPTY && this.potion != null) {
            compound.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }

        if (!this.effects.isEmpty()) {
            ListNBT listnbt = new ListNBT();

            for(EffectInstance effectinstance : this.effects) {
                listnbt.add(effectinstance.save(new CompoundNBT()));
            }

            compound.put("Effects", listnbt);
        }

    }

    public void onSyncedDataUpdated(DataParameter<?> key) {
        if (RADIUS.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntitySize getDimensions(Pose poseIn) {
        return EntitySize.scalable(this.getRadius() * 2.0F, 0.5F);
    }
}
