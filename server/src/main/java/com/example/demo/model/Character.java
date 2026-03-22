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
  public enum Allegiance {
    PLAYER, ENEMY, NEUTRAL
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;
  @Enumerated(EnumType.STRING)
  protected Allegiance allegiance;

  protected String name;
  protected int health;
  protected int maxHealth;
  protected int mana;
  protected int attack;
  protected int baseStrength;
  protected int defense;

  protected int movementRange;
  protected int posX;
  protected int posY;
  private boolean hasActed = false;
  
  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  protected List<Item> inventory = new ArrayList<>();

  public Character(String name, int health, int mana, int baseStrength, int defense) {
    this.name = name;
    this.health = health;
    this.maxHealth = health;
    this.mana = mana;
    this.attack = baseStrength;
    this.baseStrength = baseStrength;
    this.defense = defense;
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

  public void removeFromInventory(Item item) {
    inventory.remove(item);
  }

  public Weapon getEquippedWeapon() {
    return this.inventory.stream()
            .filter(item -> item instanceof Weapon)
            .map(item -> (Weapon) item)
            .findFirst()
            .orElse(null);
  }

  public int getDistanceTo(Character target) {
    return Math.abs(this.posX - target.getPosX()) + Math.abs(this.posY - target.getPosY());
  }

  public boolean canHit(Character target) {
    Weapon w = this.getEquippedWeapon();
    if (w == null || target == null) return false;
    int dist = getDistanceTo(target);
    return dist >= w.getMinRange() && dist <= w.getMaxRange();
  }

}
