package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.MULTISHOT;
import static net.minecraft.entity.Entity.getHorizontalDistanceSqr;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class ZDoneMultishotMobEnchantment extends MobEnchantment {

    private static boolean isAdding = false;

    public ZDoneMultishotMobEnchantment(Rarity rarity) {
        super(rarity, Type.RANGED);
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof ProjectileEntity) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            if (!shooterIsLiving(projectile)) return;
            LivingEntity owner = (LivingEntity) projectile.getOwner();
            executeIfPresent(owner, MULTISHOT.get(), () -> {
                if(!projectile.level.isClientSide && projectile.tickCount == 0 && !isAdding) {
                    isAdding = true;
                    CompoundNBT compoundNBT = new CompoundNBT();
                    compoundNBT = projectile.saveWithoutId(compoundNBT);
                    addProjectile(projectile, compoundNBT, 15.0F);
                    addProjectile(projectile, compoundNBT, -15.0F);
                    addProjectile(projectile, compoundNBT, 30.0F);
                    addProjectile(projectile, compoundNBT, -30.0F);
                    isAdding = false;
                }
            });
        }
    }

    private static void addProjectile(ProjectileEntity projectile, CompoundNBT compoundNBT, float rotation) {
        ProjectileEntity newProjectile = (ProjectileEntity) projectile.getType().create(projectile.level);
        UUID uuid = newProjectile.getUUID();
        newProjectile.load(compoundNBT);
        newProjectile.setUUID(uuid);
        Vector3d vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));
        newProjectile.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
        newProjectile.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
        newProjectile.xRot = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
        newProjectile.yRotO = newProjectile.yRot;
        newProjectile.xRotO = newProjectile.xRot;
        if(newProjectile instanceof DamagingProjectileEntity){
            DamagingProjectileEntity newDamagingProjectile = (DamagingProjectileEntity) newProjectile;
            Vector3d newPower = new Vector3d(newDamagingProjectile.xPower, newDamagingProjectile.yPower, newDamagingProjectile.zPower).yRot((float) (Math.PI / rotation));
            newDamagingProjectile.xPower = newPower.x;
            newDamagingProjectile.yPower = newPower.y;
            newDamagingProjectile.zPower = newPower.z;
        }
        projectile.level.addFreshEntity(newProjectile);
    }

    public static boolean shooterIsLiving(ProjectileEntity projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }
}