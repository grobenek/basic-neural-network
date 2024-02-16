package szathmary.peter.mvc.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import org.jfree.ui.ExtensionFileFilter;

public class ChooseDataDialog extends JDialog {
  private final IMainWindow mainWindow;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JFileChooser fileChooser;
  private JTextField numberOfInputsTextField;
  private JTextField numberOfOutputColumnsTextField;
  private JCheckBox hasHeaderCheckBox;
  private JTextField delimiterTextField;
  private JTextField trainTestTextField;

  public ChooseDataDialog(IMainWindow mainWindow) {
    this.mainWindow = mainWindow;

    fileChooser.setCurrentDirectory(new File("./"));
    ExtensionFileFilter csvFilter = new ExtensionFileFilter("CSV Files", "csv");
    fileChooser.addChoosableFileFilter(csvFilter);
    fileChooser.setFileFilter(csvFilter);
    fileChooser.setAcceptAllFileFilterUsed(false);

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

    setSize(600, 500);
    setAutoRequestFocus(true);
    setTitle("Choose data:");
    setLocationRelativeTo((Component) mainWindow);
    setVisible(true);
  }

  private void onOK() {
    mainWindow.loadData(
        fileChooser.getSelectedFile().getPath(),
        Integer.parseInt(numberOfInputsTextField.getText()),
        Integer.parseInt(numberOfOutputColumnsTextField.getText()),
        hasHeaderCheckBox.isSelected(),
        delimiterTextField.getText(),
        Double.parseDouble(trainTestTextField.getText()));

    dispose();
  }

  private void onCancel() {
    dispose();
  }
}
