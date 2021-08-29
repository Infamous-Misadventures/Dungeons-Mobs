package com.infamous.dungeons_mobs.entities.magic;

public enum MagicType {
    //SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
    //FANGS(2, 0.4D, 0.3D, 0.35D),
    //WOLOLO(3, 0.7D, 0.5D, 0.2D),
    //DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
    //BLINDNESS(5, 0.1D, 0.1D, 0.2D),
    NONE(0, 0.0D, 0.0D, 0.0D),
    WRAITH_FIRE(1, 0.4D, 0.3D, 0.35D),
    SUMMON_UNDEAD(2, 0.7D, 0.7D, 0.8D),
    SUMMON_VINES(3, 0.7D, 0.7D, 0.8D),
    SUMMON_LIGHTNING(1, 0.4D, 0.3D, 0.35D);

    private final int id;
    private final double[] particleSpeed;

    MagicType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
        this.id = idIn;
        this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
    }

    public double[] getParticleSpeed(){
        return this.particleSpeed;
    }

    public int getId() {
        return id;
    }

    public static MagicType getFromId(int idIn) {
        for(MagicType magicType : values()) {
            if (idIn == magicType.id) {
                return magicType;
            }
        }

        return NONE;
    }
}
