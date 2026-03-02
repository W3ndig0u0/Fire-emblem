public class Weapon extends Item {
  private int strengthBonus;;

  public Weapon(String name, int level, int strengthBonus) {
    super(name, level);
    this.strengthBonus = strengthBonus;
  }

  @Override
  public void use(Character target) {
    System.out.println("Your strength is increased by " + strengthBonus + " points");
    target.baseStrength += strengthBonus;
    System.out.println("BaseStrength: " + (target.baseStrength - strengthBonus) + " ==> " + target.baseStrength);
  }

  @Override
  public void getInfo() {
    System.out.println("The Item's Name is " + name);
    System.out.println("The Item's level is " + level);
    System.out.println("The Item's strength is " + strengthBonus);
  }

}
