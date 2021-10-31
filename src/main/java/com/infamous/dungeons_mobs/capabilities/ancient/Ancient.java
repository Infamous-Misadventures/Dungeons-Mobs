package com.infamous.dungeons_mobs.capabilities.ancient;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;

public class Ancient implements IAncient {
    private boolean ancient = false;
    private ServerBossInfo bossInfo = null;


    public boolean isAncient() {
        return ancient;
    }

    public void setAncient(boolean ancient) {
        this.ancient = ancient;
    }

    public boolean initiateBossBar(ITextComponent displayName) {
        bossInfo = new ServerBossInfo(displayName, BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS);
        return true;
    }

    public ServerBossInfo getBossInfo() {
        return bossInfo;
    }
}
