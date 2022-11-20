package com.infamous.dungeons_mobs.datagen;

import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.items.ItemTagWrappers.CURIOS_ARTIFACTS;
import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ModItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        curiosArtifactTags();
    }

    private void curiosArtifactTags() {
        ModItems.ARTIFACTS.forEach((resourceLocation, itemRegistryObject) -> this.tag(CURIOS_ARTIFACTS).add(itemRegistryObject.get()));
    }
}
