package com.example.demo.service;

import com.example.demo.dto.BattleReport;
import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final CharacterRepository characterRepository;
    private final Random random = new Random();

    @Transactional
    public BattleReport executeAttack(Character attacker, Character defender) {
        validateAction(attacker, defender);

        BattleReport report = calculateCombat(attacker, defender);

        attacker.setHasActed(true);

        checkWeaponBreak(attacker, attacker.getEquippedWeapon());
        if (defender.getEquippedWeapon() != null) {
            checkWeaponBreak(defender, defender.getEquippedWeapon());
        }

        if (!defender.isAlive()) {
            defender.setDead(true);
            defender.setPosX(-1);
            defender.setPosY(-1);
            //characterRepository.delete(defender);
        }

        characterRepository.save(attacker);
        characterRepository.save(defender);

        return report;
    }

    public BattleReport previewCombat(Character attacker, Character defender) {
        Weapon attackerWeapon = attacker.getEquippedWeapon();
        Weapon defenderWeapon = defender.getEquippedWeapon();

        if (attackerWeapon == null) return null;

        WeaponType dType = (defenderWeapon != null) ? defenderWeapon.getWeaponType() : WeaponType.NONE;
        int damageBonus = attackerWeapon.getWeaponType().getAdvantageAgainst(dType) * 2;
        boolean isBroken = attackerWeapon.getWeaponType().canBreak(dType);

        int singleDamage = Math.max(0, (attacker.getAttack() + attackerWeapon.getStrengthBonus() + damageBonus) - defender.getDefense());
        int totalDamage = (attacker.getSpeed() - defender.getSpeed() >= 4) ? singleDamage * 2 : singleDamage;

        int counterDamage = 0;
        boolean canCounter = !isBroken && isInCounterRange(attacker, defender, defenderWeapon);
        if (canCounter) {
            counterDamage = Math.max(0, (defender.getAttack() + defenderWeapon.getStrengthBonus()) - attacker.getDefense());
        }

        return new BattleReport(
                totalDamage, counterDamage, isBroken, canCounter,
                attacker.getHealth(), defender.getHealth(), (defender.getHealth() - totalDamage <= 0),
                0, false, false, false, new HashMap<>()
        );
    }

    private void validateAction(Character attacker, Character defender) {
        if (attacker.isHasActed()) throw new RuntimeException("Unit already acted!");

        Weapon weapon = attacker.getEquippedWeapon();
        if (weapon == null) throw new RuntimeException(attacker.getName() + " is unarmed!");

        int distance = attacker.getDistanceTo(defender);
        if (distance < weapon.getMinRange() || distance > weapon.getMaxRange()) {
            throw new RuntimeException("Target out of range!");
        }
    }

    private BattleReport calculateCombat(Character attacker, Character defender) {
        Weapon attackerWeapon = attacker.getEquippedWeapon();
        Weapon defenderWeapon = defender.getEquippedWeapon();

        WeaponType dType = (defenderWeapon != null) ? defenderWeapon.getWeaponType() : WeaponType.NONE;
        int damageBonus = attackerWeapon.getWeaponType().getAdvantageAgainst(dType) * 2;
        boolean isBroken = attackerWeapon.getWeaponType().canBreak(dType);

        boolean isCrit = random.nextInt(100) < calculateCritChance(attacker, defender);
        int finalDamage = (attacker.getAttack() + attackerWeapon.getStrengthBonus() + damageBonus) - defender.getDefense();
        finalDamage = Math.max(0, isCrit ? finalDamage * 3 : finalDamage);

        int totalDamageDealt = executeStrikes(attacker, defender, attackerWeapon, finalDamage);
        boolean attackerMissed = totalDamageDealt == 0 && finalDamage > 0;

        int counterDamage = 0;
        boolean defenderMissed = false;
        boolean canCounter = !isBroken && defender.isAlive() && isInCounterRange(attacker, defender, defenderWeapon);

        if (canCounter) {
            int potentialCounter = Math.max(0, (defender.getAttack() + defenderWeapon.getStrengthBonus()) - attacker.getDefense());
            if (random.nextInt(100) < calculateHitChance(defender, attacker)) {
                counterDamage = potentialCounter;
                attacker.takeDamage(counterDamage);
            } else {
                defenderMissed = potentialCounter > 0;
            }
            defenderWeapon.reduceDurability(1);
        }

        int xpGained = 0;
        boolean leveledUp = false;
        Map<String, Integer> statGains = new HashMap<>();

        if (!defender.isAlive() && attacker.getAllegiance() == Character.Allegiance.PLAYER) {
            xpGained = defender.getExpValue();

            int oldAtk = attacker.getAttack();
            int oldHp = attacker.getMaxHealth();
            int oldDef = attacker.getDefense();
            int oldSpd = attacker.getSpeed();

            leveledUp = attacker.gainExp(xpGained);

            if (leveledUp) {
                populateStatGains(statGains, attacker, oldHp, oldAtk, oldDef, oldSpd);
            }
        }

        return new BattleReport(
                totalDamageDealt, counterDamage, isBroken, canCounter,
                attacker.getHealth(), defender.getHealth(), !defender.isAlive(),
                xpGained, leveledUp, isCrit, attackerMissed || defenderMissed, statGains
        );
    }

    @Transactional
    public BattleReport executeHealing(Character healer, Character target) {
        Weapon staff = healer.getEquippedWeapon();

        if (!staff.getWeaponType().isStaff()) {
            throw new RuntimeException("This is not a healing staff!");
        }
        if (healer.getAllegiance() != target.getAllegiance()) {
            throw new RuntimeException("You cannot heal an enemy!");
        }

        int healAmount = healer.getAttack() + staff.getStrengthBonus();
        target.heal(healAmount);

        staff.reduceDurability(1);
        checkWeaponBreak(healer, staff);
        healer.setHasActed(true);

        int oldAtk = healer.getAttack();
        int oldHp = healer.getMaxHealth();
        int oldDef = healer.getDefense();
        int oldSpd = healer.getSpeed();

        boolean leveledUp = healer.gainExp(15);
        Map<String, Integer> statGains = new HashMap<>();

        if (leveledUp) {
            populateStatGains(statGains, healer, oldHp, oldAtk, oldDef, oldSpd);
        }

        return new BattleReport(
                -healAmount, 0, false, false,
                healer.getHealth(), target.getHealth(), false,
                15, leveledUp, false, false, statGains
        );
    }

    private void populateStatGains(Map<String, Integer> statGains, Character unit, int oldHp, int oldAtk, int oldDef, int oldSpd) {
        statGains.put("HP", unit.getMaxHealth() - oldHp);
        statGains.put("Atk", unit.getAttack() - oldAtk);
        statGains.put("Def", unit.getDefense() - oldDef);
        statGains.put("Spd", unit.getSpeed() - oldSpd);
    }

    private void checkWeaponBreak(Character character, Weapon weapon) {
        if (weapon != null && weapon.getDurability() <= 0) {
            character.removeFromInventory(weapon);
        }
    }

    private int executeStrikes(Character attacker, Character defender, Weapon weapon, int damage) {
        int totalDamage = 0;
        if (random.nextInt(100) < calculateHitChance(attacker, defender)) {
            defender.takeDamage(damage);
            totalDamage += damage;
        }
        weapon.reduceDurability(1);

        if (attacker.getSpeed() - defender.getSpeed() >= 4 && defender.isAlive()) {
            if (random.nextInt(100) < calculateHitChance(attacker, defender)) {
                defender.takeDamage(damage);
                totalDamage += damage;
            }
            weapon.reduceDurability(1);
        }
        return totalDamage;
    }

    private int calculateHitChance(Character attacker, Character defender) {
        int hitChance = attacker.getEquippedWeapon().getWeaponType().getBaseHit()
                + (attacker.getSpeed() * 2)
                - defender.getSpeed();
        return Math.max(0, Math.min(100, hitChance));
    }

    private int calculateCritChance(Character attacker, Character defender) {
        int attackerCrit = attacker.getEquippedWeapon().getCritRate() + (attacker.getSpeed() / 2);
        int defenderAvoid = defender.getSpeed() / 2;
        return Math.max(0, attackerCrit - defenderAvoid);
    }

    private boolean isInCounterRange(Character attacker, Character defender, Weapon defWeapon) {
        if (defWeapon == null) return false;
        int distance = attacker.getDistanceTo(defender);
        return distance >= defWeapon.getMinRange() && distance <= defWeapon.getMaxRange();
    }
}
