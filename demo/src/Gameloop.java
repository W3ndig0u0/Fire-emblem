import java.util.Scanner;

public class Gameloop {
  public void startGame(Scanner scanner) {

    Hero h = new Hero("Player", 100, 50, 20);
    h.hpBar();
    scanner.nextLine();
  }
}