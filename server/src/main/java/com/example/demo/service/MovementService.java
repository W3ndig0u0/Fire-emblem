package com.example.demo.service;

import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovementService {

    private final CharacterRepository characterRepository;
    public MovementService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Transactional
    public String moveUnit(Long unitId, int targetX, int targetY) {
        Character unit = characterRepository.findById(unitId).orElseThrow();

        int distance = Math.abs(unit.getPosX() - targetX) + Math.abs(unit.getPosY() - targetY);

        if (distance > unit.getMovementRange()) {
            throw new RuntimeException("Target is too far! Max move: " + unit.getMovementRange());
        }

        unit.setPosX(targetX);
        unit.setPosY(targetY);
        characterRepository.save(unit);

        return unit.getName() + " moved to " + targetX + "," + targetY;
    }
}