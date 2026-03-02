import java.util.List;
import java.util.Scanner;

public class Gameloop {
  public void startGame(Scanner scanner) {

    String promt = "What to you want to do?";
    List<String> menuOptions = List.of("Attack", "Items", "Quit");
    MenuFunction menu = new MenuFunction(promt, menuOptions);

    Hero hero = new Hero("Player", 100, 50, 20);
    Demon enemy = new Demon("Lesser Demon", 30, 50, 20);
    List<Charachter> characters = List.of(hero, enemy);

    int selectedIndex = menu.run(0, scanner);
    while (hero.isAlive()) {
      System.out.print("\033[H\033[2J");

      for (Charachter c : characters) {
        c.hpBar();
      }

      switch (selectedIndex) {
        case 0 -> {
          System.out.println("You attack the enemy!");
          enemy.takeDamage(10);
          enemy.hpBar();
          waitForKey(scanner);

          if (!enemy.isAlive()) {
            System.out.println("You defeated the enemy!");
            hero.gainExp(10);
            waitForKey(scanner);
            return;
          }

          System.out.println("The enemy attacks you!");
          hero.takeDamage(10);
          hero.hpBar();
          waitForKey(scanner);

        }
        case 1 -> {
          System.out.println("Items not implemented!");
          waitForKey(scanner);

        }
        case 2 -> {
          System.out.println("Quitting Game...");
          return;
        }
        default -> System.out.println("Invalid input. Please try again.");
      }
    }
    scanner.nextLine();
  }

  private void waitForKey(Scanner scanner) {
    System.out.println("\nPress Enter to continue...");
    if (scanner.hasNextLine())
      scanner.nextLine();
  }
}