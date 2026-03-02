import java.util.List;
import java.util.Scanner;

public class Menu {

  private static final Scanner scanner = new Scanner(System.in);

  public void startMenu() {
    String promt = "Welcome to the Menu. What to you want to do? @Move around in the Menu by using the S and S.@Continue by pressing Enter!@";
    promt = promt.replace("@", System.lineSeparator());

    List<String> menuOptions = List.of("Start", "About", "Quit");
    MenuFunction menu = new MenuFunction(promt, menuOptions);

    int selectedIndex = menu.run(0, scanner);
    while (selectedIndex != 0) {
      switch (selectedIndex) {
        case 0:
          StartGame();
          break;
        case 1:
          AboutGame();
          break;
      }
      selectedIndex = menu.run(selectedIndex, scanner);
    }
    QuitGame();

  }

  private void waitForKey() {
    System.out.println("\nPress Enter to continue...");
    if (scanner.hasNextLine())
      scanner.nextLine();
  }

  // !Alternativen för menyn
  private void QuitGame() {
    System.out.print("\033[H\033[2J");
    System.out.print("Quitting Game...");
    scanner.close();
    System.exit(0);
  }

  private void AboutGame() {
    System.out.print("\033[H\033[2J");
    System.out.println("lorem ipsum dolor sit amet.");
    System.out.println("lorem ipsum dolor sit amet.");
    System.out.println("lorem ipsum dolor sit amet.");
    System.out.println("Made by Jingxiang Xu");
    waitForKey();
  }

  private void StartGame() {
    System.out.print("\033[H\033[2J");
    System.out.println("Starting Game...");
    Gameloop gameloop = new Gameloop();
    gameloop.startGame(scanner);
  }
}