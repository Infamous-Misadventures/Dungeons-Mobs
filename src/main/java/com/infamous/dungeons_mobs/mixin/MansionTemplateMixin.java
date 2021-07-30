package com.infamous.dungeons_mobs.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.structure.WoodlandMansionPieces;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WoodlandMansionPieces.MansionTemplate.class)
public abstract class MansionTemplateMixin extends TemplateStructurePiece {

    @Shadow
    @Final
    private String templateName;

    public MansionTemplateMixin(IStructurePieceType structurePieceTypeIn, int componentTypeIn) {
        super(structurePieceTypeIn, componentTypeIn);
    }

    @Inject(at = @At("HEAD"), method = "handleDataMarker", cancellable = true)
    private void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb, CallbackInfo callbackInfo) {
        if (!function.startsWith("Chest") && !function.equals("Warrior") && !function.equals("Mage")) {
            ResourceLocation entityResourceLocation = new ResourceLocation(function);
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityResourceLocation);
            if (entityType != null) {
                Entity entity = entityType.create(worldIn.getLevel());
                if(entity instanceof MobEntity){
                    MobEntity mansionSpawn = (MobEntity) entity;
                    mansionSpawn.setPersistenceRequired();
                    mansionSpawn.moveTo(pos, 0.0F, 0.0F);
                    mansionSpawn.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(mansionSpawn.blockPosition()), SpawnReason.STRUCTURE, (ILivingEntityData)null, (CompoundNBT)null);
                    worldIn.addFreshEntity(mansionSpawn);
                    worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    callbackInfo.cancel();
                }
            }
        }
    }
}
