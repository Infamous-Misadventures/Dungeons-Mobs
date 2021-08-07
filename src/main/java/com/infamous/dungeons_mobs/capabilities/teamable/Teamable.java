package com.infamous.dungeons_mobs.capabilities.teamable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teamable implements ITeamable {
    private final List<UUID> teammates = new ArrayList<>();

    public Teamable(){
    }

    @Override
    public boolean addTeammate(UUID clone) {
        return this.teammates.add(clone);
    }

    @Override
    public List<UUID> getTeammates() {
        return this.teammates;
    }
}
