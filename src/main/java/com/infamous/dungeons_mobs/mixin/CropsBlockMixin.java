package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropsBlock.class)
public abstract class CropsBlockMixin{

    @Inject(at = @At("HEAD"), method = "entityInside", cancellable = true)
    private void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn, CallbackInfo callbackInfo){
        if (entityIn instanceof RedstoneGolemEntity && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entityIn)) {
            worldIn.destroyBlock(pos, true, entityIn);
        }
        else if (entityIn instanceof SquallGolemEntity && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entityIn)) {
            worldIn.destroyBlock(pos, true, entityIn);
        }
    }
}
