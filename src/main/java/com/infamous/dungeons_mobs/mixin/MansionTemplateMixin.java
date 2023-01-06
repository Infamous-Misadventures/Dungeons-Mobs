package com.infamous.dungeons_mobs.mixin;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(WoodlandMansionPieces.WoodlandMansionPiece.class)
public abstract class MansionTemplateMixin extends TemplateStructurePiece {

    public MansionTemplateMixin(StructurePieceType p_226886_, int p_226887_, StructureTemplateManager p_226888_, ResourceLocation p_226889_, String p_226890_, StructurePlaceSettings p_226891_, BlockPos p_226892_) {
        super(p_226886_, p_226887_, p_226888_, p_226889_, p_226890_, p_226891_, p_226892_);
    }

    public MansionTemplateMixin(StructurePieceType p_226894_, CompoundTag p_226895_, StructureTemplateManager p_226896_, Function<ResourceLocation, StructurePlaceSettings> p_226897_) {
        super(p_226894_, p_226895_, p_226896_, p_226897_);
    }

    @Inject(at = @At("HEAD"), method = "handleDataMarker", cancellable = true)
    private void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, RandomSource rand, BoundingBox sbb, CallbackInfo callbackInfo) {
        if (!function.startsWith("Chest") && !function.equals("Warrior") && !function.equals("Mage")) {
            ResourceLocation entityResourceLocation = new ResourceLocation(function);
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityResourceLocation);
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
