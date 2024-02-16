package szathmary.peter.mvc.view;

import java.awt.*;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public class InitializeNeuralNetworkDialog extends JDialog {
  private final IMainWindow mainWindow;
  private Vector<JComboBox<ActivationFunction>> hiddenLayerComboBoxes;
  private Vector<JTextField> hiddenLayerNumberOfNeuronsTextFields;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JPanel configurationPanel;
  private JTextField inputLayerTextField;
  private JTextField outputLayerTextField;
  private JComboBox<ActivationFunction> inputLayerActivationFunctionComboBox;
  private JComboBox<ActivationFunction> outputLayerActivationFunctionComboBox;
  private JSpinner hiddenLayersSpinner;
  private JPanel hiddenLayersPanel;
  private JScrollPane scrollPane;

  public InitializeNeuralNetworkDialog(IMainWindow mainWindow) {
    this.mainWindow = mainWindow;

    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK());
    buttonCancel.addActionListener(e -> onCancel());

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setLocationRelativeTo((Component) mainWindow);
    setVisible(true);
  }

  private void onOK() {
    int numberOfInputNeurons = Integer.parseInt(inputLayerTextField.getText());
    ActivationFunction inputLayerActivationFunction =
        (ActivationFunction) inputLayerActivationFunctionComboBox.getSelectedItem();

    int numberOfHiddenLayers = (Integer) hiddenLayersSpinner.getValue();
    int[] hiddenLayerNumberOfNeurons =
        hiddenLayerNumberOfNeuronsTextFields.stream()
            .mapToInt(textField -> Integer.parseInt(textField.getText()))
            .toArray();
    ActivationFunction[] hiddenLayerActivationFunctions =
        hiddenLayerComboBoxes.stream()
            .map(comboBox -> (ActivationFunction) comboBox.getSelectedItem())
            .toArray(ActivationFunction[]::new);

    int numberOfOutputNeurons = Integer.parseInt(outputLayerTextField.getText());
    ActivationFunction outputLayerActivationFunction =
        (ActivationFunction) outputLayerActivationFunctionComboBox.getSelectedItem();

    mainWindow.initializeNetwork(
        numberOfInputNeurons,
        inputLayerActivationFunction,
        numberOfHiddenLayers,
        hiddenLayerNumberOfNeurons,
        hiddenLayerActivationFunctions,
        numberOfOutputNeurons,
        outputLayerActivationFunction);

    dispose();
  }

  private void onCancel() {
    dispose();
  }

  private void updateHiddenLayersPanel() {
    hiddenLayersPanel.removeAll();
    hiddenLayerNumberOfNeuronsTextFields.clear();
    hiddenLayerComboBoxes.clear();

    int numHiddenLayers = (Integer) hiddenLayersSpinner.getValue();
    hiddenLayersPanel.setLayout(new GridLayout(0, 1));
    for (int i = 0; i < numHiddenLayers; i++) {
      hiddenLayersPanel.add(new JLabel("Number of neurons in hidden layer #" + (i + 1)));
      JTextField numberOfNeuronsTextField = new JTextField();
      hiddenLayersPanel.add(numberOfNeuronsTextField);
      hiddenLayersPanel.add(new JLabel("Activation function for hidden layer #" + (i + 1)));
      JComboBox<ActivationFunction> activationFunctionComboBox = createActivationFunctionComboBox();
      hiddenLayersPanel.add(activationFunctionComboBox);
      hiddenLayersPanel.add(new JSeparator(0));

      hiddenLayerComboBoxes.add(activationFunctionComboBox);
      hiddenLayerNumberOfNeuronsTextFields.add(numberOfNeuronsTextField);
    }
    hiddenLayersPanel.revalidate();
    hiddenLayersPanel.repaint();
    pack();
  }

  private JComboBox<ActivationFunction> createActivationFunctionComboBox() {
    return new JComboBox<>(new Vector<>(List.of(ActivationFunction.values())));
  }

  private void createUIComponents() {
    hiddenLayerComboBoxes = new Vector<>();
    hiddenLayerNumberOfNeuronsTextFields = new Vector<>();

    configurationPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    // input layer components
    configurationPanel.add(new JLabel("Number of neurons in input layer"), gbc);
    inputLayerTextField = new JTextField();
    inputLayerTextField.setText("1");
    configurationPanel.add(inputLayerTextField, gbc);

    configurationPanel.add(new JLabel("Activation function for input layer"), gbc);
    inputLayerActivationFunctionComboBox = createActivationFunctionComboBox();
    configurationPanel.add(inputLayerActivationFunctionComboBox, gbc);

    // output layer components
    configurationPanel.add(new JLabel("Number of neurons in output layer"), gbc);
    outputLayerTextField = new JTextField();
    outputLayerTextField.setText("1");
    configurationPanel.add(outputLayerTextField, gbc);

    configurationPanel.add(new JLabel("Activation function for output layer"), gbc);
    outputLayerActivationFunctionComboBox = createActivationFunctionComboBox();
    configurationPanel.add(outputLayerActivationFunctionComboBox, gbc);

    configurationPanel.add(new JLabel("Hidden Layers:"), gbc);
    hiddenLayersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    hiddenLayersSpinner.addChangeListener(e -> updateHiddenLayersPanel());
    configurationPanel.add(hiddenLayersSpinner, gbc);

    // hidden layers panel setup for scrolling
    hiddenLayersPanel = new JPanel();
    hiddenLayersPanel.setLayout(new BoxLayout(hiddenLayersPanel, BoxLayout.Y_AXIS));
    scrollPane = new JScrollPane(hiddenLayersPanel);
    scrollPane.setPreferredSize(new Dimension(350, 200));
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
    verticalScrollBar.setUnitIncrement(16);

    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1;
    configurationPanel.add(scrollPane, gbc);

    updateHiddenLayersPanel();
  }
}
