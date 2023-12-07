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

  private double[] input;

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
    this.input = input;

    this.weightedInput =
        IntStream.range(0, input.length).mapToDouble(i -> input[i] * weights[i]).sum();
    this.output = applyActivationFunction();
  }

  public double[] getWeights() {
    return weights;
  }

  public void setWeight(double weight, int index) {
    if (index < 0 || index >= weights.length) {
      throw new IndexOutOfBoundsException(
          String.format("Index %d is out of bounds for %d weights!", index, weights.length));
    }

    weights[index] = weight;
  }

  public IActivationFunction getActivationFunction() {
    return activationFunction;
  }

  private double applyActivationFunction() {
    return activationFunction.apply(weightedInput);
  }

  public double getOutput() {
    return output;
  }

  public double getWeightedInput() {
    return weightedInput;
  }

  public double[] getInputs() {
    return input;
  }

  private double[] initializeWeights(int size) {
    Random random = new Random();
    return Arrays.stream(new double[size]).map(weight -> random.nextDouble()).toArray();
  }
}
