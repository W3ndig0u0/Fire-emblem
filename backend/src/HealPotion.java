public class HealPotion extends Item {
  private int healAmount;

  public HealPotion(String name, int level, int healAmount, String rarety) {
    super(name, level, rarety);
    this.healAmount = healAmount;
  }

  @Override
  public void use(Character target) {
    System.out.println("You used a " + getRarety() + " " + name + " and healed for " + healAmount + " health points!");
    target.heal(healAmount);
  }

  @Override
  public void getInfo() {
    System.out.println("The Item's Name is " + name);
    System.out.println("The Item's level is " + level);
    System.out.println("The Item heals for " + healAmount + " health points");
  }

}
