package com.example.demo.service;

import com.example.demo.dto.GameState;
import com.example.demo.model.Character;
import com.example.demo.model.GameSession;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final CharacterRepository characterRepository;
    private final GameSessionRepository sessionRepository;

    public GameState getCurrentState(Long sessionId) {
        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Character> units = characterRepository.findAll().stream()
                .filter(Character::isAlive)
                .toList();

        return new GameState(
                session.getId(),
                session.getTurnNumber(),
                session.getCurrentPhase(),
                units
        );
    }
}
