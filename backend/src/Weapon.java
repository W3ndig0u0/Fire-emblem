public class Weapon extends Item {
  private int strengthBonus;
  private int durability;
  private int maxDurability;

  public Weapon(String name, int level, int strengthBonus, String rarety, int maxDurability) {
    super(name, level, rarety);
    this.strengthBonus = strengthBonus;
    this.maxDurability = maxDurability;
    this.durability = maxDurability;
  }

  @Override
  public void use(Character target) {
    System.out.println("Your strength is increased by " + strengthBonus + " points");
    target.baseStrength += strengthBonus;
    System.out.println("Strength: " + (target.baseStrength - strengthBonus) + " ==> " + target.baseStrength);
    System.out.println("The Item's durability is " + durability + "/" + maxDurability);
  }

  @Override
  public void getInfo() {
    System.out.println("The Item's Name is " + name);
    System.out.println("The Item's level is " + level);
    System.out.println("The Item's strength is " + strengthBonus);
    System.out.println("The Item's durability is " + durability + "/" + maxDurability);
  }

  public void reduceDurability(int amount) {
    durability -= amount;
    if (durability < 0) {
      durability = 0;
    }
  }

  public int getDurability() {
    return durability;
  }

  public int getMaxDurability() {
    return maxDurability;
  }

}
