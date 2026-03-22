import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuFunction {

  private int selectedIndex;
  private List<String> menuOptions = new ArrayList<String>();
  private String promt = "";

  public MenuFunction(String promt, List<String> menuOptions) {
    this.promt = promt;
    this.menuOptions = menuOptions;
    this.selectedIndex = 0;
  }

  public int run(int n, Scanner scanner) {
    int size = menuOptions.size();

    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.println(promt);
      menuOptions();
      String input = scanner.nextLine().trim().toLowerCase();

      // ?vi hoppar till sista elementet (cirkulär navigation).
      switch (input) {
        case "w" -> selectedIndex = (selectedIndex - 1 + size) % size;
        case "s" -> selectedIndex = (selectedIndex + 1) % size;
        case "" -> {
          return selectedIndex;
        }

        default -> {
          System.err.println("Invalid input. Please use 'W' to move up, 'S' to move down, and press Enter to select.");
        }
      }
    }
  }

  private void menuOptions() {
    for (int i = 0; i < menuOptions.size(); i++) {
      String currentOptions = menuOptions.get(i);
      String prefix;

      if (i == selectedIndex) {
        prefix = ">> ";
      } else {
        prefix = " ";
      }
      System.out.println(prefix + "" + currentOptions);
    }
  }

}
