import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StrawmaUtils { //collection of useful methods

  public static String readFromConsole() { // read a line from the console
    BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in)); // create a buffered reader to read from the console
    try { // try to read a line from the console
      return br.readLine();
    } catch (IOException e) { // if there is an error, throw a runtime exception
      throw new RuntimeException(e);
    }
  }

  public static String getFileFromDialog(String dialogText, String defaultPath, String fileType) {
    //lets user choose a text file from downloads
    FileDialog dialog = new FileDialog((Frame) null, dialogText);
    if (fileType != null) {
      dialog.setFile("*." + fileType);
    }
    dialog.setDirectory(System.getProperty("user.home") + "\\Downloads");
    dialog.setMode(FileDialog.LOAD);
    dialog.setVisible(true);
    return dialog.getDirectory() + "\\" + dialog.getFile();
  }

  public static String getFileFromDialog() {
    return getFileFromDialog("Select a File", System.getProperty("user.home") + "\\Downloads",
        null);
  }

  public static String getFileFromDialog(String dialogText) {
    return getFileFromDialog(dialogText, System.getProperty("user.home") + "\\Downloads", null);
  }

  public static String getFileFromDialog(String dialogText, String defaultPath) {
    return getFileFromDialog(dialogText, defaultPath, null);
  }


}
