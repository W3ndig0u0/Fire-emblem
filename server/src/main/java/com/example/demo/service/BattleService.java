package com.example.demo.service;

import com.example.demo.dto.BattleReport;
import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BattleService {

    private final CharacterRepository characterRepository;
    public BattleService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Transactional
    public BattleReport executeAttack(Character attacker, Character defender) {
        if (attacker.isHasActed()) {
            throw new RuntimeException("This unit has already acted this turn!");
        }

        Weapon aWeapon = attacker.getEquippedWeapon();
        Weapon defenderWeapon = defender.getEquippedWeapon();

        int distance = attacker.getDistanceTo(defender);

        BattleReport report = calculateCombat(attacker, defender);
        attacker.setHasActed(true);

        characterRepository.save(attacker);
        characterRepository.save(defender);

        return report;
    }
    @Transactional
    public BattleReport calculateCombat(Character attacker, Character defender) {
        Weapon attackerWeapon = attacker.getEquippedWeapon();
        Weapon defenderWeapon = defender.getEquippedWeapon();

        if (attackerWeapon == null) throw new RuntimeException(attacker.getName() + " is unarmed!");

        int distance = attacker.getDistanceTo(defender);
        if (distance < attackerWeapon.getMinRange() || distance > attackerWeapon.getMaxRange()) {
            throw new RuntimeException("Target out of range!");
        }

        WeaponType dType = (defenderWeapon != null) ? defenderWeapon.getWeaponType() : WeaponType.NONE;
        int advantage = attackerWeapon.getWeaponType().getAdvantageAgainst(dType);
        int damageBonus = (advantage == 1) ? 2 : (advantage == -1) ? -2 : 0;
        boolean isBroken = attackerWeapon.getWeaponType().canBreak(dType);

        int finalDamage = Math.max(0, (attacker.getAttack() + attackerWeapon.getStrengthBonus() + damageBonus) - defender.getHealth());
        defender.takeDamage(finalDamage);
        attackerWeapon.reduceDurability(1);

        int counterDamage = 0;
        boolean inCounterRange = (defenderWeapon != null) &&
                distance >= defenderWeapon.getMinRange() &&
                distance <= defenderWeapon.getMaxRange();

        boolean canCounter = !isBroken && defender.isAlive() && inCounterRange;

        if (canCounter) {
            counterDamage = Math.max(0, defender.getAttack() + defenderWeapon.getStrengthBonus() - attacker.getHealth());
            attacker.takeDamage(counterDamage);
            defenderWeapon.reduceDurability(1);
        }

        int xpGained = 0;
        boolean leveledUp = false;
        if (!defender.isAlive() && attacker instanceof Hero hero) {
            xpGained = 20;
            leveledUp = hero.gainExp(xpGained);
        }

        characterRepository.save(attacker);
        characterRepository.save(defender);

        return new BattleReport(
                finalDamage,
                counterDamage,
                isBroken,
                canCounter,
                attacker.getHealth(),
                defender.getHealth(),
                !defender.isAlive(),
                xpGained,
                leveledUp
        );
    }

}
