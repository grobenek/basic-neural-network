package szathmary.peter.mvc.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import szathmary.peter.mvc.view.constant.ErrorFunction;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;

public class TrainNetworkDialog extends JDialog {
  public static final String ERROR_FUNCTIONS_PACKAGE_NAME =
      "szathmary.peter.neuralnetwork.errorfunctions";
  private final IMainWindow mainWindow;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField numberOfEpochsTextField;
  private JTextField learningRateTextField;
  private JComboBox<String> errorFunctionComboBox;

  public TrainNetworkDialog(IMainWindow mainWindow) {
    this.mainWindow = mainWindow;

    for (ErrorFunction errorFunction : ErrorFunction.values()) {
      errorFunctionComboBox.addItem(errorFunction.name());
    }

    errorFunctionComboBox.repaint();

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

    try {
      mainWindow.setErrorFunction(
          (Class<IErrorFunction>)
              Class.forName(
                  ERROR_FUNCTIONS_PACKAGE_NAME
                      .concat(".")
                      .concat((String) errorFunctionComboBox.getSelectedItem())));
      mainWindow.setTrainingAlgorithm(Double.parseDouble(learningRateTextField.getText()));
      mainWindow.trainNetwork(Integer.parseInt(numberOfEpochsTextField.getText()));
    } catch (ClassNotFoundException e) {
      mainWindow.showErrorMessage(e.getLocalizedMessage());
    }
  }

  private void onCancel() {
    dispose();
  }
}
