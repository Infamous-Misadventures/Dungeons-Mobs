package com.infamous.dungeons_mobs.datagen;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import static com.infamous.dungeons_mobs.mod.ModMobEnchants.MOB_ENCHANTS_DEFERRED;

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
        MOB_ENCHANTS_DEFERRED.getEntries().forEach(mobEnchantmentRegistryObject -> {
            ResourceLocation id = mobEnchantmentRegistryObject.getId();
            MobEnchant mobEnchant = mobEnchantmentRegistryObject.get();
            add("mobenchant.enchantwithmob.name.dungeons_mobs."+mobEnchant.getRegistryName().getPath(), getNameFromId(id.getPath()));
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
