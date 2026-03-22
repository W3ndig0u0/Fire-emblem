package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int turnNumber = 1;
    private String currentPhase = "PLAYER_PHASE";

    public void togglePhase() {
        this.currentPhase = currentPhase.equals("PLAYER_PHASE") ? "ENEMY_PHASE" : "PLAYER_PHASE";
        if (currentPhase.equals("PLAYER_PHASE")) this.turnNumber++;
    }
}