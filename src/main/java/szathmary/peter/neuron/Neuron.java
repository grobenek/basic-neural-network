package szathmary.peter.neuron;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import szathmary.peter.activationfunctions.IActivationFunction;

public class Neuron implements INeuronComponent {
  private final double[] weights;
  private final IActivationFunction activationFunction;
  private double weightedInput;
  private double output;

  public Neuron(IActivationFunction activationFunction, double[] weights) {
    this.activationFunction = activationFunction;
    this.weights = weights;
  }
  public Neuron(int inputSize, IActivationFunction activationFunction) {
    this.activationFunction = activationFunction;
    this.weights = initializeWeights(inputSize);
  }

  public void processInput(double[] input) {
    if (input.length != weights.length) {
      throw new IllegalArgumentException("Input and weights must be of the same length");
    }

    this.weightedInput =
        IntStream.range(0, input.length).mapToDouble(i -> input[i] * weights[i]).sum();
    this.output = applyActivationFunction();
  }

  private double applyActivationFunction() {
    return activationFunction.apply(weightedInput);
  }

  public double getOutput() {
    return output;
  }

  private double[] initializeWeights(int size) {
    Random random = new Random();
    return Arrays.stream(new double[size]).map(weight -> random.nextDouble()).toArray();
  }
}
