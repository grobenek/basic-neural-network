package szathmary.peter.mvc.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import szathmary.peter.mvc.model.IModel;
import szathmary.peter.mvc.model.NetworkConfiguration;
import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObservable;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public class Controller implements IController {
  private final IModel model;
  private final List<IObserver> observerList = new ArrayList<>();
  private Optional<NetworkConfiguration> currentNeuralNetworkConfiguration = Optional.empty();
  private List<Double> trainingErrorList = Collections.emptyList();

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
      IErrorFunction errorFunction,
      int numberOfEpochs,
      double minErrorTreshold) {
    model.trainNetwork(errorFunction, numberOfEpochs, minErrorTreshold);
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
  public void setTrainingData(double[][] inputs, double[][] outputs) {
    model.setTrainingData(inputs, outputs);
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof INeuralNetworkObservable networkObservable)) {
      return;
    }

    trainingErrorList = networkObservable.getErrors();
    currentNeuralNetworkConfiguration = networkObservable.getNeuralNetworkConfiguration();

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
  public List<Double> getErrors() {
    return trainingErrorList;
  }
}
