import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * GUI for Bare Bones Interpreter
 */
public class BBIGUI extends JFrame {

  public static void main(String[] args) {
    new BBIGUI();
  }

  private final BBInterpreter interpreter;
  private JTextArea input;
  private JTextArea output;
  private JMenu fileMenu;
  private JButton runButton;
  private JButton stopButton;
  private Thread interpreterThread;

  public BBIGUI() {
    interpreter = new BBInterpreter(); // creates a new interpreter
    this.setTitle("Bare Bones Interpreter");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = createPanel();
    this.add(panel);
    this.pack();
    this.setVisible(true);
  }

  private JPanel createPanel() {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(1000, 600));
    panel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.NORTHWEST;

    createMenuBar(panel, gbc);

    createInputArea(panel, gbc);

    createOutputArea(panel, gbc);

    return panel;
  }

  private void createMenuBar(JPanel panel,
      GridBagConstraints gbc) { //menu bar for opening files and running code
    JMenuBar menuBar = new JMenuBar();

    fileMenu = new JMenu("File");

    JMenuItem openButton = new JMenuItem("Open"); // creates a button to open a file
    openButton.addActionListener(e -> { // when the button is clicked
      String fileName = StrawmaUtils.getFileFromDialog("Select a Text File to Interpret",
          System.getProperty("user.home") + "\\Downloads", "txt"); // lets the user choose a file
      try {
        String code = Files.readString(Path.of(fileName));
        input.setText(code); // sets the text area to the code
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    JMenuItem saveAsButton = new JMenuItem("Save as"); // creates a button to save a file
    saveAsButton.addActionListener(e -> {
      StrawmaUtils.saveFileUsingDialog(input.getText(), "Save Output",
          System.getProperty("user.home") + "\\Downloads", "txt");
    });

    fileMenu.add(openButton);
    fileMenu.add(saveAsButton);

    JLabel spacing = new JLabel();
    spacing.setText("     ");

    runButton = new JButton("Run Code"); //creates a button to run code
    runButton.addActionListener(e -> {
      interpreter.setCode(input.getText());
      interpreterThread = new Thread (() -> { //runs the code on a new thread, prevents GUI from freezing
        toggleComponents();
        interpreter.InterpretCode(output);
        toggleComponents();
      });
      interpreterThread.start();
    });

    stopButton = new JButton("Stop"); //creates a button to stop code
    stopButton.addActionListener(e -> {
      interpreterThread.interrupt();
      toggleComponents();
      output.setText("Stopped.");
    });
    stopButton.setVisible(false);


    menuBar.add(fileMenu);
    menuBar.add(spacing);
    menuBar.add(runButton);
    menuBar.add(stopButton);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.weighty = 0;
    gbc.weightx = 1;
    panel.add(menuBar, gbc);
  }

  private void createInputArea(JPanel panel, GridBagConstraints gbc) { //text area for code input
    JScrollPane scrollPane = new JScrollPane();

    input = new JTextArea();
    scrollPane.setViewportView(input);

    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weighty = 1;
    gbc.weightx = 1;
    panel.add(scrollPane, gbc);
  }

  private void createOutputArea(JPanel panel, GridBagConstraints gbc) { //text area for code output
    JScrollPane scrollPane = new JScrollPane();

    output = new JTextArea();
    output.setEditable(false);
    output.setBackground(Color.darkGray);
    output.setForeground(Color.white);
    scrollPane.setViewportView(output);

    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weighty = 1;
    gbc.weightx = 1;
    panel.add(scrollPane, gbc);
  }

  private void toggleComponents() { //toggles certain components when running code
    fileMenu.setEnabled(!fileMenu.isEnabled());
    runButton.setVisible(!runButton.isVisible());
    input.setEnabled(!input.isEnabled());
    stopButton.setVisible(!stopButton.isVisible());
  }
}
