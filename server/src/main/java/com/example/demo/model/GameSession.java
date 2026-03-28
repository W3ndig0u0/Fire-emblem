package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Character> characters;

    public void togglePhase() {
        this.currentPhase = currentPhase.equals("PLAYER_PHASE") ? "ENEMY_PHASE" : "PLAYER_PHASE";
        if (currentPhase.equals("PLAYER_PHASE")) this.turnNumber++;
    }
}