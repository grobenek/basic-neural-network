package szathmary.peter.neuralnetwork.network;

import java.util.Arrays;

public class NeuronFactory implements INeuronFactory {
  public static final IActivationFunctionFactory activationFunctionFactory =
      new ActivationFunctionFactory();

  @Override
  public Neuron createNeuron(int neuronInputSize, ActivationFunction activationFunctionName) {
    if (neuronInputSize == 0) {
      throw new IllegalArgumentException("Cannot create neuron with zero input size!");
    }

    return new Neuron(
        neuronInputSize, activationFunctionFactory.getActivationFunction(activationFunctionName));
  }

  @Override
  public Neuron createInputLayerNeuron(
          int neuronInputSize, int numberOfNeuronsInLayer, ActivationFunction activationFunctionName) {
//    double[] weights = new double[neuronInputSize];
    double[] weights = new double[1];

    Arrays.fill(weights, 1);

    return new Neuron(
        activationFunctionFactory.getActivationFunction(activationFunctionName), weights);
  }
}
