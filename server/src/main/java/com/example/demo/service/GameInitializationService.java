package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameInitializationService {
    private final CharacterRepository characterRepository;
    private final GameSessionRepository sessionRepository;

    @Transactional
    public Long createNewGame() {

        GameSession session = new GameSession();
        sessionRepository.save(session);

        Hero hero = new Hero("Marth", 20, 10, 8,5);
        hero.setPosX(0); hero.setPosY(0);
        hero.setAllegiance(Character.Allegiance.PLAYER);
        hero.setMovementRange(5);
        characterRepository.save(hero);

        Demon bandit = new Demon("Bandit", 15, 0, 5,2);
        bandit.setPosX(5); bandit.setPosY(5);
        bandit.setAllegiance(Character.Allegiance.ENEMY);
        bandit.setMovementRange(3);
        characterRepository.save(bandit);

        return session.getId();
    }
}
