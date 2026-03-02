import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        final Scanner scanner = new Scanner(System.in);
        Menu m = new Menu(scanner);
        m.startMenu();
    }
}
