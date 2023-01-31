package com.infamous.dungeons_mobs.datagen;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.tags.EntityTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(DataGenerator p_126517_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126517_, DungeonsMobs.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(EntityTags.PIGLINS).add(EntityType.PIGLIN).add(ModEntityTypes.FUNGUS_THROWER.get());
    }
}
