package szathmary.peter.neuron;

public class NetworkBuilder {
  private static final INeuronFactory neuronFactory = new NeuronFactory();
  private final Network network = new Network();

  private Layer createLayer(
      int numberOfNeuronsInLayer, int neuronInputSize, String activationFunctionName) {
    if (numberOfNeuronsInLayer == 0) {
      throw new IllegalArgumentException("Cannot create layer with zero neurons!");
    }

    Layer layer = new Layer();
    for (int i = 0; i < numberOfNeuronsInLayer; i++) {
      layer.addNeuron(neuronFactory.createNeuron(neuronInputSize, activationFunctionName));
    }
    return layer;
  }

  public NetworkBuilder addLayer(int numberOfNeuronsInLayer, String activationFunctionName) {

    int neuronInputSize;
    if (network.getNumberOfHiddenLayers() == 0) {
      Layer inputLayer = network.getInputLayer();

      if (inputLayer == null) {
        throw new IllegalStateException("Input layer must be defined first!");
      }

      neuronInputSize = inputLayer.getNeuronCount();
    } else {
      neuronInputSize = network.getLayer(network.getNumberOfLayers() - 1).getNeuronCount();
    }

    Layer layer = createLayer(numberOfNeuronsInLayer, neuronInputSize, activationFunctionName);

    network.addLayer(layer);

    return this;
  }

  public NetworkBuilder addInputLayer(int numberOfNeuronsInLayer, String activationFunctionName) {
    if (network.getInputLayer() != null) {
      throw new IllegalStateException("Input layer already defined!");
    }

    Layer inputLayer = new Layer();

    for (int i = 0; i < numberOfNeuronsInLayer; i++) {
      inputLayer.addNeuron(
          neuronFactory.createInputLayerNeuron(numberOfNeuronsInLayer, 1, activationFunctionName));
    }

    network.setInputLayer(inputLayer);

    return this;
  }

  public NetworkBuilder addOutputLayer(int numberOfNeuronsInLayer, String activationFunctionName) {
    if (network.getOutputLayer() != null) {
      throw new IllegalStateException("Output layer already defined!");
    }

    int neuronInputSize = network.getLayer(network.getNumberOfHiddenLayers() - 1).getNeuronCount();

    Layer layer = createLayer(numberOfNeuronsInLayer, neuronInputSize, activationFunctionName);

    network.setOutputLayer(layer);

    return this;
  }

  public Network build() {
    if (network.getOutputLayer() == null) {
      throw new IllegalStateException("No output layer defined!");
    }

    return network;
  }
}
