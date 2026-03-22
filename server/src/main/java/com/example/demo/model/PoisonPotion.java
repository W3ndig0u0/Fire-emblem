package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class PoisonPotion extends Item {

  private int poisonAmount;

  public PoisonPotion(String name, int level, int poisonAmount, String rarity) {
    super(name, level, rarity);
    this.poisonAmount = poisonAmount;
  }

  public void applyEffect(java.lang.Character target) {
    target.setHp(Math.max(0, target.getHp() - this.poisonAmount));
  }

  @Override
  public void use(Character target) {
    target.heal(this.healAmount);
  }
}
