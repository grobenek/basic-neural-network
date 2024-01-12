package szathmary.peter.mvc.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import szathmary.peter.mvc.controller.IController;
import szathmary.peter.mvc.model.IModel;
import szathmary.peter.mvc.model.NetworkConfiguration;
import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObservable;
import szathmary.peter.neuralnetwork.csvreader.CsvReader;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.errorfunctions.Mse;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public class MainWindow extends JFrame implements IMainWindow {
  private final IErrorFunction errorFunction = new Mse();
  private final IController controller;
  private final IModel model;
  private DefaultCategoryDataset dataset;
  private JFreeChart lineChart;
  private JPanel mainPanel;
  private ChartPanel chartPanel;
  private JComboBox<ActivationFunction> inputLayerActivationFunctionComboBox;
  private JTextField inputLayerNumberOfNeuronsInput;
  private JTextField outputLayerNumberOfNeuronsTextField;
  private JComboBox<ActivationFunction> outputLayerActivationFunctionComboBox;
  private JButton trainNetworkButton;
  private JButton testNetworkButton;
  private JButton chooseTrainingDataButton;
  private JPanel hiddenLayersJPanel;
  private JTextArea terminalTextArea;
  private JButton predictButton;
  private JTextField textField1;
  private JButton chooseTestingDataButton;
  private JButton createNeuralNetworkButton;
  private JTextPane networkInformationTextPane;
  private JScrollPane terminalScrollPane;
  private JPanel chartJPanel;
  private List<Double> trainingErrors;
  private NetworkConfiguration neuralNetworkConfiguraion;

  public MainWindow(IController controller, IModel model) {
    this.controller = controller;
    this.controller.attach(this);
    this.model = model;

    this.trainingErrors = new ArrayList<>();

    fillComboBoxes(
        List.of(inputLayerActivationFunctionComboBox, outputLayerActivationFunctionComboBox));

    setContentPane(mainPanel);
    setTitle("Simple neural network");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1400, 900);
    setLocationRelativeTo(null);
    setVisible(true);
    trainNetworkButton.addActionListener(
        e -> {
          TrainNetworkDialog trainNetworkDialog = new TrainNetworkDialog(this);
        });

    predictButton.addActionListener(
        actionEvent -> {
          PredictDataDialog predictDataDialog =
              new PredictDataDialog(this, controller.getNumberOfInputs());
        });

    createNeuralNetworkButton.addActionListener(
        actionEvent -> {
          initializeNetwork();
        });

    chooseTrainingDataButton.addActionListener(
        actionEvent -> {
          loadData();
        });
  }

  private void fillComboBoxes(List<JComboBox<ActivationFunction>> comboBoxes) {
    for (ActivationFunction function : ActivationFunction.values()) {
      for (JComboBox<ActivationFunction> comboBox : comboBoxes) {
        comboBox.addItem(function);
      }
    }
  }

  public void createUIComponents() {
    this.dataset = new DefaultCategoryDataset();

    this.lineChart =
        ChartFactory.createLineChart(
            "Train errors",
            "Epoch",
            "Error",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false);

    this.chartPanel = new ChartPanel(lineChart);
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof INeuralNetworkObservable)) {
      return;
    }

    this.trainingErrors = ((INeuralNetworkObservable) observable).getErrors();
    Optional<NetworkConfiguration> neuralNetworkConfiguration =
        ((INeuralNetworkObservable) observable).getNeuralNetworkConfiguration();

    if (neuralNetworkConfiguration.isPresent()
        && neuralNetworkConfiguration.get() != this.neuralNetworkConfiguraion) {
      neuralNetworkConfiguraion = neuralNetworkConfiguration.get();
      updateNeuralNetworkInformation();
    }

    writeErrorsToTerminal();
    redrawChart();
  }

  private void writeErrorsToTerminal() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < trainingErrors.size(); i++) {
      sb.append("Epoch ")
          .append(i + 1)
          .append(" :  Average error = ")
          .append(trainingErrors.get(i))
          .append(System.lineSeparator());
    }

    terminalTextArea.setText(sb.toString());
  }

  private void redrawChart() {
    dataset.clear();

    for (int i = 0; i < trainingErrors.size(); i++) {
      dataset.addValue(trainingErrors.get(i), "row 0", String.valueOf(i));
    }

    lineChart.fireChartChanged();
  }

  private void updateNeuralNetworkInformation() {
    networkInformationTextPane.setText(
        "Neural network information:\n" + neuralNetworkConfiguraion.toString());
  }

  @Override
  public void initializeNetwork() {
    model.initializeNetwork(
        new NetworkConfiguration(
            1,
            ActivationFunction.IDENTITY,
            1,
            new int[] {2},
            new ActivationFunction[] {ActivationFunction.TANH},
            1,
            ActivationFunction.IDENTITY));
  }

  @Override
  public void trainNetwork(int numberOfEpochs) {
    terminalTextArea.setText("Training network!");

    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() {
            model.trainNetwork(errorFunction, numberOfEpochs, Double.MIN_VALUE);
            return null;
          }
        };

    worker.execute();
  }

  @Override
  public void testNetwork() {
    // TODO treba dat dialog na train info
  }

  @Override
  public void loadData() {
    CsvReader csvReader = new CsvReader("sin_data.csv");
    double[][] data = csvReader.readCsv();
    System.out.println(data[0].length);

    double[][] inputs = new double[data[0].length][1];
    double[][] outputs = new double[data[1].length][1];

    for (int i = 0; i < data[0].length; i++) {
      inputs[i][0] = data[0][i];
      outputs[i][0] = data[1][i];
    }

    controller.setTrainingData(inputs, outputs);
  }

  @Override
  public void predict(double[] inputs) {
    SwingWorker<double[], Void> worker =
        new SwingWorker<>() {
          @Override
          protected double[] doInBackground() {
            return model.predict(inputs);
          }

          @Override
          protected void done() {
            try {
              double[] result = get();

              SwingUtilities.invokeLater(
                  () ->
                      terminalTextArea.append(
                          "Result for prediction "
                              + Arrays.toString(inputs)
                              + " : "
                              + Arrays.toString(result)
                              + "\n"));

              // scroll to bottom
              JScrollBar verticalScrollBar = terminalScrollPane.getVerticalScrollBar();
              verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
  }
}
