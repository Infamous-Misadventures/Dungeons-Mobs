package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.interfaces.BiomeSpecificRaider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;



@Mixin(Raid.class)
public abstract class RaidMixin {

    public int S;

    /*
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick() {
        if (this.S > 0) {
            --this.S;
        }
        if (this.hasFirstWaveSpawned() && this.active && this.S <= 0 && this.getTotalRaidersAlive() > 5){
            int r;
            if (this.level.getDifficulty().getId() == 1) {
                S = 1000;
                BlockPos b = this.waveSpawnPos.get();
                for (r = 0; r < 5; r++) {
                    PillagerEntity v = new PillagerEntity(EntityType.PILLAGER, this.level);
                }
            }else if (this.level.getDifficulty().getId() == 2) {
                S = 850;
                BlockPos b = this.waveSpawnPos.get();
                for (r = 0; r < 7; r++) {
                    PillagerEntity v = new PillagerEntity(EntityType.PILLAGER, this.level);
                }
            }else if (this.level.getDifficulty().getId() == 3) {
                S = 650;
                BlockPos b = this.waveSpawnPos.get();
                for (r = 0; r < 9; r++) {
                    if (this.level.getRandom().nextInt(50) <= 25) {
                        PillagerEntity v = new PillagerEntity(EntityType.PILLAGER, this.level);
                        v.moveTo(b,0,0);
                        this.addWaveMob(3,v,true);
                    }else if (this.level.getRandom().nextInt(50) <= 46) {
                        DungeonsVindicatorEntity v = new DungeonsVindicatorEntity(this.level);
                        v.moveTo(b,0,0);
                        this.addWaveMob(3,v,true);
                    }else if (this.level.getRandom().nextInt(50) == 47) {
                        GeomancerEntity v = new GeomancerEntity(this.level);
                        v.moveTo(b,0,0);
                        this.addWaveMob(3,v,true);
                    }else if (this.level.getRandom().nextInt(50) == 48) {
                        MageEntity v = new MageEntity(this.level);
                        v.moveTo(b,0,0);
                        this.addWaveMob(3,v,true);
                    }else {
                        ArmoredVindicatorEntity v = new ArmoredVindicatorEntity(this.level);
                        v.moveTo(b,0,0);
                        this.addWaveMob(3,v,true);
                    }
                }
            }
        }
    }
     */

    private static final String RAID_WAVE_MEMBER_TYPE_FIELD = "field_221285_g";
    @Shadow
    @Final
    private ServerWorld level;

    @Shadow public abstract int getNumGroups(Difficulty p_221306_1_);

    @Shadow public abstract boolean isBetweenWaves();

    @Shadow @Final private int numGroups;

    @Shadow public abstract int getTotalRaidersAlive();

    @Shadow public abstract boolean hasFirstWaveSpawned();

    @Shadow private boolean active;

    @Shadow @Nullable protected abstract BlockPos findRandomSpawnPos(int p_221298_1_, int p_221298_2_);

    @Shadow private Optional<BlockPos> waveSpawnPos;

    @Shadow public abstract void joinRaid(int p_221317_1_, AbstractRaiderEntity p_221317_2_, @Nullable BlockPos p_221317_3_, boolean p_221317_4_);

    @Shadow private float totalHealth;

    @Shadow @Final private int id;

    @Shadow @Final private ServerBossInfo raidEvent;

    @Shadow public abstract boolean addWaveMob(int p_221300_1_, AbstractRaiderEntity p_221300_2_, boolean p_221300_3_);

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "spawnGroup")
    private AbstractRaiderEntity spawnNextWave(AbstractRaiderEntity abstractRaiderEntity, BlockPos blockPos){

        if(!DungeonsMobsConfig.COMMON.ENABLE_BIOME_SPECIFIC_RAIDERS.get()){
            return abstractRaiderEntity;
        }

        Biome raidBiome = level.getBiome(blockPos);
        ResourceLocation biomeRegistryName = raidBiome.getRegistryName();
        if (biomeRegistryName != null) {
            RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(Registry.BIOME_REGISTRY, biomeRegistryName);

            // the name of the current raid's biome
            Set<BiomeDictionary.Type> raidBiomeTypes = BiomeDictionary.getTypes(biomeRegistryKey);

            for(BiomeSpecificRaider currentBiomeSpecificRaider : BiomeSpecificRaider.values()){
                // the raider type that is to be spawned by default
                EntityType<?> originalRaiderType = abstractRaiderEntity != null ? abstractRaiderEntity.getType() : null;
                // the current iterated biome specific raider type
                EntityType<? extends AbstractRaiderEntity> currentBiomeSpecificRaiderType = currentBiomeSpecificRaider.getType();
                // the type the current iterated biome specific raider type is equivalent to
                EntityType<? extends AbstractRaiderEntity> equivalentType = currentBiomeSpecificRaider.getEquivalentType();
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
                for(Raid.WaveMember waveMember : Raid.WaveMember.values()){
                    EntityType<? extends AbstractRaiderEntity> waveMemberType = ObfuscationReflectionHelper.getPrivateValue(Raid.WaveMember.class, waveMember, RAID_WAVE_MEMBER_TYPE_FIELD);
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
