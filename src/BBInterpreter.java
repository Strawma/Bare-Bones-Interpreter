import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class BBInterpreter {

  enum Method {
    CLEAR, INCR, DECR, WHILE, END
  }

  public static void main(String[] args) {
    BBInterpreter interpreter = new BBInterpreter();

    //lets user choose a text file from downloads
    final String DEFAULTPATH = System.getProperty("user.home") + "\\Downloads";
    FileDialog dialog = new FileDialog((Frame)null, "Select a Text File to Interpret");
    dialog.setFile("*.txt");
    dialog.setDirectory(DEFAULTPATH);
    dialog.setMode(FileDialog.LOAD);
    dialog.setVisible(true);
    String fileName = dialog.getDirectory()+"\\"+dialog.getFile();
    dialog.dispose();
    System.out.println(fileName);

    String code = "";
    try {
      code = Files.readString(Path.of(fileName));
      System.out.println(code);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

/*    String code = "clear X;\n"
        + "incr X;\n"
        + "incr X;\n"
        + "clear Y;\n"
        + "incr Y;\n"
        + "incr Y;\n"
        + "incr Y;\n"
        + "clear Z;\n"
        + "while X not 0 do;\n"
        + "   clear W;\n"
        + "   while Y not 0 do;\n"
        + "      incr Z;\n"
        + "      incr W;\n"
        + "      decr Y;\n"
        + "   end;\n"
        + "   while W not 0 do;\n"
        + "      incr Y;\n"
        + "      decr W;\n"
        + "   end;\n"
        + "   decr X;\n"
        + "end;";*/
    interpreter.InterpretCode(code);
  }

  private List<BBVariable> variables = new ArrayList<BBVariable>(); // list of variables
  private Stack<Integer> whileStack = new Stack<Integer>(); // stack for while loops

  public void InterpretCode(String code) { // interprets the code
    code = code.replaceAll(" +", " "); // removes extra spaces
    code = code.replaceAll("\n", ""); // removes line breaks
    String[] lines = code.split(";"); // splits the code into lines
    int lineNum = 0;
    while (lineNum > -1 && lineNum < lines.length) { // for each line
      lineNum = interpretLine(lines, lineNum); // interpret the line
      printVariables(); // print the variables
    }
  }

  private int interpretLine(String[] lines, int lineNum) { //reads a single line
    String[] words = lines[lineNum].trim().split(" "); // splits the line into words
    if (words.length == 2) {
      if (words[0].equals("clear")) {
        clear(words[1]);
        return lineNum + 1;
      } else if (words[0].equals("incr")) {
        addValue(words[1], 1);
        return lineNum + 1;
      } else if (words[0].equals("decr")) {
        addValue(words[1], -1);
        return lineNum + 1;
      }
    } else if (words.length == 5 && words[0].equals("while")
        && words[2].equals("not") && words[4].equals("do")) {
      BBVariable variable = ensureExists(words[1]);
      if (variable.getValue() != Integer.parseInt(words[3])) {
        whileStack.push(lineNum);
        return lineNum + 1;
      } else {
        return skipToEnd(lines, lineNum+1);
      }
    } else if (words.length == 1 && words[0].equals("end") && !whileStack.isEmpty()) {
      return whileStack.pop();
    }

    System.out.println("Error: Invalid line number " + lineNum + ".");
    return -1;
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
      String[] words = lines[lineNum].trim().split(" "); // splits the line into words
      if (words.length == 5 && words[0].equals("while") && words[2].equals("not")
          && words[4].equals("do")) { // if a while loop is reached
        whileCount++;
      } else if (words.length == 1 && words[0].equals(
          "end")) { // if the end of a while loop is reached
        whileCount--;
      } else if (words.length != 2 || (!words[0].equals("clear") && !words[0].equals("incr")
          && !words[0].equals("decr"))) {
        return -1; // if the line is not a valid line, return the line number to process error
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

  private void printVariables() { // prints the variables
    for (BBVariable variable : variables) {
      System.out.print(variable.getName() + ": " + variable.getValue() + " ");
    }
    System.out.println();
  }
}