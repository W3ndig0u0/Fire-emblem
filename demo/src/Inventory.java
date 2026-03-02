import java.util.ArrayList;
import java.util.List;

public class Inventory {

  List<Item> inventoryList = new ArrayList<Item>();

  public List<Item> GetInventoryList() {
    return inventoryList;
  }

  // ?returnerar baserat på listan
  public void showInventory() {
    if (inventoryList.isEmpty()) {
      System.out.println("You do not have any items...");
      return;
    }

    for (int i = 0; i < inventoryList.size(); i++) {
      System.out.println(i + ": The " + inventoryList.get(i).getName());
    }
  }

  public void add(Item item) {
    inventoryList.add(item);
  }

  public void remove(int n) {
    inventoryList.remove(n);
  }

  public void getItemInfo(int n) {
    inventoryList.get(n).getInfo();
  }

  public String getInventoryName(int n) {
    return inventoryList.get(n).getName();
  }

  public void useItem(int n, Character target) {
    inventoryList.get(n).use(target);
    ;

  }

  public int getLength() {
    return inventoryList.size();
  }

}