package com.example.demo.model;

import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
public enum UnitClass {
    MYRMIDON("Myrmidon", 16, 20, 4, 7, 9, 12, 1, 3, 5, 60, 40, 75, 20, WeaponType.SWORD),
    SOLDIER("Soldier", 18, 22, 5, 8, 3, 6, 4, 7, 5, 75, 45, 40, 45, WeaponType.LANCE),
    FIGHTER("Fighter", 20, 24, 7, 11, 2, 5, 2, 4, 5, 85, 55, 30, 35, WeaponType.AXE),

    KNIGHT("Knight", 22, 26, 6, 9, 0, 2, 9, 13, 4, 90, 60, 10, 70, WeaponType.LANCE),
    CAVALIER("Cavalier", 19, 23, 5, 8, 4, 7, 4, 7, 7, 75, 50, 45, 45, WeaponType.SWORD, WeaponType.LANCE),

    ARCHER("Archer", 17, 20, 4, 7, 4, 7, 3, 5, 5, 65, 45, 50, 35, WeaponType.BOW),
    CLERIC("Cleric", 16, 18, 5, 8, 4, 7, 0, 2, 5, 50, 40, 50, 10, WeaponType.HEAL_STAFF, WeaponType.PHYSIC_STAFF),
    MAGE("Mage", 15, 18, 6, 9, 3, 6, 0, 2, 5, 50, 55, 45, 15, WeaponType.FIRE_TOME, WeaponType.LIGHTNING_TOME, WeaponType.WIND_TOME),
    SAGE("Sage", 18, 22, 8, 12, 4, 8, 2, 5, 6, 60, 60, 50, 25, WeaponType.FIRE_TOME, WeaponType.LIGHTNING_TOME, WeaponType.WIND_TOME, WeaponType.METEOR_TOME);

    private final String displayName;
    private final int minHp, maxHp, minStr, maxStr, minSpd, maxSpd, minDef, maxDef, movement;
    private final int hpGrowth, strGrowth, spdGrowth, defGrowth;
    private final List<WeaponType> allowedWeaponTypes;

    UnitClass(String name, int minH, int maxH, int minS, int maxS,
              int minSp, int maxSp, int minD, int maxD, int mov,
              int hpG, int strG, int spG, int defG, WeaponType... allowedTypes) {
        this.displayName = name;
        this.minHp = minH; this.maxHp = maxH;
        this.minStr = minS; this.maxStr = maxS;
        this.minSpd = minSp; this.maxSpd = maxSp;
        this.minDef = minD; this.maxDef = maxD;
        this.movement = mov;
        this.hpGrowth = hpG;
        this.strGrowth = strG;
        this.spdGrowth = spG;
        this.defGrowth = defG;
        this.allowedWeaponTypes = Arrays.asList(allowedTypes);
    }
}
