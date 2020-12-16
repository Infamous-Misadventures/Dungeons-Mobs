package com.infamous.dungeons_mobs.capabilities;

import java.util.UUID;

public interface ICloneable {

    boolean addClone(UUID clone);

    UUID[] getClones();
}
