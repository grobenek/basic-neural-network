package szathmary.peter.neuralnetwork.network;

public interface INeuronFactory {
  Neuron createNeuron(int neuronInputSize, ActivationFunction activationFunctionName);

  Neuron createInputLayerNeuron(int neuronInputSize, ActivationFunction activationFunctionName);
}
