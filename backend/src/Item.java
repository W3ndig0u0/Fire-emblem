public abstract class Item {

  public abstract void use(Character target);

  protected String name;
  protected int level;

  private String rarety;

  public Item(String name, int level, String rarety) {
    setName(name);
    setLevel(level);
    this.rarety = rarety;
  }

  public void getInfo() {
    System.out.println("The Item's is a " + getRarety() + " " + getName());
  }

  public String getRarety() {
    return rarety;
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
