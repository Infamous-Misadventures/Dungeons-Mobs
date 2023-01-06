package com.infamous.dungeons_mobs.interfaces;

public interface IHasInventorySprite {
    default String getModelResource() {
        return null;
    }
}
