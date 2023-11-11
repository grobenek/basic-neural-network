package szathmary.peter.neuron;

import java.util.Arrays;
import szathmary.peter.activationfunctions.SigmoidActivationFunction;

public class SigmoidNeuronFactory implements INeuronFactory {
  private final int neuronInputSize;

  public SigmoidNeuronFactory(int neuronInputSize) {
    if (neuronInputSize == 0) {
      throw new IllegalArgumentException("Cannot create neuron with zero input size!");
    }

    this.neuronInputSize = neuronInputSize;
  }

  @Override
  public Neuron createNeuron() {
    return new Neuron(neuronInputSize, new SigmoidActivationFunction());
  }

  @Override
  public Neuron createInputLayerNeuron() {
    double[] weights = new double[neuronInputSize];

    Arrays.fill(weights, 1);

    return new Neuron(new SigmoidActivationFunction(), weights);
  }
}
