package com.infamous.dungeons_mobs.capabilities.ancient.properties;

public class Ancient implements IAncient {
    private boolean ancient = false;

    public boolean isAncient() {
        return ancient;
    }

    public void setAncient(boolean ancient) {
        this.ancient = ancient;
    }
}
