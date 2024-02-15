package szathmary.peter.mvc.controller;

import java.util.*;
import szathmary.peter.mvc.model.IModel;
import szathmary.peter.mvc.model.NetworkConfiguration;
import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObservable;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.ActivationFunction;
import szathmary.peter.neuralnetwork.trainingalgorithms.BackPropagation;
import szathmary.peter.neuralnetwork.util.DataUtils;
import szathmary.peter.neuralnetwork.util.TrainTestSplit;

public class Controller implements IController {
  private final IModel model;
  private final List<IObserver> observerList = new ArrayList<>();
  private Optional<NetworkConfiguration> currentNeuralNetworkConfiguration = Optional.empty();
  private List<Double> trainingErrorList = Collections.emptyList();
  private List<Double> testingErrorList = Collections.emptyList();
  private int bestWeightsEpoch;
  private double percentageOfCompletedTraining;

  public Controller(IModel model) {
    this.model = model;
    this.model.attach(this);
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
    NetworkConfiguration networkConfiguration =
        new NetworkConfiguration(
            numberOfInputNeurons,
            inputLayerActivationFunction,
            numberOfHiddenLayers,
            hiddenLayersNumberOfNeurons,
            hiddenLayersActivationFunctions,
            numberOfOutputNeurons,
            outputLayerActivationFunction);

    model.initializeNetwork(networkConfiguration);
  }

  @Override
  public void trainNetwork(
      IErrorFunction errorFunction, int numberOfEpochs, double minErrorTreshold) {
    try {
      model.trainNetwork(errorFunction, numberOfEpochs, minErrorTreshold);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.err.println(Arrays.toString(e.getStackTrace()));
      throw new RuntimeException(e);
    }
  }

  @Override
  public void testNetwork(
      IErrorFunction errorFunction, double[][] inputs, double[][] expectedOutputs) {
    model.testNetwork(errorFunction, inputs, expectedOutputs);
  }

  @Override
  public double[] predict(double[] input) {
    return model.predict(input);
  }

  @Override
  public int getNumberOfInputs() {
    return model.getNumberOfInputs();
  }

  @Override
  public void setData(double[][] inputs, double[][] outputs, double trainTestSplitRatio) {
    TrainTestSplit splittedData =
        DataUtils.splitDataToTrainAndTest(inputs, outputs, trainTestSplitRatio);

    model.setTrainingData(splittedData.trainInputs(), splittedData.trainOutputs());
    model.setTestingData(splittedData.testInputs(), splittedData.testOutputs());
  }

  @Override
  public void setTrainingAlgorithm(double learningRate) {
    model.setTrainingAlgorithm(new BackPropagation(learningRate));
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof INeuralNetworkObservable networkObservable)) {
      return;
    }

    trainingErrorList = networkObservable.getTrainingErrors();
    testingErrorList = networkObservable.getTestingErrors();
    percentageOfCompletedTraining = networkObservable.getPercentageOfCompletedTraining();
    currentNeuralNetworkConfiguration = networkObservable.getNeuralNetworkConfiguration();
    bestWeightsEpoch = networkObservable.getBestWeightsEpoch();

    sendNotifications();
  }

  @Override
  public void attach(IObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void detach(IObserver observer) {
    observerList.remove(observer);
  }

  @Override
  public void sendNotifications() {
    for (IObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public Optional<NetworkConfiguration> getNeuralNetworkConfiguration() {
    return currentNeuralNetworkConfiguration;
  }

  @Override
  public List<Double> getTrainingErrors() {
    return trainingErrorList;
  }

  @Override
  public List<Double> getTestingErrors() {
    return testingErrorList;
  }

  @Override
  public double getPercentageOfCompletedTraining() {
    return percentageOfCompletedTraining;
  }

  @Override
  public int getBestWeightsEpoch() {
    return bestWeightsEpoch;
  }
}
