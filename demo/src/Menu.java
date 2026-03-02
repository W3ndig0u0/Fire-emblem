import java.util.List;
import java.util.Scanner;

public class Menu {
  private Scanner scanner = null;

  public Menu(Scanner scanner) {
    this.scanner = scanner;
  }

  public void startMenu() {
    String promt = "Welcome to the RPG GAME. @What to you want to do? @Move around in the menu by using the S and S.@Continue by pressing Enter!@";
    promt = promt.replace("@", System.lineSeparator());

    List<String> menuOptions = List.of("Start", "About", "Quit");
    MenuFunction menu = new MenuFunction(promt, menuOptions);

    int selectedIndex = menu.run(0, this.scanner);
    while (selectedIndex != 2) {
      switch (selectedIndex) {
        case 0:
          StartGame();
          break;
        case 1:
          AboutGame();
          break;
      }
      selectedIndex = menu.run(selectedIndex, this.scanner);
    }
    QuitGame();

  }

  private void waitForKey() {
    System.out.println("\nPress Enter to continue...");
    if (this.scanner.hasNextLine())
      this.scanner.nextLine();
  }

  // !Alternativen för menyn
  private void QuitGame() {
    System.out.print("\033[H\033[2J");
    System.out.print("Quitting Game...");
    this.scanner.close();
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
    gameloop.startGame(this.scanner);
  }
}