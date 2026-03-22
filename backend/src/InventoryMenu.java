import java.util.List;
import java.util.Scanner;

public class InventoryMenu {
  public void InventoryListShow(Hero player, List<Demon> enemies, Scanner scanner) {
    int inventoryLength = player.getInventoryLength();

    String promt = "Your items: \nYou have " + player.getInventoryLength() + " items.";
    List<String> menuOptions = new java.util.ArrayList<>();

    if (player.getInventoryLength() <= 0) {
      System.out.println("While you were looking in your inventory, the enemy tried to attack you");
      for (Demon demon : enemies) {
        demon.doDamage(player);
      }
      return;
    }

    for (int i = 0; i < inventoryLength; i++) {
      menuOptions.add(player.getInventoryName(i));
    }

    MenuFunction menu = new MenuFunction(promt, menuOptions);

    player.showInventory();
    System.out.println("Choose the number of Item");

    int selectedIndex = menu.run(0, scanner);
    inventoryChoice(player, selectedIndex, enemies, scanner);

  }

  private void inventoryChoice(Hero player, int itemListIndex, List<Demon> enemies, Scanner scanner) {
    Item item = player.getInventoryItem(itemListIndex);

    inventoryQuestion(player, item, enemies, scanner);
  }

  // ?Parameterna innehåller vilken itemIndex spelaren valde
  private void inventoryQuestion(Hero player, Item item, List<Demon> enemies, Scanner scanner) {
    String promt = "What do you want to do with the " + item.getRarety() + " " + item.getName() + "?\n";

    List<String> menuOptions = new java.util.ArrayList<>();
    menuOptions.add("Use the " + item.getName());
    menuOptions.add("More Info about the " + item.getName());
    menuOptions.add("Go Back to the Game");
    MenuFunction menu = new MenuFunction(promt, menuOptions);

    switch (menu.run(0, scanner)) {
      case 0:
        if (item instanceof Weapon) {
          System.out.println("Already equipped weapons cannot be used again!");
          return;
        }

        if (item instanceof HealPotion) {
          System.out.println("You are using a Heal Potion!");
          HealPotion healPotion = (HealPotion) item;
          player.useItem(healPotion);
          player.removeFromInventory(item);
        }

        if (item instanceof PoisenPotion) {
          System.out.println("Using the " + item.getName());
          System.out.println("Who do you want to use it on?");
          for (Demon demon : enemies) {
            demon.hpBar();
          }

          List<String> enemyOptions = new java.util.ArrayList<>();

          for (Demon demon : enemies) {
            enemyOptions.add(demon.getName());
          }
          MenuFunction enemyMenu = new MenuFunction(promt, enemyOptions);
          int selectedIndex = enemyMenu.run(0, scanner);
          while (true) {
            if (selectedIndex >= 0 && selectedIndex < enemies.size()) {
              PoisenPotion poisenPotion = (PoisenPotion) item;
              poisenPotion.use(enemies.get(selectedIndex));
              player.removeFromInventory(item);

              for (int i = 0; i < enemies.size(); i++) {
                Demon demon = enemies.get(i);
                if (!demon.isAlive()) {
                  enemies.remove(demon);
                }
              }
              break;
            } else {
              System.out.println("Invalid selection. Please choose a valid enemy.");
              selectedIndex = enemyMenu.run(selectedIndex, scanner);
            }
          }
        }

        player.removeFromInventory(item);
        break;

      case 1:
        System.out.println("Getting More information about " + item.getName() + ":");
        System.out.println();
        player.getItemInfo(item);
        break;

      default:
        System.out.println("Exiting inventory menu.");
        break;
    }
  }

}