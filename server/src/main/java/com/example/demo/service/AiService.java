package com.example.demo.service;

import com.example.demo.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.model.Character;
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

        List<Character> enemies = characterRepository.findAll().stream()
                .filter(c -> c.getAllegiance() == Character.Allegiance.ENEMY && !c.isHasActed())
                .toList();

        List<Character> players = characterRepository.findAll().stream()
                .filter(c -> c.getAllegiance() == Character.Allegiance.PLAYER && c.isAlive())
                .toList();

        for (Character enemy : enemies) {
            Character target = findBestTarget(enemy, players);

            if (enemy.canHit(target)) {
                battleService.executeAttack(enemy, target);
            } else {
                moveTowards(enemy, target);
                if (enemy.canHit(target)) {
                    battleService.executeAttack(enemy, target);
                } else {
                    enemy.setHasActed(true);
                }
            }
            characterRepository.save(enemy);
        }
    }

    private void moveTowards(Character actor, Character target) {
        int remainingMove = actor.getMovementRange();

        while (remainingMove > 0) {
            int nextX = actor.getPosX();
            int nextY = actor.getPosY();

            if (actor.getPosX() < target.getPosX()) nextX++;
            else if (actor.getPosX() > target.getPosX()) nextX--;
            else if (actor.getPosY() < target.getPosY()) nextY++;
            else if (actor.getPosY() > target.getPosY()) nextY--;

            if (mapService.isWalkable(nextX, nextY) && !mapService.isTileOccupied(nextX, nextY)) {
                actor.setPosX(nextX);
                actor.setPosY(nextY);
                remainingMove--;
            } else {
                break;
            }
            if (actor.canHit(target)) break;
        }
    }

    private Character findBestTarget(Character actor, List<Character> targets) {
        return targets.stream()
                .min(Comparator.comparingInt(actor::getDistanceTo)
                        .thenComparingInt(Character::getHealth))
                .orElse(null);
    }
}
