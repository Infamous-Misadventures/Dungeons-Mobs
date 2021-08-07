package com.infamous.dungeons_mobs.capabilities.teamable;

import java.util.List;
import java.util.UUID;

public interface ITeamable {

    boolean addTeammate(UUID clone);

    List<UUID> getTeammates();
}
