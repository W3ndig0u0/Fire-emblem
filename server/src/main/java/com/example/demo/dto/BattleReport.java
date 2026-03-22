package com.example.demo.dto;

import lombok.Builder;

@Builder
public record BattleReport(
        int damageDealt,
        int counterDamageDealt,
        boolean isBroken,
        boolean canCounter,
        int attackerRemainingHp,
        int defenderRemainingHp,
        boolean defenderDied,
        int xpGained,
        boolean leveledUp
) {}
