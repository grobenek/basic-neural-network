package szathmary.peter.mvc.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PredictDataDialog extends JDialog {
  private final IMainWindow mainWindow;
  private final int numberOfInputs;
  private final List<JTextField> inputFields;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JPanel inputPanel;

  public PredictDataDialog(IMainWindow mainWindow, int numberOfInputs) {
    this.mainWindow = mainWindow;
    this.numberOfInputs = numberOfInputs;
    this.inputFields = new ArrayList<>(numberOfInputs);

    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            onCancel();
          }
        });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(
        e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    SwingUtilities.invokeLater(
        () -> {
          for (int i = 0; i < numberOfInputs; i++) {
            inputPanel.add(new JLabel(i + 1 + " input"));
            JTextField textField = new JTextField();
            inputPanel.add(textField);

            inputFields.add(textField);
          }
        });

    setSize(400, 200);
    setAutoRequestFocus(true);
    setTitle("Predict data");
    setLocationRelativeTo((Component) mainWindow);
    setVisible(true);
  }

  private void onOK() {
    double[] inputs = inputFields.stream().mapToDouble(field -> Double.parseDouble(field.getText())).toArray();

    dispose();

    mainWindow.predict(inputs);
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  private void createUIComponents() {
    inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout(0, 2)); // Set a layout
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);
  }
}
