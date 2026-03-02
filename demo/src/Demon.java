public class Demon extends Character {

  public Demon(String name, int health, int mana, int baseStrength) {
    super(name, health, mana, baseStrength);
  }

  public int giveExp() {
    return 5;
  }
}