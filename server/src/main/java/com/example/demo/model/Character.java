package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "characters")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "character_type")
@Getter @Setter @NoArgsConstructor
public abstract class Character {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  protected String name;
  protected int health;
  protected int maxHealth;
  protected int mana;
  protected int attack;
  protected int baseStrength;

  protected int posX;
  protected int posY;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  protected List<Item> inventory = new ArrayList<>();

  public Character(String name, int health, int mana, int baseStrength) {
    this.name = name;
    this.health = health;
    this.maxHealth = health;
    this.mana = mana;
    this.attack = baseStrength;
    this.baseStrength = baseStrength;
  }

  public void takeDamage(int damage) {
    this.health = Math.max(0, this.health - damage);
  }

  public void heal(int amount) {
    this.health = Math.min(this.maxHealth, this.health + amount);
  }

  public boolean isAlive() {
    return this.health > 0;
  }

  public void addToInventory(Item item) {
    inventory.add(item);
    item.setOwner(this);
  }

}
