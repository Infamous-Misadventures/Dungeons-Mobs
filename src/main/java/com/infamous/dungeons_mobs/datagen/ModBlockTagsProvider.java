package com.infamous.dungeons_mobs.datagen;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
	}
}
