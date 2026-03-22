package com.example.demo.service;

import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapService {
    private final CharacterRepository characterRepository;

    public boolean isWalkable(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    public boolean isTileOccupied(int x, int y) {
        return characterRepository.findAll().stream()
                .anyMatch(c -> c.getPosX() == x && c.getPosY() == y && c.isAlive());
    }

    public Optional<Character> getUnitAt(int x, int y) {
        return characterRepository.findAll().stream()
                .filter(c -> c.getPosX() == x && c.getPosY() == y && c.isAlive())
                .findFirst();
    }
}
