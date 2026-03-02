public abstract class Charachter {
  protected String name;
  protected int health;
  protected int maxHealth;
  protected int mana;
  protected int attack;

  public Charachter(String name, int health, int mana, int attack) {
    this.name = name;
    this.health = health;
    this.maxHealth = health;
    this.mana = mana;
    this.attack = attack;
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public int getMana() {
    return mana;
  }

  public int getAttack() {
    return attack;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void takeDamage(int damage) {
    health -= damage;
    if (health < 0) {
      health = 0;
    }
  }

  public void doDamage(Charachter enemy) {
    if (enemy == null) {
      System.out.println("No enemy to attack.");
      return;
    }
    enemy.takeDamage(attack);
    System.out.println(this.name + " attacks " + enemy.getName() + " for " + attack + " damage!");
  }

  private void hpCheck() {
    if (health > maxHealth) {
      health = maxHealth;
    }
  }

  public void hpBar() {
    hpCheck();
    System.out.print(this.name + " Health: \n[");

    String reset = "\u001B[0m";
    String greenBackground = "\u001B[42m";
    String redBackground = "\u001B[41m";

    // ? Välj färg baserat på HP
    if (health <= 19) {
      System.out.print(redBackground);
    } else {
      System.out.print(greenBackground);
    }

    for (int i = 0; i < health; i++) {
      System.out.print(" ");
    }

    System.out.print(reset);
    for (int i = health; i < maxHealth; i++) {
      System.out.print(" ");
    }

    System.out.println("] (" + health + "/" + maxHealth + ")");
  }

  public void heal(int amount) {
    health += amount;
    if (health > maxHealth) {
      health = maxHealth;
    }
  }

  public boolean isAlive() {
    return health > 0;
  }

  public String toString() {
    return String.format("%s - Health: %d, Mana: %d, Attack: %d", name, health, mana, attack);
  }
}
