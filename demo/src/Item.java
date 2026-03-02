public abstract class Item {

  public abstract void use(Character target);

  protected String name;
  protected int level;

  public Item(String name, int level) {
    setName(name);
    setLevel(level);
  }

  public void getInfo() {
    System.out.println("The Item's Name is " + getName());
    System.out.println("The Item's level is " + getLevel());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

}
