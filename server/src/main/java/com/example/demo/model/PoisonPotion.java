package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class PoisonPotion extends Item {

  private int poisonAmount;

  public PoisonPotion(String name, int level, int poisonAmount, Rarity rarity) {
    super(name, level, rarity);
    this.poisonAmount = poisonAmount;
  }

  @Override
  public void applyEffect(Character target) {
    target.takeDamage(this.poisonAmount);
  }
}
