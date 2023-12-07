package szathmary.peter.trainingalgorithms;

import szathmary.peter.errorfunctions.IErrorFunction;
import szathmary.peter.neuron.Network;

public abstract class TrainingAlgorithm {
  protected final double learningRate;
  public double errorTreshold;
  private double lastError;

  public TrainingAlgorithm(double learningRate) {
    this.learningRate = learningRate;
  }

  public final void train(
      Network network,
      IErrorFunction errorFunction,
      double[] inputs,
      double[] expectedOutputs,
      int numberOfEpochs,
      double minErrorTreshold) {
    if (inputs.length != expectedOutputs.length) {
      throw new IllegalArgumentException(
          String.format(
              "Input length %d does not match expectedOutputs length %d!",
              inputs.length, expectedOutputs.length));
    }

    this.errorTreshold = minErrorTreshold;

    for (int i = 0; i < numberOfEpochs; i++) {
      for (int currentDataInTrainIndex = 0;
          currentDataInTrainIndex < inputs.length;
          currentDataInTrainIndex++) {
        double[] input = {inputs[currentDataInTrainIndex]};
        double[] expectedOutput = {expectedOutputs[currentDataInTrainIndex]};
        trainOnSelectedData(network, input, expectedOutput, errorFunction);
      }
      System.out.println("Epoch " + i + " : error " + lastError);

      if (lastError <= errorTreshold) {
        return;
      }
    }
  }

  private void trainOnSelectedData(
      Network network, double[] input, double[] expectedOutput, IErrorFunction errorFunction) {
    forwardPropagate(network, input);

    double error = calculateError(network, expectedOutput, errorFunction);
    this.lastError = error;
    backPropagate(network, error);
  }

  protected abstract void forwardPropagate(Network network, double[] input);

  protected abstract double calculateError(
      Network network, double[] expectedOutput, IErrorFunction errorFunction);

  protected abstract void backPropagate(Network network, double error);
}
