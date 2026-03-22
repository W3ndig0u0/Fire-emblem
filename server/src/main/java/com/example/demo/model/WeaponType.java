package com.example.demo.model;

@Getter
public enum WeaponType {
    SWORD("Sword", 1, 1),
    LANCE("Lance", 1, 1),
    AXE("Axe", 1, 1),
    BOW("Bow", 2, 2),
    MAGIC("Magic", 1, 2),
    NONE("None", 1, 1);

    private final String displayName;
    private final int defaultMinRange;
    private final int defaultMaxRange;

    WeaponType(String displayName, int min, int max) {
        this.displayName = displayName;
        this.defaultMinRange = min;
        this.defaultMaxRange = max;
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
