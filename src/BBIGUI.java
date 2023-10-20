import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BBIGUI extends JFrame {

  public static void main(String[] args) {
    new BBIGUI();
  }

  private JTextArea input;
  private JTextArea output;
  public BBIGUI() {
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

    createTextArea(panel, gbc);

    createOutputArea(panel, gbc);

    return panel;
  }

  private void createMenuBar(JPanel panel, GridBagConstraints gbc) {
    JMenuBar menuBar = new JMenuBar();

    JButton openButton = new JButton("Open File"); // creates a button to open a file
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

    JButton runButton = new JButton("Run Code");
    runButton.addActionListener(e -> {
      BBInterpreter interpreter = new BBInterpreter();
      output.setText(interpreter.InterpretCode(input.getText()));
    });

    menuBar.add(openButton);
    menuBar.add(runButton);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.weighty = 0;
    gbc.weightx = 1;
    panel.add(menuBar, gbc);
  }

  private void createTextArea(JPanel panel, GridBagConstraints gbc) {
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

  private void createOutputArea(JPanel panel, GridBagConstraints gbc) {
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

}
