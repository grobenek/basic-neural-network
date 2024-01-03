package szathmary.peter.neuralnetwork.trainingalgorithms;

import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.neuron.Network;

public abstract class TrainingAlgorithm {
  protected final double learningRate;
  public double errorTreshold;
  private double lastError;

  public TrainingAlgorithm(double learningRate) {
    this.learningRate = learningRate;
  }

  //  public final void train(
  //      Network network,
  //      IErrorFunction errorFunction,
  //      double[] inputs,
  //      double[] expectedOutputs,
  //      int numberOfEpochs,
  //      double minErrorTreshold) {
  //    if (inputs.length != expectedOutputs.length) {
  //      throw new IllegalArgumentException(
  //          String.format(
  //              "Input length %d does not match expectedOutputs length %d!",
  //              inputs.length, expectedOutputs.length));
  //    }
  //
  //    this.errorTreshold = minErrorTreshold;
  //
  //    for (int i = 0; i < numberOfEpochs; i++) {
  //      for (int currentDataInTrainIndex = 0;
  //          currentDataInTrainIndex < inputs.length;
  //          currentDataInTrainIndex++) {
  //        double[] input = {inputs[currentDataInTrainIndex]};
  //        double[] expectedOutput = {expectedOutputs[currentDataInTrainIndex]};
  //        trainOnSelectedData(network, input, expectedOutput, errorFunction);
  ////        System.out.println("error " + lastError);
  //      }
  //      System.out.println("Epoch " + i + " : error " + lastError);
  //
  //      if (lastError <= errorTreshold) {
  //        return;
  //      }
  //    }
  //  }

  public final void train(
    Network network,
    IErrorFunction errorFunction,
    double[] inputs, // assuming inputs is an array of single numbers
    double[] expectedOutputs, // assuming expectedOutputs is an array of single numbers
    int numberOfEpochs,
    double minErrorTreshold) {

  if (inputs.length != expectedOutputs.length) {
    throw new IllegalArgumentException(
        String.format(
            "Input length %d does not match expectedOutputs length %d!",
            inputs.length, expectedOutputs.length));
  }

  this.errorTreshold = minErrorTreshold;

  for (int epoch = 0; epoch < numberOfEpochs; epoch++) {
    double totalError = 0;

    for (int i = 0; i < inputs.length; i++) {
      double[] input = new double[]{inputs[i]}; // Wrap single input number in an array
      double[] expectedOutput = new double[]{expectedOutputs[i]}; // Wrap single output number in an array

      trainOnSelectedData(network, input, expectedOutput, errorFunction);

      totalError += this.lastError;
    }

    double averageError = totalError / inputs.length;
    System.out.println("Epoch " + epoch + " : Average error = " + averageError);

    if (averageError <= errorTreshold) {
      break; // Stop training if error is below threshold
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
