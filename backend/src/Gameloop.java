import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Gameloop {
  public void startGame(Scanner scanner) {
    Hero hero = new Hero("Player", 20, 5, 10);

    Weapon startingWeapon = new Weapon("Rusty Sword", 1, 5, "Basic", 5);
    hero.addToInventory(startingWeapon);

    PoisenPotion poisenPotion = new PoisenPotion("Poisen Potion", 1, 5, "Basic");
    hero.addToInventory(poisenPotion);

    HealPotion healPotion = new HealPotion("Heal Potion", 1, 10, "Basic");
    hero.addToInventory(healPotion);

    List<Demon> enemies = new ArrayList<>();
    int wave = 1;
    boolean inRewardStage = false;
    InventoryMenu inventoryMenu = new InventoryMenu();

    while (hero.isAlive()) {
      // ! Om listan är tom, spawn ny våg med fler fiender +1
      if (enemies.isEmpty()) {
        System.out.println("--- WAVE " + wave + " BEGINS ---\n");
        for (int i = 0; i < wave; i++) {
          enemies.add(new Demon("Lesser Demon " + (i + 1), 10 + (i * 2), 5, 5 + i));
        }
        if (wave > 1) {
          inRewardStage = true;
        }
        wave++;
      }

      if (inRewardStage) {
        rewardStage(hero, scanner, wave);
        inRewardStage = false;
      }

      // ! Menyval
      System.out.println("=== STATUS ===");
      System.out.println("Wave: " + (wave - 1));
      hero.hpBar();
      System.out.println("- ENEMIES -");
      for (Demon e : enemies) {
        e.hpBar();
      }
      System.out.println("==============");

      String prompt = "\n[ " + hero.getName() + "'s Turn ]\nChoose your action:";

      List<String> menuOptions = List.of("Attack enemy", "Item Menu", "Quit");
      MenuFunction menu = new MenuFunction(prompt, menuOptions);
      waitForKey(scanner);

      int selectedIndex = menu.run(0, scanner);

      switch (selectedIndex) {
        case 0 -> {

          List<String> enemyOptions = new java.util.ArrayList<>();

          for (Demon demon : enemies) {
            enemyOptions.add(demon.getName() + " (HP: " + demon.getHealth() + ")" + " (" + demon.getAttack() + " ATK)");
          }

          MenuFunction enemyMenu = new MenuFunction("Choose an enemy to attack:", enemyOptions);
          int enemyIndex = enemyMenu.run(0, scanner);
          Demon target = null;

          while (target == null) {
            if (enemyIndex >= 0 && enemyIndex < enemies.size()) {
              target = enemies.get(enemyIndex);
              break;
            } else {
              System.out.println("Invalid selection. Please choose a valid enemy.");
              enemyIndex = enemyMenu.run(enemyIndex, scanner);
            }
          }

          System.out.println("You attacked " + target.getName() + "!" + " (" + hero.getAttack() + " damage)");
          target.takeDamage(hero.getAttack());

          for (Demon e : enemies) {
            if (e.isAlive()) {
              System.out.println(e.getName() + " attacked you!" + " (" + e.getAttack() + " damage)");
              hero.takeDamage(e.getAttack());
            }
          }

          if (!target.isAlive()) {
            System.out.println("You defeated " + target.getName() + "!");
            hero.gainExp(target.giveExp());
            enemies.remove(0);
          }

          waitForKey(scanner);
        }
        case 1 -> {
          inventoryMenu.InventoryListShow(hero, enemies, scanner);
          waitForKey(scanner);
        }
        case 2 -> {
          QuitGame(scanner);
        }
      }

      if (!hero.isAlive()) {
        System.out.println("Game Over! You reached wave " + (wave - 1) + ".");
        waitForKey(scanner);
        QuitGame(scanner);
      }
    }
  }

  private void QuitGame(Scanner scanner) {
    System.out.print("\033[H\033[2J");
    System.out.print("Quitting Game...");
    scanner.close();
    System.exit(0);
  }

  private void rewardStage(Hero hero, Scanner scanner, int completedWave) {
    System.out.print("\033[H\033[2J");
    System.out.println(">>> Wave " + completedWave + " Cleared! <<<");
    System.out.println("An old NPC approaches you: 'Good work, traveler. Take a gift before the next swarm!'");

    String prompt = "Choose your reward:";
    List<String> rewards = List.of("Heal 5 HP", "Permanent +2 Attack", "Recive Random Potion");
    MenuFunction rewardMenu = new MenuFunction(prompt, rewards);

    int choice = rewardMenu.run(0, scanner);
    switch (choice) {
      case 0 -> {
        System.out.println("You feel rejuvenated!");
        hero.heal(5);
      }
      case 1 -> {
        System.out.println("You feel your muscles grow stronger!");
        hero.imporveBaseStrength(2);
      }
      case 2 -> {
        System.out.print("\033[H\033[2J");
        PoisenPotion potion = new PoisenPotion("Poisen Potion", 1, 5, "Basic");
        hero.addToInventory(potion);
        potion.getInfo();
      }
    }
    waitForKey(scanner);
  }

  private void waitForKey(Scanner scanner) {
    System.out.println("\nPress Enter to continue...");
    scanner.nextLine();
  }
}
