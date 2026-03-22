public class PoisenPotion extends Item {
  private int poisonAmount;

  public PoisenPotion(String name, int level, int poisonAmount, String rarety) {
    super(name, level, rarety);
    this.poisonAmount = poisonAmount;
  }

  @Override
  public void use(Character target) {
    System.out
        .println("You used a " + getRarety() + " " + name + " and poisoned for " + poisonAmount + " health points!");
    target.takeDamage(poisonAmount);
  }

  @Override
  public void getInfo() {
    System.out.println("The Item's Name is " + name);
    System.out.println("The Item's level is " + level);
    System.out.println("The Item poisons for " + poisonAmount + " health points");
  }

}
