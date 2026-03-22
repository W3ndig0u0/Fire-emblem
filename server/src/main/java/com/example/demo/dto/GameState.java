package com.example.demo.dto;

import com.example.demo.model.Character;

import java.util.List;

public record GameState (
        Long sessionId,
        int turnNumber,
        String gameSession,
        List<Character> units,
        int mapWidth,
        int mapHeight,
        String status
){
}
