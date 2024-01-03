package szathmary.peter.neuralnetwork.neuron;

import java.util.Arrays;

public class NeuronFactory implements INeuronFactory {
  public static final IActivationFunctionFactory activationFunctionFactory =
      new ActivationFunctionFactory();

  @Override
  public Neuron createNeuron(int neuronInputSize, String activationFunctionName) {
    if (neuronInputSize == 0) {
      throw new IllegalArgumentException("Cannot create neuron with zero input size!");
    }

    return new Neuron(
        neuronInputSize, activationFunctionFactory.getActivationFunction(activationFunctionName));
  }

  @Override
  public Neuron createInputLayerNeuron(
      int neuronInputSize, int numberOfNeuronsInLayer, String activationFunctionName) {
    double[] weights = new double[neuronInputSize];

    Arrays.fill(weights, 1);

    return new Neuron(
        activationFunctionFactory.getActivationFunction(activationFunctionName), weights);
  }
}
