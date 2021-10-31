package com.infamous.dungeons_mobs.capabilities.ancient;


import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerBossInfo;

public interface IAncient {
    boolean isAncient();
    void setAncient(boolean ancient);
    boolean initiateBossBar(ITextComponent displayName);
    public ServerBossInfo getBossInfo();
}
