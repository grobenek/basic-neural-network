package szathmary.peter.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import szathmary.peter.mvc.observable.IObservable;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.mvc.observable.ITraningAlgorithmObservable;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.NeuralNetwork;
import szathmary.peter.neuralnetwork.network.NeuralNetworkBuilder;
import szathmary.peter.neuralnetwork.trainingalgorithms.TrainingAlgorithm;

public class NeuralNetworkModel implements IModel {
  private final TrainingAlgorithm trainingAlgorithm;
  private final List<IObserver> observerList = new ArrayList<>();
  private NeuralNetwork neuralNetwork;
  private Optional<NetworkConfiguration> currentNeuralNetworkConfiguration = Optional.empty();
  private List<Double> trainingErrorList = Collections.emptyList();

  public NeuralNetworkModel(TrainingAlgorithm trainingAlgorithm) {
    this.trainingAlgorithm = trainingAlgorithm;
    trainingAlgorithm.attach(this);
  }

  @Override
  public void initializeNetwork(NetworkConfiguration configuration) {
    if (configuration == null) {
      throw new IllegalArgumentException("Cannot create network from null configuration!");
    }

    currentNeuralNetworkConfiguration = Optional.of(configuration);

    NeuralNetworkBuilder networkBuilder = new NeuralNetworkBuilder();

    networkBuilder.addInputLayer(
        configuration.numberOfInputNeurons(), configuration.inputLayerActivationFunction());

    for (int i = 0; i < configuration.numberOfHiddenLayers(); i++) {
      networkBuilder.addLayer(
          configuration.hiddenLayersNumberOfNeurons()[i],
          configuration.hiddenLayersActivationFunctions()[i]);
    }

    networkBuilder.addOutputLayer(
        configuration.numberOfOutputNeurons(), configuration.outputLayerActivationFunction());

    neuralNetwork = networkBuilder.build();
  }

  @Override
  public void trainNetwork(
      IErrorFunction errorFunction,
      double[][] inputs,
      double[][] expectedOutputs,
      int numberOfEpochs,
      double minErrorTreshold) {
    trainingAlgorithm.train(
        neuralNetwork, errorFunction, inputs, expectedOutputs, numberOfEpochs, minErrorTreshold);
    trainingErrorList = trainingAlgorithm.getErrors();
    sendNotifications();
  }

  @Override
  public void testNetwork(
      IErrorFunction errorFunction, double[][] inputs, double[][] expectedOutputs) {}

  @Override
  public double[] predict(double[] input) {
    neuralNetwork.processInput(input);
    return neuralNetwork.getOutput();
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof ITraningAlgorithmObservable)) {
      return;
    }

    trainingErrorList = ((ITraningAlgorithmObservable) observable).getErrors();
  }

  @Override
  public Optional<NetworkConfiguration> getNeuralNetworkConfiguration() {
    return currentNeuralNetworkConfiguration;
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
  public List<Double> getErrors() {
    return trainingErrorList;
  }
}
