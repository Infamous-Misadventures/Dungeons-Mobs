package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.mobenchants.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.LEVITATION_SHOT;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class LevitationShotMobEnchantment extends MobEnchantment {

    public LevitationShotMobEnchantment(Rarity rarity) {
        super(rarity, Type.RANGED);
    }

    //TODO: Verify if enchant should be applied to projectile instead of checking for owner.
    @SubscribeEvent
    public static void onImpact(ProjectileImpactEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof ProjectileEntity) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            RayTraceResult rayTraceResult = event.getRayTraceResult();
            if(!projectileHitLivingEntity(rayTraceResult)) return;
            if(!shooterIsLiving(projectile)) return;
            LivingEntity victim = (LivingEntity) ((EntityRayTraceResult) rayTraceResult).getEntity();
            LivingEntity owner = (LivingEntity) projectile.getOwner();
            executeIfPresent(owner, LEVITATION_SHOT.get(), () -> {
                victim.addEffect(new EffectInstance(Effects.LEVITATION, 100));
            });
        }
    }

    public static boolean projectileHitLivingEntity(RayTraceResult rayTraceResult) {
        if(rayTraceResult instanceof EntityRayTraceResult){
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
            if(entityRayTraceResult.getEntity() instanceof LivingEntity){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static boolean shooterIsLiving(ProjectileEntity projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }
}