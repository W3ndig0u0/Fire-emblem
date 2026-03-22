package com.example.demo.service;

import com.example.demo.model.Character;
import com.example.demo.model.GameSession;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnService {
    private final CharacterRepository characterRepository;
    private final AiService aiService;
    private final GameSessionRepository sessionRepository;

    @Transactional
    public void endPlayerTurn(Long sessionId) {
        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setCurrentPhase("ENEMY_PHASE");

        List<Character> enemies = characterRepository.findByGameSessionIdAndAllegiance(sessionId, Character.Allegiance.ENEMY);
        enemies.forEach(u -> u.setHasActed(false));
        characterRepository.saveAll(enemies);

        aiService.executeEnemyTurn(sessionId);

        session.setCurrentPhase("PLAYER_PHASE");
        session.setTurnNumber(session.getTurnNumber() + 1);

        List<Character> players = characterRepository.findByGameSessionIdAndAllegiance(sessionId, Character.Allegiance.PLAYER);
        players.forEach(u -> u.setHasActed(false));

        sessionRepository.save(session);
        characterRepository.saveAll(players);
    }
}

