package com.example.demo.service;

import com.example.demo.dto.GameState;
import com.example.demo.model.Character;
import com.example.demo.model.Character.Allegiance;
import com.example.demo.model.GameSession;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final CharacterRepository characterRepository;
    private final GameSessionRepository sessionRepository;

    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;

    public GameState getCurrentState(Long sessionId) {

        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Character> allUnits = characterRepository.findByGameSessionId(sessionId);
        boolean lordFallen = allUnits.stream()
                .filter(Character::isLord)
                .anyMatch(u -> !u.isAlive());

        boolean enemiesAlive = allUnits.stream()
                .anyMatch(u -> u.getAllegiance() == Allegiance.ENEMY && u.isAlive());

        String status = "ACTIVE";
        if (lordFallen) {
            status = "GAME_OVER";
        } else if (!enemiesAlive) {
            status = "VICTORY";
        }

        return new GameState(
                session.getId(),
                session.getTurnNumber(),
                session.getCurrentPhase(),
                allUnits,
                MAP_WIDTH,
                MAP_HEIGHT,
                status
        );
    }
}
