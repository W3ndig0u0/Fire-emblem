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

        session.togglePhase();

        aiService.executeEnemyTurn();

        List<Character> allUnits = characterRepository.findAll();
        allUnits.forEach(u -> u.setHasActed(false));

        session.togglePhase();

        sessionRepository.save(session);
        characterRepository.saveAll(allUnits);
    }
}
