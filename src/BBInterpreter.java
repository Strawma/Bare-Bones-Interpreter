import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Interprets Barebones code from a text file.
 */
public class BBInterpreter {

  // Barebones Methods explained:
  // clear x: sets the variable x to 0
  // incr x: adds 1 to the variable x
  // decr x: subtracts 1 from the variable x
  // while x not 0 do: starts a while loop that will run until the variable x is 0
  // end: ends a while loop

  enum Method { // enum for BB methods
    CLEAR, INCR, DECR, WHILE, END, ERROR
  }

  private List<BBVariable> variables; // list of variables
  private Stack<Integer> whileStack; // stack for while loops
  private String code; // the code to interpret

  /**
   * Interprets the code from a String.
   *
   */
  public String InterpretCode() { // interprets code string
    String output = "";
    variables = new ArrayList<>(); // creates new list of variables
    whileStack = new Stack<>(); // creates new stack for while loops
    code = code.replaceAll(" +", " "); // removes extra spaces
    code = code.replaceAll("\n", ""); // removes line breaks
    String[] lines = code.split(";"); // splits the code into lines
    int lineNum = 1;
    while (lineNum > 0 && lineNum < lines.length + 1) { // for each line
      lineNum = interpretLine(lines, lineNum); // interpret the line
      output += (printVariables() + "\n"); // print the variables
    }
    if (lineNum < 0) {
      output += ("Error: Invalid line number " + -lineNum + ".");
    }
    return output;
  }

  private int interpretLine(String[] lines, int lineNum) { //reads a single line
    String[] words = lines[lineNum - 1].trim().split(" "); // splits the line into words
    Method method = getMethodFromWords(words); // gets the method from the words
    switch (method) { // interprets the method
      case CLEAR -> { // if the method is clear, clear the variable
        clear(words[1]);
        return lineNum + 1;
      }
      case INCR -> { // if the method is incr, add 1 to the variable
        addValue(words[1], 1);
        return lineNum + 1;
      }
      case DECR -> { // if the method is decr, subtract 1 from the variable
        addValue(words[1], -1);
        return lineNum + 1;
      }
      case WHILE -> { // if the method is while, check if the variable is equal to the value
        BBVariable variable = ensureExists(words[1]);
        if (variable.getValue() != Integer.parseInt(
            words[3])) { // variable not equal to the value, skip to the end of the loop
          whileStack.push(lineNum);
          return lineNum + 1;
        } else { // variable is equal to the value, skip to the end of the while loop
          return skipToEnd(lines, lineNum + 1);
        }
      }
      case END -> {
        if (!whileStack.isEmpty()) {
          return whileStack.pop();
        }
      }
      case ERROR -> {
        return -lineNum;
      }
    }
    throw new RuntimeException("how?");
  }

  private void clear(String variableName) { // clears the value of a variable
    BBVariable variable = ensureExists(variableName);
    variable.clear();
  }

  private void addValue(String variableName, int x) { // change the value of a variable
    BBVariable variable = ensureExists(variableName);
    variable.addValue(x);
  }

  private int skipToEnd(String[] lines, int lineNum) { // skips to the end of a while loop
    int whileCount = 1;
    while (whileCount > 0) { // while there are still while loops to go through
      String[] words = lines[lineNum - 1].trim().split(" "); // splits the line into words
      Method method = getMethodFromWords(words); // gets the method from the words
      switch (method) {
        case WHILE -> whileCount++;
        case END -> whileCount--;
        case ERROR -> {
          return -lineNum;
        }
      }
      lineNum++;
    }
    return lineNum; // if the while loop is valid, return the line number to continue
  }

  private BBVariable ensureExists(String variableName) { // ensures that a variable exists
    for (BBVariable variable : variables) {
      if (variable.getName().equals(variableName)) {
        return variable; // if the variable exists, return it
      }
    }
    BBVariable variable = new BBVariable(
        variableName); // if the variable does not exist, creates it
    variables.add(variable);
    return variable;
  }

  private boolean validVariableName(String variableName) { // checks if a variable name is valid
    if (!Character.isJavaIdentifierPart(variableName.charAt(0))) {
      return false; // if the first character is not a valid character, return false
    }
    for (int i = 1; i < variableName.length(); i++) {
      if (!Character.isJavaIdentifierPart(variableName.charAt(i))) {
        return false; // if any other character is not a valid character, return false
      }
    }
    return true; // if all characters are valid, return true
  }

  private String printVariables() { // prints the variables
    String output = "";
    for (BBVariable variable : variables) {
      output += (variable.getName() + ": " + variable.getValue() + " ");
    }
    return output;
  }

  private boolean isInteger(String input) { // checks if a string is an integer
    try {
      Integer.parseInt(input);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Method getMethodFromWords(String[] words) { // gets the BB method from words in a line
    if (words.length == 2 && validVariableName(words[1])) {
      switch (words[0]) { //checks for clear, incr, and decr methods
        case "clear" -> {
          return Method.CLEAR;
        }
        case "incr" -> {
          return Method.INCR;
        }
        case "decr" -> {
          return Method.DECR;
        }
      }
    } else if (words.length == 5 && words[0].equals("while")
        && words[2].equals("not") && words[4].equals("do") && validVariableName(words[1])
        && isInteger(words[3])) { //checks for while method
      return Method.WHILE;
    } else if (words.length == 1 && words[0].equals("end")) { //checks for end method
      return Method.END;
    }
    return Method.ERROR; // if the line is not valid, return error
  }

  public void setCode(String code) {
    this.code = code;
  }
}