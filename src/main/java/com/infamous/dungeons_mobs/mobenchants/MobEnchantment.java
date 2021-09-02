package com.infamous.dungeons_mobs.mobenchants;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class MobEnchantment extends ForgeRegistryEntry<MobEnchantment> {

    private final MobEnchantment.Rarity rarity;

    public MobEnchantment(Rarity rarity) {
        this.rarity = rarity;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public static enum Rarity {
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
}
