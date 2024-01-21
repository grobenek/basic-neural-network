package szathmary.peter.mvc.view;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
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
import szathmary.peter.mvc.model.NetworkConfiguration;
import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObservable;
import szathmary.peter.neuralnetwork.csvreader.CsvReader;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public class MainWindow extends JFrame implements IMainWindow {
  public static final int NUMBER_OF_X_POINTS = 18;
  private final IController controller;
  private IErrorFunction errorFunction;
  private DefaultCategoryDataset dataset;
  private JFreeChart lineChart;
  private JPanel mainPanel;
  private ChartPanel chartPanel;
  private JButton trainNetworkButton;
  private JButton testNetworkButton;
  private JButton chooseTrainingDataButton;
  private JTextArea terminalTextArea;
  private JButton predictButton;
  private JButton chooseTestingDataButton;
  private JButton createNeuralNetworkButton;
  private JTextPane networkInformationTextPane;
  private JScrollPane terminalScrollPane;
  private JProgressBar progressBar;
  private JPanel chartJPanel;
  private List<Double> trainingErrors;
  private NetworkConfiguration neuralNetworkConfiguraion;

  public MainWindow(IController controller) {
    this.controller = controller;
    this.controller.attach(this);

    progressBar.setVisible(false);

    this.trainingErrors = new ArrayList<>();

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
          terminalTextArea.setText("");
          dataset.clear();

          InitializeNeuralNetworkDialog initializeNeuralNetworkDialog =
              new InitializeNeuralNetworkDialog(this);
        });

    chooseTrainingDataButton.addActionListener(
        actionEvent -> {
          ChooseDataDialog chooseDataDialog = new ChooseDataDialog(this);
        });
  }

  public void createUIComponents() {
    dataset = new DefaultCategoryDataset();

    lineChart =
        ChartFactory.createLineChart(
            "Train errors",
            "Epoch",
            "Error",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            false,
            false);

    chartPanel = new ChartPanel(lineChart);
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
          .append(" :  ")
          .append(errorFunction.getClass().getSimpleName())
          .append(" error = ")
          .append(trainingErrors.get(i))
          .append(System.lineSeparator());
    }

    terminalTextArea.setText(sb.toString());
  }

  private void redrawChart() {
    dataset.clear();

    int interval;
    if (trainingErrors.size() <= NUMBER_OF_X_POINTS) {
      interval = 1;
    } else {
      interval = trainingErrors.size() / (NUMBER_OF_X_POINTS - 1);
    }

    for (int i = 0; i < trainingErrors.size(); i++) {
      if (i % interval == 0 || i == trainingErrors.size() - 1) {
        dataset.addValue(trainingErrors.get(i), "Error", String.valueOf(i + 1));
      }
    }

    lineChart.fireChartChanged();
  }

  private void updateNeuralNetworkInformation() {
    networkInformationTextPane.setText(
        "Neural network information:\n" + neuralNetworkConfiguraion.toString());
  }

  @Override
  public void initializeNetwork(
      int numberOfInputNeurons,
      ActivationFunction inputLayerActivationFunction,
      int numberOfHiddenLayers,
      int[] hiddenLayersNumberOfNeurons,
      ActivationFunction[] hiddenLayersActivationFunctions,
      int numberOfOutputNeurons,
      ActivationFunction outputLayerActivationFunction) {

    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() {
            controller.initializeNetwork(
                numberOfInputNeurons,
                inputLayerActivationFunction,
                numberOfHiddenLayers,
                hiddenLayersNumberOfNeurons,
                hiddenLayersActivationFunctions,
                numberOfOutputNeurons,
                outputLayerActivationFunction);
            return null;
          }

          @Override
          protected void done() {
            try {
              get();
            } catch (InterruptedException | ExecutionException e) {
              showErrorMessage(e.getLocalizedMessage());
            }
          }
        };

    worker.execute();
  }

  @Override
  public void trainNetwork(int numberOfEpochs) {
    terminalTextArea.setText("Training network!");
    progressBar.setVisible(true);
    progressBar.repaint();

    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() {
            controller.trainNetwork(errorFunction, numberOfEpochs, Double.MIN_VALUE);
            return null;
          }

          @Override
          protected void done() {
            try {
              get();
              progressBar.setIndeterminate(false);
              progressBar.setVisible(false);
              progressBar.repaint();
            } catch (InterruptedException | ExecutionException e) {
              progressBar.setIndeterminate(false);
              progressBar.setVisible(false);
              progressBar.repaint();
              showErrorMessage(e.getLocalizedMessage());
            }
          }
        };

    worker.execute();
  }

  @Override
  public void testNetwork() {
    // TODO treba dat dialog na train info
  }

  @Override
  public void loadData(
      String filePath,
      int numberIfInputColumns,
      int numberOfOutputColumns,
      boolean hasHeader,
      String delimiter) {
    CsvReader csvReader = new CsvReader(filePath);
    csvReader.setHasHeader(hasHeader);
    csvReader.setDelimiter(delimiter);
    csvReader.readCsv(numberIfInputColumns, numberOfOutputColumns);

    double[][] inputs = csvReader.getInputDataArray();
    double[][] outputs = csvReader.getExpectedOutputArray();

    controller.setTrainingData(inputs, outputs);
  }

  @Override
  public void predict(double[] inputs) {
    SwingWorker<double[], Void> worker =
        new SwingWorker<>() {
          @Override
          protected double[] doInBackground() {
            return controller.predict(inputs);
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
              showErrorMessage(e.getLocalizedMessage());
            }
          }
        };

    worker.execute();
  }

  @Override
  public void setTrainingAlgorithm(double learningRate) {
    controller.setTrainingAlgorithm(learningRate);
  }

  @Override
  public void showErrorMessage(String message) {
    // setting maximum size - errors are often long
    JTextArea errorTextArea = new JTextArea(message);
    errorTextArea.setLineWrap(true);
    errorTextArea.setWrapStyleWord(true);
    errorTextArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(errorTextArea);
    scrollPane.setPreferredSize(new Dimension(500, 300));

    JOptionPane.showMessageDialog(this, scrollPane, "Nastala chyba :(", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void setErrorFunction(Class<IErrorFunction> selectedItem) {
    try {
      errorFunction = selectedItem.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      showErrorMessage(e.getLocalizedMessage());
    }
  }
}
