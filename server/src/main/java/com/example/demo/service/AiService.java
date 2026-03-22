package com.example.demo.service;

import com.example.demo.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import com.example.demo.model.Character;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class AiService {
    private final CharacterRepository characterRepository;
    private final BattleService battleService;

    public AiService(CharacterRepository characterRepository, BattleService battleService) {
        this.characterRepository = characterRepository;
        this.battleService = battleService;
    }

    @Transactional
    public void executeEnemyTurn() {
        List<Character> enemies = characterRepository.findByAllegiance(Character.Allegiance.ENEMY);

        List<Character> players = characterRepository.findByAllegiance(Character.Allegiance.PLAYER);

        for (Character enemy : enemies) {
            Character target = findBestTarget(enemy, players);
            if (enemy.canHit(target)) {
                battleService.executeAttack(enemy, target);
            } else {
                moveTowards(enemy, target);
            }
        }
    }

    private void moveTowards(Character actor, Character target) {
        if (target == null) return;

        int nextX = actor.getPosX();
        int nextY = actor.getPosY();

        if (actor.getPosX() < target.getPosX()) nextX++;
        else if (actor.getPosX() > target.getPosX()) nextX--;
        else if (actor.getPosY() < target.getPosY()) nextY++;
        else if (actor.getPosY() > target.getPosY()) nextY--;

        actor.setPosX(nextX);
        actor.setPosY(nextY);
        characterRepository.save(actor);
    }

    private Character findBestTarget(Character actor, List<Character> targets) {
        if (targets == null || targets.isEmpty()) return null;

        return targets.stream()
                .min(Comparator.comparingInt(actor::getDistanceTo)
                        .thenComparingInt(Character::getHealth))
                .orElse(null);
    }
}
