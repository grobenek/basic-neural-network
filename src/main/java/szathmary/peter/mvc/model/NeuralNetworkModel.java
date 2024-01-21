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
  private final List<IObserver> observerList = new ArrayList<>();
  private TrainingAlgorithm trainingAlgorithm;
  private NeuralNetwork neuralNetwork;
  private Optional<NetworkConfiguration> currentNeuralNetworkConfiguration = Optional.empty();
  private List<Double> trainingErrorList = Collections.emptyList();
  private double[][] trainingInputs;
  private double[][] trainingOutputs;

  public NeuralNetworkModel() {}

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
      networkBuilder.addHiddenLayer(
          configuration.hiddenLayersNumberOfNeurons()[i],
          configuration.hiddenLayersActivationFunctions()[i]);
    }

    networkBuilder.addOutputLayer(
        configuration.numberOfOutputNeurons(), configuration.outputLayerActivationFunction());

    neuralNetwork = networkBuilder.build();

    sendNotifications();
  }

  @Override
  public void trainNetwork(
      IErrorFunction errorFunction, int numberOfEpochs, double minErrorTreshold) {
    trainingAlgorithm.train(
        neuralNetwork,
        errorFunction,
        trainingInputs,
        trainingOutputs,
        numberOfEpochs
    );
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
  public int getNumberOfInputs() {
    return neuralNetwork.getInputLayer().getNeuronCount();
  }

  @Override
  public void setTrainingData(double[][] inputs, double[][] outputs) {
    trainingInputs = inputs;
    trainingOutputs = outputs;
  }

  @Override
  public void setTrainingAlgorithm(TrainingAlgorithm trainingAlgorithm) {
    if (trainingAlgorithm == null) {
      throw new IllegalArgumentException("Cannot set null training algorithm!");
    }

    if (this.trainingAlgorithm != null) {
      trainingAlgorithm.detach(this);
    }

    this.trainingAlgorithm = trainingAlgorithm;
    trainingAlgorithm.attach(this);
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
