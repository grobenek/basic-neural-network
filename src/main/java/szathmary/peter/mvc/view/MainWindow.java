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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import szathmary.peter.mvc.controller.IController;
import szathmary.peter.mvc.model.NetworkConfiguration;
import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObservable;
import szathmary.peter.neuralnetwork.csvreader.CsvReader;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public class MainWindow extends JFrame implements IMainWindow {
  private final IController controller;
  private IErrorFunction errorFunction;
  private XYSeriesCollection dataset;
  private JFreeChart lineChart;
  private JPanel mainPanel;
  private ChartPanel chartPanel;
  private JButton trainNetworkButton;
  private JButton chooseDataButton;
  private JTextArea terminalTextArea;
  private JButton predictButton;
  private JButton createNeuralNetworkButton;
  private JTextPane networkInformationTextPane;
  private JScrollPane terminalScrollPane;
  private JProgressBar progressBar;
  private JLabel networkTrainingLabel;
  private List<Double> trainingErrors;
  private List<Double> testingErrors;
  private int bestWeightsEpoch;
  private NetworkConfiguration neuralNetworkConfiguration;

  public MainWindow(IController controller) {
    this.controller = controller;
    this.controller.attach(this);

    progressBar.setVisible(false);
    networkTrainingLabel.setVisible(false);

    this.trainingErrors = new ArrayList<>();
    this.testingErrors = new ArrayList<>();

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
          SwingUtilities.invokeLater(this::resetGui);

          InitializeNeuralNetworkDialog initializeNeuralNetworkDialog =
              new InitializeNeuralNetworkDialog(this);
        });

    chooseDataButton.addActionListener(
        actionEvent -> {
          ChooseDataDialog chooseDataDialog = new ChooseDataDialog(this);
        });
  }

  public void createUIComponents() {
    dataset = new XYSeriesCollection();

    lineChart =
        ChartFactory.createXYLineChart(
            "training and testing errors",
            "Epoch",
            "Error",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false);

    chartPanel = new ChartPanel(lineChart);
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof INeuralNetworkObservable)) {
      return;
    }

    trainingErrors = ((INeuralNetworkObservable) observable).getTrainingErrors();

    testingErrors = ((INeuralNetworkObservable) observable).getTestingErrors();

    bestWeightsEpoch = ((INeuralNetworkObservable) observable).getBestWeightsEpoch();

    Optional<NetworkConfiguration> neuralNetworkConfiguration =
        ((INeuralNetworkObservable) observable).getNeuralNetworkConfiguration();

    if (neuralNetworkConfiguration.isPresent()
        && neuralNetworkConfiguration.get() != this.neuralNetworkConfiguration) {
      this.neuralNetworkConfiguration = neuralNetworkConfiguration.get();
      updateNeuralNetworkInformation();
    }

    writeErrorsToTerminal();
    updateProgressBar(((INeuralNetworkObservable) observable).getPercentageOfCompletedTraining());
    redrawChart();
  }

  private void updateProgressBar(double percentageOfCompletedTraining) {
    SwingUtilities.invokeLater(
        () -> {
          progressBar.setValue((int) percentageOfCompletedTraining);
          progressBar.repaint();
        });
  }

  private void resetGui() {
    trainingErrors.clear();
    testingErrors.clear();
    progressBar.setValue(0);
    progressBar.repaint();
    terminalTextArea.setText("");
    redrawChart();
  }

  private void writeErrorsToTerminal() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < trainingErrors.size(); i++) {
      sb.append("Epoch ")
          .append(i + 1)
          .append(" : training  ")
          .append(errorFunction.getClass().getSimpleName())
          .append(" error = ")
          .append(trainingErrors.get(i))
          .append(System.lineSeparator());
    }

    SwingUtilities.invokeLater(
        () -> {
          terminalTextArea.setText(sb.toString());
          scrollTerminalToBottom();
        });
  }

  private void redrawChart() {
    dataset.removeAllSeries();

    XYSeries trainingErrorsSeries = new XYSeries("Training errors");
    XYSeries testingErrorsSeries = new XYSeries("Testing errors");

    for (int i = 0; i < trainingErrors.size(); i++) {
      trainingErrorsSeries.add((Double) (i + 1.0), trainingErrors.get(i));
      testingErrorsSeries.add((Double) (i + 1.0), testingErrors.get(i));
    }

    if (bestWeightsEpoch >= 1 && bestWeightsEpoch <= trainingErrors.size()) {
      XYSeries bestWeightsLine = new XYSeries("Best Weights Epoch");
      bestWeightsLine.add(bestWeightsEpoch, 0); // Add a point at the best weights epoch
      bestWeightsLine.add(bestWeightsEpoch, trainingErrorsSeries.getMaxY());

      dataset.addSeries(bestWeightsLine);

      // set best weights line as dashed
      lineChart
          .getXYPlot()
          .getRenderer()
          .setSeriesStroke(
              dataset.getSeriesCount() - 1,
              new BasicStroke(
                  1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {5}, 0));
      lineChart.getXYPlot().getRenderer().setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
    }

    dataset.addSeries(trainingErrorsSeries);
    dataset.addSeries(testingErrorsSeries);

    lineChart.fireChartChanged();
  }

  private void updateNeuralNetworkInformation() {
    networkInformationTextPane.setText(
        "Neural network information:\n" + neuralNetworkConfiguration.toString());
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
    networkTrainingLabel.setVisible(true);
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
              changeStateOfButtons(true);
              resetProgressBar();
            } catch (InterruptedException | ExecutionException e) {
              showErrorMessage(e.getLocalizedMessage());
            }
          }
        };

    changeStateOfButtons(false);
    worker.execute();
  }

  private void changeStateOfButtons(boolean enable) {
    SwingUtilities.invokeLater(
        () -> {
          chooseDataButton.setEnabled(enable);
          predictButton.setEnabled(enable);
          createNeuralNetworkButton.setEnabled(enable);
          trainNetworkButton.setEnabled(enable);
        });
  }

  private void resetProgressBar() {
    SwingUtilities.invokeLater(
        () -> {
          progressBar.setValue(0);
          progressBar.setVisible(false);
          networkTrainingLabel.setVisible(false);
        });
  }

  @Override
  public void loadData(
      String filePath,
      int numberIfInputColumns,
      int numberOfOutputColumns,
      boolean hasHeader,
      String delimiter,
      double trainTestSplitRatio) {
    CsvReader csvReader = new CsvReader(filePath);
    csvReader.setHasHeader(hasHeader);
    csvReader.setDelimiter(delimiter);
    csvReader.readCsv(numberIfInputColumns, numberOfOutputColumns);

    double[][] inputs = csvReader.getInputDataArray();
    double[][] outputs = csvReader.getExpectedOutputArray();

    controller.setData(inputs, outputs, trainTestSplitRatio);
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
              scrollTerminalToBottom();
            } catch (InterruptedException | ExecutionException e) {
              showErrorMessage(e.getLocalizedMessage());
            }
          }
        };

    worker.execute();
  }

  private void scrollTerminalToBottom() {
    JScrollBar verticalScrollBar = terminalScrollPane.getVerticalScrollBar();
    verticalScrollBar.setValue(verticalScrollBar.getMaximum());
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
      // set error name as y axis label
      lineChart
          .getXYPlot()
          .getRangeAxis()
          .setLabel(errorFunction != null ? errorFunction.getClass().getSimpleName() : "Error");
    } catch (InstantiationException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      showErrorMessage(e.getLocalizedMessage());
    }
  }
}
