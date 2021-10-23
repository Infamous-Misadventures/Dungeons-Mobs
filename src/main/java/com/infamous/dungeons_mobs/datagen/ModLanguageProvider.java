package com.infamous.dungeons_mobs.datagen;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.MOB_ENCHANTMENTS_DEFERRED;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, DungeonsMobs.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addConfigOptions();
        addMobEnchantments();
        addTips();
    }

    private void addMobEnchantments() {
        MOB_ENCHANTMENTS_DEFERRED.getEntries().forEach(mobEnchantmentRegistryObject -> {
            ResourceLocation id = mobEnchantmentRegistryObject.getId();
            MobEnchantment mobEnchantment = mobEnchantmentRegistryObject.get();
            add(mobEnchantment.getDescriptionId(), getNameFromId(id.getPath()));
        });
    }

    private void addTips() {
    }

    private void addConfigOptions() {
    }

    private String getNameFromId(String idString) {
        StringBuilder sb = new StringBuilder();
        for(String word : idString.toLowerCase().split("_") )
        {
            sb.append(word.substring(0,1).toUpperCase() );
            sb.append(word.substring(1) );
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
