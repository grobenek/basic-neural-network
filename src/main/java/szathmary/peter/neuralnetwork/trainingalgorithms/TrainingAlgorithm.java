package szathmary.peter.neuralnetwork.trainingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.mvc.observable.ITraningAlgorithmObservable;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.NeuralNetwork;

public abstract class TrainingAlgorithm implements ITraningAlgorithmObservable {
  protected final double learningRate;
  private final List<IObserver> observerList = new ArrayList<>();
  private double lastError;
  private List<Double> errorList = Collections.emptyList();

  public TrainingAlgorithm(double learningRate) {
    this.learningRate = learningRate;
  }

  public final void train(
      NeuralNetwork neuralNetwork,
      IErrorFunction errorFunction,
      double[][] inputs, // assuming inputs is an array of single numbers
      double[][] expectedOutputs, // assuming expectedOutputs is an array of single numbers
      int numberOfEpochs,
      double minErrorTreshold) {

    if (inputs.length != expectedOutputs.length) {
      throw new IllegalArgumentException(
          String.format(
              "Input length %d does not match expectedOutputs length %d!",
              inputs.length, expectedOutputs.length));
    }

    errorList = new ArrayList<>(numberOfEpochs);

    for (int epoch = 0; epoch < numberOfEpochs; epoch++) {
      double totalError = 0;

      for (int i = 0; i < inputs.length; i++) {
        double[] input = inputs[i];
        double[] expectedOutput = expectedOutputs[i];

        trainOnSelectedData(neuralNetwork, input, expectedOutput, errorFunction);

        totalError += this.lastError;
      }

      double averageError = totalError / inputs.length;
      errorList.add(averageError);
      System.out.println("Epoch " + epoch + " : Average error = " + averageError);

      if (averageError <= minErrorTreshold) {
        break; // Stop training if error is below threshold //TODO zapamatat si najlepsie vahy a tie pouzit
      }
    }
  }

  private void trainOnSelectedData(
      NeuralNetwork neuralNetwork,
      double[] input,
      double[] expectedOutput,
      IErrorFunction errorFunction) {
    forwardPropagate(neuralNetwork, input);

//    double error = calculateError(neuralNetwork, expectedOutput, errorFunction);
    double error = expectedOutput[0] - neuralNetwork.getOutput()[0];
    this.lastError = Math.pow(error, 2);
    backPropagate(neuralNetwork, error);
  }

  protected abstract void forwardPropagate(NeuralNetwork neuralNetwork, double[] input);

  protected abstract double calculateError(
      NeuralNetwork neuralNetwork, double[] expectedOutput, IErrorFunction errorFunction);

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
