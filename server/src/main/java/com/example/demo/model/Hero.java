package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hero extends Character {

  private int currExp = 0;
  private int maxExp = 10;
  private int lv = 1;

  public Hero(String name, int health, int mana, int baseStrength,  int defense) {
    super(name, health, mana, baseStrength, defense);
    this.movementRange = 5;
  }

  public boolean gainExp(int exp) {
    this.currExp += exp;
    if (this.currExp >= this.maxExp) {
      levelUp();
      return true;
    }
    return false;
  }

  public void levelUp() {
    this.lv++;
    this.currExp = 0;
    this.maxExp += 10;

    this.attack += 5;
    this.maxHealth += 5;
    this.health = health+2;
  }
}
