public class Hero extends Charachter {

  private int currExp = 0;
  private int maxExp = 10;
  private int lv = 1;

  public Hero(String name, int health, int mana, int attack) {
    super(name, health, mana, attack);
  }

  public void gainExp(int exp) {
    currExp += exp;
    System.out.println("You gained " + exp + " Exp.");
    if (currExp >= maxExp) {
      LevelUp();
    }
  }

  public void LevelUp() {
    System.out.print("\033[H\033[2J");
    lv++;
    currExp = 0;
    maxExp += 10;
    attack += 5;
    maxHealth += 5;
    health += 5;
    System.out.println("You gained 1 Lv.");
    System.out.println("Level: " + (lv - 1) + " ==> " + lv);
    System.out.println("Attack: " + (attack - 10) + " ==> " + attack);
    System.out.println("MaxHealth: " + (maxHealth - 5) + " ==> " + maxHealth);
    System.out.println("Health: " + (health - 5) + " ==> " + health);
  }

  public int getCurrExp() {
    return currExp;
  }

  public int getMaxExp() {
    return maxExp;
  }

  public int getLv() {
    return lv;
  }
}
