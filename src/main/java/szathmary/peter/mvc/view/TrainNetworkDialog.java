package szathmary.peter.mvc.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrainNetworkDialog extends JDialog {
  private final IMainWindow mainWindow;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField numberOfEpochsTextField;

  public TrainNetworkDialog(IMainWindow mainWindow) {
    this.mainWindow = mainWindow;

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

    setSize(400, 200);
    setAutoRequestFocus(true);
    setTitle("Training configuration");
    setLocationRelativeTo((Component) mainWindow);
    setVisible(true);
  }

  private void onOK() {
    dispose();
    mainWindow.trainNetwork(Integer.parseInt(numberOfEpochsTextField.getText()));
  }

  private void onCancel() {
    dispose();
  }
}
