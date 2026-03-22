package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Demon extends Character {

  private int expValue = 5;
  private String monsterType;

  public Demon(String name, int health, int mana, int baseStrength,  int defense) {
    super(name, health, mana, baseStrength, defense);
    this.movementRange = 3;
  }

  public int giveExp() {
    return this.expValue;
  }

  public Hero chooseTarget(java.util.List<Hero> potentialTargets) {
    if (potentialTargets.isEmpty()) return null;

    return potentialTargets.stream()
            .min(java.util.Comparator.comparingInt(Hero::getHealth))
            .orElse(null);
  }
}
