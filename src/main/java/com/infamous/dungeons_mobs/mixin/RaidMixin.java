package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.interfaces.BiomeSpecificRaider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(Raid.class)
public class RaidMixin {
    private static final String RAID_WAVE_MEMBER_TYPE_FIELD = "f_37815_";
    @Shadow
    @Final
    private ServerLevel level;

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "spawnGroup")
    private Raider spawnNextWave(Raider abstractRaiderEntity, BlockPos blockPos){

        if(!DungeonsMobsConfig.COMMON.ENABLE_BIOME_SPECIFIC_RAIDERS.get()){
            return abstractRaiderEntity;
        }

        Holder<Biome> raidBiome = level.getBiome(blockPos);
        ResourceLocation biomeRegistryName = raidBiome.value().getRegistryName();
        if (biomeRegistryName != null) {
            ResourceKey<Biome> biomeRegistryKey = ResourceKey.create(Registry.BIOME_REGISTRY, biomeRegistryName);

            // the name of the current raid's biome
            Set<BiomeDictionary.Type> raidBiomeTypes = BiomeDictionary.getTypes(biomeRegistryKey);

            for(BiomeSpecificRaider currentBiomeSpecificRaider : BiomeSpecificRaider.values()){
                // the raider type that is to be spawned by default
                EntityType<?> originalRaiderType = abstractRaiderEntity != null ? abstractRaiderEntity.getType() : null;
                // the current iterated biome specific raider type
                EntityType<? extends Raider> currentBiomeSpecificRaiderType = currentBiomeSpecificRaider.getType();
                // the type the current iterated biome specific raider type is equivalent to
                EntityType<? extends Raider> equivalentType = currentBiomeSpecificRaider.getEquivalentType();
                // the allowed string values a biome name can contain to spawn the current iterated biome specific raider type
                Set<BiomeDictionary.Type> biomeTypeSet = currentBiomeSpecificRaider.getBiomeTypeSet();

                // whether or not the current raid's biome name contains any of the string values required to spawn the current iterated biome specific raider type
                boolean biomeTypeMatch = false;
                for(BiomeDictionary.Type allowedBiomeType : biomeTypeSet){
                    for(BiomeDictionary.Type raidBiomeType : raidBiomeTypes){
                        if (raidBiomeType == allowedBiomeType) {
                            biomeTypeMatch = true;
                            break;
                        }
                    }
                }

                // check if the raider type to spawn is a biome specific raider
                // and the raid biome's name does not contains any matching keys
                // if so, convert it to the type that replaces it
                // and don't run the other checks
                if(!biomeTypeMatch
                        && currentBiomeSpecificRaiderType == originalRaiderType){
                    // replace the biome specific raider type to spawn with its replacement and return it
                    abstractRaiderEntity = equivalentType.create(this.level);
                    return abstractRaiderEntity;
                }

                // check if the biome specific raider is contained in the raid wave member values
                // in order to determine if the raider type to spawn should be replaced with the current iterated biome specific raider type
                boolean raiderFoundInWaveMembers = false;
                for(Raid.RaiderType waveMember : Raid.RaiderType.values()){
                    EntityType<? extends Raider> waveMemberType = waveMember.entityType;
                    if(waveMemberType != null && waveMemberType == currentBiomeSpecificRaiderType){
                        raiderFoundInWaveMembers = true;
                        break;
                    }
                }

                // convert raider type to spawn
                // if the raids biome name contains any matching keys
                // if the raider type to spawn is a type that gets replaced by the biome specific raider's type
                // if the biome speciic raider that replaces it is not already contained in raid wave member values
                if(biomeTypeMatch
                        && originalRaiderType != null
                        && currentBiomeSpecificRaiderType != null
                        && originalRaiderType == equivalentType
                        && !raiderFoundInWaveMembers){
                    // replace the raider type to spawn with its biome specific replacement and return it
                    abstractRaiderEntity = currentBiomeSpecificRaiderType.create(this.level);
                    return abstractRaiderEntity;
                }
            }
            // if no replacements were determined to be made, allow the original raider type to be spawned
        }
        return abstractRaiderEntity;
    }
}
