package szathmary.peter.neuron;

public class NetworkBuilder {
  Network network = new Network();

  public NetworkBuilder() {}

  private static Layer createLayer(int numberOfNeuronsInLayer, INeuronFactory neuronFactory) {
    if (numberOfNeuronsInLayer == 0) {
      throw new IllegalArgumentException("Cannot create layer with zero neurons!");
    }

    Layer layer = new Layer();
    for (int i = 0; i < numberOfNeuronsInLayer; i++) {
      layer.addNeuron(neuronFactory.createNeuron());
    }
    return layer;
  }

  public NetworkBuilder addLayer(int numberOfNeuronsInLayer, INeuronFactory neuronFactory) {
    Layer layer = createLayer(numberOfNeuronsInLayer, neuronFactory);

    network.addLayer(layer);

    return this;
  }

  public NetworkBuilder addInputLayer(int numberOfNeuronsInLayer, INeuronFactory neuronFactory) {
    Layer inputLayer = new Layer();

    for (int i = 0; i < numberOfNeuronsInLayer; i++) {
      inputLayer.addNeuron(neuronFactory.createInputLayerNeuron());
    }

    network.setInputLayer(inputLayer);

    return this;
  }

  public NetworkBuilder addOutputLayer(int numberOfNeuronsInLayer, INeuronFactory neuronFactory) {
    Layer layer = createLayer(numberOfNeuronsInLayer, neuronFactory);

    network.setOutputLayer(layer);

    return this;
  }

  public Network build() {
    return network;
  }
}
