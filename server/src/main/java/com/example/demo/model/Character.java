package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "characters")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "character_type")
@Getter @Setter @NoArgsConstructor
public class Character {

  public enum Allegiance {
    PLAYER, ENEMY, NEUTRAL
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Enumerated(EnumType.STRING)
  protected Allegiance allegiance;

  @Enumerated(EnumType.STRING)
  protected UnitClass unitClass;

  protected String name;
  protected int health;
  protected int maxHealth;
  protected int mana;
  protected int attack;
  protected int defense;
  protected int speed;
  private int currExp = 0;
  private int maxExp = 100;
  private int lv = 1;

  protected int movementRange;
  protected int posX;
  protected int posY;
  private boolean hasActed = false;
  private int expValue = 20;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  protected List<Item> inventory = new ArrayList<>();

  public Character(String name, int health, int mana, int attack, int defense, int speed, int movementRange) {
    this.name = name;
    this.health = health;
    this.maxHealth = health;
    this.mana = mana;
    this.attack = attack;
    this.defense = defense;
    this.speed = speed;
    this.movementRange = movementRange;
  }

  public Character(String name, UnitClass uClass, Allegiance allegiance) {
    this(name, uClass, allegiance, 1);
  }

  public Character(String name, UnitClass uClass, Allegiance allegiance, int targetLevel) {
    this.name = name;
    this.unitClass = uClass;
    this.allegiance = allegiance;
    this.lv = targetLevel;
    this.movementRange = uClass.getMovement();

    this.expValue = 20 + (targetLevel * 5);

    this.maxHealth = roll(uClass.getMinHp(), uClass.getMaxHp());
    this.attack = roll(uClass.getMinStr(), uClass.getMaxStr());
    this.speed = roll(uClass.getMinSpd(), uClass.getMaxSpd());
    this.defense = roll(uClass.getMinDef(), uClass.getMaxDef());

    for (int i = 1; i < targetLevel; i++) {
      applyGrowth();
    }
    this.health = this.maxHealth;
  }

  private int roll(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  private void applyGrowth() {
    if (unitClass != null) {
      if (ThreadLocalRandom.current().nextInt(100) < unitClass.getHpGrowth()) this.maxHealth++;
      if (ThreadLocalRandom.current().nextInt(100) < unitClass.getStrGrowth()) this.attack++;
      if (ThreadLocalRandom.current().nextInt(100) < unitClass.getSpdGrowth()) this.speed++;
      if (ThreadLocalRandom.current().nextInt(100) < unitClass.getDefGrowth()) this.defense++;
    }
  }

  public void levelUp() {
    this.lv++;
    this.currExp = 0;
    this.maxExp += 10;
    applyGrowth();
    this.health = this.maxHealth;
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
    if (item instanceof Weapon weapon) {
      if (!this.unitClass.getAllowedWeaponTypes().contains(weapon.getWeaponType())) {
        throw new RuntimeException(this.name + " cannot use this weapon type!");
      }
    }
    inventory.add(item);
    item.setOwner(this);
  }

  public Weapon getEquippedWeapon() {
    return this.inventory.stream()
            .filter(item -> item instanceof Weapon && ((Weapon) item).isEquipped())
            .map(item -> (Weapon) item)
            .findFirst()
            .orElse(null);
  }

  public void equipWeapon(Weapon newWeapon) {
    inventory.stream()
            .filter(item -> item instanceof Weapon)
            .forEach(item -> ((Weapon) item).setEquipped(false));

    newWeapon.setEquipped(true);
  }

  public void removeFromInventory(Item item) {
    if (item != null) {
      this.inventory.remove(item);
      item.setOwner(null);
    }
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

  public boolean gainExp(int exp) {
    this.currExp += exp;
    if (this.currExp >= this.maxExp) {
      levelUp();
      return true;
    }
    return false;
  }

  public Character chooseTarget(java.util.List<Character> potentialTargets) {
    if (potentialTargets.isEmpty()) return null;

    return potentialTargets.stream()
            .filter(target -> target.getAllegiance() != this.getAllegiance())
            .min(java.util.Comparator.comparingInt(Character::getHealth))
            .orElse(null);
  }
}
