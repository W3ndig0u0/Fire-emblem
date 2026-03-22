package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class HealPotion extends Item {

  private int healAmount;

  public HealPotion(String name, int level, int healAmount, Rarity rarity) {
    super(name, level, rarity);
    this.healAmount = healAmount;
  }

  @Override
  public void applyEffect(Character target) {
    target.heal(this.healAmount);
  }
}
