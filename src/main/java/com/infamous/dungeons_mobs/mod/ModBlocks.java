package com.infamous.dungeons_mobs.mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.blocks.CorruptedPyreBlock;
import com.infamous.dungeons_mobs.blocks.WraithFireBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // WRAITH FIRE
    public static final RegistryObject<WraithFireBlock> WRAITH_FIRE_BLOCK = BLOCKS.register("wraith_fire",
            () -> new WraithFireBlock(AbstractBlock.Properties.of(Material.FIRE, MaterialColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .instabreak()
                    .lightLevel((state) -> 10)
                    .sound(SoundType.WOOL)));
    
    // CORRUPTED PYRE
    public static final RegistryObject<CorruptedPyreBlock> CORRUPTED_PYRE_BLOCK = BLOCKS.register("corrupted_pyre",
            () -> new CorruptedPyreBlock(AbstractBlock.Properties.of(Material.FIRE, MaterialColor.COLOR_MAGENTA)
                    .noCollission()
                    .instabreak()
                    .lightLevel((state) -> 5)
                    .sound(SoundType.WOOL)));
}
