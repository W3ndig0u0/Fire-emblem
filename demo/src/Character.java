public abstract class Character {
  protected String name;
  protected int health;
  protected int maxHealth;
  protected int mana;
  protected int attack;
  protected int baseStrength;
  protected Inventory inventory;

  public Character(String name, int health, int mana, int baseStrength) {
    this.name = name;
    this.health = health;
    this.maxHealth = health;
    this.mana = mana;
    this.attack = baseStrength;
    this.baseStrength = baseStrength;
    this.inventory = new Inventory();
  }

  public int getBaseStrength() {
    return baseStrength;
  }

  public void imporveBaseStrength(int addStrength) {
    this.baseStrength += addStrength;
    attack += addStrength;
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
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

  public void doDamage(Character enemy) {
    if (enemy == null) {
      System.out.println("No enemy to attack.");
      return;
    }
    enemy.takeDamage(attack);
    System.out.println(this.name + " attacks " + enemy.getName() + " for " + attack + " damage!");
  }

  public void doDamage(Character enemy, Weapon weapon) {
    if (enemy == null) {
      System.out.println("No enemy to attack.");
      return;
    }
    weapon.reduceDurability(1);
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

  public void showInventory() {
    inventory.showInventory();
  }

  public void addToInventory(Item item) {
    System.out.println("You added a " + item.getRarety() + " " + item.getName() + " to your inventory.");
    inventory.add(item);
  }

  public void removeFromInventory(int n) {
    inventory.remove(n);
  }

  public void removeFromInventory(Item item) {
    inventory.remove(item);
  }

  public void useItem(int n, Character target) {
    inventory.useItem(n, target);
  }

  public void useItem(int n) {
    inventory.useItem(n, this);
  }

  public void useItem(Item item, Character target) {
    inventory.useItem(item, target);
  }

  public void useItem(Item item) {
    inventory.useItem(item, this);
  }

  public void getItemInfo(int n) {
    inventory.getItemInfo(n);
  }

  public void getItemInfo(Item item) {
    inventory.getItemInfo(item);
  }

  public int getInventoryLength() {
    int amount = inventory.getLength();
    return amount;
  }

  public String getInventoryName(int itemListIndex) {
    return inventory.getInventoryName(itemListIndex);
  }

  public Item getInventoryItem(int itemListIndex) {
    return inventory.getItem(itemListIndex);
  }

}
