package szathmary.peter.neuralnetwork.trainingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.mvc.observable.ITraningAlgorithmObservable;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.NeuralNetwork;
import szathmary.peter.neuralnetwork.network.Neuron;

public abstract class TrainingAlgorithm implements ITraningAlgorithmObservable {
  protected final double learningRate;
  private final List<IObserver> observerList = new ArrayList<>();
  private List<Double> errorList = Collections.emptyList();

  public TrainingAlgorithm(double learningRate) {
    this.learningRate = learningRate;
  }

  public final void train(
      NeuralNetwork neuralNetwork,
      IErrorFunction errorFunction,
      double[][] inputs,
      double[][] expectedOutputs,
      int numberOfEpochs) {

    // TODO zapamatat si najlepsie vahy a tie pouzivat

    if (inputs.length != expectedOutputs.length) {
      throw new IllegalArgumentException(
          String.format(
              "Input length %d does not match expectedOutputs length %d!",
              inputs.length, expectedOutputs.length));
    }

    List<Neuron> inputNeurons = neuralNetwork.getClonedInputNeurons();
    List<List<Neuron>> hiddenLayersNeurons = neuralNetwork.getClonedHiddenLayersNeurons();
    List<Neuron> outputNeurons = neuralNetwork.getClonedOutputNeuronList();

    errorList = new ArrayList<>(numberOfEpochs);
    double lowestError = Double.MAX_VALUE;

    for (int epoch = 0; epoch < numberOfEpochs; epoch++) {
      double[][] outputsAfterTraining = new double[inputs.length][inputs[0].length];

      for (int i = 0; i < inputs.length; i++) {
        double[] input = inputs[i];
        double[] expectedOutput = expectedOutputs[i];

        trainOnSelectedData(neuralNetwork, input, expectedOutput);

        outputsAfterTraining[i] = neuralNetwork.getOutput();
      }

      double calculatedError = errorFunction.calculateError(outputsAfterTraining, expectedOutputs);

      if (calculatedError < lowestError) {
        lowestError = calculatedError;

        inputNeurons = neuralNetwork.getClonedInputNeurons();
        hiddenLayersNeurons = neuralNetwork.getClonedHiddenLayersNeurons();
        neuralNetwork.getClonedOutputNeuronList();
      }

      errorList.add(calculatedError);
      System.out.println(
          "Epoch "
              + epoch
              + " : "
              + errorFunction.getClass().getSimpleName()
              + " error = "
              + calculatedError);
    }

    System.out.println("LOWEST ERROR: " + lowestError);
    neuralNetwork.setNeurons(inputNeurons, hiddenLayersNeurons, outputNeurons);
  }

  private void trainOnSelectedData(
      NeuralNetwork neuralNetwork, double[] input, double[] expectedOutput) {
    forwardPropagate(neuralNetwork, input);

    double error = expectedOutput[0] - neuralNetwork.getOutput()[0];
    backPropagate(neuralNetwork, error);
  }

  protected abstract void forwardPropagate(NeuralNetwork neuralNetwork, double[] input);

  protected abstract void backPropagate(NeuralNetwork neuralNetwork, double error);

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
    return errorList;
  }
}
