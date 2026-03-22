package com.example.demo.controller;

import com.example.demo.dto.BattleReport;
import com.example.demo.dto.GameState;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    private final BattleService battleService;
    private final MovementService movementService;
    private final TurnService turnService;
    private final GameInitializationService initService;
    private final GameService gameService;
    private final CharacterRepository characterRepository;

    @PostMapping("/start")
    public ResponseEntity<Long> startGame() {
        return ResponseEntity.status(HttpStatus.CREATED).body(initService.createNewGame());
    }

    @PostMapping("/move")
    public ResponseEntity<String> move(@RequestParam Long unitId, @RequestParam int x, @RequestParam int y) {
        String result = movementService.moveUnit(unitId, x, y);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/attack")
    public ResponseEntity<BattleReport> attack(@RequestParam Long attackerId, @RequestParam Long defenderId) {
        Character attacker = characterRepository.findById(attackerId)
                .orElseThrow(() -> new RuntimeException("Attacker not found"));
        Character defender = characterRepository.findById(defenderId)
                .orElseThrow(() -> new RuntimeException("Defender not found"));

        BattleReport report = battleService.executeAttack(attacker, defender);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/end-turn")
    public ResponseEntity<Void> endTurn(@RequestParam Long sessionId) {
        turnService.endPlayerTurn(sessionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/state/{sessionId}")
    public ResponseEntity<GameState> getGameState(@PathVariable Long sessionId) {
        GameState state = gameService.getCurrentState(sessionId);
        return ResponseEntity.ok(state);
    }
}
