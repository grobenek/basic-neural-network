package szathmary.peter.neuron;

public interface INeuronFactory {
  Neuron createNeuron();

  Neuron createInputLayerNeuron();
}
