package com.example.demo.service;

import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovementService {

    private final CharacterRepository characterRepository;
    private final MapService mapService;

    public MovementService(CharacterRepository characterRepository, MapService mapService) {
        this.characterRepository = characterRepository;
        this.mapService = mapService;
    }

    public List<int[]> getValidMoves(Long unitId) {
        Character unit = characterRepository.findById(unitId).orElseThrow();
        List<int[]> validTiles = new ArrayList<>();
        int range = unit.getMovementRange();

        for (int x = unit.getPosX() - range; x <= unit.getPosX() + range; x++) {
            for (int y = unit.getPosY() - range; y <= unit.getPosY() + range; y++) {
                if (mapService.isWalkable(x, y) && !mapService.isTileOccupied(x, y)) {
                    if (Math.abs(unit.getPosX() - x) + Math.abs(unit.getPosY() - y) <= range) {
                        validTiles.add(new int[]{x, y});
                    }
                }
            }
        }
        return validTiles;
    }


    @Transactional
    public String moveUnit(Long unitId, int targetX, int targetY) {
        Character unit = characterRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        if(unit.isHasMoved()){
            throw new RuntimeException(unit.getName() + " has already moved this turn!");
        }

        if (unit.isHasActed()) {
            throw new RuntimeException(unit.getName() + " has already acted this turn!");
        }

        if (mapService.isTileOccupied(targetX, targetY)) {
            throw new RuntimeException("Tile is occupied!");
        }

        if (!mapService.isWalkable(targetX, targetY)) {
            throw new RuntimeException("You cant move there!");
        }

        int distance = Math.abs(unit.getPosX() - targetX) + Math.abs(unit.getPosY() - targetY);
        if (distance > unit.getMovementRange()) {
            throw new RuntimeException("Target is too far! Max move: " + unit.getMovementRange());
        }

        unit.setPosX(targetX);
        unit.setPosY(targetY);
        unit.setHasActed(true);

        characterRepository.save(unit);

        return unit.getName() + " moved to " + targetX + "," + targetY;
    }
}
