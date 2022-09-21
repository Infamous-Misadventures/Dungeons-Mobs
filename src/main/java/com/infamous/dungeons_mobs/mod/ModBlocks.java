package com.infamous.dungeons_mobs.mod;

import com.infamous.dungeons_mobs.blocks.WraithFireBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // WRAITH FIRE
    public static final RegistryObject<WraithFireBlock> WRAITH_FIRE_BLOCK = BLOCKS.register("wraith_fire",
            () -> new WraithFireBlock(AbstractBlock.Properties.of(Material.FIRE, MaterialColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .instabreak()
                    .lightLevel((state) -> 10)
                    .sound(SoundType.WOOL)));
}
