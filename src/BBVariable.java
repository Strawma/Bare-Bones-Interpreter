/**
 * Represents a variable in the Barebones program.
 */
public class BBVariable {

  private final String name;
  private int value;

  public BBVariable(String name) {
    this.name = name;
    this.value = 0;
  }

  public void clear() {
    this.value = 0;
  }

  /**
   * Adds a value to the variable.
   *
   * @param x the value to add
   */
  public void addValue(int x) {
    this.value += x;
  }

  public String getName() {
    return this.name;
  }

  public int getValue() {
    return this.value;
  }
}
