package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.Character;

@Entity
@Table(name = "weapons")
@Getter
@Setter
@NoArgsConstructor
public class Weapon extends Item {

  @Id
  private Long id;
  private int strengthBonus;
  private int durability;
  private int maxDurability;

  @ManyToOne
  @JoinColumn(name = "character_id")
  @com.fasterxml.jackson.annotation.JsonIgnore // Prevent infinite loops in JSON
  private Character owner;

  public Weapon(String name, int level, String rarity, int strengthBonus, int maxDurability) {
    super(name, level, rarity);
    this.strengthBonus = strengthBonus;
    this.maxDurability = maxDurability;
    this.durability = maxDurability;
  }

  public void reduceDurability(int amount) {
    this.durability = Math.max(0, this.durability - amount);
  }
}
