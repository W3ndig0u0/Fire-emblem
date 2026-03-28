package com.example.demo.controller;

import com.example.demo.dto.BattleReport;
import com.example.demo.dto.GameState;
import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    private final BattleService battleService;
    private final GameSessionRepository gameSessionRepository;
    private final MovementService movementService;
    private final TurnService turnService;
    private final GameInitializationService initService;
    private final GameService gameService;
    private final CharacterRepository characterRepository;
    private final AiService aiService;

    @PostMapping("/start")
    public ResponseEntity<Long> startGame() {
        return ResponseEntity.status(HttpStatus.CREATED).body(initService.createNewGame());
    }

    @PostMapping("/move")
    public ResponseEntity<String> move(@RequestParam Long unitId, @RequestParam int x, @RequestParam int y) {
        Character unit = characterRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));
        verifyGameIsActive(unit);

        String result = movementService.moveUnit(unitId, x, y);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/attack")
    public ResponseEntity<BattleReport> attack(@RequestParam Long attackerId, @RequestParam Long defenderId) {
        Character attacker = characterRepository.findById(attackerId)
                .orElseThrow(() -> new RuntimeException("Attacker not found"));
        Character defender = characterRepository.findById(defenderId)
                .orElseThrow(() -> new RuntimeException("Defender not found"));

        verifyGameIsActive(attacker);

        BattleReport report = battleService.executeAttack(attacker, defender);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/end-turn")
    public ResponseEntity<Void> endTurn(@RequestParam Long sessionId) {
        turnService.endPlayerTurn(sessionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unit/{id}")
    public ResponseEntity<Character> getUnitDetails(@PathVariable Long id) {
        return ResponseEntity.ok(characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found")));
    }

    @GetMapping("/state/{sessionId}")
    public ResponseEntity<GameState> getGameState(@PathVariable Long sessionId) {
        GameState state = gameService.getCurrentState(sessionId);
        return ResponseEntity.ok(state);
    }

    @PostMapping("/spawn")
    public ResponseEntity<Character> spawnUnit(
            @RequestParam String name,
            @RequestParam UnitClass unitClass,
            @RequestParam Character.Allegiance allegiance,
            @RequestParam int level,
            @RequestParam int x,
            @RequestParam int y
    ) {
        Character unit = new Character(name, unitClass, allegiance, level);
        unit.setPosX(x);
        unit.setPosY(y);

        return ResponseEntity.ok(characterRepository.save(unit));
    }

    @PostMapping("/heal")
    public ResponseEntity<BattleReport> heal(
            @RequestParam Long healerId,
            @RequestParam Long targetId
    ) {
        Character healer = characterRepository.findById(healerId)
                .orElseThrow(() -> new RuntimeException("Healer not found"));
        Character target = characterRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("Target not found"));

        verifyGameIsActive(healer);

        BattleReport report = battleService.executeHealing(healer, target);

        characterRepository.save(healer);
        characterRepository.save(target);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<GameSession>> getAllSessions() {
        return ResponseEntity.ok(gameSessionRepository.findAll());
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        gameSessionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/use-item")
    public ResponseEntity<String> useItem(@RequestParam Long unitId, @RequestParam Long itemId) {
        Character unit = characterRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        verifyGameIsActive(unit);

        Item item = unit.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not in inventory"));

        item.applyEffect(unit);
        unit.removeFromInventory(item);
        unit.setHasActed(true);

        characterRepository.save(unit);
        return ResponseEntity.ok("Used " + item.getName());
    }

    @PostMapping("/equip-weapon")
    public ResponseEntity<String> equipWeapon(@RequestParam Long unitId, @RequestParam Long weaponId) {
        Character unit = characterRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        verifyGameIsActive(unit);

        Weapon weaponToEquip = (Weapon) unit.getInventory().stream()
                .filter(i -> i.getId().equals(weaponId) && i instanceof Weapon)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Weapon not found in inventory"));

        if (!unit.getUnitClass().getAllowedWeaponTypes().contains(weaponToEquip.getWeaponType())) {
            throw new RuntimeException(unit.getName() + " is a " + unit.getUnitClass().getDisplayName() +
                    " and cannot equip " + weaponToEquip.getWeaponType().getDisplayName() + "!");
        }

        unit.getInventory().stream()
                .filter(i -> i instanceof Weapon)
                .forEach(i -> ((Weapon) i).setEquipped(i.getId().equals(weaponId)));

        characterRepository.save(unit);

        return ResponseEntity.ok(unit.getName() + " equipped " + weaponToEquip.getName());
    }

    @PostMapping("/trade")
    public ResponseEntity<String> tradeItem(
            @RequestParam Long giverId,
            @RequestParam Long receiverId,
            @RequestParam Long itemId
    ) {
        Character giver = characterRepository.findById(giverId)
                .orElseThrow(() -> new RuntimeException("Giver not found"));
        Character receiver = characterRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        verifyGameIsActive(giver);

        Item item = giver.getInventory().stream()
                .filter(i -> i.getId().equals(itemId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (giver.getDistanceTo(receiver) > 1) throw new RuntimeException("Too far to trade!");

        giver.removeFromInventory(item);
        receiver.addToInventory(item);

        characterRepository.save(giver);
        characterRepository.save(receiver);
        return ResponseEntity.ok("Traded " + item.getName());
    }

    @GetMapping("/unit/{unitId}/valid-moves")
    public ResponseEntity<List<int[]>> getValidMoves(@PathVariable Long unitId) {
        return ResponseEntity.ok(movementService.getValidMoves(unitId));
    }

    @GetMapping("/forecast")
    public ResponseEntity<BattleReport> getForecast(@RequestParam Long attackerId, @RequestParam Long defenderId) {
        Character attacker = characterRepository.findById(attackerId).orElseThrow();
        Character defender = characterRepository.findById(defenderId).orElseThrow();
        return ResponseEntity.ok(battleService.previewCombat(attacker, defender));
    }

    private void verifyGameIsActive(Character unit) {
        if (unit.getGameSession() == null) return;
        Long sessionId = unit.getGameSession().getId();
        GameState state = gameService.getCurrentState(sessionId);
        if (!state.status().equals("ACTIVE")) {
            throw new RuntimeException("The battle has ended. No further actions allowed!");
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}

