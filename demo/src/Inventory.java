import java.util.ArrayList;
import java.util.List;

public class Inventory {

  List<Item> inventoryList = new ArrayList<Item>();

  public List<Item> getInventoryList() {
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

  public void remove(Item item) {
    inventoryList.remove(item);
  }

  public Item getItem(int n) {
    return inventoryList.get(n);
  }

  public void getItemInfo(int n) {
    inventoryList.get(n).getInfo();
  }

  public void getItemInfo(Item item) {
    item.getInfo();
  }

  public String getInventoryName(int n) {
    return inventoryList.get(n).getName();
  }

  public void useItem(int n, Character target) {
    Item item = inventoryList.get(n);
    if (item instanceof Weapon) {
      Weapon weapon = (Weapon) item;
      weapon.use(target);
      weapon.reduceDurability(1);
      if (weapon.getDurability() <= 0) {
        System.out.println("Your " + weapon.getName() + " broke!");
        inventoryList.remove(n);
      }
    } else {
      item.use(target);
    }
    inventoryList.get(n).use(target);
  }

  public void useItem(Item item, Character target) {
    int index = inventoryList.indexOf(item);
    if (index != -1) {
      useItem(index, target);
    }
  }

  public int getLength() {
    return inventoryList.size();
  }

}