package com.example.demo.service;

import com.example.demo.dto.BattleReport;
import com.example.demo.model.Character;
import com.example.demo.model.Item;
import com.example.demo.model.Weapon;
import com.example.demo.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {
    private final CharacterRepository characterRepository;
    private final BattleService battleService;
    private final MapService mapService;

    @Transactional
    public void executeEnemyTurn(Long sessionId) {

        List<Character> allUnits = characterRepository.findByGameSessionId(sessionId);

        List<Character> enemies = filterUnits(allUnits, Character.Allegiance.ENEMY, true);
        List<Character> players = filterUnits(allUnits, Character.Allegiance.PLAYER, false);

        for (Character enemy : enemies) {
            processSingleEnemy(enemy, enemies, players);
            characterRepository.save(enemy);
        }
    }

    private void processSingleEnemy(Character enemy, List<Character> enemies, List<Character> players) {
        if (handleSelfPreservation(enemy)) return;
        if (handleSupportHealing(enemy, enemies)) return;

        Character target = findBestTarget(enemy, players);
        performCombatSequence(enemy, target);
    }

    private void performCombatSequence(Character enemy, Character target) {
        if (enemy.canHit(target)) {
            battleService.executeAttack(enemy, target);
        } else {
            moveAsCloseAsPossible(enemy, target);
            if (enemy.canHit(target)) {
                battleService.executeAttack(enemy, target);
            } else {
                enemy.setHasActed(true);
            }
        }
    }

    private void moveAsCloseAsPossible(Character actor, Character target) {
        int remainingMove = actor.getMovementRange();
        while (remainingMove > 0 && !actor.canHit(target)) {
            if (!tryTakeStep(actor, target)) break;
            remainingMove--;
        }
    }

    private boolean tryTakeStep(Character actor, Character target) {
        int nextX = calculateNextCord(actor.getPosX(), target.getPosX());
        int nextY = calculateNextCord(actor.getPosY(), target.getPosY());

        if (mapService.isWalkable(nextX, nextY) && !mapService.isTileOccupied(nextX, nextY)) {
            actor.setPosX(nextX);
            actor.setPosY(nextY);
            return true;
        }
        return false;
    }

    private int calculateNextCord(int current, int target) {
        if (current < target) return current + 1;
        if (current > target) return current - 1;
        return current;
    }

    private boolean handleSelfPreservation(Character enemy) {
        if (enemy.getHealth() >= (enemy.getMaxHealth() / 2)) return false;

        Item potion = findItemInInventory(enemy, "potion");
        potion.applyEffect(enemy);
        enemy.removeFromInventory(potion);
        enemy.setHasActed(true);
        return true;
    }

    private boolean handleSupportHealing(Character healer, List<Character> allies) {
        Weapon staff = healer.getEquippedWeapon();
        if (staff == null || !staff.getWeaponType().isStaff()) return false;

        Character wounded = allies.stream()
                .filter(a -> a.isAlive() && a.getHealth() < a.getMaxHealth())
                .filter(a -> healer.getDistanceTo(a) <= staff.getWeaponType().getDefaultMaxRange())
                .min(Comparator.comparingInt(Character::getHealth))
                .orElse(null);

        if (wounded != null) {
            battleService.executeHealing(healer, wounded);
            characterRepository.save(wounded);
            return true;
        }
        return false;
    }

    private Character findBestTarget(Character actor, List<Character> targets) {
        return targets.stream()
                .min((t1, t2) -> Integer.compare(calculateThreatScore(actor, t2), calculateThreatScore(actor, t1)))
                .orElse(null);
    }

    private int calculateThreatScore(Character actor, Character target) {
        int score = (30 - target.getDefense()) - (actor.getDistanceTo(target) * 10);
        BattleReport forecast = battleService.previewCombat(actor, target);

        if (forecast != null) {
            score += forecast.damageDealt() * 2;
            if (forecast.defenderDied()) score += 1000;
            score += forecast.canCounter() ? -forecast.counterDamageDealt() : 50;
        }
        return score;
    }

    private List<Character> filterUnits(List<Character> units, Character.Allegiance allegiance, boolean checkActed) {
        return units.stream()
                .filter(c -> c.getAllegiance() == allegiance && c.isAlive())
                .filter(c -> !checkActed || !c.isHasActed())
                .toList();
    }

    private Item findItemInInventory(Character unit, String keyword) {
        return unit.getInventory().stream()
                .filter(i -> i.getName().toLowerCase().contains(keyword))
                .findFirst()
                .orElse(null);
    }
}
