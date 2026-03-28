package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Weapon extends Item {

  private int strengthBonus;
  private int durability;
  private int maxDurability;
  private int minRange = 1;
  private int maxRange = 1;
  private boolean equipped = false;

  @Enumerated(EnumType.STRING)
  private WeaponType weaponType;

  public Weapon(String name, int level, Rarity rarity, int strengthBonus, int maxDurability, WeaponType type) {
    super(name, level, rarity);
    this.strengthBonus = strengthBonus;
    this.maxDurability = maxDurability;
    this.durability = maxDurability;
    this.weaponType = type;
  }

  public void reduceDurability(int amount) {
    this.durability = Math.max(0, this.durability - amount);
  }

  @Override
  public void applyEffect(Character target) {
    this.reduceDurability(1);
  }
  public int getStrengthBonus() {
    double multiplier = (this.rarity != null) ? this.rarity.getDamageMultiplier() : 1.0;
    return (int) (this.strengthBonus * multiplier);
  }

  public int getCritRate() {
    int bonusCrit = (this.rarity != null) ? this.rarity.getBonusCrit() : 0;
    return this.weaponType.getBaseCrit() + bonusCrit;
  }
}
