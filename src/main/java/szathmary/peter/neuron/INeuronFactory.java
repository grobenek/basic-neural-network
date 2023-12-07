package szathmary.peter.neuron;

public interface INeuronFactory {
  Neuron createNeuron(int neuronInputSize, String activationFunctionName);

  Neuron createInputLayerNeuron(
      int neuronInputSize, int numberOfNeuronsInLayer, String activationFunctionName);
}
