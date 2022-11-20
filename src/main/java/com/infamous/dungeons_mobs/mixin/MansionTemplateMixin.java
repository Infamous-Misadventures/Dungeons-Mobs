package com.infamous.dungeons_mobs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.WoodlandMansionPieces;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WoodlandMansionPieces.WoodlandMansionPiece.class)
public abstract class MansionTemplateMixin extends TemplateStructurePiece {


    public MansionTemplateMixin(StructurePieceType p_210083_, int p_210084_, StructureManager p_210085_, ResourceLocation p_210086_, String p_210087_, StructurePlaceSettings p_210088_, BlockPos p_210089_) {
        super(p_210083_, p_210084_, p_210085_, p_210086_, p_210087_, p_210088_, p_210089_);
    }

    @Inject(at = @At("HEAD"), method = "handleDataMarker", cancellable = true)
    private void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb, CallbackInfo callbackInfo) {
        if (!function.startsWith("Chest") && !function.equals("Warrior") && !function.equals("Mage")) {
            ResourceLocation entityResourceLocation = new ResourceLocation(function);
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityResourceLocation);
            if (entityType != null) {
                Entity entity = entityType.create(worldIn.getLevel());
                if(entity instanceof Mob){
                    Mob mansionSpawn = (Mob) entity;
                    mansionSpawn.setPersistenceRequired();
                    mansionSpawn.moveTo(pos, 0.0F, 0.0F);
                    mansionSpawn.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(mansionSpawn.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                    worldIn.addFreshEntity(mansionSpawn);
                    worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    callbackInfo.cancel();
                }
            }
        }
    }
}
