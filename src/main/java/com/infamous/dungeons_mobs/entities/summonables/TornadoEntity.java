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
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(TornadoEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(TornadoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.createKey(TornadoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<IParticleData> PARTICLE = EntityDataManager.createKey(TornadoEntity.class, DataSerializers.PARTICLE_DATA);
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
    private LivingEntity target;
    private UUID targetUniqueId;

    public TornadoEntity(World world){
        super(ModEntityTypes.TORNADO.get(), world);
    }

    public TornadoEntity(EntityType<? extends TornadoEntity> cloud, World world) {
        super(cloud, world);
        this.noClip = true;
        this.setRadius(3.0F);
    }

    public TornadoEntity(World worldIn, LivingEntity caster, double x, double y, double z) {
        this(ModEntityTypes.TORNADO.get(), worldIn);
        this.setOwner(caster);
        this.setPosition(x, y, z);
    }

    public TornadoEntity(World world, LivingEntity caster, LivingEntity livingEntity) {
        this(world, caster, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ());
        this.setTarget(livingEntity);
    }

    protected void registerData() {
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(RADIUS, 0.5F);
        this.getDataManager().register(IGNORE_RADIUS, false);
        this.getDataManager().register(PARTICLE, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
    }

    public void setRadius(float radiusIn) {
        if (!this.world.isRemote) {
            this.getDataManager().set(RADIUS, radiusIn);
        }

    }

    public void recalculateSize() {
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        super.recalculateSize();
        this.setPosition(d0, d1, d2);
    }

    public float getRadius() {
        return this.getDataManager().get(RADIUS);
    }

    public void setPotion(Potion potionIn) {
        this.potion = potionIn;
        if (!this.colorSet) {
            this.updateFixedColor();
        }

    }

    private void updateFixedColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getDataManager().set(COLOR, 0);
        } else {
            this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
        }

    }

    public void addEffect(EffectInstance effect) {
        this.effects.add(effect);
        if (!this.colorSet) {
            this.updateFixedColor();
        }

    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int colorIn) {
        this.colorSet = true;
        this.getDataManager().set(COLOR, colorIn);
    }

    public IParticleData getParticleData() {
        return this.getDataManager().get(PARTICLE);
    }

    public void setParticleData(IParticleData particleData) {
        this.getDataManager().set(PARTICLE, particleData);
    }

    /**
     * Sets if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    protected void setIgnoreRadius(boolean ignoreRadius) {
        this.getDataManager().set(IGNORE_RADIUS, ignoreRadius);
    }

    /**
     * Returns true if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    public boolean shouldIgnoreRadius() {
        return this.getDataManager().get(IGNORE_RADIUS);
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
        if (this.world.isRemote) {
            IParticleData iparticledata = this.getParticleData();
            if (shouldIgnoreRadius) {
                if (this.rand.nextBoolean()) {
                    for(int i = 0; i < 2; ++i) {
                        float randWithin2Pi = this.rand.nextFloat() * ((float)Math.PI * 2F);
                        float adjustedSqrtFloat = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                        float adjustedCosine = MathHelper.cos(randWithin2Pi) * adjustedSqrtFloat;
                        float adjustedSine = MathHelper.sin(randWithin2Pi) * adjustedSqrtFloat;
                        this.world.addOptionalParticle(iparticledata, true,
                                this.getPosX() + (double)adjustedCosine,
                                this.getPosY(),
                                this.getPosZ() + (double)adjustedSine,
                                0.0D,
                                0.07D,
                                0.0D);
                        //this.world.addOptionalParticle(iparticledata, this.getPosX() + (double)adjustedCosine, this.getPosY(), this.getPosZ() + (double)adjustedSine, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                float areaOfEffect = (float)Math.PI * radius * radius;

                for(int k1 = 0; (float)k1 < areaOfEffect; ++k1) {
                    float randWithin2Pi = this.rand.nextFloat() * ((float)Math.PI * 2F);
                    float adjustedSqrtFloat = MathHelper.sqrt(this.rand.nextFloat()) * radius;
                    float adjustedCosine = MathHelper.cos(randWithin2Pi) * adjustedSqrtFloat;
                    float adjustedSine = MathHelper.sin(randWithin2Pi) * adjustedSqrtFloat;
                    this.world.addOptionalParticle(iparticledata,
                            this.getPosX() + (double)adjustedCosine,
                            this.getPosY(),
                            this.getPosZ() + (double)adjustedSine,
                            0.0D,
                            0.07D,
                            0.0D);
                    //this.world.addOptionalParticle(iparticledata, this.getPosX() + (double)adjustedCosine, this.getPosY(), this.getPosZ() + (double)adjustedSine, (0.5D - this.rand.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.rand.nextDouble()) * 0.15D);
                }
            }
        } else {
            if(this.target != null){
                this.setPosition(target.getPosX(), this.getPosY(), target.getPosZ());
            }
            if (this.ticksExisted >= this.waitTime + this.duration) {
                this.remove();
                return;
            }

            boolean flag1 = this.ticksExisted < this.waitTime;
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

            if (this.ticksExisted % 5 == 0) {

                this.reapplicationDelayMap.entrySet().removeIf(entry -> this.ticksExisted >= entry.getValue());

                List<EffectInstance> list = Lists.newArrayList();

                for(EffectInstance effectinstance1 : this.potion.getEffects()) {
                    list.add(new EffectInstance(effectinstance1.getPotion(), effectinstance1.getDuration() / 4, effectinstance1.getAmplifier(), effectinstance1.isAmbient(), effectinstance1.doesShowParticles()));
                }

                list.addAll(this.effects);
                if (list.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                } else {
                    List<LivingEntity> list1 = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox());
                    if (!list1.isEmpty()) {
                        for(LivingEntity livingentity : list1) {
                            if (!this.reapplicationDelayMap.containsKey(livingentity) && livingentity.canBeHitWithPotion()) {
                                double xDifference = livingentity.getPosX() - this.getPosX();
                                double zDifference = livingentity.getPosZ() - this.getPosZ();
                                double horizontalDistanceSquared = xDifference * xDifference + zDifference * zDifference;
                                if (horizontalDistanceSquared <= (double)(radius * radius)) {
                                    this.reapplicationDelayMap.put(livingentity, this.ticksExisted + this.reapplicationDelay);

                                    for(EffectInstance effectinstance : list) {
                                        if (effectinstance.getPotion().isInstant()) {
                                            effectinstance.getPotion().affectEntity(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), 0.5D);
                                        } else {
                                            livingentity.addPotionEffect(new EffectInstance(effectinstance));
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
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    public void setTarget(@Nullable LivingEntity targetIn) {
        this.target = targetIn;
        this.targetUniqueId = targetIn == null ? null : targetIn.getUniqueID();
    }
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }
    @Nullable
    public LivingEntity getTarget() {
        if (this.target == null && this.targetUniqueId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.targetUniqueId);
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity)entity;
            }
        }

        return this.target;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(CompoundNBT compound) {
        this.ticksExisted = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.reapplicationDelay = compound.getInt("ReapplicationDelay");
        this.durationOnUse = compound.getInt("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        if (compound.hasUniqueId("Owner")) {
            this.ownerUniqueId = compound.getUniqueId("Owner");
        }
        if (compound.hasUniqueId("Target")) {
            this.targetUniqueId = compound.getUniqueId("Target");
        }

        if (compound.contains("Particle", 8)) {
            try {
                this.setParticleData(ParticleArgument.parseParticle(new StringReader(compound.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                PRIVATE_LOGGER.warn("Couldn't load custom particle {}", compound.getString("Particle"), commandsyntaxexception);
            }
        }

        if (compound.contains("Color", 99)) {
            this.setColor(compound.getInt("Color"));
        }

        if (compound.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotionTypeFromNBT(compound));
        }

        if (compound.contains("Effects", 9)) {
            ListNBT listnbt = compound.getList("Effects", 10);
            this.effects.clear();

            for(int i = 0; i < listnbt.size(); ++i) {
                EffectInstance effectinstance = EffectInstance.read(listnbt.getCompound(i));
                if (effectinstance != null) {
                    this.addEffect(effectinstance);
                }
            }
        }

    }

    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Age", this.ticksExisted);
        compound.putInt("Duration", this.duration);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("ReapplicationDelay", this.reapplicationDelay);
        compound.putInt("DurationOnUse", this.durationOnUse);
        compound.putFloat("RadiusOnUse", this.radiusOnUse);
        compound.putFloat("RadiusPerTick", this.radiusPerTick);
        compound.putFloat("Radius", this.getRadius());
        compound.putString("Particle", this.getParticleData().getParameters());
        if (this.ownerUniqueId != null) {
            compound.putUniqueId("Owner", this.ownerUniqueId);
        }
        if (this.targetUniqueId != null) {
            compound.putUniqueId("Target", this.targetUniqueId);
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
                listnbt.add(effectinstance.write(new CompoundNBT()));
            }

            compound.put("Effects", listnbt);
        }

    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        if (RADIUS.equals(key)) {
            this.recalculateSize();
        }

        super.notifyDataManagerChange(key);
    }

    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntitySize getSize(Pose poseIn) {
        return EntitySize.flexible(this.getRadius() * 2.0F, 0.5F);
    }
}
