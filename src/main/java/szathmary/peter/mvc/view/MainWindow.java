package szathmary.peter.mvc.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
  private JButton chooseDataButton;
  private JPanel hiddenLayersJPanel;
  private JTextPane terminalTextPane;
  private JButton predictButton;
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
          terminalTextPane.setText("Training network!");
          SwingWorker<Void, Void> worker =
              new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                  model.initializeNetwork(
                      new NetworkConfiguration(
                          1,
                          ActivationFunction.IDENTITY,
                          1,
                          new int[] {2},
                          new ActivationFunction[] {ActivationFunction.TANH},
                          1,
                          ActivationFunction.IDENTITY));

                  CsvReader csvReader = new CsvReader("sin_data.csv");
                  double[][] data = csvReader.readCsv();
                  System.out.println(data[0].length);

                  IErrorFunction errorFunction = new Mse();

                  double[][] inputs = new double[data[0].length][1];
                  double[][] outputs = new double[data[1].length][1];

                  for (int i = 0; i < data[0].length; i++) {
                    inputs[i][0] = data[0][i];
                    outputs[i][0] = data[1][i];
                  }

                  model.trainNetwork(errorFunction, inputs, outputs, 100, Double.MIN_VALUE);
                  return null;
                }
              };

          worker.execute();
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
          .append(i)
          .append(" :  Average error = ")
          .append(trainingErrors.get(i))
          .append(System.lineSeparator());
    }

    double predictionInput = 0.618;
    sb.append("PREDICTION FOR NUMBER ").append(predictionInput).append(": ").append(Arrays.toString(model.predict(new double[]{predictionInput})));

    terminalTextPane.setText(sb.toString());
  }

  private void redrawChart() {
    dataset.clear();

    for (int i = 0; i < trainingErrors.size(); i++) {
      dataset.addValue(trainingErrors.get(i), "row 0", String.valueOf(i));
    }

    lineChart.fireChartChanged();
  }

  private void updateNeuralNetworkInformation() {
    // TODO
  }

  @Override
  public void initializeNetwork() {
    // TODO
  }

  @Override
  public void trainNetwork() {
    // TODO treba dat dialog na train info
  }

  @Override
  public void testNetwork() {
    // TODO treba dat dialog na train info
  }

  @Override
  public void loadData() {
    // TODO dialog kde sa vyberie file
  }
}
