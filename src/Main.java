import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    BBIGUI gui = new BBIGUI();
    BBInterpreter interpreter = new BBInterpreter();

    System.out.println("Welcome to my Bare Bones Interpreter!");
    String choice;

    System.out.println("What would you like to do?\n"
        + "1: Choose a Text File to Interpret\n"
        + "2: Quit Program");
    choice = StrawmaUtils.readFromConsole();
    switch (choice) {
      case "1": {
        String fileName = StrawmaUtils.getFileFromDialog("Select a Text File to Interpret",
            System.getProperty("user.home") + "\\Downloads", "txt");
        String code;
        try {
          code = Files.readString(Path.of(fileName));
          System.out.println(code);
          System.out.println();
          interpreter.InterpretCode(code);
        } catch (IOException e) {
          System.out.println("Error: File not found.");
        }
      }
      case "2":
        System.exit(0);
      default:
        System.out.println("Invalid choice!");
    }
  }
}
