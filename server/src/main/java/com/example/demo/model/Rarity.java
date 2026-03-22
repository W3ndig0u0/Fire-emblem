package com.example.demo.model;

import lombok.Getter;

@Getter
public enum Rarity {
    COMMON(1.0, 0),
    UNCOMMON(1.1, 2),
    RARE(1.25, 5),
    EPIC(1.5, 10),
    LEGENDARY(2.0, 20);

    private final double damageMultiplier;
    private final int bonusCrit;

    Rarity(double dmgMulti, int crit) {
        this.damageMultiplier = dmgMulti;
        this.bonusCrit = crit;
    }
}