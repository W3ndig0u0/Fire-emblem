package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weapons")
@Getter
@Setter
@NoArgsConstructor
public class Weapon extends Item {

  private int strengthBonus;
  private int durability;
  private int maxDurability;
  private int minRange = 1;
  private int maxRange = 1;

  @Enumerated(EnumType.STRING)
  private WeaponType weaponType;

  public Weapon(String name, int level, String rarity, int strengthBonus, int maxDurability, WeaponType type, int minRange, int maxRange) {
    super(name, level, rarity);
    this.strengthBonus = strengthBonus;
    this.maxDurability = maxDurability;
    this.durability = maxDurability;
    this.weaponType = type;
    this.minRange = minRange;
    this.maxRange = maxRange;
  }

  @ManyToOne
  @JoinColumn(name = "character_id")
  @com.fasterxml.jackson.annotation.JsonIgnore
  private Character owner;

  public void reduceDurability(int amount) {
    this.durability = Math.max(0, this.durability - amount);
  }

  @Override
  public void applyEffect(Character target) {
    this.reduceDurability(1);
  }

}
