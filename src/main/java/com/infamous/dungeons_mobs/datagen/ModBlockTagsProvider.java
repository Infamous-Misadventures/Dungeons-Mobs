package com.infamous.dungeons_mobs.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
