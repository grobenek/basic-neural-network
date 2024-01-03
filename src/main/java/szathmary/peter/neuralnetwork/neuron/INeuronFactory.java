package szathmary.peter.neuralnetwork.neuron;

public interface INeuronFactory {
  Neuron createNeuron(int neuronInputSize, String activationFunctionName);

  Neuron createInputLayerNeuron(
      int neuronInputSize, int numberOfNeuronsInLayer, String activationFunctionName);
}
