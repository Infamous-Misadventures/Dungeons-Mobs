package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = ItemSupplier.class
)
public class BlueNethershroomEntity extends ThrowableItemProjectile implements ItemSupplier {

    public static final int LIGHT_BLUE_HEX_COLOR_CODE = 0x00e0ff;

    public BlueNethershroomEntity(EntityType<? extends BlueNethershroomEntity> entityType, Level world) {
        super(entityType, world);
    }

    public BlueNethershroomEntity(Level world, LivingEntity shooter) {
        super(ModEntityTypes.BLUE_NETHERSHROOM.get(), shooter, world);
    }

    public BlueNethershroomEntity(Level world, double x, double y, double z) {
        super(ModEntityTypes.BLUE_NETHERSHROOM.get(), x, y, z, world);
    }

    protected Item getDefaultItem() {
        return ModItems.BLUE_NETHERSHROOM.get();
    }

    protected float getGravity() {
        return 0.05F;
    }

    protected void onHit(HitResult rtr) {
        super.onHit(rtr);
        ItemStack itemstack = this.getItem();
        List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
        if (!list.isEmpty()) {
            if (!this.level.isClientSide) {
                Entity target = null;
                if (rtr instanceof EntityHitResult entityHitResult) {
                    target = entityHitResult.getEntity();
                }
                this.makeAreaOfEffectCloud(target, itemstack);
                this.discard();
                BlockPos blockPos = this.blockPosition();
                Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
                this.level.playSound(null, vec3.x + 0.5D, vec3.y() + 0.5D, vec3.z() + 0.5D, ModSoundEvents.FUNGUS_THROWER_FUNGUS_LAND.get(), SoundSource.NEUTRAL, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    private void makeAreaOfEffectCloud(@Nullable Entity target, ItemStack itemStack) {
        AreaEffectCloud aoeCloud = new AreaEffectCloud(this.level,
                target != null ? target.getX() : this.getX(),
                target != null ? target.getY() : this.getY(),
                target != null ? target.getZ() : this.getZ());
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity) {
            aoeCloud.setOwner((LivingEntity) owner);
        }

        aoeCloud.setRadius(3.0F);
        aoeCloud.setRadiusOnUse(-0.5F);
        aoeCloud.setWaitTime(10);
        aoeCloud.setRadiusPerTick(-aoeCloud.getRadius() / (float) aoeCloud.getDuration());

        for (MobEffectInstance customEffects : PotionUtils.getCustomEffects(itemStack)) {
            aoeCloud.addEffect(new MobEffectInstance(customEffects));
        }

        this.level.addFreshEntity(aoeCloud);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}