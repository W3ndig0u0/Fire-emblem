package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
@Getter @Setter @NoArgsConstructor
public abstract class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private int level;
  private String rarity;

  @ManyToOne
  @JoinColumn(name = "character_id")
  @com.fasterxml.jackson.annotation.JsonIgnore
  private Character owner;

  public Item(String name, int level, String rarity) {
    this.name = name;
    this.level = level;
    this.rarity = rarity;
  }

  public abstract void applyEffect(Character target);

}
