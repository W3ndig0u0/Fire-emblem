import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Gameloop {
  public void startGame(Scanner scanner) {
    Hero hero = new Hero("Player", 100, 10, 20);
    List<Demon> enemies = new ArrayList<>();
    int wave = 1;

    while (hero.isAlive()) {
      // ! Om listan är tom, spawn ny våg med fler fiender +1
      if (enemies.isEmpty()) {
        System.out.println("--- WAVE " + wave + " BEGINS ---\n");
        for (int i = 0; i < wave; i++) {
          enemies.add(new Demon("Lesser Demon " + (i + 1), 30, 5, 10));
        }
        wave++;
      }

      // ! Menyval
      System.out.println("=== STATUS ===");
      hero.hpBar();
      System.out.println("- ENEMIES -");
      for (Demon e : enemies) {
        e.hpBar();
      }
      System.out.println("==============");

      String prompt = "\n[ " + hero.getName() + "'s Turn ]\nChoose your action:";

      List<String> menuOptions = List.of("Attack first enemy", "Items", "Quit");
      MenuFunction menu = new MenuFunction(prompt, menuOptions);
      waitForKey(scanner);

      int selectedIndex = menu.run(0, scanner);

      switch (selectedIndex) {
        case 0 -> {
          // ! Attackera den första fienden i listan
          Demon target = enemies.get(0);
          System.out.println("You attack " + target.getName() + "!" + " (" + hero.getAttack() + " damage)");
          target.takeDamage(hero.getAttack());

          for (Demon e : enemies) {
            if (e.isAlive()) {
              System.out.println(e.getName() + " attacks you!" + " (" + e.getAttack() + " damage)");
              hero.takeDamage(e.getAttack());
            }
          }

          if (!target.isAlive()) {
            System.out.println("You defeated " + target.getName() + "!");
            hero.gainExp(target.giveExp());
            enemies.remove(0);
            waitForKey(scanner);
          }

          waitForKey(scanner);
        }
        case 1 -> {
          System.out.println("Items not implemented!");
          waitForKey(scanner);
        }
        case 2 -> {
          return;
        }
      }

      if (!hero.isAlive()) {
        System.out.println("Game Over! You reached wave " + (wave - 1));
        waitForKey(scanner);
      }
    }
  }

  private void waitForKey(Scanner scanner) {
    System.out.println("\nPress Enter to continue...");
    scanner.nextLine();
  }
}
