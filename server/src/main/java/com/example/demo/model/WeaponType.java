package com.example.demo.model;

import lombok.Getter;

@Getter
public enum WeaponType {
    SWORD("Sword", 1, 1, 1, false, false, 90),
    LANCE("Lance", 1, 2, 1, false, false, 80),
    AXE("Axe", 1, 1, 1, false, false, 70),
    BOW("Bow", 2, 4, 5, false, false, 85),

    FIRE_TOME("Fire", 1, 2, 5, false, false, 90),
    LIGHTNING_TOME("Lightning", 1, 2, 20, false, false, 75),
    WIND_TOME("Wind", 1, 2, 10, false, false, 95),
    METEOR_TOME("Meteor", 3, 10, 5, true, false, 60),

    HEAL_STAFF("Heal Staff", 1, 1, 0, false, true, 100),
    PHYSIC_STAFF("Physic", 1, 10, 0, false, true, 100),

    NONE("None", 1, 1, 0, false, false, 70);

    private final String displayName;
    private final int defaultMinRange;
    private final int defaultMaxRange;
    private final int baseCrit;
    private final boolean isAoE;
    private final boolean isStaff;
    private final int baseHit;

    WeaponType(String displayName, int min, int max, int crit, boolean isAoE, boolean isStaff, int baseHit) {
        this.displayName = displayName;
        this.defaultMinRange = min;
        this.defaultMaxRange = max;
        this.baseCrit = crit;
        this.isAoE = isAoE;
        this.isStaff = isStaff;
        this.baseHit = baseHit;
    }

    public int getAdvantageAgainst(WeaponType other) {
        if (this == SWORD && other == AXE) return 1;
        if (this == AXE && other == LANCE) return 1;
        if (this == LANCE && other == SWORD) return 1;

        if (this == AXE && other == SWORD) return -1;
        if (this == LANCE && other == AXE) return -1;
        if (this == SWORD && other == LANCE) return -1;

        return 0;
    }

    public boolean canBreak(WeaponType other) {
        return getAdvantageAgainst(other) == 1;
    }
}
