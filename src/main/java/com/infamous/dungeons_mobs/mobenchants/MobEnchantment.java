package com.infamous.dungeons_mobs.mobenchants;

import net.minecraftforge.registries.ForgeRegistryEntry;

import static com.infamous.dungeons_mobs.mobenchants.MobEnchantment.Type.ANY;

public class MobEnchantment extends ForgeRegistryEntry<MobEnchantment> {

    private MobEnchantment.Rarity rarity;
    private MobEnchantment.Type type;


    public MobEnchantment(Rarity rarity) {
        this.rarity = rarity;
        this.type = ANY;
    }

    public MobEnchantment(Rarity rarity, Type type) {
        this.rarity = rarity;
        this.type = type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight) {
            this.weight = rarityWeight;
        }

        /**
         * Retrieves the weight of Rarity.
         */
        public int getWeight() {
            return this.weight;
        }
    }

    public enum Type {
        ANY,
        RANGED;
    }
}
