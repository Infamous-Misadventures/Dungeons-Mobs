package com.infamous.dungeons_mobs.mixin;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = IllusionerEntity.class)
public abstract class IllusionerEntityMixin extends SpellcastingIllagerEntity implements IRangedAttackMob {

    private Vector3d[] emptyRenderLocation = new Vector3d[0];

    protected IllusionerEntityMixin(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    @OnlyIn(Dist.CLIENT)
    @Inject(at = @At("HEAD"), method = "getIllusionOffsets", cancellable = true)
    private void getRenderLocations(float partialTicks, CallbackInfoReturnable<Vector3d[]> callbackInfoReturnable){
        if(DungeonsMobsConfig.COMMON.ENABLE_CLONING_ILLUSIONERS.get()){
            callbackInfoReturnable.setReturnValue(emptyRenderLocation);
        }
    }

    @Inject(at = @At("HEAD"), method = "performRangedAttack", cancellable = true)
    private void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor, CallbackInfo callbackInfo){
        if(DungeonsMobsConfig.COMMON.ENABLE_CLONING_ILLUSIONERS.get()){
            ItemStack fireworkRocket = createPinkRocket();
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(this.level, fireworkRocket, this, this.getX(), this.getEyeY() - (double)0.15F, this.getZ(), true);
            double xDifference = target.getX() - this.getX();
            double yDifference = target.getY(0.3333333333333333D) - fireworkrocketentity.getY();
            double zDifference = target.getZ() - this.getZ();
            double horizontalDifference = (double) MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference);
            fireworkrocketentity.shoot(xDifference, yDifference + horizontalDifference * (double)0.2F, zDifference, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(fireworkrocketentity);
            callbackInfo.cancel();
        }
    }

    private static ItemStack createPinkRocket() {
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundNBT starExplosionNBT = star.getOrCreateTagElement("Explosion");
        starExplosionNBT.putInt("Type", FireworkRocketItem.Shape.BURST.getId());
        CompoundNBT rocketFireworksNBT = rocket.getOrCreateTagElement("Fireworks");
        ListNBT rocketExplosionsNBT = new ListNBT();
        CompoundNBT actualStarExplosionNBT = star.getTagElement("Explosion");
        if (actualStarExplosionNBT != null) {
            // making firework pink
            List<Integer> colorList = Lists.newArrayList();
            int pinkFireworkColor = DyeColor.PINK.getFireworkColor();
            colorList.add(pinkFireworkColor);
            actualStarExplosionNBT.putIntArray("Colors", colorList);
            actualStarExplosionNBT.putIntArray("FadeColors", colorList);
            // adding actualStarExplosionNBT to rocketExplosionsNBT
            rocketExplosionsNBT.add(actualStarExplosionNBT);
        }
        if (!rocketExplosionsNBT.isEmpty()) {
            rocketFireworksNBT.put("Explosions", rocketExplosionsNBT);
        }
        return rocket;
    }
}
